package com.hybris.datahub.runtime.domain;

public interface DataHubFeedAction extends DataHubAction
{
    public static final String _TYPECODE = "DataHubFeedAction";


    DataHubFeed getFeed();


    void setFeed(DataHubFeed paramDataHubFeed);
}
