package de.hybris.platform.core.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum CreditCardType implements HybrisEnumValue
{
    AMEX("amex"),
    MAESTRO("maestro"),
    SWITCH("switch"),
    VISA("visa"),
    MASTER("master"),
    MASTERCARD_EUROCARD("mastercard_eurocard"),
    DINERS("diners");
    public static final String _TYPECODE = "CreditCardType";
    public static final String SIMPLE_CLASSNAME = "CreditCardType";
    private final String code;


    CreditCardType(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "CreditCardType";
    }
}
