package de.hybris.platform.commerceservices.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum SiteChannel implements HybrisEnumValue
{
    B2B("B2B"),
    B2C("B2C");
    public static final String _TYPECODE = "SiteChannel";
    public static final String SIMPLE_CLASSNAME = "SiteChannel";
    private final String code;


    SiteChannel(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "SiteChannel";
    }
}
