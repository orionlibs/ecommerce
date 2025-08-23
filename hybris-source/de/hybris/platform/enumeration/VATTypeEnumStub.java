package de.hybris.platform.enumeration;

import de.hybris.platform.core.HybrisEnumValue;

public enum VATTypeEnumStub implements HybrisEnumValue
{
    FULL("FULL"),
    HALF("HALF"),
    NONE("NONE");
    private final String code;


    VATTypeEnumStub(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "VATType";
    }
}
