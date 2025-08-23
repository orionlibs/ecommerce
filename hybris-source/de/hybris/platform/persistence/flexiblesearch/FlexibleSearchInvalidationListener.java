package de.hybris.platform.persistence.flexiblesearch;

import de.hybris.platform.cache.Cache;
import de.hybris.platform.cache.InvalidationListener;
import de.hybris.platform.cache.InvalidationTarget;
import de.hybris.platform.cache.RemoteInvalidationSource;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.util.typesystem.PlatformStringUtils;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class FlexibleSearchInvalidationListener implements InvalidationListener
{
    public static final String SEARCHRESTRICTION_TC = PlatformStringUtils.valueOf(90);
    public static final String COMPOSEDTYPE_TC = PlatformStringUtils.valueOf(82);
    public static final String USER_USERGROUP_RELATION_TC = PlatformStringUtils.valueOf(201);
    public static final String LANGUAGE_TC = PlatformStringUtils.valueOf(32);
    private final Cache cache;


    public FlexibleSearchInvalidationListener(Cache cache)
    {
        this.cache = cache;
    }


    private static final Set INVALIDATING_TYPES = new HashSet(Arrays.asList((Object[])new String[] {SEARCHRESTRICTION_TC, COMPOSEDTYPE_TC, USER_USERGROUP_RELATION_TC, LANGUAGE_TC}));


    public void keyInvalidated(Object[] key, int invalidationType, InvalidationTarget target, RemoteInvalidationSource remoteSrc)
    {
        if(INVALIDATING_TYPES.contains(key[2]))
        {
            FlexibleSearch.getInstance().getQueryParser().clearCachedQueries(this.cache);
        }
        Object[] fsKey = new Object[2];
        fsKey[0] = Cache.CACHEKEY_FLEXSEARCH;
        fsKey[1] = key[2];
        target.invalidate(fsKey, invalidationType);
    }
}
