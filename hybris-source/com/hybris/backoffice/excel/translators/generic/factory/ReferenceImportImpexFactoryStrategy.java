package com.hybris.backoffice.excel.translators.generic.factory;

import com.hybris.backoffice.excel.data.Impex;
import com.hybris.backoffice.excel.data.ImpexForType;
import com.hybris.backoffice.excel.data.ImpexHeaderValue;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.translators.generic.RequiredAttribute;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

public class ReferenceImportImpexFactoryStrategy implements ImportImpexFactoryStrategy
{
    public boolean canHandle(RequiredAttribute rootUniqueAttribute, ImportParameters importParameters)
    {
        return !rootUniqueAttribute.isPartOf();
    }


    public Impex create(RequiredAttribute rootUniqueAttribute, ImportParameters importParameters)
    {
        Impex impex = new Impex();
        ImpexForType impexForCurrentType = impex.findUpdates(importParameters.getTypeCode());
        impexForCurrentType.putValue(Integer.valueOf(0), prepareImpexHeader(rootUniqueAttribute, importParameters),
                        prepareImpexValue(rootUniqueAttribute, importParameters));
        return impex;
    }


    @Deprecated(since = "2005", forRemoval = true)
    protected ImpexHeaderValue prepareImpexHeader(RequiredAttribute rootUniqueAttribute, boolean unique, boolean mandatory)
    {
        String header = innerPrepareImpexHeader(rootUniqueAttribute);
        return (new ImpexHeaderValue.Builder(header)).withUnique(unique).withMandatory(mandatory)
                        .withQualifier(rootUniqueAttribute.getQualifier()).build();
    }


    protected ImpexHeaderValue prepareImpexHeader(RequiredAttribute rootUniqueAttribute, ImportParameters importParameters)
    {
        String header = innerPrepareImpexHeader(rootUniqueAttribute);
        return (new ImpexHeaderValue.Builder(header)).withUnique(rootUniqueAttribute.isUnique())
                        .withMandatory(rootUniqueAttribute.isMandatory()).withQualifier(rootUniqueAttribute.getQualifier())
                        .withLang(importParameters.getIsoCode()).build();
    }


    private static String innerPrepareImpexHeader(RequiredAttribute requiredAttribute)
    {
        List<String> childrenValues = new ArrayList<>();
        for(RequiredAttribute child : requiredAttribute.getChildren())
        {
            String childValue = innerPrepareImpexHeader(child);
            childrenValues.add(childValue);
        }
        String joinedChildrenValues = String.join(",", (Iterable)childrenValues);
        if(!requiredAttribute.getChildren().isEmpty())
        {
            joinedChildrenValues = String.format("(%s)", new Object[] {joinedChildrenValues});
        }
        return requiredAttribute.getQualifier() + requiredAttribute.getQualifier();
    }


    protected String prepareImpexValue(RequiredAttribute rootUniqueAttribute, ImportParameters importParameters)
    {
        List<String> multiValues = new ArrayList<>();
        for(Map<String, String> params : (Iterable<Map<String, String>>)importParameters.getMultiValueParameters())
        {
            List<String> values = prepareImpexValueForSingleValue(rootUniqueAttribute, params);
            if(hasValue(values))
            {
                multiValues.add(String.join(":", (Iterable)values));
            }
        }
        return String.join(",", (Iterable)multiValues);
    }


    private static List<String> prepareImpexValueForSingleValue(RequiredAttribute attribute, Map<String, String> params)
    {
        List<String> values = new ArrayList<>();
        String attributeKey = String.format("%s.%s", new Object[] {attribute.getEnclosingType(), attribute.getQualifier()});
        if(attribute.getChildren().isEmpty() && params.containsKey(attributeKey))
        {
            values.add(params.get(attributeKey));
        }
        for(RequiredAttribute child : attribute.getChildren())
        {
            values.addAll(prepareImpexValueForSingleValue(child, params));
        }
        return (List<String>)values.stream().map(value -> (value == null) ? "" : value).collect(Collectors.toList());
    }


    private static boolean hasValue(List<String> values)
    {
        return values.stream().anyMatch(StringUtils::isNotBlank);
    }
}
