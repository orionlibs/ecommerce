package de.hybris.platform.ruleengineservices.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum RuleStatus implements HybrisEnumValue
{
    UNPUBLISHED("UNPUBLISHED"),
    PUBLISHED("PUBLISHED"),
    MODIFIED("MODIFIED"),
    INACTIVE("INACTIVE"),
    ARCHIVED("ARCHIVED");
    public static final String _TYPECODE = "RuleStatus";
    public static final String SIMPLE_CLASSNAME = "RuleStatus";
    private final String code;


    RuleStatus(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "RuleStatus";
    }
}
