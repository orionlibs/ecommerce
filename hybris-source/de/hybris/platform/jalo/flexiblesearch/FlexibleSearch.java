package de.hybris.platform.jalo.flexiblesearch;

import com.google.common.base.Suppliers;
import de.hybris.bootstrap.util.LocaleHelper;
import de.hybris.platform.cache.Cache;
import de.hybris.platform.cache.InvalidationListener;
import de.hybris.platform.cache.InvalidationManager;
import de.hybris.platform.cache.InvalidationTopic;
import de.hybris.platform.core.Constants;
import de.hybris.platform.core.LazyLoadItemList;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.WrapperFactory;
import de.hybris.platform.core.suspend.SystemIsSuspendedException;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInternalException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.Manager;
import de.hybris.platform.jalo.SearchResult;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.flexiblesearch.hints.Hint;
import de.hybris.platform.jalo.flexiblesearch.hints.impl.FlexibleSearchHints;
import de.hybris.platform.jalo.flexiblesearch.internal.FlexibleSearchExecutor;
import de.hybris.platform.jalo.flexiblesearch.internal.FlexibleSearchHintsProviderFactory;
import de.hybris.platform.jalo.flexiblesearch.internal.ReadOnlyConditionsHelper;
import de.hybris.platform.jalo.security.Principal;
import de.hybris.platform.jalo.security.PrincipalGroup;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jdbcwrapper.HybrisDataSource;
import de.hybris.platform.persistence.PolyglotPersistenceGenericItemSupport;
import de.hybris.platform.persistence.flexiblesearch.CaseInsensitiveParameterMap;
import de.hybris.platform.persistence.flexiblesearch.FlexibleSearchInvalidationListener;
import de.hybris.platform.persistence.flexiblesearch.QueryParser;
import de.hybris.platform.persistence.flexiblesearch.TranslatedQuery;
import de.hybris.platform.persistence.flexiblesearch.polyglot.PolyglotFsResult;
import de.hybris.platform.persistence.flexiblesearch.polyglot.PolyglotPersistenceFlexibleSearchSupport;
import de.hybris.platform.persistence.flexiblesearch.typecache.FlexibleSearchTypeCacheProvider;
import de.hybris.platform.persistence.flexiblesearch.typecache.impl.DefaultFlexibleSearchTypeCacheProvider;
import de.hybris.platform.persistence.polyglot.TypeInfoFactory;
import de.hybris.platform.persistence.polyglot.config.TypeInfo;
import de.hybris.platform.persistence.polyglot.model.Identity;
import de.hybris.platform.persistence.polyglot.model.Key;
import de.hybris.platform.persistence.polyglot.model.PolyglotModelFactory;
import de.hybris.platform.persistence.property.PersistenceManager;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.FlexibleSearchUtils;
import de.hybris.platform.util.SQLSearchResultFactory;
import de.hybris.platform.util.StandardSearchResult;
import de.hybris.platform.util.typesystem.PlatformStringUtils;
import java.io.ObjectStreamException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Deprecated(since = "ages", forRemoval = false)
public class FlexibleSearch extends Manager
{
    private static final String PARAM_POLYGLOT_VALIDATION_SHOULD_THROW_EXCEPTION = "polyglot.validation.should.throw.exception";
    static final String PARAM_FLEXIBLE_SEARCH_DATASOURCE_CACHE_DOMAIN_PREFIX = "flexiblesearch.datasource.";
    static final String PARAM_FLEXIBLE_SEARCH_DATASOURCE_CACHE_DOMAIN_SUFFIX = ".cacheDomain";
    static final String PARAM_FLEXIBLE_SEARCH_CACHE_DOMAIN_TTL_PREFIX = "flexiblesearch.cacheDomain.";
    static final String PARAM_FLEXIBLE_SEARCH_CACHE_DOMAIN_TTL_SUFFIX = ".ttl";
    private final transient Supplier<CacheDomainsConfig> cacheDomainsConfig = (Supplier<CacheDomainsConfig>)Suppliers.memoize(this::buildCacheDomainsConfig);


    @Deprecated(since = "4.8", forRemoval = false)
    public static Object getCacheUnit()
    {
        throw new UnsupportedOperationException();
    }


    private static final Logger LOG = LoggerFactory.getLogger(FlexibleSearch.class);
    public static final String BEAN_NAME = "core.flexibleSearch";
    public static final Comparator VALUE_COMPARATOR = (Comparator)new ValueComparator();
    public static final String DISABLE_RESTRICTIONS = "disableRestrictions";
    public static final String DISABLE_RESTRICTION_GROUP_INHERITANCE = "disableRestrictionGroupInheritance";
    public static final String DISABLE_SESSION_ATTRIBUTES = "disableSessionAttributes";
    public static final String DISABLE_EXECUTION = "disableExecution";
    public static final String ENABLE_CACHE_FOR_READ_ONLY_DATA_SOURCE = "ctx.enable.fs.cache.on.read-replica";
    public static final String CACHE_TTL = "TTLForUnitInCache";
    private static final int NO_TTL_VALUE = -1;
    public static final String CTX_SEARCH_RESTRICTIONS = "ctxSearchRestrictions";
    public static final String DISABLE_CACHE = "disableCache";
    public static final String UNION_ALL_TYPE_HIERARCHY = "unionAllTypeHierarchy";
    public static final String PREFETCH_SIZE = "prefetchSize";
    public static long interval = 100L;
    private final QueryParser queryParser;
    private final FlexibleSearchExecutor fsExecutor;
    private final SessionParamTranslator sessionParamTranslator = new SessionParamTranslator();
    private final ReadOnlyConditionsHelper readOnlyConditionsHelper;
    private final FlexibleSearchHintsProviderFactory hintsProviderFactory;
    static final int DONT_NEED_TOTAL = 1;
    static final int EXECUTE_QUERY = 2;
    private static final int TTL_MODE = 4;


