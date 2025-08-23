package de.hybris.platform.ruleengineservices.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum OrderEntrySelectionStrategy implements HybrisEnumValue
{
    CHEAPEST("CHEAPEST"),
    MOST_EXPENSIVE("MOST_EXPENSIVE"),
    DEFAULT("DEFAULT");
    public static final String _TYPECODE = "OrderEntrySelectionStrategy";
    public static final String SIMPLE_CLASSNAME = "OrderEntrySelectionStrategy";
    private final String code;


    OrderEntrySelectionStrategy(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "OrderEntrySelectionStrategy";
    }
}
