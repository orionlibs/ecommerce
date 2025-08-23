package de.hybris.platform.cockpit.model.search.impl;

import de.hybris.platform.cockpit.model.search.SortCriterion;

public class DefaultSortCriterion implements SortCriterion
{
    private final String qualifier;
    private boolean directionApplicable;


    public DefaultSortCriterion(String qualifier)
    {
        this.qualifier = qualifier;
    }


    public String getQualifier()
    {
        return this.qualifier;
    }


    public void setDirectionApplicable(boolean directionApplicable)
    {
        this.directionApplicable = directionApplicable;
    }


    public boolean supportsDirection()
    {
        return this.directionApplicable;
    }
}
