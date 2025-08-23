package de.hybris.platform.cockpit.model.query.impl;

import de.hybris.platform.cockpit.model.dynamicquery.DynamicQuery;

public class UIDynamicQuery extends UIQuery
{
    private final DynamicQuery query;


    public UIDynamicQuery(DynamicQuery query)
    {
        super(query.getLabel());
        this.query = query;
    }


    public DynamicQuery getQuery()
    {
        return this.query;
    }
}
