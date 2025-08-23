package de.hybris.platform.ruleengine.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum DroolsEventProcessingMode implements HybrisEnumValue
{
    STREAM("STREAM"),
    CLOUD("CLOUD");
    public static final String _TYPECODE = "DroolsEventProcessingMode";
    public static final String SIMPLE_CLASSNAME = "DroolsEventProcessingMode";
    private final String code;


    DroolsEventProcessingMode(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "DroolsEventProcessingMode";
    }
}
