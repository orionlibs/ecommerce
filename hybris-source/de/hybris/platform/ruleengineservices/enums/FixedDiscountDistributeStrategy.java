package de.hybris.platform.ruleengineservices.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum FixedDiscountDistributeStrategy implements HybrisEnumValue
{
    UNIFORM("UNIFORM");
    private final String code;
    public static final String SIMPLE_CLASSNAME = "FixedDiscountDistributeStrategy";
    public static final String _TYPECODE = "FixedDiscountDistributeStrategy";


    FixedDiscountDistributeStrategy(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "FixedDiscountDistributeStrategy";
    }
}
