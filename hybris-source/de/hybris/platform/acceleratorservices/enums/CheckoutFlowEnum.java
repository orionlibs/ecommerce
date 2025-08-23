package de.hybris.platform.acceleratorservices.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum CheckoutFlowEnum implements HybrisEnumValue
{
    MULTISTEP("MULTISTEP"),
    SINGLE("SINGLE");
    public static final String _TYPECODE = "CheckoutFlowEnum";
    public static final String SIMPLE_CLASSNAME = "CheckoutFlowEnum";
    private final String code;


    CheckoutFlowEnum(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "CheckoutFlowEnum";
    }
}
