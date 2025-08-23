package de.hybris.deltadetection.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum ChangeType implements HybrisEnumValue
{
    NEW("NEW"),
    DELETED("DELETED"),
    MODIFIED("MODIFIED");
    public static final String _TYPECODE = "changeType";
    public static final String SIMPLE_CLASSNAME = "ChangeType";
    private final String code;


    ChangeType(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "ChangeType";
    }
}
