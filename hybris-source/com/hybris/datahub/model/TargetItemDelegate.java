package com.hybris.datahub.model;

import java.util.Map;

public interface TargetItemDelegate
{
    Map<String, String> getExportCodeAttributeMap();


    CanonicalItem getCanonicalItem();
}
