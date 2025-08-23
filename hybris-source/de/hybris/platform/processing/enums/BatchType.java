package de.hybris.platform.processing.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum BatchType implements HybrisEnumValue
{
    INITIAL("INITIAL"),
    INPUT("INPUT"),
    RESULT("RESULT");
    public static final String _TYPECODE = "BatchType";
    public static final String SIMPLE_CLASSNAME = "BatchType";
    private final String code;


    BatchType(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "BatchType";
    }
}
