package de.hybris.platform.cms2lib.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum FlashSalign implements HybrisEnumValue
{
    L("l"),
    R("r"),
    T("t"),
    TL("tl"),
    TR("tr");
    public static final String _TYPECODE = "FlashSalign";
    public static final String SIMPLE_CLASSNAME = "FlashSalign";
    private final String code;


    FlashSalign(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "FlashSalign";
    }
}
