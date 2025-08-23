package de.hybris.platform.commerceservices.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum WarehouseConsignmentState implements HybrisEnumValue
{
    COMPLETE("COMPLETE"),
    CANCEL("CANCEL"),
    PARTIAL("PARTIAL");
    public static final String _TYPECODE = "WarehouseConsignmentState";
    public static final String SIMPLE_CLASSNAME = "WarehouseConsignmentState";
    private final String code;


    WarehouseConsignmentState(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "WarehouseConsignmentState";
    }
}
