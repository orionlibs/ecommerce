package de.hybris.platform.catalog.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum IDType implements HybrisEnumValue
{
    DUNS("duns"),
    ILN("iln"),
    SUPPLIER_SPECIFIC("supplier_specific"),
    BUYER_SPECIFIC("buyer_specific"),
    UNSPECIFIED("unspecified");
    public static final String _TYPECODE = "IDType";
    public static final String SIMPLE_CLASSNAME = "IDType";
    private final String code;


    IDType(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "IDType";
    }
}
