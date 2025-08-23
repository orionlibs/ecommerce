package de.hybris.platform.servicelayer.search.impl;

import de.hybris.platform.cache.Cache;
import de.hybris.platform.core.LazyLoadItemList;
import de.hybris.platform.core.LazyLoadMultiColumnList;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.WrapperFactory;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SearchResult;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearchException;
import de.hybris.platform.jalo.flexiblesearch.QueryOptions;
import de.hybris.platform.persistence.PolyglotPersistenceGenericItemSupport;
import de.hybris.platform.persistence.polyglot.TypeInfoFactory;
import de.hybris.platform.persistence.polyglot.config.TypeInfo;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.internal.converter.ConverterRegistry;
import de.hybris.platform.servicelayer.internal.converter.ModelConverter;
import de.hybris.platform.servicelayer.internal.model.ModelSearchStrategy;
import de.hybris.platform.servicelayer.internal.service.AbstractBusinessService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.RelationQuery;
import de.hybris.platform.servicelayer.search.TranslationResult;
import de.hybris.platform.servicelayer.search.internal.preprocessor.QueryPreprocessorRegistry;
import de.hybris.platform.servicelayer.search.internal.resolver.ItemObjectResolver;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.SQLSearchResult;
import de.hybris.platform.util.StandardSearchResult;
import edu.umd.cs.findbugs.annotations.SuppressWarnings;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultFlexibleSearchService extends AbstractBusinessService implements FlexibleSearchService
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultFlexibleSearchService.class);
    private ItemObjectResolver modelResolver;
    private ItemObjectResolver multiModelResolver;
    private volatile transient QueryPreprocessorRegistry queryPreprocessorRegistry;
    private volatile transient ModelSearchStrategy modelSearchStrategy;
    private volatile transient ConverterRegistry converterRegistry;
    private static final int DEFAULT_PREFETCH_SIZE = -1;


    @SuppressWarnings(value = {"DMI_UNSUPPORTED_METHOD"}, justification = "because I know better")
    public ConverterRegistry getConverterRegistry()
    {
        if(this.converterRegistry == null)
        {
            synchronized(this)
            {
                if(this.converterRegistry == null)
                {
                    this.converterRegistry = lookupConverterRegistry();
                }
            }
        }
        return this.converterRegistry;
    }


    public <T> T getModelByExample(T example)
    {
        ServicesUtil.validateParameterNotNull(example, "The parameter 'example' was null");
        return (T)getModelSearchStrategy().getModelByExample(getModelConverterByModel(example), example);
    }


    public <T> List<T> getModelsByExample(T example)
    {
        return getModelSearchStrategy().getModelsByExample(getModelConverterByModel(example), example);
    }


    @SuppressWarnings(value = {"DMI_UNSUPPORTED_METHOD"}, justification = "because I know better")
    public ModelSearchStrategy getModelSearchStrategy()
    {
        if(this.modelSearchStrategy == null)
        {
            synchronized(this)
            {
                if(this.modelSearchStrategy == null)
                {
                    this.modelSearchStrategy = lookupModelSearchStrategy();
                }
            }
        }
        return this.modelSearchStrategy;
    }


    public QueryPreprocessorRegistry getQueryPreprocessorRegistry()
    {
        if(this.queryPreprocessorRegistry == null)
        {
            synchronized(this)
            {
                if(this.queryPreprocessorRegistry == null)
                {
                    this.queryPreprocessorRegistry = lookupQueryPreprocessorRegistry();
                }
            }
        }
        return this.queryPreprocessorRegistry;
    }


    public ConverterRegistry lookupConverterRegistry()
    {
        throw new UnsupportedOperationException("please override DefaultFlexibleSearchService#lookupConverterRegistry() or use <lookup-method>");
    }


    public ModelSearchStrategy lookupModelSearchStrategy()
    {
        throw new UnsupportedOperationException("please override DefaultFlexibleSearchService#lookupModelSearchStrategy() or use <lookup-method>");
    }


    public QueryPreprocessorRegistry lookupQueryPreprocessorRegistry()
    {
        throw new UnsupportedOperationException("please override DefaultFlexibleSearchService#lookupQueryPreprocessorRegistry() or use <lookup-method>");
    }


    public <T> SearchResult<T> search(FlexibleSearchQuery query)
    {
        ServicesUtil.validateParameterNotNull(query, "The 'query' is null!");
        SearchResult<T> jaloResult = getJaloResult(query);
        if(jaloResult.getCount() == 0)
        {
            return (SearchResult<T>)createEmptyResult(jaloResult);
        }
        if(jaloResult instanceof StandardSearchResult)
        {
            return (SearchResult)wrapOrConvert(query, (StandardSearchResult)jaloResult, () -> convert(jaloResult));
        }
        return (SearchResult<T>)convert(jaloResult);
    }


    public <T> void processSearchRows(FlexibleSearchQuery query, Consumer<T> rowConsumer)
    {
        getSessionService().executeInLocalView((SessionExecutionBody)new Object(this, query, rowConsumer));
    }


    private Object resolveObj(Object obj, List<Class<?>> resultClasses, Cache cache)
    {
        if(resultClasses.size() == 1)
        {
            return getWrappedObject(obj, cache);
        }
        List<Object> resList = new ArrayList();
        List<Object> col = (List<Object>)obj;
        for(int i = 0; i < resultClasses.size(); i++)
        {
            resList.add(getWrappedObject(col.get(i), cache));
        }
        return resList;
    }


    private Object getWrappedObject(Object obj, Cache cache)
    {
        return getModelService().toModelLayer(WrapperFactory.wrap(cache, obj));
    }


    protected <T> SearchResultImpl<T> createEmptyResult(SearchResult<T> jaloResult)
    {
        SearchResultImpl<T> slSearchResult = new SearchResultImpl(Collections.emptyList(), jaloResult.getTotalCount(), jaloResult.getRequestedCount(), jaloResult.getRequestedStart(), jaloResult.getDataSourceId());
        if(jaloResult instanceof StandardSearchResult)
        {
            slSearchResult.setFromCache(((StandardSearchResult)jaloResult).isFromCache());
        }
        return slSearchResult;
    }


    private <T> SearchResultImpl<T> convert(SearchResult<T> jaloResult)
    {
        List<T> convertedResult = new ArrayList<>(jaloResult.getCount());
        for(Object row : jaloResult.getResult())
        {
            T convertedRow = convertRow(row);
            convertedResult.add(convertedRow);
        }
        SearchResultImpl<T> tmp = new SearchResultImpl(convertedResult, jaloResult.getTotalCount(), jaloResult.getRequestedCount(), jaloResult.getRequestedStart(), jaloResult.getDataSourceId());
        return tmp;
    }


    private <T> SearchResultImpl<T> wrapOrConvert(FlexibleSearchQuery query, StandardSearchResult<T> jaloResult, Supplier<SearchResultImpl<T>> fallbackIfWeCannotWrap)
    {
        SearchResultImpl<T> slSearchResult;
        int totalCount = jaloResult.getTotalCount();
        int reqStart = jaloResult.getRequestedStart();
        int reqCount = jaloResult.getRequestedCount();
        List searchResultList = jaloResult.getOriginalResultList();
        AbstractList resultList = prepareResultList(query, searchResultList);
        if(resultList != null)
        {
            slSearchResult = new SearchResultImpl(resultList, totalCount, reqCount, reqStart, jaloResult.getDataSourceId());
        }
        else
        {
            slSearchResult = fallbackIfWeCannotWrap.get();
        }
        slSearchResult.setFromCache(jaloResult.isFromCache());
        return slSearchResult;
    }


    public <T> SearchResult<T> search(String query)
    {
        return search(query, null);
    }


    public <T> SearchResult<T> search(String query, Map<String, ? extends Object> queryParams)
    {
        FlexibleSearchQuery fquery = new FlexibleSearchQuery(query);
        if(queryParams != null)
        {
            fquery.addQueryParameters(queryParams);
        }
        return search(fquery);
    }


    public <T> SearchResult<T> searchRelation(ItemModel model, String attribute, int start, int count)
    {
        return searchRelation(new RelationQuery(model, attribute, start, count));
    }


    public <T> SearchResult<T> searchRelation(RelationQuery query)
    {
        throw new UnsupportedOperationException("not implemented yet");
    }


    public <T> T searchUnique(FlexibleSearchQuery searchQuery)
    {
        SearchResult<T> searchResult = search(searchQuery);
        List<T> result = searchResult.getResult();
        if(result.isEmpty())
        {
            throw new ModelNotFoundException("No result for the given query");
        }
        if(result.size() > 1)
        {
            throw new AmbiguousIdentifierException("Found " + result.size() + " results for the given query");
        }
        T resultingModel = result.get(0);
        if(resultingModel == null)
        {
            throw new ModelNotFoundException("No result for the given query");
        }
        return resultingModel;
    }


    @Required
    public void setModelResolver(ItemObjectResolver modelResolver)
    {
        this.modelResolver = modelResolver;
    }


    @Required
    public void setMultiModelResolver(ItemObjectResolver multiModelResolver)
    {
        this.multiModelResolver = multiModelResolver;
    }


    public TranslationResult translate(FlexibleSearchQuery query)
    {
        return (TranslationResult)getSessionService().executeInLocalView((SessionExecutionBody)new Object(this, query));
    }


    private TranslationResult createTranslationResultForNotSupportedQuery(FlexibleSearchQuery query)
    {
        String message = "Method translate is not supported by the polyglot persistence. " + getDetailsErrorMessage(query.getQuery());
        if(Config.getBoolean("polyglot.validation.should.throw.exception", false))
        {
            throw new FlexibleSearchException(message);
        }
        LOG.info("{}", message);
        return new TranslationResult(message, Collections.emptyList());
    }


    private TranslationResult createTranslationResultForSupportedQuery(FlexibleSearchQuery query)
    {
        try
        {
            SearchResult searchResult = getFlexibleSearchInstance().search(
                            getFlexibleSearchInstance().getSession().getSessionContext(), query
                                            .getQuery(), query.getQueryParameters(), Collections.singletonList(Item.class), true, true, query
                                            .getStart(), query
                                            .getCount());
            String sqlQuery = ((SQLSearchResult)searchResult).getSQLForPreparedStatement();
            List<Object> valuesForPreparedStatement = ((SQLSearchResult)searchResult).getValuesForPreparedStatement();
            return new TranslationResult(sqlQuery, valuesForPreparedStatement);
        }
        catch(FlexibleSearchException e)
        {
            throw new FlexibleSearchException(e.getMessage(), e);
        }
    }


    private String getDetailsErrorMessage(String query)
    {
        return Config.getBoolean("flexible.search.exception.show.query.details", false) ?
                        String.format("query = '%s'", new Object[] {query}) : "Enable the property 'flexible.search.exception.show.query.details' for more details";
    }


    private boolean isTranslateMethodSupportedForQuery(FlexibleSearchQuery query)
    {
        QueryOptions options = QueryOptions.newBuilder().withQuery(query.getQuery()).withValues(query.getQueryParameters()).build();
        if(options.isPolyglotDialectQuery())
        {
            TypeInfo typeInfo = TypeInfoFactory.getTypeInfo(options.getPolyglotQueryCriteria());
            return PolyglotPersistenceGenericItemSupport.isFullyBackedByTheEJBPersistence(getCurrentTenant(), typeInfo);
        }
        return true;
    }


    protected ModelConverter getModelConverterByModel(Object model)
    {
        String type = getSourceTypeFromModel(model);
        ModelConverter ret = null;
        if(type != null)
        {
            ret = getConverterRegistry().getModelConverterBySourceType(type);
            if(model instanceof de.hybris.platform.servicelayer.model.AbstractItemModel && ret instanceof de.hybris.platform.servicelayer.internal.converter.impl.EnumValueModelConverter)
            {
                ret = null;
            }
        }
        if(ret == null)
        {
            ret = getConverterRegistry().getModelConverterByModelType(model.getClass());
        }
        return ret;
    }


    protected String getSourceTypeFromModel(Object model)
    {
        return getConverterRegistry().getModelConverterByModelType(model.getClass()).getType(model);
    }


    private <T> T convertRow(Object row)
    {
        if(row instanceof List)
        {
            List rowElems = (List)row;
            List<Object> convertedRow = new ArrayList(rowElems.size());
            for(Object rowElem : rowElems)
            {
                convertedRow.add(getModelService().toModelLayer(rowElem));
            }
            return (T)convertedRow;
        }
        return (T)getModelService().toModelLayer(row);
    }


    private <T> SearchResult<T> getJaloResult(FlexibleSearchQuery query)
    {
        return (SearchResult<T>)getSessionService().executeInLocalView((SessionExecutionBody)new Object(this, query));
    }


    private List<Class<?>> convertModelClassList(FlexibleSearchQuery query)
    {
        List<Class<?>> resultClassList = new ArrayList<>(query.getResultClassList().size());
        for(Class modelClass : query.getResultClassList())
        {
            resultClassList.add(getModelService().getModelTypeClass(modelClass));
        }
        return resultClassList;
    }


    protected FlexibleSearch getFlexibleSearchInstance()
    {
        return FlexibleSearch.getInstance();
    }


    public <T> T toPersistenceLayer(Object modelValue)
    {
        T ret = (T)modelValue;
        if(modelValue == null)
        {
            ret = null;
        }
        else if(modelValue instanceof Collection)
        {
            Collection orig = (Collection)modelValue;
            if(orig.isEmpty())
            {
                Collection collection = orig;
            }
            else
            {
                int s = orig.size();
                Collection wrapped = (modelValue instanceof java.util.Set) ? new LinkedHashSet(s) : new ArrayList(s);
                for(Object o : orig)
                {
                    wrapped.add(toPersistenceLayer(o));
                }
                Collection collection1 = wrapped;
            }
        }
        else if(modelValue instanceof Map)
        {
            Map<?, ?> orig = (Map<?, ?>)modelValue;
            if(orig.isEmpty())
            {
                Map<?, ?> map = orig;
            }
            else
            {
                Map<Object, Object> wrapped = new LinkedHashMap<>(orig.size());
                for(Map.Entry<?, ?> e : orig.entrySet())
                {
                    wrapped.put(toPersistenceLayer(e.getKey()), toPersistenceLayer(e.getValue()));
                }
                Map<Object, Object> map1 = wrapped;
            }
        }
        else if(modelValue instanceof ItemModel)
        {
            PK pK = ((ItemModel)modelValue).getPk();
            if(pK == null)
            {
                throw new IllegalStateException("Model used as a search parameter must contain PK!");
            }
        }
        else if(modelValue instanceof de.hybris.platform.core.HybrisEnumValue)
        {
            Object model = getModelService().getSource(modelValue);
            ret = (model != null) ? (T)model : (T)modelValue;
        }
        return ret;
    }


    private AbstractList prepareResultList(FlexibleSearchQuery query, List searchResultList)
    {
        LazyLoadMultiColumnModelList lazyLoadMultiColumnModelList;
        AbstractList result = null;
        if(searchResultList instanceof LazyLoadItemList)
        {
            LazyLoadItemList jaloList = (LazyLoadItemList)searchResultList;
            LazyLoadModelList lazyLoadModelList = new LazyLoadModelList(jaloList, -1, query.getResultClassList(), this.modelResolver);
        }
        else if(searchResultList instanceof LazyLoadMultiColumnList)
        {
            LazyLoadMultiColumnList jaloList = (LazyLoadMultiColumnList)searchResultList;
            lazyLoadMultiColumnModelList = new LazyLoadMultiColumnModelList(jaloList, -1, query.getResultClassList(), this.multiModelResolver);
        }
        return (AbstractList)lazyLoadMultiColumnModelList;
    }


    public Optional<Boolean> isReadOnlyDataSourceEnabled()
    {
        return getFlexibleSearchInstance().isReadOnlyDataSourceEnabled(JaloSession.getCurrentSession().getSessionContext());
    }


    public Optional<Boolean> isReadOnlyDataSourceEnabled(SessionContext ctx)
    {
        return getFlexibleSearchInstance().isReadOnlyDataSourceEnabled(ctx);
    }
}
