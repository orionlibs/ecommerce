package de.hybris.platform.processengine.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum BooleanOperator implements HybrisEnumValue
{
    AND("AND"),
    OR("OR");
    public static final String _TYPECODE = "BooleanOperator";
    public static final String SIMPLE_CLASSNAME = "BooleanOperator";
    private final String code;


    BooleanOperator(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "BooleanOperator";
    }
}
