package de.hybris.platform.basecommerce.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum OrderCancelState implements HybrisEnumValue
{
    PENDINGORHOLDINGAREA("PendingOrHoldingArea"),
    SENTTOWAREHOUSE("SentToWarehouse"),
    SHIPPING("Shipping"),
    PARTIALLYSHIPPED("PartiallyShipped"),
    CANCELIMPOSSIBLE("CancelImpossible");
    public static final String _TYPECODE = "OrderCancelState";
    public static final String SIMPLE_CLASSNAME = "OrderCancelState";
    private final String code;


    OrderCancelState(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "OrderCancelState";
    }
}
