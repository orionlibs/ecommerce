package com.hybris.backoffice.excel.translators.generic.factory;

import com.hybris.backoffice.excel.translators.generic.RequiredAttribute;
import java.util.ArrayList;
import java.util.List;

public class DefaultReferenceFormatFactory implements ReferenceFormatFactory
{
    public String create(RequiredAttribute requiredAttribute)
    {
        List<String> flatReferenceFormat = flatReferenceFormat(requiredAttribute);
        return String.join(":", (Iterable)flatReferenceFormat);
    }


    private static List<String> flatReferenceFormat(RequiredAttribute uniqueAttribute)
    {
        List<String> references = new ArrayList<>();
        if(uniqueAttribute.getChildren().isEmpty())
        {
            if(uniqueAttribute.getTypeModel() instanceof de.hybris.platform.core.model.type.MapTypeModel)
            {
                references.add("key");
                references.add("value");
            }
            else
            {
                references.add(String.format("%s.%s", new Object[] {uniqueAttribute.getEnclosingType(), uniqueAttribute.getQualifier()}));
            }
        }
        else
        {
            for(RequiredAttribute child : uniqueAttribute.getChildren())
            {
                references.addAll(flatReferenceFormat(child));
            }
        }
        return references;
    }
}
