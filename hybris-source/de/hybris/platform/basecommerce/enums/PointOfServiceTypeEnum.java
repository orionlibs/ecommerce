package de.hybris.platform.basecommerce.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum PointOfServiceTypeEnum implements HybrisEnumValue
{
    STORE("STORE"),
    WAREHOUSE("WAREHOUSE"),
    POS("POS");
    public static final String _TYPECODE = "PointOfServiceTypeEnum";
    public static final String SIMPLE_CLASSNAME = "PointOfServiceTypeEnum";
    private final String code;


    PointOfServiceTypeEnum(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "PointOfServiceTypeEnum";
    }
}
