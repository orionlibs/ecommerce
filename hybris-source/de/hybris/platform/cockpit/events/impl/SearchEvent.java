package de.hybris.platform.cockpit.events.impl;

public class SearchEvent extends AbstractCockpitEvent
{
    private final String query;


    public SearchEvent(Object source, String query)
    {
        super(source);
        this.query = query;
    }


    public String getQuery()
    {
        return (this.query == null) ? "" : this.query;
    }
}
