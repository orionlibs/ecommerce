package de.hybris.platform.droolsruleengineservices.compiler.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import de.hybris.platform.ruleengineservices.rao.CartRAO;
import de.hybris.platform.ruleengineservices.rao.RuleEngineResultRAO;
import de.hybris.platform.ruleengineservices.rule.evaluation.RuleActionContext;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.drools.core.spi.KnowledgeHelper;

public class DefaultDroolsRuleActionContext implements RuleActionContext
{
    private final Map<String, Object> variables;
    private final KnowledgeHelper helper;
    private Map<String, Object> parameters;
    private final Set<Object> factsToUpdate;


    public DefaultDroolsRuleActionContext(Map<String, Object> variables, Object helper)
    {
        this.variables = variables;
        this.helper = (KnowledgeHelper)helper;
        this.factsToUpdate = Sets.newHashSet();
    }


    public Map<String, Object> getVariables()
    {
        return this.variables;
    }


    public Object getDelegate()
    {
        return this.helper;
    }


    public String getMetaData(String key)
    {
        Object value = this.helper.getRule().getMetaData().get(key);
        return (value == null) ? null : value.toString();
    }


    public <T> T getValue(Class<T> type)
    {
        Set<T> values = findValues(type, new String[0]);
        if(CollectionUtils.isNotEmpty(values))
        {
            return values.iterator().next();
        }
        return null;
    }


    public void scheduleForUpdate(Object... facts)
    {
        this.factsToUpdate.addAll(Arrays.asList(facts));
    }


    public <T> Set<T> getValues(Class<T> type)
    {
        return findValues(type, new String[0]);
    }


    public <T> T getValue(Class<T> type, String... path)
    {
        Set<T> values = findValues(type, path);
        if(CollectionUtils.isNotEmpty(values))
        {
            return values.iterator().next();
        }
        return null;
    }


    public <T> Set<T> getValues(Class<T> type, String... path)
    {
        return findValues(type, path);
    }


    public void insertFacts(Object... facts)
    {
        if(ArrayUtils.isNotEmpty(facts))
        {
            Objects.requireNonNull(this.helper);
            Arrays.<Object>stream(facts).forEach(this.helper::insert);
        }
    }


    public void updateScheduledFacts()
    {
        if(CollectionUtils.isNotEmpty(this.factsToUpdate))
        {
            updateFacts(this.factsToUpdate.toArray(new Object[0]));
            this.factsToUpdate.clear();
        }
    }


    public void updateFacts(Object... facts)
    {
        if(ArrayUtils.isNotEmpty(facts))
        {
            Objects.requireNonNull(this.helper);
            Arrays.<Object>stream(facts).forEach(this.helper::update);
        }
    }


    public void insertFacts(Collection facts)
    {
        if(CollectionUtils.isNotEmpty(facts))
        {
            Objects.requireNonNull(this.helper);
            facts.forEach(this.helper::insert);
        }
    }


    protected <T> Set<T> findValues(Class<T> type, String... path)
    {
        String key = (path.length == 0) ? type.getName() : (StringUtils.join((Object[])path, "/") + "/" + StringUtils.join((Object[])path, "/"));
        Object value = this.variables.get(key);
        if(value instanceof Set)
        {
            Set<T> values = (Set<T>)value;
            return evaluateValues(type, values, path);
        }
        if(value instanceof List)
        {
            List<T> values = (List<T>)value;
            return evaluateValues(type, values, path);
        }
        if(Objects.nonNull(value))
        {
            return (Set<T>)ImmutableSet.of(value);
        }
        return Collections.emptySet();
    }


    protected <T> Set<T> evaluateValues(Class<T> type, List<T> values, String... path)
    {
        if(CollectionUtils.isNotEmpty(values))
        {
            return new HashSet<>(values);
        }
        if(ArrayUtils.isEmpty((Object[])path))
        {
            return Collections.emptySet();
        }
        return findValues(type, Arrays.<String>copyOf(path, path.length - 1));
    }


    protected <T> Set<T> evaluateValues(Class<T> type, Set<T> values, String... path)
    {
        if(CollectionUtils.isNotEmpty(values))
        {
            return (Set<T>)values.stream().filter(Objects::nonNull).collect(Collectors.toSet());
        }
        if(ArrayUtils.isEmpty((Object[])path))
        {
            return Collections.emptySet();
        }
        return findValues(type, Arrays.<String>copyOf(path, path.length - 1));
    }


    public CartRAO getCartRao()
    {
        return getValue(CartRAO.class);
    }


    public RuleEngineResultRAO getRuleEngineResultRao()
    {
        return getValue(RuleEngineResultRAO.class);
    }


    public String getRuleName()
    {
        KnowledgeHelper knowledgeHelper = checkAndGetRuleContext(getDelegate());
        return knowledgeHelper.getRule().getName();
    }


    public Optional<String> getRulesModuleName()
    {
        Optional<String> rulesModuleName = Optional.empty();
        KnowledgeHelper knowledgeHelper = checkAndGetRuleContext(getDelegate());
        Map<String, Object> metaData = knowledgeHelper.getRule().getMetaData();
        if(MapUtils.isNotEmpty(metaData))
        {
            rulesModuleName = Optional.ofNullable((String)metaData.get("moduleName"));
        }
        return rulesModuleName;
    }


    public Map<String, Object> getRuleMetadata()
    {
        KnowledgeHelper knowledgeHelper = checkAndGetRuleContext(getDelegate());
        return knowledgeHelper.getRule().getMetaData();
    }


    protected KnowledgeHelper checkAndGetRuleContext(Object ruleContext)
    {
        Preconditions.checkState(ruleContext instanceof KnowledgeHelper, "ruleContext must be of type org.drools.core.spi.KnowledgeHelper.");
        return (KnowledgeHelper)ruleContext;
    }


    public Map<String, Object> getParameters()
    {
        return this.parameters;
    }


    public Object getParameter(String parameterName)
    {
        Map<String, Object> params = getParameters();
        return (params != null) ? params.get(parameterName) : null;
    }


    public <T> T getParameter(String parameterName, Class<T> type)
    {
        Map<String, Object> params = getParameters();
        Preconditions.checkArgument((params != null && params
                                        .containsKey(parameterName) && params.get(parameterName).getClass().isAssignableFrom(type)),
                        String.format("Property '%1$s' must not be null and must be of type %2$s", new Object[] {parameterName, type.getName()}));
        return (T)params.get(parameterName);
    }


    public void setParameters(Map<String, Object> parameters)
    {
        this.parameters = (parameters == null) ? null : Collections.<String, Object>unmodifiableMap(parameters);
    }


    public void halt()
    {
        KnowledgeHelper knowledgeHelper = checkAndGetRuleContext(getDelegate());
        knowledgeHelper.halt();
    }
}
