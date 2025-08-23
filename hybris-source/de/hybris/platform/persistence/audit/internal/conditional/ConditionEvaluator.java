package de.hybris.platform.persistence.audit.internal.conditional;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.directpersistence.cache.SLDDataContainer;
import de.hybris.platform.directpersistence.cache.SLDDataContainerProvider;
import de.hybris.platform.persistence.audit.AuditChangeFilter;
import de.hybris.platform.persistence.audit.AuditableChange;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.ItemPropertyValue;
import de.hybris.platform.util.config.ConfigIntf;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.MethodResolver;
import org.springframework.expression.ParseException;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.DataBindingMethodResolver;
import org.springframework.expression.spel.support.SimpleEvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class ConditionEvaluator implements AuditChangeFilter
{
    private static final Logger LOG = LoggerFactory.getLogger(ConditionEvaluator.class);
    private static final int DEFAULT_MAXIMUM_SIZE = 1000;
    public static final String FULL_SPEL_SUPPORT = "audit.conditional.fullSpELSupport";
    private final ConditionalAuditGroup config;
    private final SLDDataContainerProvider sldDataContainerProvider;
    private final TypeService typeService;
    private final ConfigIntf tenantConfig;
    private final ExpressionParser spelParser = (ExpressionParser)new SpelExpressionParser();
    private final LoadingCache<String, Expression> expressionCache;
    private final Supplier<Map<String, ConditionalAuditType>> typeToConditionalAuditType = Suppliers.memoize(this::prepareMapIncludingSubtypes);


    public ConditionEvaluator(ConditionalAuditGroup config, SLDDataContainerProvider sldDataContainerProvider, TypeService typeService, ConfigIntf tenantConfig)
    {
        this.config = config;
        this.sldDataContainerProvider = sldDataContainerProvider;
        this.typeService = typeService;
        this.tenantConfig = tenantConfig;
        this.expressionCache = buildExpressionCache();
    }


    private LoadingCache<String, Expression> buildExpressionCache()
    {
        Object object = new Object(this);
        return CacheBuilder.newBuilder().maximumSize(1000L).build((CacheLoader)object);
    }


    public boolean ignoreAudit(AuditableChange change)
    {
        SLDDataContainer changeData = getDataContainer(change);
        if(isDisabled() || notConfiguredForType(changeData))
        {
            return false;
        }
        if(isRootType(changeData) && stateBeforeMatchesCondition(change))
        {
            return false;
        }
        SLDDataContainer rootData = getRootTypeData(change);
        return evaluateIgnoreExpression(rootData);
    }


    private SLDDataContainer getRootTypeData(AuditableChange change)
    {
        SLDDataContainer dataContainer = getDataContainer(change);
        ConditionalAuditType node = getConfigForType(dataContainer.getTypeCode());
        return traverseToRoot(node, dataContainer);
    }


    private boolean isDisabled()
    {
        boolean enabled = this.tenantConfig.getBoolean("audit.conditional." + this.config.getName() + ".enabled", true);
        return !enabled;
    }


    private boolean isRootType(SLDDataContainer data)
    {
        return this.config.getRootType().getCode().equalsIgnoreCase(data.getTypeCode());
    }


    private boolean notConfiguredForType(SLDDataContainer data)
    {
        if(data == null)
        {
            return true;
        }
        ConditionalAuditType typeConfig = getConfigForType(data.getTypeCode());
        return (typeConfig == null || !typeConfig.isConditional());
    }


    private ConditionalAuditType getConfigForType(String typeCode)
    {
        Map<String, ConditionalAuditType> typeToConfig = (Map<String, ConditionalAuditType>)this.typeToConditionalAuditType.get();
        return typeToConfig.get(typeCode);
    }


    private Map<String, ConditionalAuditType> prepareMapIncludingSubtypes()
    {
        Map<String, ConditionalAuditType> typeToConfigMapping = new HashMap<>();
        this.config.traverse(type -> {
            if(!type.isConditional())
            {
                return;
            }
            tryToPut(typeToConfigMapping, type.getCode(), type);
            if(type.isSubtypes())
            {
                ComposedTypeModel composedType = this.typeService.getComposedTypeForCode(type.getCode());
                Collection<ComposedTypeModel> allSubTypes = composedType.getAllSubTypes();
                allSubTypes.forEach(());
            }
        });
        return typeToConfigMapping;
    }


    private static void tryToPut(Map<String, ConditionalAuditType> map, String key, ConditionalAuditType value)
    {
        if(map.containsKey(key))
        {
            throw new ConditionalAuditException("Type was already mapped in conditional audit configuration!");
        }
        map.put(key, value);
    }


    private SLDDataContainer getDataContainer(AuditableChange change)
    {
        return (change.getAfter() != null) ? change.getAfter() : change.getBefore();
    }


    private SLDDataContainer traverseToRoot(ConditionalAuditType currentNode, SLDDataContainer currentDataContainer)
    {
        if(currentNode.getParent() == null)
        {
            return currentDataContainer;
        }
        String attribute = currentNode.getAttribute();
        SLDDataContainer.AttributeValue attributeValue = currentDataContainer.getAttributeValue(attribute, null);
        if(attributeValue.getValue() instanceof ItemPropertyValue)
        {
            ItemPropertyValue ipv = (ItemPropertyValue)attributeValue.getValue();
            SLDDataContainer parentContainer = this.sldDataContainerProvider.get(ipv.getPK());
            return traverseToRoot(currentNode.getParent(), parentContainer);
        }
        throw new ConditionalAuditException("Not supported expression");
    }


    private boolean stateBeforeMatchesCondition(AuditableChange currentChange)
    {
        SLDDataContainer before = currentChange.getBefore();
        return (before != null && shouldAuditData(before));
    }


    private boolean shouldAuditData(SLDDataContainer dataContainer)
    {
        return !evaluateIgnoreExpression(dataContainer);
    }


    private boolean evaluateIgnoreExpression(SLDDataContainer rootData)
    {
        String condition = this.config.getCondition();
        AttributeAccessor accessor = new AttributeAccessor(rootData);
        Expression expression = getExpression(condition);
        try
        {
            SimpleEvaluationContext simpleEvaluationContext;
            if(Config.getBoolean("audit.conditional.fullSpELSupport", false))
            {
                StandardEvaluationContext standardEvaluationContext = new StandardEvaluationContext();
            }
            else
            {
                SimpleEvaluationContext.Builder builder = SimpleEvaluationContext.forReadOnlyDataBinding().withMethodResolvers(new MethodResolver[] {(MethodResolver)DataBindingMethodResolver.forInstanceMethodInvocation()});
                simpleEvaluationContext = builder.build();
            }
            Boolean shouldAudit = (Boolean)expression.getValue((EvaluationContext)simpleEvaluationContext, accessor, Boolean.class);
            LOG.debug("Condition {} evaluated; ignore audit: {}", condition, Boolean.valueOf(!shouldAudit.booleanValue()));
            return !shouldAudit.booleanValue();
        }
        catch(ParseException | NullPointerException e)
        {
            LOG.debug("Failed to evaluate condition: {}", condition, e);
            return false;
        }
    }


    private Expression getExpression(String condition)
    {
        try
        {
            return (Expression)this.expressionCache.get(condition);
        }
        catch(ExecutionException e)
        {
            LOG.warn("Failed to get condition: '{}' from cache", condition, e);
            return this.spelParser.parseExpression(condition);
        }
    }
}
