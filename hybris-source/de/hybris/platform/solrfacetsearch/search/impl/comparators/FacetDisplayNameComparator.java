package de.hybris.platform.solrfacetsearch.search.impl.comparators;

import de.hybris.platform.solrfacetsearch.search.FacetValue;
import java.util.Comparator;
import org.apache.commons.lang.StringUtils;

public class FacetDisplayNameComparator implements Comparator<FacetValue>
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
        return getDisplayName(value1).compareTo(getDisplayName(value2));
    }


    private String getDisplayName(FacetValue value)
    {
        return StringUtils.defaultString(StringUtils.defaultString(value.getDisplayName(), value.getName()));
    }
}
