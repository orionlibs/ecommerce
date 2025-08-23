package de.hybris.platform.cockpit.model.config;

import de.hybris.platform.cockpit.model.search.Facet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchProviderConfiguration extends PropertyGroupServiceConfiguration
{
    private final List<FacetEntry> entries = new ArrayList<>();


    public SearchProviderConfiguration(String qualifier)
    {
        super(qualifier);
    }


    public void setFacetEntries(List<FacetEntry> entries)
    {
        this.entries.clear();
        if(entries != null)
        {
            this.entries.addAll(entries);
        }
    }


    public FacetEntry createFacetEntry(int pos, Facet facet)
    {
        if(getFacetEntry(facet) != null)
        {
            throw new IllegalStateException("alrady got entry for facet " + facet);
        }
        FacetEntry ret;
        addFacetEntry(pos, ret = new FacetEntry(facet));
        return ret;
    }


    public void addFacetEntry(int pos, FacetEntry facetEntry)
    {
        if(pos < 0)
        {
            this.entries.add(facetEntry);
        }
        else
        {
            this.entries.add(pos, facetEntry);
        }
    }


    public void removeAllFacetEntries()
    {
        this.entries.clear();
    }


    public void removeFacetEntry(FacetEntry facetEntry)
    {
        this.entries.remove(facetEntry);
    }


    public List<FacetEntry> getFacetEntries()
    {
        return this.entries.isEmpty() ? Collections.EMPTY_LIST : Collections.<FacetEntry>unmodifiableList(this.entries);
    }


    public List<FacetEntry> getVisibleFacetEntries()
    {
        if(this.entries.isEmpty())
        {
            return Collections.EMPTY_LIST;
        }
        List<FacetEntry> ret = new ArrayList<>(this.entries.size());
        for(FacetEntry e : this.entries)
        {
            if(e.isVisible())
            {
                ret.add(e);
            }
        }
        return ret;
    }


    public List<FacetEntry> getHiddenFacetEntries()
    {
        if(this.entries.isEmpty())
        {
            return Collections.EMPTY_LIST;
        }
        List<FacetEntry> ret = new ArrayList<>(this.entries.size());
        for(FacetEntry e : this.entries)
        {
            if(!e.isVisible())
            {
                ret.add(e);
            }
        }
        return ret;
    }


    public FacetEntry getFacetEntry(Facet facet)
    {
        if(!this.entries.isEmpty())
        {
            for(FacetEntry e : this.entries)
            {
                if(facet.equals(e.getFacet()))
                {
                    return e;
                }
            }
        }
        return null;
    }
}
