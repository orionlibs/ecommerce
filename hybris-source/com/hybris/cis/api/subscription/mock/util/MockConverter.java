package com.hybris.cis.api.subscription.mock.util;

import com.hybris.cis.api.model.AnnotationHashMap;
import com.hybris.cis.api.model.AnnotationHashMapEntryType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.util.CollectionUtils;

public abstract class MockConverter
{
    protected MockConverter()
    {
        throw new IllegalStateException("Utility class");
    }


    protected static AnnotationHashMap convertMapToVendorParameters(Map<String, String> map)
    {
        if(!CollectionUtils.isEmpty(map))
        {
            List<AnnotationHashMapEntryType> entryTypeList = new ArrayList<>();
            for(Map.Entry<String, String> entry : map.entrySet())
            {
                AnnotationHashMapEntryType entryType = new AnnotationHashMapEntryType(entry);
                entryTypeList.add(entryType);
            }
            AnnotationHashMap annotationHashMap = new AnnotationHashMap();
            annotationHashMap.setEntries(entryTypeList);
            return annotationHashMap;
        }
        return null;
    }


    protected static Map<String, String> convertVendorParametersToMap(AnnotationHashMap vendorParameters)
    {
        if(vendorParameters == null || CollectionUtils.isEmpty(vendorParameters.getEntries()))
        {
            return Collections.emptyMap();
        }
        Map<String, String> vendorParamMap = new HashMap<>();
        for(AnnotationHashMapEntryType entryType : vendorParameters.getEntries())
        {
            vendorParamMap.put(entryType.getKey(), entryType.getValue());
        }
        return vendorParamMap;
    }
}
