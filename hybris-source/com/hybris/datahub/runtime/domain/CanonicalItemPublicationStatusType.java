package com.hybris.datahub.runtime.domain;

public enum CanonicalItemPublicationStatusType
{
    SUCCESS("SUCCESS"),
    IN_PROGRESS("IN_PROGRESS"),
    FAILURE("FAILURE"),
    IGNORED("IGNORED");
    public static final String _TYPECODE = "CanonicalItemPublicationStatusType";
    private final String code;


    CanonicalItemPublicationStatusType(String code)
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
