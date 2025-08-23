package de.hybris.platform.ruleengineservices.rule.strategies.impl;

import de.hybris.platform.ruleengineservices.rule.strategies.RuleParameterValueNormalizer;
import java.util.Collection;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

public class CatalogVersionStripperRuleParameterValueNormalizer implements RuleParameterValueNormalizer
{
    private String delimiter;


    public Object normalize(Object value)
    {
        if(value instanceof Collection)
        {
            return ((Collection)value).stream().map(this::normalizeSingleValue).collect(Collectors.toList());
        }
        return normalizeSingleValue(value);
    }


    protected Object normalizeSingleValue(Object value)
    {
        if(value == null)
        {
            return null;
        }
        return StringUtils.substringBefore(String.valueOf(value), getDelimiter());
    }


    protected String getDelimiter()
    {
        return this.delimiter;
    }


    public void setDelimiter(String delimiter)
    {
        this.delimiter = delimiter;
    }
}
