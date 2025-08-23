package de.hybris.bootstrap.loader.rule.internal;

import de.hybris.bootstrap.util.ConfigParameterHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RuleParamsConfigurator
{
    public static List<IgnoreClassLoaderRuleParam> loadIgnoreRuleClassLoaderProperties(Map<String, String> properties)
    {
        Map<Integer, IgnoreClassLoaderRuleParam> ruleListParams = new HashMap<>();
        Map<String, String> activeRules = ConfigParameterHelper.getParametersMatching(properties, "YURLCLASSLOADER\\.IGNORE\\.RULE\\.(.*)\\.CLASS", true);
        Map<String, String> paramsForRules = ConfigParameterHelper.getParametersMatching(properties, "YURLCLASSLOADER\\.IGNORE\\.RULE\\.(.*)\\.PARAMS", true);
        for(Map.Entry<String, String> entry : activeRules.entrySet())
        {
            String order = entry.getKey();
            String name = entry.getValue();
            Integer orderInt = Integer.valueOf(order);
            if(ruleListParams.containsKey(orderInt))
            {
                throw new IllegalStateException("Rule number: " + order + " exist more than once");
            }
            ruleListParams.put(orderInt, new IgnoreClassLoaderRuleParam(orderInt, name));
        }
        for(Map.Entry<String, String> entry : paramsForRules.entrySet())
        {
            String order = entry.getKey();
            Integer orderInt = Integer.valueOf(order);
            if(ruleListParams.containsKey(orderInt))
            {
                ((IgnoreClassLoaderRuleParam)ruleListParams.get(orderInt)).setParams(entry.getValue());
            }
        }
        return new ArrayList<>(ruleListParams.values());
    }
}
