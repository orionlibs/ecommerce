package de.hybris.platform.cms2lib.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum FlashScale implements HybrisEnumValue
{
    DEFAULT("default"),
    NOORDER("noorder"),
    EXACTFIT("exactfit");
    public static final String _TYPECODE = "FlashScale";
    public static final String SIMPLE_CLASSNAME = "FlashScale";
    private final String code;


    FlashScale(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "FlashScale";
    }
}
