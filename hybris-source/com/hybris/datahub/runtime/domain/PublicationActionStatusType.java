package com.hybris.datahub.runtime.domain;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public enum PublicationActionStatusType
{
    SUCCESS, PENDING, IN_PROGRESS, FAILURE, COMPLETE_W_ERRORS;
    public static final String _TYPECODE = "PublicationActionStatusType";
    private static final List<PublicationActionStatusType> ALL_STATUSES;

    static
    {
        ALL_STATUSES = Arrays.asList(new PublicationActionStatusType[] {PENDING, IN_PROGRESS, FAILURE, COMPLETE_W_ERRORS, SUCCESS});
    }

    public static PublicationActionStatusType calculateOverallStatus(Collection<PublicationActionStatusType> statuses)
    {
        int worstStatusIdx = ALL_STATUSES.size();
        for(PublicationActionStatusType status : statuses)
        {
            int currIdx = ALL_STATUSES.indexOf(status);
            if(currIdx < worstStatusIdx)
            {
                worstStatusIdx = currIdx;
            }
        }
        return (worstStatusIdx >= 0 && worstStatusIdx < ALL_STATUSES.size()) ? ALL_STATUSES.get(worstStatusIdx) : null;
    }


    public String getCode()
    {
        return name();
    }


    public String getType()
    {
        return getClass().getSimpleName();
    }


    public boolean isFinal()
    {
        return ("SUCCESS".equals(name()) || "COMPLETE_W_ERRORS".equals(name()) || "FAILURE".equals(name()));
    }
}
