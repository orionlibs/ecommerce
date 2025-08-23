package de.hybris.platform.basecommerce.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum OrderReturnEntryStatus implements HybrisEnumValue
{
    ARRIVED("ARRIVED"),
    WAITING("WAITING");
    public static final String _TYPECODE = "OrderReturnEntryStatus";
    public static final String SIMPLE_CLASSNAME = "OrderReturnEntryStatus";
    private final String code;


    OrderReturnEntryStatus(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "OrderReturnEntryStatus";
    }
}
