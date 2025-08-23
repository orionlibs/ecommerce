package com.hybris.backoffice.excel.importing.parser;

import com.hybris.backoffice.excel.importing.parser.matcher.ExcelParserMatcher;
import com.hybris.backoffice.excel.importing.parser.splitter.ExcelParserSplitter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultImportParameterParser implements ImportParameterParser
{
    private ExcelParserMatcher matcher;
    private ExcelParserSplitter splitter;
    private int order;


    public boolean matches(@Nonnull String referenceFormat)
    {
        return this.matcher.test(referenceFormat);
    }


    public DefaultValues parseDefaultValues(String referenceFormat, String defaultValues)
    {
        String trimmedReferenceFormat = StringUtils.trim(referenceFormat);
        String trimmedDefaultValues = StringUtils.trim(defaultValues);
        Map<String, String> defaultValuesMap = new LinkedHashMap<>();
        if(StringUtils.isBlank(trimmedReferenceFormat))
        {
            return new DefaultValues(trimmedDefaultValues, trimmedReferenceFormat, defaultValuesMap);
        }
        String[] referenceFormatTokens = this.splitter.apply(trimmedReferenceFormat);
        String[] defaultValuesTokens = this.splitter.apply(trimmedDefaultValues);
        for(int i = 0; i < referenceFormatTokens.length; i++)
        {
            String referenceFormatToken = referenceFormatTokens[i];
            String defaultValueToken = (defaultValuesTokens.length > i && StringUtils.isNotBlank(defaultValuesTokens[i])) ? defaultValuesTokens[i] : null;
            defaultValuesMap.put(referenceFormatToken, defaultValueToken);
        }
        return new DefaultValues(trimmedDefaultValues, trimmedReferenceFormat, defaultValuesMap);
    }


    public ParsedValues parseValue(String cellValue, DefaultValues defaultValues)
    {
        String trimmedCellValue = StringUtils.trim(cellValue);
        Map<String, String> defaultValuesMap = defaultValues.toMap();
        List<Map<String, String>> convertedParameters = new ArrayList<>();
        String[] multiValues = StringUtils.splitPreserveAllTokens(trimmedCellValue, ",");
        if(ArrayUtils.isEmpty((Object[])multiValues))
        {
            if(!defaultValuesMap.isEmpty())
            {
                defaultValuesMap.put("rawValue", joinValues(defaultValues.getValues()));
            }
            else if(StringUtils.isBlank(trimmedCellValue) && StringUtils.isNotBlank(defaultValues.getDefaultValues()))
            {
                defaultValuesMap.put("rawValue", defaultValues.getDefaultValues());
            }
            else
            {
                defaultValuesMap.put("rawValue", trimmedCellValue);
            }
            convertedParameters.add(defaultValuesMap);
        }
        for(String multivalue : multiValues)
        {
            Map<String, String> params = new LinkedHashMap<>();
            String[] providedValues = this.splitter.apply(multivalue);
            int index = 0;
            List<String> values = new ArrayList<>();
            if(CollectionUtils.isEmpty(defaultValues.toMap().entrySet()))
            {
                values.add(multivalue);
            }
            for(Map.Entry<String, String> defaultValue : (Iterable<Map.Entry<String, String>>)defaultValues.toMap().entrySet())
            {
                String value = (providedValues.length > index && StringUtils.isNotBlank(providedValues[index])) ? providedValues[index] : defaultValue.getValue();
                params.put(defaultValue.getKey(), value);
                values.add(value);
                index++;
            }
            params.put("rawValue", joinValues(values));
            convertedParameters.add(params);
        }
        return new ParsedValues(
                        String.join(",", (Iterable<? extends CharSequence>)convertedParameters
                                        .stream().map(map -> (String)map.get("rawValue")).collect(Collectors.toList())), convertedParameters);
    }


    private static String joinValues(Collection<String> values)
    {
        List<String> valuesWithoutNulls = (List<String>)values.stream().map(value -> (value == null) ? "" : value).collect(Collectors.toList());
        if(valuesWithoutNulls.stream().anyMatch(StringUtils::isNotBlank))
        {
            return String.join(":", (Iterable)valuesWithoutNulls);
        }
        return "";
    }


    @Required
    public void setMatcher(ExcelParserMatcher matcher)
    {
        this.matcher = matcher;
    }


    @Required
    public void setSplitter(ExcelParserSplitter splitter)
    {
        this.splitter = splitter;
    }


    public int getOrder()
    {
        return this.order;
    }


    @Required
    public void setOrder(int order)
    {
        this.order = order;
    }
}
