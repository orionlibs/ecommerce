package de.hybris.platform.catalog.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum LineOfBusiness implements HybrisEnumValue
{
    TRADE("trade"),
    BANK("bank"),
    INDUSTRY("industry"),
    BUILDING("building"),
    GOVERNMENT("government"),
    SERVICE("service");
    public static final String _TYPECODE = "LineOfBusiness";
    public static final String SIMPLE_CLASSNAME = "LineOfBusiness";
    private final String code;


    LineOfBusiness(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "LineOfBusiness";
    }
}
