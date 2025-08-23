package de.hybris.platform.cms2lib.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum CarouselScroll implements HybrisEnumValue
{
    ONE("one"),
    ALLVISIBLE("allVisible");
    public static final String _TYPECODE = "CarouselScroll";
    public static final String SIMPLE_CLASSNAME = "CarouselScroll";
    private final String code;


    CarouselScroll(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "CarouselScroll";
    }
}
