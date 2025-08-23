package de.hybris.platform.servicelayer.interceptor;

import de.hybris.platform.core.HybrisEnumValue;

public enum GenderEnumStub implements HybrisEnumValue
{
    MALE("MALE"),
    FEMALE("FEMALE");
    private final String code;


    GenderEnumStub(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "Gender";
    }
}
