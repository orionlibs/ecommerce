package de.hybris.platform.core;

import java.io.Serializable;
import java.util.Map;

public abstract class FlexibleSearchTranslatable implements Serializable
{
    public abstract void toFlexibleSearch(StringBuilder paramStringBuilder, Map<String, String> paramMap, Map<String, Object> paramMap1);


    public void toPolyglotSearch(StringBuilder queryBuffer, Map<String, String> typeIndexMap, Map<String, Object> valueMap)
    {
        toFlexibleSearch(queryBuffer, typeIndexMap, valueMap);
    }


    protected static String getAliasFromTypeMap(Map<String, String> typeIndexMap, String typeIdentifier)
    {
        String alias = typeIndexMap.get(typeIdentifier);
        if(alias == null)
        {
            throw new IllegalStateException("Unknown type identifier " + typeIdentifier + " - expected one of " + typeIndexMap
                            .keySet());
        }
        return alias;
    }
}
