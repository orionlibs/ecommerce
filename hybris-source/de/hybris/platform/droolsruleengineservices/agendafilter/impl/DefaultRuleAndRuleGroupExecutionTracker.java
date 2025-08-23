package de.hybris.platform.droolsruleengineservices.agendafilter.impl;

import de.hybris.platform.ruleengineservices.rule.evaluation.impl.RuleAndRuleGroupExecutionTracker;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.drools.core.definitions.rule.impl.RuleImpl;
import org.drools.core.spi.KnowledgeHelper;
import org.kie.api.definition.rule.Rule;

public class DefaultRuleAndRuleGroupExecutionTracker implements RuleAndRuleGroupExecutionTracker
{
    private final Map<String, Integer> executedRules = new HashMap<>();
    private final List<String> actionsInvoked = new ArrayList<>();
    private final Map<String, Map<String, Integer>> executedRuleGroups = new HashMap<>();


    public boolean allowedToExecute(Object theRule)
    {
        Rule rule = (Rule)theRule;
        String ruleCode = getRuleCode(rule);
        int maximumRuleExecutions = getMaximumRuleExecutions(rule);
        String ruleGroupCode = getRuleGroupCode(rule);
        boolean ruleGroupIsExclusive = isRuleGroupExclusive(rule);
        Integer currentRuns = getExecutedRules().get(ruleCode);
        if(currentRuns != null && maximumRuleExecutions <= currentRuns.intValue())
        {
            return false;
        }
        if(ruleGroupCode != null && ruleGroupIsExclusive)
        {
            Map<String, Integer> groupEntries = getExecutedRuleGroups().get(ruleGroupCode);
            if(Objects.nonNull(groupEntries) && !groupEntries.isEmpty() && !groupEntries.containsKey(ruleCode))
            {
                return false;
            }
        }
        return true;
    }


    public void trackActionExecutionStarted(String ruleCode)
    {
        this.actionsInvoked.add(ruleCode);
    }


    public void trackRuleExecution(Object kcontext)
    {
        KnowledgeHelper helper = (KnowledgeHelper)kcontext;
        RuleImpl ruleImpl = helper.getRule();
        String ruleCode = getRuleCode((Rule)ruleImpl);
        if(this.actionsInvoked.contains(ruleCode))
        {
            getExecutedRules().compute(ruleCode, (k, v) -> Integer.valueOf((v == null) ? 1 : Integer.valueOf(v.intValue() + 1).intValue()));
            String ruleGroupCode = getRuleGroupCode((Rule)ruleImpl);
            if(Objects.nonNull(ruleGroupCode))
            {
                Map<String, Integer> rgExecutions = getExecutedRuleGroups().computeIfAbsent(ruleGroupCode, k -> new HashMap<>());
                rgExecutions.compute(ruleCode, (k, v) -> Integer.valueOf((v == null) ? 1 : Integer.valueOf(v.intValue() + 1).intValue()));
            }
        }
    }


    protected int getMaximumRuleExecutions(Rule rule)
    {
        String maxRuns = getMetaData(rule, "maxRuleExecutions");
        return (maxRuns == null) ? 1000 : Integer.parseInt(maxRuns);
    }


    protected boolean isRuleGroupExclusive(Rule rule)
    {
        return Boolean.parseBoolean(getMetaData(rule, "ruleGroupExclusive"));
    }


    protected String getRuleGroupCode(Rule rule)
    {
        return getMetaData(rule, "ruleGroupCode");
    }


    protected String getRuleCode(Rule rule)
    {
        return getMetaData(rule, "ruleCode");
    }


    protected String getMetaData(Rule rule, String key)
    {
        Object value = rule.getMetaData().get(key);
        return Objects.isNull(value) ? null : value.toString();
    }


    protected Map<String, Integer> getExecutedRules()
    {
        return this.executedRules;
    }


    protected Map<String, Map<String, Integer>> getExecutedRuleGroups()
    {
        return this.executedRuleGroups;
    }


    protected List<String> getActionsInvoked()
    {
        return this.actionsInvoked;
    }
}