    public FlexibleSearch()
    {
        this.readOnlyConditionsHelper = new ReadOnlyConditionsHelper();
        this.fsExecutor = new FlexibleSearchExecutor(getTenant(), this.readOnlyConditionsHelper);
        this.queryParser = new QueryParser((FlexibleSearchTypeCacheProvider)new DefaultFlexibleSearchTypeCacheProvider(this));
        this.hintsProviderFactory = new FlexibleSearchHintsProviderFactory(this.readOnlyConditionsHelper);
    }


    protected FlexibleSearch(ReadOnlyConditionsHelper readOnlyConditionsHelper)
    {
        this.readOnlyConditionsHelper = readOnlyConditionsHelper;
        this.fsExecutor = new FlexibleSearchExecutor(getTenant(), readOnlyConditionsHelper);
        this.queryParser = new QueryParser((FlexibleSearchTypeCacheProvider)new DefaultFlexibleSearchTypeCacheProvider(this));
        this.hintsProviderFactory = new FlexibleSearchHintsProviderFactory(readOnlyConditionsHelper);
    }


    protected FlexibleSearch(FlexibleSearchExecutor executor)
    {
        this.readOnlyConditionsHelper = executor.getReadOnlyConditionsHelper();
        this.fsExecutor = executor;
        this.queryParser = new QueryParser((FlexibleSearchTypeCacheProvider)new DefaultFlexibleSearchTypeCacheProvider(this));
        this.hintsProviderFactory = new FlexibleSearchHintsProviderFactory(this.readOnlyConditionsHelper);
    }


