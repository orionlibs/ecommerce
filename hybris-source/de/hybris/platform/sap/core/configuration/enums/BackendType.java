package de.hybris.platform.sap.core.configuration.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum BackendType implements HybrisEnumValue
{
    ERP("ERP");
    private final String code;
    public static final String SIMPLE_CLASSNAME = "BackendType";
    public static final String _TYPECODE = "BackendType";


    BackendType(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "BackendType";
    }
}
