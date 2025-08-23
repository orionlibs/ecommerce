package de.hybris.platform.ruleengine.exception;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.kie.api.definition.rule.Rule;

public class DroolsRuleLoopException extends RuleEngineRuntimeException
{
    private final long limit;
    private final transient Map<Rule, Long> ruleMap;


    public DroolsRuleLoopException(long limit, Map<Rule, Long> ruleMap)
    {
        this.limit = limit;
        this.ruleMap = ruleMap;
    }


    public long getLimit()
    {
        return this.limit;
    }


    public List<String> getAllRuleFirings()
    {
        return getRuleFirings(Long.MAX_VALUE);
    }


    public List<String> getRuleFirings(long size)
    {
        if(this.ruleMap == null)
        {
            return Collections.emptyList();
        }
        return (List<String>)this.ruleMap.entrySet().stream().sorted(Map.Entry.comparingByValue().reversed()).limit(size)
                        .map(e -> ((Long)e.getValue()).toString() + ":" + ((Long)e.getValue()).toString()).collect(Collectors.toList());
    }


    public String getMessage()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Possible rule-loop detected. Maximum allowed rule matches has been exceeded.").append(System.lineSeparator());
        sb.append("Current Limit:").append(this.limit).append(System.lineSeparator());
        for(String ruleFiring : getRuleFirings(10L))
        {
            sb.append(ruleFiring).append(System.lineSeparator());
        }
        sb.append("You can adjust or disable the limit for rule matches by changing the ruleFiringLimit field in the 'Drools Engine Context' object (see the 'Rule Firing Limit' attribute).\n");
        return sb.toString();
    }
}
