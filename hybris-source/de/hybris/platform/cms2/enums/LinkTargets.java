package de.hybris.platform.cms2.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum LinkTargets implements HybrisEnumValue
{
    SAMEWINDOW("sameWindow"),
    NEWWINDOW("newWindow");
    public static final String _TYPECODE = "LinkTargets";
    public static final String SIMPLE_CLASSNAME = "LinkTargets";
    private final String code;


    LinkTargets(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "LinkTargets";
    }
}
