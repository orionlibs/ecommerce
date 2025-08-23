package de.hybris.platform.cockpit.model.query.impl;

import de.hybris.platform.cockpit.model.CockpitSavedQueryModel;
import de.hybris.platform.cockpit.session.UISessionUtils;

public class UISavedQuery extends UIQuery
{
    private final CockpitSavedQueryModel savedQuery;


    public UISavedQuery(CockpitSavedQueryModel savedQuery)
    {
        super(savedQuery.getLabel());
        this.savedQuery = savedQuery;
    }


    public CockpitSavedQueryModel getSavedQuery()
    {
        return this.savedQuery;
    }


    public void setLabel(String label)
    {
        UISessionUtils.getCurrentSession().getSavedQueryService().renameQuery(getSavedQuery(), label);
    }


    public String getLabel()
    {
        return getSavedQuery().getLabel();
    }
}
