package de.hybris.platform.ruleengine.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum DroolsEqualityBehavior implements HybrisEnumValue
{
    EQUALITY("EQUALITY"),
    IDENTITY("IDENTITY");
    public static final String _TYPECODE = "DroolsEqualityBehavior";
    public static final String SIMPLE_CLASSNAME = "DroolsEqualityBehavior";
    private final String code;


    DroolsEqualityBehavior(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "DroolsEqualityBehavior";
    }
}
