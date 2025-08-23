package de.hybris.platform.sap.core.configuration.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum JCoCPICTrace implements HybrisEnumValue
{
    NO_TRACE("NO_TRACE"),
    GLOBAL_TRACELEVEL("GLOBAL_TRACELEVEL"),
    MINIMAL_TRACELEVEL("MINIMAL_TRACELEVEL"),
    FULL_TRACELEVEL("FULL_TRACELEVEL"),
    FULL_TRACELEVEL_DATA("FULL_TRACELEVEL_DATA");
    public static final String _TYPECODE = "JCoCPICTrace";
    public static final String SIMPLE_CLASSNAME = "JCoCPICTrace";
    private final String code;


    JCoCPICTrace(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "JCoCPICTrace";
    }
}
