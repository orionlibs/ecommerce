package de.hybris.platform.basecommerce.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum OrderEntryStatus implements HybrisEnumValue
{
    LIVING("LIVING"),
    DEAD("DEAD");
    public static final String _TYPECODE = "OrderEntryStatus";
    public static final String SIMPLE_CLASSNAME = "OrderEntryStatus";
    private final String code;


    OrderEntryStatus(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "OrderEntryStatus";
    }
}
