package de.hybris.platform.cms2lib.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum FlashQuality implements HybrisEnumValue
{
    LOW("low"),
    AUTOLOW("autolow"),
    AUTOHIGH("autohigh"),
    MEDIUM("medium"),
    HIGH("high"),
    BEST("best");
    public static final String _TYPECODE = "FlashQuality";
    public static final String SIMPLE_CLASSNAME = "FlashQuality";
    private final String code;


    FlashQuality(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "FlashQuality";
    }
}
