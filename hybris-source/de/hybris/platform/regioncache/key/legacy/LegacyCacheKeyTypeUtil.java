package de.hybris.platform.regioncache.key.legacy;

import de.hybris.platform.cache.Cache;
import de.hybris.platform.cache.impl.DefaultCache;
import de.hybris.platform.core.PK;
import de.hybris.platform.util.typesystem.PlatformStringUtils;
import java.util.Arrays;

public final class LegacyCacheKeyTypeUtil
{
    public static Object calculateKeyCode(Object[] key)
    {
        Object result;
        if(key == null || key.length < 1)
        {
            throw new Error("Unable to create key for " + Arrays.deepToString(key));
        }
        if(DefaultCache.CACHEKEY_C2LMANAGER.equals(key[0]))
        {
            result = DefaultCache.CACHEKEY_C2LMANAGER;
        }
        else if(key.length == 2 && DefaultCache.CACHEKEY_JALOITEMCACHE.equals(key[0]))
        {
            result = new LegacyCacheKeyType(PlatformStringUtils.valueOf(((PK)key[1]).getTypeCode()), Cache.CACHEKEY_JALOITEMCACHE);
        }
        else if(key.length == 3 && DefaultCache.CACHEKEY_JALOITEMDATA.equals(key[0]))
        {
            result = new LegacyCacheKeyType(PlatformStringUtils.valueOf(((PK)key[2]).getTypeCode()), Cache.CACHEKEY_JALOITEMDATA, key[1]);
        }
        else if(key.length == 3 && DefaultCache.CACHEKEY_JALOTYPE.equals(key[0]))
        {
            result = new LegacyCacheKeyType(key[2], DefaultCache.CACHEKEY_JALOTYPE, key[1]);
        }
        else if(key.length == 2 && DefaultCache.CACHEKEY_FLEXSEARCH.equals(key[0]))
        {
            result = String.valueOf(key[1]);
        }
        else if(key.length == 3 && DefaultCache.CACHEKEY_HJMP.equals(key[0]) && Cache.CACHEKEY_FIND.equals(key[1]))
        {
            result = new LegacyCacheKeyType(key[2], Cache.CACHEKEY_HJMP, key[1]);
        }
        else if(key.length == 4 && DefaultCache.CACHEKEY_HJMP.equals(key[0]))
        {
            result = new LegacyCacheKeyType(key[2], Cache.CACHEKEY_HJMP, key[1]);
        }
        else if(key.length == 3)
        {
            if(key[2] instanceof PK)
            {
                result = new LegacyCacheKeyType(PlatformStringUtils.valueOf(((PK)key[2]).getTypeCode()), String.valueOf(key[0]), key[1]);
            }
            else
            {
                result = new LegacyCacheKeyType(key[2], String.valueOf(key[0]), key[1]);
            }
        }
        else if(key.length == 2)
        {
            if(key[1] instanceof PK)
            {
                result = new LegacyCacheKeyType(PlatformStringUtils.valueOf(((PK)key[1]).getTypeCode()), String.valueOf(key[0]));
            }
            else
            {
                result = new LegacyCacheKeyType(key[1], String.valueOf(key[0]));
            }
        }
        else
        {
            int pkPosition = key.length - 2;
            if(pkPosition < 0)
            {
                pkPosition = 0;
            }
            result = key[pkPosition].toString();
        }
        return result;
    }
}
