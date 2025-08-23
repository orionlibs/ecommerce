package de.hybris.platform.catalog.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum ProfiClassVersion implements HybrisEnumValue
{
    VERSION_3_0("VERSION_3_0");
    private final String code;
    public static final String SIMPLE_CLASSNAME = "ProfiClassVersion";
    public static final String _TYPECODE = "ProfiClassVersion";


    ProfiClassVersion(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "ProfiClassVersion";
    }
}
