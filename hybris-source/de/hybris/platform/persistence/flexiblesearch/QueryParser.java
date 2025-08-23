package de.hybris.platform.persistence.flexiblesearch;

import de.hybris.platform.cache.Cache;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.flexiblesearch.ContextQueryFilter;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearchException;
import de.hybris.platform.jalo.security.Principal;
import de.hybris.platform.persistence.flexiblesearch.oracle.InParametersQueryTranslator;
import de.hybris.platform.persistence.flexiblesearch.typecache.FlexibleSearchTypeCacheProvider;
import de.hybris.platform.persistence.flexiblesearch.typecache.impl.DefaultFlexibleSearchTypeCacheProvider;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.SingletonCreator;
import de.hybris.platform.util.config.ConfigIntf;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import org.apache.log4j.Logger;

public class QueryParser
{
    private static final Logger log = Logger.getLogger(QueryParser.class);
    public static final String QUERYCACHSIZE_KEY = "cache.flexiblesearchquery";
    public static final int QUERYCACHSIZE_DEFAULT = 1000;
    public static final int QUERYCACHSIZE_LIMIT = 50000;
    private final Cache cache;
    private final int queryCacheSize;
    private final boolean cacheQueries;
    private final FlexibleSearchTypeCacheProvider fsTypeCacheProvider;
    private final SingletonCreator.Creator<Map<Object, TranslatedQuery>> cacheCreator = (SingletonCreator.Creator<Map<Object, TranslatedQuery>>)new Object(this);


    public QueryParser()
    {
        this(getDefaultQueryTranslationCacheSize(), (FlexibleSearchTypeCacheProvider)new DefaultFlexibleSearchTypeCacheProvider());
    }


    public QueryParser(FlexibleSearchTypeCacheProvider fsCache)
    {
        this(getDefaultQueryTranslationCacheSize(), fsCache);
    }


    public QueryParser(int translationCacheSize)
    {
        this(translationCacheSize, (FlexibleSearchTypeCacheProvider)new DefaultFlexibleSearchTypeCacheProvider());
    }


    public QueryParser(int translationCacheSize, FlexibleSearchTypeCacheProvider fsCache)
    {
        this.queryCacheSize = translationCacheSize;
        if(this.queryCacheSize > 0)
        {
            this.cacheQueries = true;
            this.cache = Registry.getCurrentTenantNoFallback().getCache();
        }
        else
        {
            this.cacheQueries = false;
            this.cache = null;
        }
        this.fsTypeCacheProvider = fsCache;
    }


    private static int getDefaultQueryTranslationCacheSize()
    {
        ConfigIntf cfg = Registry.getCurrentTenantNoFallback().getConfig();
        int cacheSize = cfg.getInt("cache.flexiblesearchquery", 1000);
        return getDefaultQueryTranslationCacheSize(cfg, cacheSize);
    }


    private static int getDefaultQueryTranslationCacheSize(ConfigIntf cfg, int preset)
    {
        int cacheSize = preset;
        if(!cfg.getBoolean(Config.Params.BYPASS_HYBRIS_RECOMMENDATIONS, false) && preset > 50000)
        {
            log.warn("**********");
            log.warn("Value '" + preset + "' for property 'cache.flexiblesearchquery' is bigger than maximal allowed value '50000'. For more information please contact the hybris support.");
            log.warn("Using maximal allowed value '50000'.");
            log.warn("**********");
            cacheSize = 50000;
        }
        return cacheSize;
    }


    public int getQueryCacheSize()
    {
        return this.queryCacheSize;
    }


    public final Map<Object, TranslatedQuery> getQueryCacheMap(Cache cache)
    {
        if(!this.cacheQueries)
        {
            return Collections.EMPTY_MAP;
        }
        return (Map<Object, TranslatedQuery>)cache.getStaticCacheContent(this.cacheCreator);
    }


    private final TranslatedQuery getCachedQuery(Cache cache, Object key)
    {
        if(this.cacheQueries)
        {
            Map<Object, TranslatedQuery> queryCacheMap = getQueryCacheMap(cache);
            return queryCacheMap.get(key);
        }
        return null;
    }


    private final void putCachedQuery(Cache cache, Object key, TranslatedQuery query)
    {
        if(this.cacheQueries)
        {
            Map<Object, TranslatedQuery> queryCacheMap = getQueryCacheMap(cache);
            queryCacheMap.put(key, query);
        }
    }


    public final void clearCachedQueries(Cache cache)
    {
        if(this.cacheQueries)
        {
            Map<Object, TranslatedQuery> queryCacheMap = getQueryCacheMap(cache);
            queryCacheMap.clear();
        }
    }


    public TranslatedQuery translateQuery(Principal principal, String query, int valueCount, boolean hasLanguage, boolean failOnUnknownFields, boolean disableRestrictions, boolean disablePrincipalGroupRestrictions, Collection<ContextQueryFilter> dynamicRestrictions, Map values)
                    throws FlexibleSearchException
    {
        InParametersQueryTranslator translator = new InParametersQueryTranslator();
        if(this.cacheQueries && !translator.isAnyParamExceedingLimit(query, values))
        {
            Object key = getQueryKey(principal, query, hasLanguage, failOnUnknownFields, disableRestrictions, disablePrincipalGroupRestrictions, dynamicRestrictions);
            TranslatedQuery res = getCachedQuery(this.cache, key);
            if(res == null)
            {
                ParsedQuery parsedQuery = new ParsedQuery(this.fsTypeCacheProvider, principal, query, valueCount, hasLanguage, failOnUnknownFields, disableRestrictions, disablePrincipalGroupRestrictions);
                res = parsedQuery.getTranslatedQuery(values);
                if(res.cacheStatement())
                {
                    putCachedQuery(this.cache, key, res);
                }
            }
            return res;
        }
        return (new ParsedQuery(this.fsTypeCacheProvider, principal, query, valueCount, hasLanguage, failOnUnknownFields, disableRestrictions, disablePrincipalGroupRestrictions))
                        .getTranslatedQuery(values);
    }


    private final Object getQueryKey(Principal principal, String sql, boolean hasLanguage, boolean failOnUnknownFields, boolean disableRestrictions, boolean disableGroupRestrictions, Collection<ContextQueryFilter> dynamicRestrictions)
    {
        return Arrays.asList(new Object[] {(principal != null) ? principal.getPK() : null, sql,
                        getDynamicQueriesKey(dynamicRestrictions),
                        Boolean.valueOf(failOnUnknownFields),
                        Boolean.valueOf(hasLanguage),
                        Boolean.valueOf(disableRestrictions),
                        Boolean.valueOf(disableGroupRestrictions),
                        Boolean.valueOf(FlexibleSearch.isUnionAllForTypeHierarchyEnabled())});
    }


    protected Object getDynamicQueriesKey(Collection<ContextQueryFilter> dynamicRestrictions)
    {
        if(dynamicRestrictions == null || dynamicRestrictions.isEmpty())
        {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder(dynamicRestrictions.size() * 50);
        for(ContextQueryFilter f : dynamicRestrictions)
        {
            stringBuilder.append(f.getCode()).append("/").append(f.getRestrictionType().getPK().toString()).append("/")
                            .append(f.getQuery().hashCode()).append(";");
        }
        return stringBuilder.toString();
    }
}
