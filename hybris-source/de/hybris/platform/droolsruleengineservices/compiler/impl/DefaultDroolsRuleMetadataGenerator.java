package de.hybris.platform.droolsruleengineservices.compiler.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import de.hybris.platform.droolsruleengineservices.compiler.DroolsRuleGeneratorContext;
import de.hybris.platform.droolsruleengineservices.compiler.DroolsRuleMetadataGenerator;
import de.hybris.platform.ruleengineservices.compiler.RuleIrCondition;
import de.hybris.platform.ruleengineservices.util.DroolsStringUtils;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class DefaultDroolsRuleMetadataGenerator implements DroolsRuleMetadataGenerator
{
    private DroolsStringUtils droolsStringUtils;


    public String generateMetadata(DroolsRuleGeneratorContext context, String indentation)
    {
        List<RuleIrCondition> conditions = context.getRuleIr().getConditions();
        Map<String, List<Object>> conditionMetadata = Maps.newHashMap();
        List<Map<String, Object>> metadataList = (List<Map<String, Object>>)conditions.stream().filter(c -> Objects.nonNull(c.getMetadata())).map(RuleIrCondition::getMetadata).collect(Collectors.toList());
        for(Map<String, Object> metadata : metadataList)
        {
            for(Map.Entry<String, Object> entry : metadata.entrySet())
            {
                if(!conditionMetadata.containsKey(entry.getKey()))
                {
                    conditionMetadata.put(entry.getKey(), Lists.newArrayList());
                }
                if(entry.getValue() instanceof Collection)
                {
                    ((List)conditionMetadata.get(entry.getKey())).addAll((Collection)entry.getValue());
                    continue;
                }
                ((List)conditionMetadata.get(entry.getKey())).add(entry.getValue());
            }
        }
        StringJoiner conditionsJoiner = new StringJoiner("");
        for(Map.Entry<String, List<Object>> entry : conditionMetadata.entrySet())
        {
            String metadataValue = ((List)entry.getValue()).stream().map(o -> String.format("\"%s\"", new Object[] {getDroolsStringUtils().encodeMvelStringLiteral(o.toString())})).collect(Collectors.joining(","));
            conditionsJoiner.add("@").add(getDroolsStringUtils().validateVariableName(entry.getKey())).add(" ( ").add(metadataValue).add(" )\n");
        }
        return conditionsJoiner.toString();
    }


    protected DroolsStringUtils getDroolsStringUtils()
    {
        return this.droolsStringUtils;
    }


    public void setDroolsStringUtils(DroolsStringUtils droolsStringUtils)
    {
        this.droolsStringUtils = droolsStringUtils;
    }
}