    protected FlexibleSearch(FlexibleSearchExecutor executor, FlexibleSearchHintsProviderFactory hintsProviderFactory)
    {
        this.readOnlyConditionsHelper = executor.getReadOnlyConditionsHelper();
        this.fsExecutor = executor;
        this.queryParser = new QueryParser((FlexibleSearchTypeCacheProvider)new DefaultFlexibleSearchTypeCacheProvider(this));
        this.hintsProviderFactory = hintsProviderFactory;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void addContextQueryFilter(ContextQueryFilter filter)
    {
        addContextQueryFilter(getSession().getSessionContext(), filter);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void addContextQueryFilter(SessionContext ctx, ContextQueryFilter filter)
    {
        addContextQueryFilters(ctx, Collections.singleton(filter));
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void addContextQueryFilters(Collection<ContextQueryFilter> filters)
    {
        addContextQueryFilters(getSession().getSessionContext(), filters);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void addContextQueryFilters(SessionContext ctx, Collection<ContextQueryFilter> filters)
    {
        if(ctx == null)
        {
            throw new NullPointerException("ctx was null");
        }
        if(filters != null && !filters.isEmpty())
        {
            Collection<ContextQueryFilter> current = (Collection<ContextQueryFilter>)ctx.getAttribute("ctxSearchRestrictions");
            if(current == null)
            {
                ctx.setAttribute("ctxSearchRestrictions", new ArrayList<>(filters));
            }
            else
            {
                Collection<ContextQueryFilter> newFilters = new ArrayList<>(current);
                newFilters.addAll(filters);
                ctx.setAttribute("ctxSearchRestrictions", newFilters);
            }
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public String checkQuery(String query, boolean disableRestrictions) throws FlexibleSearchException
    {
        SessionContext ctx = getSession().getSessionContext();
        return translate(ctx,
                        disableRestrictions ? getRestrictionPrincipal(ctx) : null, query, 10000, true, true, (disableRestrictions ||
                                        disableRestrictions(ctx)), (disableRestrictions ||
                                        disablePrincipalGroupRestrictions(ctx)), null).getSQLTemplate();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void clearContextQueryFilters()
    {
        clearContextQueryFilters(getSession().getSessionContext());
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void clearContextQueryFilters(SessionContext ctx)
    {
        if(ctx == null)
        {
            throw new NullPointerException("ctx was null");
        }
        ctx.removeAttribute("ctxSearchRestrictions");
    }


    public SavedQuery createSavedQuery(String code, ComposedType resultType, String query, Map<?, ?> params) throws FlexibleSearchException
    {
        if(code == null)
        {
            throw new FlexibleSearchException(null, "code cannot be NULL", 0);
        }
        if(query == null)
        {
            throw new FlexibleSearchException(null, "query cannot be NULL", 0);
        }
        if(resultType == null)
        {
            throw new FlexibleSearchException(null, "result type cannot be NULL", 0);
        }
        checkQuery(replaceTypePlaceholder(query, resultType.getCode()), true);
        Map<Object, Object> attributes = new HashMap<>(8);
        attributes.put("code", code);
        attributes.put("resultType", resultType);
        attributes.put("query", query);
        if(params != null && !params.isEmpty())
        {
            Map<Object, Object> myParams = new HashMap<>(params);
            for(Iterator<Map.Entry> iter = myParams.entrySet().iterator(); iter.hasNext(); )
            {
                Map.Entry e = iter.next();
                if(!(e.getKey() instanceof String) || !(e.getValue() instanceof de.hybris.platform.jalo.type.Type))
                {
                    throw new JaloInvalidParameterException("invalid parameter map " + params + " for saved query " + code + " - only {String->Type} mappings are allowed", 0);
                }
            }
            attributes.put("params", params);
        }
        try
        {
            return (SavedQuery)getTenant().getJaloConnection().getTypeManager().getComposedType(SavedQuery.class)
                            .newInstance(attributes);
        }
        catch(Exception e)
        {
            throw new FlexibleSearchException(e, "cannot create saved query because of " + e.getMessage(), 0);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection<ContextQueryFilter> getContextQueryFilters()
    {
        return getContextQueryFilters(getSession().getSessionContext());
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection<ContextQueryFilter> getContextQueryFilters(SessionContext ctx)
    {
        if(ctx == null)
        {
            throw new NullPointerException("ctx was null");
        }
        Collection<ContextQueryFilter> filters = (Collection<ContextQueryFilter>)ctx.getAttribute("ctxSearchRestrictions");
        return (filters != null) ? Collections.<ContextQueryFilter>unmodifiableCollection(filters) : Collections.EMPTY_LIST;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Set<AbstractQueryFilter> getQueryFilters(Principal principal, ComposedType type, boolean includeGroups, boolean includeSuperTypes, boolean includeSubtypes)
    {
        Collection<ComposedType> types;
        if(type == null)
        {
            throw new NullPointerException("type was null");
        }
        List<ComposedType> superTypes = includeSuperTypes ? type.getAllSuperTypes() : Collections.EMPTY_LIST;
        Set<ComposedType> subtypes = includeSubtypes ? type.getAllSubTypes() : Collections.EMPTY_SET;
        if(superTypes.isEmpty() && subtypes.isEmpty())
        {
            types = Collections.singleton(type);
        }
        else
        {
            types = new HashSet<>((int)((subtypes.size() + superTypes.size() + 1) / 0.75F) + 1);
            types.addAll(superTypes);
            types.addAll(subtypes);
            types.add(type);
        }
        Collection<AbstractQueryFilter> userFilters = getUserFilters(principal, includeGroups, types);
        Collection<AbstractQueryFilter> ctxFilters = (Collection)getContextQueryFilters(getSession().getSessionContext(), types);
        if(!userFilters.isEmpty() || !ctxFilters.isEmpty())
        {
            Set<AbstractQueryFilter> ret = new HashSet<>(userFilters.size() + ctxFilters.size());
            Map<String, AbstractQueryFilter> code2Filter = null;
            boolean gotSuperTypes = !superTypes.isEmpty();
            for(AbstractQueryFilter f : ctxFilters)
            {
                ComposedType t = f.getRestrictionType();
                if(gotSuperTypes && (superTypes.contains(t) || t.equals(type)))
                {
                    if(code2Filter == null)
                    {
                        code2Filter = new HashMap<>((int)(ctxFilters.size() / 0.75F) + 1);
                    }
                    String code = PlatformStringUtils.toLowerCaseCached(f.getCode());
                    AbstractQueryFilter current = code2Filter.get(code);
                    if(current == null || superTypes.indexOf(t) < superTypes.indexOf(current.getRestrictionType()))
                    {
                        code2Filter.put(code, f);
                        continue;
                    }
                    if(t.equals(type))
                    {
                        if(current.getRestrictionType().equals(type))
                        {
                            LOG.warn("found ambigous search restriction " + current
                                            .toString() + " vs " + f.toString() + " - ignored");
                            continue;
                        }
                        code2Filter.put(code, f);
                    }
                    continue;
                }
                ret.add(f);
            }
            for(AbstractQueryFilter f : userFilters)
            {
                ComposedType t = f.getRestrictionType();
                if(gotSuperTypes && (superTypes.contains(t) || t.equals(type)))
                {
                    if(code2Filter == null)
                    {
                        code2Filter = new HashMap<>((int)(userFilters.size() / 0.75F) + 1);
                    }
                    String code = f.getCode().toLowerCase(LocaleHelper.getPersistenceLocale());
                    AbstractQueryFilter current = code2Filter.get(code);
                    if(current == null || superTypes.indexOf(t) < superTypes.indexOf(current.getRestrictionType()))
                    {
                        code2Filter.put(code, f);
                        continue;
                    }
                    if(t.equals(type) && current instanceof SearchRestrictionFilter)
                    {
                        if(current.getRestrictionType().equals(type))
                        {
                            LOG.warn("found ambigous search restriction " + current
                                            .toString() + " vs " + f.toString() + " - ignored");
                            continue;
                        }
                        code2Filter.put(code, f);
                    }
                    continue;
                }
                ret.add(f);
            }
            if(code2Filter != null)
            {
                ret.addAll(code2Filter.values());
            }
            return ret;
        }
        return Collections.EMPTY_SET;
    }


    public QueryParser getQueryParser()
    {
        return this.queryParser;
    }


    public SavedQuery getSavedQuery(String code)
    {
        List<SavedQuery> res = search("SELECT {" + Item.PK + "} FROM {" + getTenant().getJaloConnection().getTypeManager().getComposedType(SavedQuery.class).getCode() + "} WHERE {code}=?code ORDER BY {" + Item.CREATION_TIME + "} ASC", Collections.singletonMap("code", code),
                        Collections.singletonList(SavedQuery.class), true, true, 0, -1).getResult();
        if(res.isEmpty())
        {
            return null;
        }
        SavedQuery sq = res.iterator().next();
        if(res.size() > 1)
        {
            LOG.warn("found multiple saved queries for code '" + code + "' : " + res + " - chosing " + sq);
        }
        return sq;
    }


    public void init()
    {
        InvalidationTopic topic = InvalidationManager.getInstance().getInvalidationTopic((Object[])new String[] {Cache.CACHEKEY_HJMP, Cache.CACHEKEY_ENTITY});
        topic.addInvalidationListener((InvalidationListener)new FlexibleSearchInvalidationListener(getTenant().getCache()));
    }


    public boolean isRestrictionEvaluationDisabled(SessionContext ctx)
    {
        return disableExecution(ctx);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public boolean isTypeRestricted(SessionContext ctx, ComposedType type, boolean includingSubtypes)
    {
        return
                        !getQueryFilters(getRestrictionPrincipal(ctx), type, !disablePrincipalGroupRestrictions(ctx), true, includingSubtypes).isEmpty();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void removeContextQueryFilter(ContextQueryFilter filter)
    {
        removeContextQueryFilter(getSession().getSessionContext(), filter);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void removeContextQueryFilter(SessionContext ctx, ContextQueryFilter filter)
    {
        removeContextQueryFilters(ctx, Collections.singleton(filter));
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void removeContextQueryFilters(Collection<ContextQueryFilter> filters)
    {
        removeContextQueryFilters(getSession().getSessionContext(), filters);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void removeContextQueryFilters(SessionContext ctx, Collection<ContextQueryFilter> filters)
    {
        if(ctx == null)
        {
            throw new NullPointerException("ctx was null");
        }
        if(filters != null && !filters.isEmpty())
        {
            Collection<ContextQueryFilter> current = (Collection<ContextQueryFilter>)ctx.getAttribute("ctxSearchRestrictions");
            if(current != null)
            {
                current.removeAll(filters);
            }
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public SearchResult search(SessionContext ctx, String query, List values, List resultClasses, boolean failOnUnknownFields, boolean dontNeedTotal, boolean ignoreEmptyProperties, int start, int count, String timeoutCacheKey) throws FlexibleSearchException
    {
        return search(ctx, query, listToMap(values), resultClasses, failOnUnknownFields, dontNeedTotal, start, count);
    }


    public SearchResult search(SessionContext ctx, String query, Map values, Class<?> resultClass) throws FlexibleSearchException
    {
        return search(ctx, query, values, Collections.singletonList(resultClass), true, true, 0, -1);
    }


    public SearchResult search(SessionContext ctx, String query, Map values, List resultClasses, boolean failOnUnknownFields, boolean dontNeedTotal, int start, int count) throws FlexibleSearchException
    {
        QueryOptions queryOptions = QueryOptions.newBuilder().withQuery(query).withValues(values).withResultClasses(resultClasses).withFailOnUnknownFields(failOnUnknownFields).withDontNeedTotal(dontNeedTotal).withStart(start).withCount(count).build();
        return search(ctx, queryOptions);
    }


    public SearchResult search(QueryOptions options)
    {
        return search(JaloSession.hasCurrentSession() ? getSession().getSessionContext() : null, options);
    }


    private SearchResult search(SessionContext ctx, QueryOptions options)
    {
        if(options.isPolyglotDialectQuery())
        {
            return executeSearchUsingPolyglotDialect(ctx, options);
        }
        return executeSearchUsingFlexibleSearchDialect(ctx, options);
    }


    private void validateQueryForFlexibleSearchDialect(TranslatedQuery tQuery)
    {
        if(checkIfPolyglotQueryHasTypesSupportedByEJBOnly(tQuery))
        {
            StringBuilder unsupportedTypes = new StringBuilder();
            for(Integer typeCode : getBeansTypeCodes(tQuery))
            {
                TypeInfo typeInfo = TypeInfoFactory.getTypeInfo(typeCode.intValue());
                boolean backed = PolyglotPersistenceGenericItemSupport.isFullyBackedByTheEJBPersistence(getTenant(), typeInfo);
                if(!backed)
                {
                    ComposedType type = getTenant().getJaloConnection().getTypeManager().getRootComposedType(typeCode.intValue());
                    unsupportedTypes.append("[").append(type.getCode()).append("] ");
                }
            }
            String message = "Types: " + unsupportedTypes + " that have been used in the query are not supported by the sql dialect. The query must be written in the polyglot dialect. " + getErrorMessage(tQuery
                            .getSQLTemplate());
            if(Config.getBoolean("polyglot.validation.should.throw.exception", false))
            {
                throw new FlexibleSearchException(message);
            }
            LOG.info("{}", message);
        }
    }


    private boolean checkIfPolyglotValidationNeeded(QueryOptions options)
    {
        return (Config.getBoolean("polyglot.validation.enabled", false) && !options.isStartFromPolyglot());
    }


    private boolean checkIfPolyglotQueryHasTypesSupportedByEJBOnly(TranslatedQuery tQuery)
    {
        Set<Integer> beanTCs = getBeansTypeCodes(tQuery);
        for(Integer beanTC : beanTCs)
        {
            TypeInfo typeInfo = TypeInfoFactory.getTypeInfo(beanTC.intValue());
            boolean byTheEJBPersistence = PolyglotPersistenceGenericItemSupport.isFullyBackedByTheEJBPersistence(
                            getTenant(), typeInfo);
            if(!byTheEJBPersistence)
            {
                return true;
            }
        }
        return false;
    }


    private String getErrorMessage(String query)
    {
        return Config.getBoolean("flexible.search.exception.show.query.details", false) ? ("query = '" +
                        query + "'") :
                        "Enable the property 'flexible.search.exception.show.query.details' for more details";
    }


    private SearchResult executeSearchUsingPolyglotDialect(SessionContext ctx, QueryOptions options)
    {
        PolyglotFsResult result = PolyglotPersistenceFlexibleSearchSupport.search(getTenant(), this, options);
        if(result.isUnknown())
        {
            QueryOptions fsOptions = options.toFlexibleSearchDialect();
            return executeSearchUsingFlexibleSearchDialect(ctx, fsOptions);
        }
        return (SearchResult)getStandardSearchResult(ctx, options, result, "undefined");
    }


    private StandardSearchResult getStandardSearchResult(SessionContext ctx, QueryOptions options, PolyglotFsResult polyFsResult, String dataSourceId)
    {
        LazyLoadItemList lazyLoadItemList;
        List<PK> pks = (List<PK>)polyFsResult.getResult().map(itemState -> (Identity)itemState.get((Key)PolyglotModelFactory.pk())).map(PolyglotPersistenceGenericItemSupport.PolyglotJaloConverter::toJaloLayer).collect(Collectors.toCollection(ArrayList::new));
        if(shouldPolyglotReturnPKs(options))
        {
            List<?> resultList = pks;
        }
        else
        {
            lazyLoadItemList = new LazyLoadItemList(WrapperFactory.getPrefetchLanguages(ctx), pks, 100);
        }
        int totalSize = lazyLoadItemList.size();
        if(!options.isDontNeedTotal())
        {
            totalSize = polyFsResult.getTotalCount();
        }
        return new StandardSearchResult((List)lazyLoadItemList, totalSize, options.getStart(), options.getCount(), dataSourceId);
    }


    private boolean shouldPolyglotReturnPKs(QueryOptions options)
    {
        return (CollectionUtils.size(options.getResultClasses()) == 1 && PK.class.equals(options.getResultClasses().get(0)));
    }


    public void processSearchRows(QueryOptions options, Consumer<Object> rowConsumer)
    {
        processSearchRows(JaloSession.hasCurrentSession() ? getSession().getSessionContext() : null, options, rowConsumer);
    }


    private <T> T withLocalCtx(SessionContext ctx, Function<SessionContext, T> closure)
    {
        SessionContext localCtx = null;
        try
        {
            if(ctx != null)
            {
                localCtx = JaloSession.getCurrentSession(getTenant()).createLocalSessionContext(ctx);
                localCtx.setAttribute("disableExecution", Boolean.FALSE);
            }
            return closure.apply(localCtx);
        }
        finally
        {
            if(localCtx != null)
            {
                JaloSession.getCurrentSession(getTenant()).removeLocalSessionContext();
            }
        }
    }


    private void processSearchRows(SessionContext ctx, QueryOptions options, Consumer<Object> rowConsumer)
    {
        try
        {
            withLocalCtx(ctx, localCtx -> {
                PK langPK = getSearchLangPK(localCtx);
                TranslatedQuery tQuery = translate(localCtx, getRestrictionPrincipal(localCtx), options.getQuery(), options.getValuesCount(), (langPK != null), options.isFailOnUnknownFields(), disableRestrictions(localCtx), disablePrincipalGroupRestrictions(localCtx), options.getValues());
                Map params = tQuery.removeUnusedValues(translatePathValueKeys(localCtx, tQuery.getValueKeys(), (Map)new CaseInsensitiveParameterMap(options.getValues())));
                this.fsExecutor.processSearchRows(options.getStart(), options.getCount(), tQuery, options.getResultClasses(), params, langPK, options.getHints(), rowConsumer);
                return null;
            });
        }
        catch(FlexibleSearchException e)
        {
            LOG.error("Flexible search error occured...");
            throw e;
        }
        catch(SystemIsSuspendedException e)
        {
            LOG.error("System is " + e.getSystemStatus() + ". Polling is paused.");
            throw e;
        }
        catch(JaloItemNotFoundException e)
        {
            throw new JaloInternalException(e, "cannot get type by translated query type pk", 0);
        }
    }


    private SearchResult executeSearchUsingFlexibleSearchDialect(SessionContext ctx, QueryOptions options)
    {
        TranslatedQuery tQuery = null;
        int prefetchSize = getSearchPrefetchSize(ctx);
        LOG.debug("query='{}'; signature='{}'; principal='{}'; lang='{}'; disabled [{} {} {}]; failOnUnknown={}; start={}; count={}; prefetch={}, withHints={}", new Object[] {
                        options
                                        .getQuery(), options.getResultClasses(), getRestrictionPrincipal(ctx), getSearchLangPK(ctx),
                        disableRestrictions(ctx) ? "R" : "", disablePrincipalGroupRestrictions(ctx) ? "GR" : "",
                        disableSessionAttributes(ctx) ? "SA" : "", Boolean.valueOf(options.isFailOnUnknownFields()), Integer.valueOf(options.getStart()),
                        Integer.valueOf(options.getCount()),
                        Integer.valueOf(prefetchSize)});
        boolean doExecuteQuery = enableExecution(ctx);
        SessionContext localCtx = null;
        try
        {
            if(ctx != null)
            {
                localCtx = JaloSession.getCurrentSession(getTenant()).createLocalSessionContext(ctx);
                localCtx.setAttribute("disableExecution", Boolean.FALSE);
            }
            PK langPK = getSearchLangPK(localCtx);
            tQuery = translate(localCtx,
                            getRestrictionPrincipal(localCtx), options
                                            .getQuery(), options
                                            .getValuesCount(), (langPK != null), options
                                            .isFailOnUnknownFields(),
                            disableRestrictions(localCtx),
                            disablePrincipalGroupRestrictions(localCtx), options
                                            .getValues());
            if(checkIfPolyglotValidationNeeded(options))
            {
                PolyglotQueryLogger.logQueryIfNeeded(options, getBeansTypeCodes(tQuery));
                validateQueryForFlexibleSearchDialect(tQuery);
            }
            Map _values = tQuery.removeUnusedValues(
                            translatePathValueKeys(localCtx, tQuery
                                            .getValueKeys(), (Map)new CaseInsensitiveParameterMap(options
                                            .getValues())));
            List<Hint> hintsForQuery = options.getHints();
            HybrisDataSource dataSourceForQuery = this.fsExecutor.getDataSourceForQuery(hintsForQuery);
        }
        catch(FlexibleSearchException e)
        {
            LOG.error("Flexible search error occured...");
            if(tQuery == null)
            {
                LOG.error("Query translation was not successful.");
            }
            throw e;
        }
        catch(SystemIsSuspendedException e)
        {
            LOG.error("System is " + e.getSystemStatus() + ". Polling is paused.");
            throw e;
        }
        catch(JaloItemNotFoundException e)
        {
            throw new JaloInternalException(e, "cannot get type by translated query type pk", 0);
        }
        finally
        {
            if(localCtx != null)
            {
                JaloSession.getCurrentSession(getTenant()).removeLocalSessionContext();
            }
        }
    }


    private boolean isCachingReadOnlyDisabled(SessionContext localCtx)
    {
        if(localCtx != null)
        {
            Object attributeValue = localCtx.getAttribute("ctx.enable.fs.cache.on.read-replica");
            if(attributeValue != null)
            {
                return Boolean.FALSE.toString().equalsIgnoreCase(attributeValue.toString());
            }
        }
        return false;
    }


    public Optional<Boolean> isReadOnlyDataSourceEnabled(SessionContext ctx)
    {
        return this.readOnlyConditionsHelper.readOnlyDataSourceEnabledInSessionContext(ctx);
    }


    private Set<Integer> getBeansTypeCodes(TranslatedQuery tQuery)
    {
        Set<PK> queryTCs = tQuery.getTypePKs();
        Set<Integer> beanTCs = new HashSet<>(queryTCs.size() * 4);
        PersistenceManager pm = getTenant().getPersistenceManager();
        for(PK typePK : queryTCs)
        {
            beanTCs.addAll(pm.getBeanTypeCodes(typePK));
        }
        if(beanTCs.isEmpty())
        {
            throw new JaloInvalidParameterException("empty bean typcode list ( typePKs = " + tQuery
                            .getTypePKs() + ", query = " + tQuery.getSQLTemplate() + " )", 0);
        }
        return beanTCs;
    }


    protected boolean isCachingDisabled(SessionContext localCtx)
    {
        return (localCtx != null && Boolean.TRUE.equals(localCtx.getAttribute("disableCache")));
    }


    FlexibleSearchCacheUnit createCacheUnit(int prefetchSize, Set<PK> languages, Map _values, FlexibleSearchCacheKey cacheKey)
    {
        HybrisDataSource dataSource = this.fsExecutor.getDataSourceForQuery(cacheKey.hints);
        return new FlexibleSearchCacheUnit(this, _values, cacheKey, prefetchSize, languages, getTenant().getCache(), dataSource.getID());
    }


    FlexibleSearchCacheKey createCacheKey(List<Class<?>> resultClasses, boolean dontNeedTotal, int start, int count, TranslatedQuery tQuery, boolean doExecuteQuery, PK langPK, Map _values, Set<Integer> beanTCs, int ttl, List<Hint> hints)
    {
        String cacheDomain = getDataSourceCacheDomain(this.fsExecutor.getDataSourceForQuery(hints));
        int ttlForQuery = getTTLForQuery(cacheDomain, ttl, hints);
        if(StringUtils.isNotBlank(cacheDomain) && ((CacheDomainsConfig)this.cacheDomainsConfig.get()).getTTLForDomain(cacheDomain).isPresent())
        {
            return (FlexibleSearchCacheKey)new FlexibleSearchTypeGenIgnoreCacheKey(tQuery, _values, langPK, resultClasses, dontNeedTotal, start, count, beanTCs, doExecuteQuery, ttlForQuery,
                            getTenant().getTenantID(), cacheDomain, hints);
        }
        return new FlexibleSearchCacheKey(tQuery, _values, langPK, resultClasses, dontNeedTotal, start, count, beanTCs, doExecuteQuery, ttlForQuery,
                        getTenant().getTenantID(), cacheDomain, hints);
    }


    private int getTTLForQuery(String cacheDomain, int defaultTTL, List<Hint> hints)
    {
        if(defaultTTL != -1)
        {
            return defaultTTL;
        }
        return ((Integer)getTTLForHints(hints).or(() -> getTTLForCacheDomain(cacheDomain))
                        .orElse(Integer.valueOf(defaultTTL))).intValue();
    }


    private Optional<Integer> getTTLForCacheDomain(String cacheDomain)
    {
        if(StringUtils.isBlank(cacheDomain))
        {
            return Optional.empty();
        }
        return ((CacheDomainsConfig)this.cacheDomainsConfig.get()).getTTLForDomain(cacheDomain);
    }


    private Optional<Integer> getTTLForHints(List<Hint> hints)
    {
        if(hints.isEmpty())
        {
            return Optional.empty();
        }
        Objects.requireNonNull(FlexibleSearchHints.CategorizedQueryHint.class);
        Objects.requireNonNull(FlexibleSearchHints.CategorizedQueryHint.class);
        return hints.stream().filter(FlexibleSearchHints.CategorizedQueryHint.class::isInstance).map(FlexibleSearchHints.CategorizedQueryHint.class::cast)
                        .map(c -> c.getTTL(getTenant()))
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .reduce(Integer::min);
    }


    private String getDataSourceCacheDomain(HybrisDataSource dataSource)
    {
        String id = dataSource.getID();
        if(StringUtils.isBlank(id))
        {
            return "";
        }
        return ((CacheDomainsConfig)this.cacheDomainsConfig.get()).getDomain(dataSource.getID()).orElse("");
    }


    private CacheDomainsConfig buildCacheDomainsConfig()
    {
        Map<String, String> dataSourceDomains = getConfiguredParameters("flexiblesearch.datasource.", ".cacheDomain", Map.Entry::getValue);
        Map<String, Integer> domainTTLs = getConfiguredParameters("flexiblesearch.cacheDomain.", ".ttl", e -> {
            try
            {
                int ttl = Config.getInt((String)e.getKey(), -1);
                return Integer.valueOf((ttl <= 0) ? -1 : ttl);
            }
            catch(NumberFormatException ex)
            {
                LOG.warn("TTL value provided in property {} is not valid - TTL for this cache domain will be disabled", e.getKey());
                return Integer.valueOf(-1);
            }
        });
        return new CacheDomainsConfig(dataSourceDomains, domainTTLs);
    }


    private <T> Map<String, T> getConfiguredParameters(String prefix, String suffix, Function<Map.Entry<String, String>, T> valueMapper)
    {
        Map<String, String> configuredDomains = Config.getParametersByPattern(prefix);
        return (Map<String, T>)configuredDomains.entrySet()
                        .stream()
                        .filter(e -> ((String)e.getKey()).endsWith(suffix))
                        .collect(Collectors.toMap(e -> {
                            domain = StringUtils.removeStart((String)e.getKey(), prefix);
                            return StringUtils.removeEnd(domain, suffix);
                        }valueMapper));
    }


    public static boolean isUnionAllForTypeHierarchyEnabled()
    {
        Object attribute = JaloSession.getCurrentSession().getAttribute("unionAllTypeHierarchy");
        if(attribute != null && attribute instanceof Boolean)
        {
            return ((Boolean)attribute).booleanValue();
        }
        return true;
    }


    protected SearchResult wrapSearchResult(SearchResult cachedSearchResult) throws Exception
    {
        return SQLSearchResultFactory.wrap(cachedSearchResult);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public SearchResult search(SessionContext ctx, String query, Map values, List resultClasses, boolean failOnUnknownFields, boolean dontNeedTotal, int start, int count, String timeoutCacheKey) throws FlexibleSearchException
    {
        return search(ctx, query, values, resultClasses, failOnUnknownFields, dontNeedTotal, start, count);
    }


    public SearchResult search(String query, Class<?> resultClass) throws FlexibleSearchException
    {
        return search(getSession().getSessionContext(), query, Collections.EMPTY_MAP, Collections.singletonList(resultClass), true, true, 0, -1);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public SearchResult search(String query, List values, List resultClasses, boolean failOnUnknownFields, boolean dontNeedTotal, boolean ignoreEmptyProperties, int start, int count, String timeoutCacheKey) throws FlexibleSearchException
    {
        return search(getSession().getSessionContext(), query, values, resultClasses, failOnUnknownFields, dontNeedTotal, ignoreEmptyProperties, start, count, timeoutCacheKey);
    }


    public SearchResult search(String query, Map values, Class resultClass) throws FlexibleSearchException
    {
        return search(getSession().getSessionContext(), query, values, resultClass);
    }


    public SearchResult search(String query, Map values, List resultClasses, boolean failOnUnknownFields, boolean dontNeedTotal, int start, int count) throws FlexibleSearchException
    {
        return search(JaloSession.hasCurrentSession() ? getSession().getSessionContext() : null, query, values, resultClasses, failOnUnknownFields, dontNeedTotal, start, count);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public SearchResult search(String query, Map values, List resultClasses, boolean failOnUnknownFields, boolean dontNeedTotal, int start, int count, String timeoutCacheKey) throws FlexibleSearchException
    {
        return search(JaloSession.hasCurrentSession() ? getSession().getSessionContext() : null, query, values, resultClasses, failOnUnknownFields, dontNeedTotal, start, count);
    }


    public void setContextQueryFilters(Collection<ContextQueryFilter> filters)
    {
        setContextQueryFilters(getSession().getSessionContext(), filters);
    }


    public void setContextQueryFilters(SessionContext ctx, Collection<ContextQueryFilter> filters)
    {
        if(ctx == null)
        {
            throw new NullPointerException("ctx was null");
        }
        if(filters != null && !filters.isEmpty())
        {
            ctx.setAttribute("ctxSearchRestrictions", new ArrayList<>(filters));
        }
        else
        {
            ctx.removeAttribute("ctxSearchRestrictions");
        }
    }


    public Object writeReplace() throws ObjectStreamException
    {
        return new FlexibleSearchSerializableDTO(getTenant());
    }


    protected void checkBeforeItemRemoval(SessionContext ctx, Item item) throws ConsistencyCheckException
    {
    }


    protected boolean disableExecution(SessionContext ctx)
    {
        return (ctx != null && Boolean.TRUE.equals(ctx.getAttribute("disableExecution")));
    }


    protected boolean enableExecution(SessionContext ctx)
    {
        return !disableExecution(ctx);
    }


    protected boolean disablePrincipalGroupRestrictions(SessionContext ctx)
    {
        return (ctx != null && Boolean.TRUE.equals(ctx.getAttribute("disableRestrictionGroupInheritance")));
    }


    @Deprecated(since = "ages", forRemoval = false)
    protected boolean disableRestrictions(SessionContext ctx)
    {
        if(!Config.getBoolean("restrictions.enabled", false))
        {
            return true;
        }
        if(ctx != null)
        {
            if(Boolean.TRUE.equals(ctx.getAttribute("disableRestrictions")))
            {
                return true;
            }
            User user = ctx.getUser();
            if(user != null && Constants.USER.ADMIN_EMPLOYEE.equals(user.getUID()))
            {
                return true;
            }
        }
        return false;
    }


    protected boolean disableSessionAttributes(SessionContext ctx)
    {
        return (ctx != null && Boolean.TRUE.equals(ctx.getAttribute("disableSessionAttributes")));
    }


    protected SearchResult executeSearch(TranslatedQuery tQuery, Map values, PK langPK, List<Class<?>> resultClasses, boolean dontNeedTotal, int start, int count, int prefetchSize, Set<PK> prefetchLanguages, boolean doExecuteQuery, List<Hint> hints, DataSource dataSource)
                    throws FlexibleSearchException
    {
        if(doExecuteQuery)
        {
            if(dataSource == null)
            {
                return executeSearch(tQuery, values, langPK, resultClasses, dontNeedTotal, start, count, prefetchSize, prefetchLanguages, doExecuteQuery, hints);
            }
            return this.fsExecutor.execute(start, count, dontNeedTotal, tQuery, resultClasses, values, langPK, prefetchSize, prefetchLanguages, hints, dataSource);
        }
        return this.fsExecutor.simulate(start, count, tQuery, values, langPK, hints);
    }


    @Deprecated(since = "2105", forRemoval = true)
    protected SearchResult executeSearch(TranslatedQuery tQuery, Map values, PK langPK, List<Class<?>> resultClasses, boolean dontNeedTotal, int start, int count, int prefetchSize, Set<PK> prefetchLanguages, boolean doExecuteQuery, List<Hint> hints) throws FlexibleSearchException
    {
        return executeSearch(tQuery, values, langPK, resultClasses, dontNeedTotal, start, count, prefetchSize, prefetchLanguages, doExecuteQuery, hints, (DataSource)this.fsExecutor
                        .getDataSourceForQuery(hints));
    }


    @Deprecated(since = "ages", forRemoval = false)
    protected Collection<ContextQueryFilter> getContextQueryFilters(SessionContext ctx, Collection<ComposedType> types)
    {
        Collection<ContextQueryFilter> filters = (ctx != null) ? (Collection<ContextQueryFilter>)ctx.getAttribute("ctxSearchRestrictions") : null;
        Collection<ContextQueryFilter> ret = null;
        if(filters != null)
        {
            for(ContextQueryFilter f : filters)
            {
                if(types.contains(f.getRestrictionType()))
                {
                    if(ret == null)
                    {
                        ret = new ArrayList<>(filters.size());
                    }
                    ret.add(f);
                }
            }
        }
        return (ret != null) ? ret : Collections.EMPTY_LIST;
    }


    protected Principal getRestrictionPrincipal(SessionContext ctx)
    {
        return (ctx != null) ? (Principal)ctx.getUser() : null;
    }


    protected PK getSearchLangPK(SessionContext ctx)
    {
        return (ctx != null && ctx.getLanguage() != null) ? ctx.getLanguage().getPK() : null;
    }


    protected int getSearchPrefetchSize(SessionContext ctx)
    {
        Integer size = (ctx != null) ? (Integer)ctx.getAttribute("prefetchSize") : null;
        return (size != null) ? size.intValue() : 100;
    }


    protected int getTTL(SessionContext ctx)
    {
        if(ctx != null)
        {
            try
            {
                Integer i = (Integer)ctx.getAttribute("TTLForUnitInCache");
                return (i != null) ? i.intValue() : -1;
            }
            catch(ClassCastException e)
            {
                LOG.warn("illegal value for TTLForUnitInCache=" + ctx.getAttribute("TTLForUnitInCache"));
            }
        }
        return -1;
    }


    protected Collection<AbstractQueryFilter> getUserFilters(Principal principal, boolean includeGroups, Collection<ComposedType> types)
    {
        Collection<Principal> principals;
        List<List> matches;
        if(principal == null || principal.isAdmin())
        {
            return Collections.EMPTY_LIST;
        }
        Set<PrincipalGroup> groups = includeGroups ? principal.getAllGroups() : Collections.EMPTY_SET;
        if(groups.isEmpty())
        {
            principals = Collections.singleton(principal);
        }
        else
        {
            principals = new ArrayList(groups);
            principals.add(principal);
        }
        StringBuilder q = new StringBuilder();
        Map<String, Object> values = new HashMap<>(3);
        values.put("active", Boolean.TRUE);
        q.append("SELECT {")
                        .append("code")
                        .append("},{")
                        .append("restrictedType")
                        .append("},{")
                        .append("query")
                        .append("} ");
        q.append("FROM {SearchRestriction*} WHERE ");
        q.append(
                        FlexibleSearchUtils.buildOracleCompatibleCollectionStatement("{principal} IN (?principals)", "principals", "OR", principals, values));
        q.append(" AND ").append(
                        FlexibleSearchUtils.buildOracleCompatibleCollectionStatement("{restrictedType} IN (?types)", "types", "OR", types, values));
        q.append(" AND {").append("active").append("} = ?active ");
        SessionContext myCtx = getSession().getSessionContext();
        boolean useLocalCtx = !Boolean.TRUE.equals(myCtx.getAttribute("disableRestrictions"));
        if(useLocalCtx)
        {
            myCtx = getSession().createLocalSessionContext();
            myCtx.setAttribute("disableRestrictions", Boolean.TRUE);
        }
        try
        {
            matches = getTenant().getJaloConnection().getFlexibleSearch().search(myCtx, q.toString(), values, Arrays.asList((Class<?>[][])new Class[] {String.class, ComposedType.class, String.class}, ), true, true, 0, -1).getResult();
        }
        finally
        {
            if(useLocalCtx)
            {
                getSession().removeLocalSessionContext();
            }
        }
        if(matches.isEmpty())
        {
            return Collections.EMPTY_LIST;
        }
        Collection<AbstractQueryFilter> ret = new ArrayList<>(matches.size());
        for(List<String> row : matches)
        {
            ret.add(new SearchRestrictionFilter(row.get(0), (ComposedType)row.get(1), row.get(2)));
        }
        return ret;
    }


    protected Map listToMap(List list)
    {
        if(list == null)
        {
            return null;
        }
        if(list.isEmpty())
        {
            return Collections.EMPTY_MAP;
        }
        Map<Object, Object> map = new HashMap<>();
        for(int i = 0, s = list.size(); i < s; i++)
        {
            Object key = Integer.valueOf(i + 1);
            map.put(key, list.get(i));
        }
        return map;
    }


    protected void notifyItemRemoval(SessionContext ctx, Item item)
    {
    }


    protected TranslatedQuery translate(SessionContext ctx, Principal principal, String query, int valueCount, boolean hasLanguage, boolean failOnUnknownFields, boolean disableRestrictions, boolean disableGrouprestrictions, Map values) throws FlexibleSearchException
    {
        return getQueryParser().translateQuery(principal, query, valueCount, hasLanguage, failOnUnknownFields, disableRestrictions, disableGrouprestrictions,
                        (ctx != null) ? (Collection)ctx.getAttribute("ctxSearchRestrictions") :
                                        null, values);
    }


    protected Object translateObject(SessionContext ctx, Object tmp, String[] qualifiers, int i, String key)
    {
        return this.sessionParamTranslator.translateObject(ctx, tmp, qualifiers, i, key);
    }


    protected Map translatePathValueKeys(SessionContext ctx, List valueKeys, Map _values)
    {
        return this.sessionParamTranslator.translatePathValueKeys(ctx, valueKeys, _values);
    }


    private final String replaceTypePlaceholder(String query, String realTypeName)
    {
        StringBuilder sb = new StringBuilder();
        int pos = query.indexOf("$$$");
        int last = 0;
        while(pos >= 0)
        {
            sb.append(query.substring(last, pos));
            sb.append(realTypeName);
            last = pos + "$$$".length();
            pos = query.indexOf("$$$", last);
        }
        if(last <= query.length())
        {
            sb.append(query.substring(last));
        }
        return sb.toString();
    }


    public static FlexibleSearch getInstance()
    {
        return Registry.getCurrentTenant().getJaloConnection().getFlexibleSearch();
    }
}
