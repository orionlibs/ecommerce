package com.hybris.datahub.model;

import java.util.Map;

public abstract class ExportCodeAwareTargetItem
{
    public static Map<String, String> getExportCodeAttributeMap(TargetItem targetItem)
    {
        return targetItem.getExportCodeAttributeMap();
    }


    public static void setExportCodeAttributeMap(Map<String, String> exportCodeAttributeMap, TargetItem targetItem)
    {
        targetItem.setExportCodeAttributeMap(exportCodeAttributeMap);
    }


    public static void addToExportCodeMap(String key, String value, TargetItem targetItem)
    {
        targetItem.addToExportCodeMap(key, value);
    }
}
