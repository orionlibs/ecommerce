package de.hybris.platform.ruleengineservices.versioning.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import de.hybris.platform.ruleengineservices.enums.RuleStatus;
import de.hybris.platform.ruleengineservices.model.AbstractRuleModel;
import de.hybris.platform.ruleengineservices.model.SourceRuleModel;
import de.hybris.platform.ruleengineservices.rule.dao.RuleDao;
import de.hybris.platform.ruleengineservices.versioning.HistoricalRuleContentProvider;
import de.hybris.platform.ruleengineservices.versioning.RuleModelHistoricalContentCreator;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PersistenceOperation;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.servicelayer.model.ModelContextUtils;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import javax.annotation.Nonnull;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class SourceRuleModelHistoricalContentCreator implements RuleModelHistoricalContentCreator
{
    private static final Logger LOG = LoggerFactory.getLogger(SourceRuleModelHistoricalContentCreator.class);
    private List<HistoricalRuleContentProvider> historicalRuleContentProviders;
    private RuleDao ruleDao;


    public void createHistoricalVersion(@Nonnull SourceRuleModel sourceRule, @Nonnull InterceptorContext context) throws InterceptorException
    {
        Preconditions.checkArgument(Objects.nonNull(sourceRule), "Model should not be null here");
        Preconditions.checkArgument(Objects.nonNull(context), "InterceptorContext should not be null here");
        if(!isUnpublished(sourceRule, context))
        {
            createHistoricalVersionIfNeeded(sourceRule, context);
        }
    }


    protected boolean isUnpublished(SourceRuleModel sourceRule, InterceptorContext context)
    {
        return hasStatus(context, sourceRule, RuleStatus.UNPUBLISHED);
    }


    protected void createHistoricalVersionIfNeeded(SourceRuleModel sourceRule, InterceptorContext ctx) throws InterceptorException
    {
        if(historicalVersionMustBeCreated(sourceRule, ctx))
        {
            Optional<AbstractRuleModel> latestUnpublishedRule = getRuleDao().findRuleByCodeAndStatus(sourceRule.getCode(), RuleStatus.UNPUBLISHED);
            if(latestUnpublishedRule.isPresent())
            {
                AbstractRuleModel unpublishedRule = latestUnpublishedRule.get();
                throw new InterceptorException("The modifications are allowed to be made for version [" + unpublishedRule
                                .getVersion() + "] only");
            }
            SourceRuleModel newSourceRuleVersion = doCreateHistoricalVersion(sourceRule, ctx);
            incrementRuleModelVersion(newSourceRuleVersion);
            newSourceRuleVersion.setStatus(RuleStatus.UNPUBLISHED);
            newSourceRuleVersion.setRulesModules(Lists.newArrayList());
            ctx.registerElementFor(newSourceRuleVersion, PersistenceOperation.SAVE);
            resetModifiedFields(sourceRule, ctx);
        }
        LOG.debug("Registering modified source rule model: PK={}, code={}, uuid={}, version={}", new Object[] {sourceRule.getPk(), sourceRule
                        .getCode(), sourceRule.getUuid(), sourceRule.getVersion()});
    }


    protected void resetModifiedFields(SourceRuleModel toSourceRule, InterceptorContext ctx)
    {
        if(Objects.isNull(toSourceRule))
        {
            LOG.warn("Target SourceRule is null");
            return;
        }
        ItemModelContext modelContext = ModelContextUtils.getItemModelContext((AbstractItemModel)toSourceRule);
        Map<String, Set<Locale>> dirtyAttributes = ctx.getDirtyAttributes(toSourceRule);
        dirtyAttributes.entrySet().forEach(e -> copyField(modelContext, toSourceRule, e));
    }


    protected void copyField(ItemModelContext modelContext, SourceRuleModel sourceRule, Map.Entry<String, Set<Locale>> attribute)
    {
        String propertyName = attribute.getKey();
        if(CollectionUtils.isEmpty(attribute.getValue()))
        {
            sourceRule.setProperty(propertyName, modelContext.getOriginalValue(propertyName));
        }
        else
        {
            ((Set)attribute.getValue())
                            .forEach(l -> sourceRule.setProperty(propertyName, l, modelContext.getOriginalValue(propertyName, l)));
        }
    }


    protected void incrementRuleModelVersion(SourceRuleModel ruleModel)
    {
        Long maxRuleVersion = getRuleDao().getRuleVersion(ruleModel.getCode());
        long nextVersion = 1L + maxRuleVersion.longValue();
        ruleModel.setVersion(Long.valueOf(nextVersion));
    }


    protected boolean historicalVersionMustBeCreated(SourceRuleModel sourceRule, InterceptorContext context)
    {
        return (modelIsBeingModified((AbstractRuleModel)sourceRule, context) && modelIsValid(sourceRule));
    }


    protected boolean ruleStatusChangedToPublished(SourceRuleModel model, InterceptorContext context)
    {
        return (!model.getStatus().equals(getOriginal(model, context, "status")) && model
                        .getStatus().equals(RuleStatus.PUBLISHED));
    }


    protected SourceRuleModel doCreateHistoricalVersion(SourceRuleModel sourceRule, InterceptorContext context)
    {
        SourceRuleModel historicalSourceRule = (SourceRuleModel)context.getModelService().clone(sourceRule);
        putOriginalValuesIntoHistoricalVersion(sourceRule, historicalSourceRule, context);
        return historicalSourceRule;
    }


    protected void putOriginalValuesIntoHistoricalVersion(SourceRuleModel sourceRule, SourceRuleModel historicalSourceRule, InterceptorContext ctx)
    {
        getHistoricalRuleContentProviders()
                        .forEach(p -> p.copyOriginalValuesIntoHistoricalVersion(sourceRule, historicalSourceRule, ctx));
    }


    protected boolean modelIsValid(SourceRuleModel sourceRule)
    {
        return (Objects.nonNull(sourceRule.getActions()) && Objects.nonNull(sourceRule.getConditions()));
    }


    protected boolean modelIsBeingModified(AbstractRuleModel ruleModel, InterceptorContext ctx)
    {
        return (!ctx.isNew(ruleModel) && !ctx.isRemoved(ruleModel) && (
                        essentialFieldsAreModified(ruleModel, ctx) || associatedTypesChanged(ruleModel, ctx)));
    }


    protected boolean essentialFieldsAreModified(AbstractRuleModel ruleModel, InterceptorContext ctx)
    {
        if(ctx.isModified(ruleModel))
        {
            Map<String, Set<Locale>> dirtyAttributes = ctx.getDirtyAttributes(ruleModel);
            return (MapUtils.isNotEmpty(dirtyAttributes) && dirtyAttributes.entrySet().stream()
                            .anyMatch(e -> !matchAnyOf((String)e.getKey(), getNonEssentialAttributes())));
        }
        return false;
    }


    protected boolean associatedTypesChanged(AbstractRuleModel ruleModel, InterceptorContext ctx)
    {
        return false;
    }


    protected String[] getNonEssentialAttributes()
    {
        return new String[] {"engineRules", "status", "version", "rulesModules"};
    }


    protected boolean matchAnyOf(String sample, String... probes)
    {
        if(Objects.nonNull(sample) && ArrayUtils.isNotEmpty((Object[])probes))
        {
            for(String probe : probes)
            {
                if(sample.equals(probe))
                {
                    return true;
                }
            }
        }
        return false;
    }


    protected boolean hasStatus(InterceptorContext ctx, SourceRuleModel sourceRule, RuleStatus ruleStatus)
    {
        RuleStatus originalRuleStatus = getOriginal(sourceRule, ctx, "status");
        return ruleStatus.equals(originalRuleStatus);
    }


    protected <T> T getOriginal(SourceRuleModel sourceRule, InterceptorContext context, String attributeQualifier)
    {
        if(context.isModified(sourceRule, attributeQualifier))
        {
            ItemModelContext modelContext = ModelContextUtils.getItemModelContext((AbstractItemModel)sourceRule);
            return (T)modelContext.getOriginalValue(attributeQualifier);
        }
        ModelService modelService = Objects.<ModelService>requireNonNull(context.getModelService());
        try
        {
            return (T)modelService.getAttributeValue(sourceRule, attributeQualifier);
        }
        catch(RuntimeException e)
        {
            LOG.error("Exception caught: ", e);
            return null;
        }
    }


    protected RuleDao getRuleDao()
    {
        return this.ruleDao;
    }


    @Required
    public void setRuleDao(RuleDao ruleDao)
    {
        this.ruleDao = ruleDao;
    }


    protected List<HistoricalRuleContentProvider> getHistoricalRuleContentProviders()
    {
        return this.historicalRuleContentProviders;
    }


    @Required
    public void setHistoricalRuleContentProviders(List<HistoricalRuleContentProvider> historicalRuleContentProviders)
    {
        this.historicalRuleContentProviders = historicalRuleContentProviders;
    }
}
