package de.hybris.platform.searchservices.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum SnFieldType implements HybrisEnumValue
{
    STRING("STRING"),
    TEXT("TEXT"),
    BOOLEAN("BOOLEAN"),
    INTEGER("INTEGER"),
    LONG("LONG"),
    FLOAT("FLOAT"),
    DOUBLE("DOUBLE"),
    DATE_TIME("DATE_TIME");
    public static final String _TYPECODE = "SnFieldType";
    public static final String SIMPLE_CLASSNAME = "SnFieldType";
    private final String code;


    SnFieldType(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "SnFieldType";
    }
}
