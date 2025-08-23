package de.hybris.platform.catalog.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum EtimVersion implements HybrisEnumValue
{
    VERSION_2_0("VERSION_2_0"),
    VERSION_3_0("VERSION_3_0");
    public static final String _TYPECODE = "EtimVersion";
    public static final String SIMPLE_CLASSNAME = "EtimVersion";
    private final String code;


    EtimVersion(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "EtimVersion";
    }
}
