package de.hybris.platform.cms2lib.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum FlashWmode implements HybrisEnumValue
{
    WINDOW("window"),
    OPAQUE("opaque"),
    TRANSPARENT("transparent");
    public static final String _TYPECODE = "FlashWmode";
    public static final String SIMPLE_CLASSNAME = "FlashWmode";
    private final String code;


    FlashWmode(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "FlashWmode";
    }
}
