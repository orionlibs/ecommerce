package de.hybris.platform.scripting.engine.internal.cache.impl;

import de.hybris.platform.regioncache.region.impl.LRUCacheRegion;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Required;

public class ScriptsCacheRegion extends LRUCacheRegion
{
    public ScriptsCacheRegion(String name, int maxEntries, boolean statsEnabled)
    {
        super(name, maxEntries, statsEnabled);
    }


    @Required
    public void setHandledTypes(String[] handledTypes)
    {
        this.handledTypes = Arrays.<String>copyOf(handledTypes, handledTypes.length);
    }
}
