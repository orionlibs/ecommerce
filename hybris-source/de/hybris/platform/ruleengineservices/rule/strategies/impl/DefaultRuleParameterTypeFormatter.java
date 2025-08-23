package de.hybris.platform.ruleengineservices.rule.strategies.impl;

import de.hybris.platform.ruleengineservices.rule.strategies.RuleParameterTypeFormatter;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

public class DefaultRuleParameterTypeFormatter implements RuleParameterTypeFormatter
{
    private static final String DEFAULT_PARAM_TYPE = "java.lang.String";
    private Map<String, String> formats;


    public String formatParameterType(String paramType)
    {
        if(StringUtils.isEmpty(paramType))
        {
            return "java.lang.String";
        }
        String convertedType = "";
        if(MapUtils.isNotEmpty(this.formats))
        {
            convertedType = formatConfigurableTypes(paramType);
        }
        if(StringUtils.isEmpty(convertedType))
        {
            convertedType = paramType;
        }
        return convertedType;
    }


    protected String formatConfigurableTypes(String paramType)
    {
        for(Map.Entry<String, String> entry : this.formats.entrySet())
        {
            Matcher typeMatcher = Pattern.compile(entry.getKey()).matcher(paramType);
            if(typeMatcher.matches())
            {
                int matchesNumber = typeMatcher.groupCount();
                String[] arrayOfString = new String[matchesNumber];
                for(int i = 0; i < matchesNumber; i++)
                {
                    String formattedValue = formatParameterType(typeMatcher.group(i + 1));
                    arrayOfString[i] = formattedValue;
                }
                return String.format(entry.getValue(), (Object[])arrayOfString);
            }
        }
        return "";
    }


    public Map<String, String> getFormats()
    {
        return this.formats;
    }


    public void setFormats(Map<String, String> formats)
    {
        this.formats = formats;
    }
}
