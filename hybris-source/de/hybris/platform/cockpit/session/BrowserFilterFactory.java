package de.hybris.platform.cockpit.session;

import de.hybris.platform.cockpit.model.meta.ObjectType;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;

public class BrowserFilterFactory
{
    private Map<String, Set<BrowserFilter>> browserFilters;


    public Map<String, Set<BrowserFilter>> getBrowserFilters()
    {
        return this.browserFilters;
    }


    public void setBrowserFilters(Map<String, Set<BrowserFilter>> browserFilters)
    {
        this.browserFilters = browserFilters;
    }


    public Set<BrowserFilter> getBrowserFilters(ObjectType searchObjectType)
    {
        Set<BrowserFilter> ret = new LinkedHashSet<>();
        ObjectType searchObjectTypeInternal = searchObjectType;
        while(searchObjectTypeInternal != null && CollectionUtils.isEmpty(ret))
        {
            String searchTypeCode = searchObjectTypeInternal.getCode();
            if(this.browserFilters != null && this.browserFilters.containsKey(searchTypeCode))
            {
                ret.addAll(this.browserFilters.get(searchTypeCode));
            }
            searchObjectTypeInternal = CollectionUtils.isNotEmpty(searchObjectTypeInternal.getSupertypes()) ? searchObjectTypeInternal.getSupertypes().iterator().next() : null;
        }
        return ret;
    }
}
