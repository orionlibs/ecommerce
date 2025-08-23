package de.hybris.platform.basecommerce.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum InStockStatus implements HybrisEnumValue
{
    FORCEINSTOCK("forceInStock"),
    FORCEOUTOFSTOCK("forceOutOfStock"),
    NOTSPECIFIED("notSpecified");
    public static final String _TYPECODE = "InStockStatus";
    public static final String SIMPLE_CLASSNAME = "InStockStatus";
    private final String code;


    InStockStatus(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "InStockStatus";
    }
}
