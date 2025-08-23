package com.hybris.datahub.runtime.domain;

import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.Set;

public enum CompositionActionStatusType
{
    SUCCESS("SUCCESS"),
    PENDING("PENDING"),
    IN_PROGRESS("IN_PROGRESS"),
    COMPLETE_W_ERRORS("COMPLETE_W_ERRORS"),
    FAILURE("FAILURE");
    public static final String _TYPECODE = "CompositionActionStatusType";
    private static final Set<CompositionActionStatusType> FINAL_STATUSES;
    private final String code;

    static
    {
        FINAL_STATUSES = Sets.newEnumSet(Arrays.asList(new CompositionActionStatusType[] {SUCCESS, FAILURE, COMPLETE_W_ERRORS}, ), CompositionActionStatusType.class);
    }

    CompositionActionStatusType(String code)
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


    public boolean isFinal()
    {
        return FINAL_STATUSES.contains(this);
    }
}
