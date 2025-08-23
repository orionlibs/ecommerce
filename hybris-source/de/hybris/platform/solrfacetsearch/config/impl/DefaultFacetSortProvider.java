package de.hybris.platform.solrfacetsearch.config.impl;

import de.hybris.platform.solrfacetsearch.config.FacetSortProvider;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.search.FacetValue;
import java.util.Comparator;
import org.apache.commons.collections.ComparatorUtils;
import org.apache.log4j.Logger;

public class DefaultFacetSortProvider implements FacetSortProvider
{
    private static final Logger LOG = Logger.getLogger(DefaultFacetSortProvider.class);
    private boolean descending;
    private Comparator<FacetValue> comparator;


    public boolean isDescending()
    {
        return this.descending;
    }


    public void setDescending(boolean descending)
    {
        this.descending = descending;
    }


    public Comparator<FacetValue> getComparatorForTypeAndProperty(IndexedType indexedType, IndexedProperty indexedProperty)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Resolved comparator for facet " + indexedProperty.getName() + " sorting : (reversed=" + isDescending() + ") " + this.comparator
                            .getClass());
        }
        return isDescending() ? ComparatorUtils.reversedComparator(this.comparator) : this.comparator;
    }


    public void setComparator(Comparator<FacetValue> comparator)
    {
        this.comparator = comparator;
    }
}
