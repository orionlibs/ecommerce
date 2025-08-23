package de.hybris.platform.catalog.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum EclassVersion implements HybrisEnumValue
{
    VERSION_4_10("VERSION_4_10"),
    VERSION_5_00("VERSION_5_00"),
    VERSION_5_10("VERSION_5_10");
    public static final String _TYPECODE = "EclassVersion";
    public static final String SIMPLE_CLASSNAME = "EclassVersion";
    private final String code;


    EclassVersion(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "EclassVersion";
    }
}
