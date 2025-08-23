package de.hybris.platform.cockpit.services.search.impl;

import de.hybris.platform.cockpit.model.search.Operator;
import de.hybris.platform.cockpit.services.search.ConditionTranslator;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ConditionTranslatorRegistry implements ApplicationContextAware
{
    private ApplicationContext applicationContext = null;
    private final Map<String, ConditionTranslator> translatorMap = new HashMap<>();


    public void initTranslatorMappings()
    {
        Map<String, ConditionTranslatorMapping> mappingBeans = this.applicationContext.getBeansOfType(ConditionTranslatorMapping.class);
        for(Map.Entry<String, ConditionTranslatorMapping> entry : mappingBeans.entrySet())
        {
            for(String type : ((ConditionTranslatorMapping)entry.getValue()).getAttributeTypes())
            {
                for(String quali : ((ConditionTranslatorMapping)entry.getValue()).getOperatorQualifiers())
                {
                    String key = getKey(type, quali);
                    this.translatorMap.put(key, ((ConditionTranslatorMapping)entry.getValue()).getTranslator());
                }
            }
        }
    }


    protected String getKey(String attributeType, String operatorQualifier)
    {
        StringBuilder builder = new StringBuilder();
        if(attributeType != null)
        {
            builder.append(attributeType + "####");
        }
        if(operatorQualifier != null)
        {
            builder.append(operatorQualifier.toLowerCase());
        }
        return builder.toString();
    }


    public ConditionTranslator getTranslator(String attributeType, Operator operator)
    {
        return this.translatorMap.get(getKey(attributeType, operator.getQualifier()));
    }


    public ConditionTranslator getTranslator(String attributeType, String operatorQualifier)
    {
        return this.translatorMap.get(getKey(attributeType, operatorQualifier));
    }


    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        this.applicationContext = applicationContext;
    }
}
