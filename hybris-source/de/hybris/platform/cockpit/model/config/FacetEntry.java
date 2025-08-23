package de.hybris.platform.cockpit.model.config;

import de.hybris.platform.cockpit.model.search.Facet;

public class FacetEntry
{
    private boolean visible;
    private final Facet facet;


    public FacetEntry(Facet facet)
    {
        this.facet = facet;
    }


    public boolean isVisible()
    {
        return this.visible;
    }


    public void setVisible(boolean visible)
    {
        this.visible = visible;
    }


    public Facet getFacet()
    {
        return this.facet;
    }
}
