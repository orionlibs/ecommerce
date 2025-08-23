package de.hybris.platform.advancedsavedquery.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum AdvancedQueryComparatorEnum implements HybrisEnumValue
{
    EQUALS("equals"),
    CONTAINS("contains"),
    GT("gt"),
    GTANDEQUALS("gtandequals"),
    LT("lt"),
    LTANDEQUALS("ltandequals"),
    STARTWIDTH("startwidth");
    public static final String _TYPECODE = "AdvancedQueryComparatorEnum";
    public static final String SIMPLE_CLASSNAME = "AdvancedQueryComparatorEnum";
    private final String code;


    AdvancedQueryComparatorEnum(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "AdvancedQueryComparatorEnum";
    }
}
