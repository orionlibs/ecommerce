package de.hybris.platform.sap.core.configuration.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum SncQoP implements HybrisEnumValue
{
    AUTHENTICATION_ONLY("AUTHENTICATION_ONLY"),
    INTEGRITY_PROTECTION("INTEGRITY_PROTECTION"),
    PRIVACY_PROTECTION("PRIVACY_PROTECTION");
    public static final String _TYPECODE = "SncQoP";
    public static final String SIMPLE_CLASSNAME = "SncQoP";
    private final String code;


    SncQoP(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "SncQoP";
    }
}
