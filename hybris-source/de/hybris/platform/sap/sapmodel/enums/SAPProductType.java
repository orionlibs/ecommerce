package de.hybris.platform.sap.sapmodel.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum SAPProductType implements HybrisEnumValue
{
    PHYSICAL("PHYSICAL"),
    SERVICE("SERVICE"),
    SUBSCRIPTION("SUBSCRIPTION");
    public static final String _TYPECODE = "SAPProductType";
    public static final String SIMPLE_CLASSNAME = "SAPProductType";
    private final String code;


    SAPProductType(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "SAPProductType";
    }
}
