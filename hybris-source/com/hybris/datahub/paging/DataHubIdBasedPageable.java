package com.hybris.datahub.paging;

public interface DataHubIdBasedPageable
{
    int getPageSize();


    long getLastProcessedId();
}
