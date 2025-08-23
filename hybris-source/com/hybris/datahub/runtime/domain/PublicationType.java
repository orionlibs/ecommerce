package com.hybris.datahub.runtime.domain;

public enum PublicationType
{
    DELETE("DELETE"),
    INSERT("INSERT");
    public static final String _TYPECODE = "PublicationType";
    private final String code;


    PublicationType(String code)
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
