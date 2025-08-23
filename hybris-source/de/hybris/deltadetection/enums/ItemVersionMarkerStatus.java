package de.hybris.deltadetection.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum ItemVersionMarkerStatus implements HybrisEnumValue
{
    ACTIVE("ACTIVE"),
    DELETED("DELETED");
    public static final String _TYPECODE = "ItemVersionMarkerStatus";
    public static final String SIMPLE_CLASSNAME = "ItemVersionMarkerStatus";
    private final String code;


    ItemVersionMarkerStatus(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "ItemVersionMarkerStatus";
    }
}
