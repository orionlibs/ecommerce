package de.hybris.platform.ruleengineservices.rule.strategies.impl;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.type.CollectionType;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;
import de.hybris.platform.ruleengineservices.rule.strategies.RuleConverterException;
import de.hybris.platform.ruleengineservices.rule.strategies.RuleParametersConverter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

public class DefaultRuleParametersConverter extends AbstractRuleConverter implements RuleParametersConverter
{
    public String toString(List<RuleParameterData> parameters)
    {
        try
        {
            return getObjectWriter().writeValueAsString(parameters);
        }
        catch(IOException e)
        {
            throw new RuleConverterException(e);
        }
    }


    public List<RuleParameterData> fromString(String parameters)
    {
        if(StringUtils.isBlank(parameters))
        {
            return Collections.emptyList();
        }
        try
        {
            ObjectReader objectReader = getObjectReader();
            CollectionType collectionType = objectReader.getTypeFactory().constructCollectionType(List.class, RuleParameterData.class);
            List<RuleParameterData> parsedParameters = (List<RuleParameterData>)objectReader.forType((JavaType)collectionType).readValue(parameters);
            convertParameterValues(parsedParameters);
            return parsedParameters;
        }
        catch(IOException e)
        {
            throw new RuleConverterException(e);
        }
    }


    protected void convertParameterValues(List<RuleParameterData> parameters)
    {
        if(CollectionUtils.isEmpty(parameters))
        {
            return;
        }
        for(RuleParameterData parameter : parameters)
        {
            Object value = getRuleParameterValueConverter().fromString((String)parameter.getValue(), parameter.getType());
            parameter.setValue(value);
        }
    }
}
