package com.hybris.datahub.model;

import com.google.common.collect.ImmutableMap;
import com.hybris.datahub.domain.RawAttributeModelDefinition;
import com.hybris.datahub.domain.RawItemMetadata;
import java.util.HashMap;
import java.util.Map;

public abstract class MetadataAwareRawItem extends RawItem
{
    protected MetadataAwareRawItem(String type)
    {
        super(type);
    }


    public static void setItemMetadata(RawItemMetadata metadata)
    {
        rawItemMetadataMap.put(metadata.getItemType(), convertMetadataAttributesToMap(metadata));
        memoizedAttributeDefinition.clear();
        memoizedTypeDefinitions.clear();
    }


    public static void removeItemMetadata(String itemType)
    {
        rawItemMetadataMap.remove(itemType);
    }


    private static ImmutableMap<String, Boolean> convertMetadataAttributesToMap(RawItemMetadata metadata)
    {
        Map<String, Boolean> attrMap = new HashMap<>();
        if(metadata.getRawAttributeDefinitions() != null)
        {
            for(RawAttributeModelDefinition definition : metadata.getRawAttributeDefinitions())
            {
                attrMap.put(definition.getAttributeName(), Boolean.valueOf(definition.isSecured()));
            }
        }
        return ImmutableMap.copyOf(attrMap);
    }
}
