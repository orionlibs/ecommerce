package com.hybris.datahub.domain;

public enum RawItemStatusType
{
    IGNORED("IGNORED"),
    PROCESSED("PROCESSED"),
    PENDING("PENDING");
    public static final String _TYPECODE = "RawItemStatusType";
    private final String code;


    RawItemStatusType(String code)
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
