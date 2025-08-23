package de.hybris.platform.basecommerce.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum OrderCancelEntryStatus implements HybrisEnumValue
{
    FULL("FULL"),
    PARTIAL("PARTIAL"),
    DENIED("DENIED");
    public static final String _TYPECODE = "OrderCancelEntryStatus";
    public static final String SIMPLE_CLASSNAME = "OrderCancelEntryStatus";
    private final String code;


    OrderCancelEntryStatus(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "OrderCancelEntryStatus";
    }
}
