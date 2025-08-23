package com.hybris.datahub.grouping.impl;

import com.hybris.datahub.grouping.PublicationGroupingHandler;

public abstract class ConfigurablePublicationGroupingHandler implements PublicationGroupingHandler
{
    private int order;


    public int getOrder()
    {
        return this.order;
    }


    public ConfigurablePublicationGroupingHandler setOrder(int ord)
    {
        this.order = ord;
        return this;
    }
}
