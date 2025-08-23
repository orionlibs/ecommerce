package de.hybris.platform.acceleratorservices.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum ImportStatus implements HybrisEnumValue
{
    PROCESSING("PROCESSING"),
    COMPLETED("COMPLETED");
    public static final String _TYPECODE = "ImportStatus";
    public static final String SIMPLE_CLASSNAME = "ImportStatus";
    private final String code;


    ImportStatus(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "ImportStatus";
    }
}
