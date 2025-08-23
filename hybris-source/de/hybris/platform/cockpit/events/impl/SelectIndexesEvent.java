package de.hybris.platform.cockpit.events.impl;

import java.util.List;

public class SelectIndexesEvent extends AbstractCockpitEvent
{
    private List<Integer> indexes;
    private Class browserClass;


    public SelectIndexesEvent(Object source, List<Integer> indexes, Class browserClass)
    {
        super(source);
        this.indexes = indexes;
        this.browserClass = browserClass;
    }


    public List<Integer> getIndexes()
    {
        return this.indexes;
    }


    public void setIndexes(List<Integer> indexes)
    {
        this.indexes = indexes;
    }


    public Class getBrowserClass()
    {
        return this.browserClass;
    }


    public void setBrowserClass(Class browserClass)
    {
        this.browserClass = browserClass;
    }
}
