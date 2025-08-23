package de.hybris.platform.ruleengineservices.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum FactContextType implements HybrisEnumValue
{
    PROMOTION_ORDER("PROMOTION_ORDER"),
    RULE_GROUP("RULE_GROUP"),
    PROMOTION_PRODUCT("PROMOTION_PRODUCT"),
    RULE_CONFIGURATION("RULE_CONFIGURATION");
    public static final String _TYPECODE = "FactContextType";
    public static final String SIMPLE_CLASSNAME = "FactContextType";
    private final String code;


    FactContextType(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "FactContextType";
    }
}
