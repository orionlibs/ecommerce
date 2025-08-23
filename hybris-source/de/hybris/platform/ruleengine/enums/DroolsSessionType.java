package de.hybris.platform.ruleengine.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum DroolsSessionType implements HybrisEnumValue
{
    STATEFUL("STATEFUL"),
    STATELESS("STATELESS");
    public static final String _TYPECODE = "DroolsSessionType";
    public static final String SIMPLE_CLASSNAME = "DroolsSessionType";
    private final String code;


    DroolsSessionType(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "DroolsSessionType";
    }
}
