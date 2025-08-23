package de.hybris.platform.acceleratorcms.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum CartTotalDisplayType implements HybrisEnumValue
{
    SUBTOTAL("SUBTOTAL"),
    TOTAL_WITHOUT_DELIVERY("TOTAL_WITHOUT_DELIVERY"),
    TOTAL("TOTAL");
    public static final String _TYPECODE = "CartTotalDisplayType";
    public static final String SIMPLE_CLASSNAME = "CartTotalDisplayType";
    private final String code;


    CartTotalDisplayType(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "CartTotalDisplayType";
    }
}
