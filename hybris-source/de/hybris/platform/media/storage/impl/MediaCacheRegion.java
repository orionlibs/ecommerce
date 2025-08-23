package de.hybris.platform.media.storage.impl;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.primitives.Ints;
import com.googlecode.concurrentlinkedhashmap.Weigher;
import de.hybris.platform.regioncache.CacheStatistics;
import de.hybris.platform.regioncache.CacheValueLoadException;
import de.hybris.platform.regioncache.CacheValueLoader;
import de.hybris.platform.regioncache.key.CacheKey;
import de.hybris.platform.regioncache.region.UnlimitedCacheRegion;
import de.hybris.platform.regioncache.region.impl.LRUCacheRegion;
import java.util.Arrays;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class MediaCacheRegion extends LRUCacheRegion implements UnlimitedCacheRegion
{
    private static final Logger LOG = Logger.getLogger(MediaCacheRegion.class);
    public static final String MEDIA_TYPE = "__MEDIA__";
    private static final int MAX_CACHE_SIZE_IN_KB = 2147483647;
    private final String cacheFolder;


    public MediaCacheRegion(String name, int sizeInMb, String cacheFolder, boolean statsEnabled)
    {
        super(name, new CacheStatistics(), statsEnabled, convertCacheSizeToKilobytes(sizeInMb), (Weigher)new Object());
        this.cacheFolder = cacheFolder;
        if(LOG.isDebugEnabled())
        {
            LOG.debug(">>> Initializing MediaCacheRegion [configured size in MB: " + sizeInMb + ", in KB: " + getCacheMaxEntries() + ", configured for handling:__MEDIA__" + cacheFolder + "]");
        }
    }


    @Required
    public void setHandledTypes(String[] handledTypes)
    {
        Iterable<String> transformed = Iterables.transform(Arrays.asList(handledTypes), (Function)new Object(this));
        this.handledTypes = (String[])Iterables.toArray(transformed, String.class);
    }


    public Object getWithLoader(CacheKey key, CacheValueLoader loader) throws CacheValueLoadException
    {
        Object cacheUnit = super.getWithLoader(key, loader);
        if(cacheUnit instanceof DefaultLocalMediaFileCacheService.MediaCacheUnit)
        {
            if(!((DefaultLocalMediaFileCacheService.MediaCacheUnit)cacheUnit).isCachedFileExists())
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Returned media from cache doesn't exist. Invalidating cache for the key:" + key + " and loading it once again");
                }
                invalidate(key, true);
                return super.getWithLoader(key, loader);
            }
        }
        return cacheUnit;
    }


    private static int convertCacheSizeToKilobytes(int sizeInMb)
    {
        long sizeInKilobytes = (sizeInMb * 1024);
        return Ints.checkedCast((sizeInKilobytes > 2147483647L) ? 2147483647L : sizeInKilobytes);
    }
}
