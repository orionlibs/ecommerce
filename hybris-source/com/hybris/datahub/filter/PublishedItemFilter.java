package com.hybris.datahub.filter;

import com.hybris.datahub.runtime.domain.CanonicalItemPublicationStatusType;

public class PublishedItemFilter
{
    private final CanonicalItemPublicationStatusType[] statuses;


    public PublishedItemFilter(CanonicalItemPublicationStatusType[] statuses)
    {
        this.statuses = statuses;
    }


    public CanonicalItemPublicationStatusType[] getStatuses()
    {
        return this.statuses;
    }
}
