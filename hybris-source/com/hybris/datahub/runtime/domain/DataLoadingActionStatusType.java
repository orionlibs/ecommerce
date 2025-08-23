package com.hybris.datahub.runtime.domain;

public enum DataLoadingActionStatusType
{
    PROCESSED("PROCESSED"),
    COMPLETE("COMPLETE"),
    IN_PROGRESS("IN_PROGRESS"),
    FAILURE("FAILURE");
    public static final String _TYPECODE = "DataLoadingActionStatusType";
    private final String code;


    DataLoadingActionStatusType(String code)
    {
        this.code = code.intern();
    }
}
