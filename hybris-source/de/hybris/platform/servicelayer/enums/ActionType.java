package de.hybris.platform.servicelayer.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum ActionType implements HybrisEnumValue
{
    PLAIN("PLAIN"),
    TASK("TASK"),
    PROCESS("PROCESS");
    public static final String _TYPECODE = "ActionType";
    public static final String SIMPLE_CLASSNAME = "ActionType";
    private final String code;


    ActionType(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "ActionType";
    }
}
