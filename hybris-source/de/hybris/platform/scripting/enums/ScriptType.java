package de.hybris.platform.scripting.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum ScriptType implements HybrisEnumValue
{
    GROOVY("GROOVY"),
    BEANSHELL("BEANSHELL"),
    JAVASCRIPT("JAVASCRIPT");
    public static final String _TYPECODE = "ScriptType";
    public static final String SIMPLE_CLASSNAME = "ScriptType";
    private final String code;


    ScriptType(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "ScriptType";
    }
}
