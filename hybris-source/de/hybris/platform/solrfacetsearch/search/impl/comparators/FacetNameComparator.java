package de.hybris.platform.solrfacetsearch.search.impl.comparators;

import de.hybris.platform.solrfacetsearch.search.FacetValue;
import java.util.Comparator;

public class FacetNameComparator implements Comparator<FacetValue>
{
    public int compare(FacetValue value1, FacetValue value2)
    {
        if(value1 == null && value2 == null)
        {
            return 0;
        }
        if(value1 == null)
        {
            return 1;
        }
        if(value2 == null)
        {
            return -1;
        }
        return value1.getName().compareTo(value2.getName());
    }
}
