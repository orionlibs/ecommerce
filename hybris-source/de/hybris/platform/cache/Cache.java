package de.hybris.platform.cache;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.util.SingletonCreator;
import java.util.Set;

public interface Cache extends InvalidationTarget
{
    public static final String CACHEKEY_HJMP = "hjmp".intern();
    public static final String CACHEKEY_ENTITY = "entity".intern();
    public static final String CACHEKEY_FIND = "find".intern();
    public static final String CACHEKEY_FLEXSEARCH = "flexsearch".intern();
    public static final String CACHEKEY_ENTITYISALIVE = "entityisalive".intern();
    public static final String CACHEKEY_JALOITEMDATA = "jaloitemdata".intern();
    public static final String CACHEKEY_JALOTYPE = "jalotype".intern();
    public static final String CACHEKEY_JALOITEMCACHE = "jaloitemcache".intern();
    public static final String CACHEKEY_CUSTOM = "custom".intern();
    public static final String CACHEKEY_C2LMANAGER = "c2lmanager".intern();
    public static final String CONFIG_CACHE_MAIN = "cache.main";
    public static final String CONFIG_CACHE_SHARED = "cache.shared";
    public static final boolean DEFAULT_CACHE_SHARED = false;
    public static final int DEFAULT_CACHE_MAIN = 150000;
    public static final int LIMIT_CACHE_MAIN = 1000000;
    public static final String CONFIG_CONCURRENCY_LEVEL = "cache.concurrency.level";
    public static final String CONFIG_EVICTION_POLICY = "cache.evictionpolicy";
    public static final String CONFIG_FORCE_EXCLUSIVE_CALCULATION = "cache.unit.exclusive.computation";


    void setEnabled(boolean paramBoolean);


    AbstractCacheUnit getOrAddUnit(AbstractCacheUnit paramAbstractCacheUnit);


    AbstractCacheUnit getUnit(AbstractCacheUnit paramAbstractCacheUnit);


    boolean isForceExclusiveComputation();


    void clear();


    void removeUnit(AbstractCacheUnit paramAbstractCacheUnit);


    void invalidate(Object[] paramArrayOfObject, int paramInt);


    AbstractCacheUnit getAbstractCacheUnit(String paramString1, String paramString2, String paramString3, PK paramPK);


    void clearStats();


    boolean statsEnabled();


    void setStatsEnabled(boolean paramBoolean);


    void enableStats(boolean paramBoolean);


    boolean isStatsEmpty();


    Set<? extends CacheStatisticsEntry> getStatistics(int paramInt1, int paramInt2);


    int getMaxAllowedSize();


    int getMaxReachedSize();


    int getSize();


    long getAddCount();


    long getRemoveCount();


    long getGetCount();


    long getMissCount();


    void destroy();


    Tenant getTenant();


    String getTenantId();


    <T> T getStaticCacheContent(SingletonCreator.Creator<T> paramCreator);


    <T> T getRequestCacheContent(SingletonCreator.Creator<T> paramCreator);


    <T> T getRequestCacheContentIfExists(SingletonCreator.Creator<T> paramCreator);
}
