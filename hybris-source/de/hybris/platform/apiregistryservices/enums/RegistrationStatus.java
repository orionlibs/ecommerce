package de.hybris.platform.apiregistryservices.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum RegistrationStatus implements HybrisEnumValue
{
    STARTED("STARTED"),
    IN_PROGRESS("IN_PROGRESS"),
    REGISTERED("REGISTERED"),
    ERROR("ERROR");
    public static final String _TYPECODE = "RegistrationStatus";
    public static final String SIMPLE_CLASSNAME = "RegistrationStatus";
    private final String code;


    RegistrationStatus(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "RegistrationStatus";
    }
}
