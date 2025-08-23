package com.hybris.datahub.domain;

public enum CompositionStatusType
{
    SUCCESS("SUCCESS"),
    ARCHIVED("ARCHIVED"),
    ERROR("ERROR"),
    DELETED("DELETED");
    public static final String _TYPECODE = "CompositionStatusType";
    private final String code;


    CompositionStatusType(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return getClass().getSimpleName();
    }
}
