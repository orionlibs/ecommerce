package de.hybris.platform.regioncache;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import de.hybris.platform.regioncache.region.CacheRegion;
import de.hybris.platform.regioncache.region.CacheRegionRegistrar;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class AutoLoadedCacheRegionsHolder implements CacheRegionsHolder
{
    @Autowired
    private Map<String, CacheRegionRegistrar> registrars;
    private final Collection<CacheRegion> regions;


    public AutoLoadedCacheRegionsHolder(Collection<CacheRegion> staticRegions)
    {
        this.regions = CollectionUtils.isNotEmpty(staticRegions) ? staticRegions : new ArrayList<>();
    }


    @PostConstruct
    public void init()
    {
        if(this.registrars != null)
        {
            this.regions.addAll(Collections2.transform(this.registrars.values(), (Function)new Object(this)));
        }
    }


    public Collection<CacheRegion> getRegions()
    {
        return Collections.unmodifiableCollection(this.regions);
    }
}
