package de.hybris.platform.persistence.hjmp;

import de.hybris.platform.cache.AbstractCacheUnit;
import de.hybris.platform.cache.Cache;
import de.hybris.platform.core.ItemDeployment;
import de.hybris.platform.persistence.framework.PersistencePool;
import de.hybris.platform.regioncache.key.RegistrableCacheKey;
import de.hybris.platform.util.Config;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class FinderResult extends AbstractCacheUnit
{
    private final String beanKey;
    private final String theFinderName;
    protected final PersistencePool pool;
    private final ItemDeployment depl;
    private final List theParameters;
    private final String db;
    private final FinderResultCacheKey cacheKey;
    private final Object[] finderKey;


    public FinderResult(PersistencePool pool, ItemDeployment depl, String beanKey, String finderName, Object[] parameters)
    {
        super(pool.getCache());
        this.pool = pool;
        this.depl = depl;
        this.beanKey = beanKey;
        this.theFinderName = finderName;
        this.theParameters = Collections.unmodifiableList(Arrays.asList(parameters));
        this.db = Config.getDatabase();
        this
                        .finderKey = new Object[] {Cache.CACHEKEY_HJMP, Cache.CACHEKEY_FIND, this.beanKey.intern(), this.theFinderName.intern(), this.theParameters};
        this.cacheKey = new FinderResultCacheKey(beanKey, this.finderKey, pool.getCache().getTenantId());
    }


    public Object[] createKey()
    {
        return this.finderKey;
    }


    public RegistrableCacheKey getKey()
    {
        return (RegistrableCacheKey)this.cacheKey;
    }


    public PersistencePool getPersistencePool()
    {
        return this.pool;
    }


    public ItemDeployment getDeployment()
    {
        return this.depl;
    }


    public String getColumn(String qualifier)
    {
        return getDeployment().getAttribute(qualifier).getColumnName(this.db);
    }


    public String getTable()
    {
        return getDeployment().getDatabaseTableName();
    }


    public String getFullBeanName()
    {
        return this.beanKey;
    }


    public String getFinderName()
    {
        return this.theFinderName;
    }


    public List getParameters()
    {
        return this.theParameters;
    }
}
