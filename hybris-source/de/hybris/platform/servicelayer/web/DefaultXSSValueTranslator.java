package de.hybris.platform.servicelayer.web;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class DefaultXSSValueTranslator implements XSSFilter.XSSValueTranslator
{
    private final List<Pattern> patterns;


    public DefaultXSSValueTranslator(List<Pattern> patterns)
    {
        this.patterns = patterns;
    }


    public Map<String, String[]> translateHeaders(Map<String, String[]> original)
    {
        return stringValuesFromMap(original);
    }


    public Map<String, String[]> translateParameters(Map<String, String[]> original)
    {
        return stringValuesFromMap(original);
    }


    private Map<String, String[]> stringValuesFromMap(Map<String, String[]> original)
    {
        if(original != null)
        {
            Map<String, String[]> copy = null;
            for(Map.Entry<String, String[]> entry : original.entrySet())
            {
                String[] originalValues = entry.getValue();
                String[] strippedValues = stripXSS(originalValues);
                if(strippedValues != originalValues)
                {
                    if(copy == null)
                    {
                        copy = (Map)new LinkedHashMap<>((Map)original);
                    }
                    copy.put(entry.getKey(), strippedValues);
                }
            }
            return (copy == null) ? original : copy;
        }
        return original;
    }


    private String[] stripXSS(String[] values)
    {
        if(values == null || values.length == 0)
        {
            return values;
        }
        String[] copyValues = null;
        int i = 0;
        for(String vOrig : values)
        {
            String vStripped = stripXSS(vOrig);
            if(vStripped != vOrig)
            {
                if(copyValues == null)
                {
                    int l = values.length;
                    copyValues = new String[l];
                    System.arraycopy(values, 0, copyValues, 0, l);
                }
                copyValues[i] = vStripped;
            }
            i++;
        }
        return (copyValues == null) ? values : copyValues;
    }


    private String stripXSS(String value)
    {
        if(value != null && value.length() > 0)
        {
            for(Pattern scriptPattern : this.patterns)
            {
                value = scriptPattern.matcher(value).replaceAll("");
            }
        }
        return value;
    }
}
