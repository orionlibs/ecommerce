package de.hybris.platform.persistence.flexiblesearch.polyglot;

import de.hybris.bootstrap.util.LocaleHelper;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.genericsearch.impl.GenericSearchQueryAdjuster;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SearchResult;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.flexiblesearch.QueryOptions;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.persistence.PolyglotPersistenceGenericItemSupport;
import de.hybris.platform.persistence.polyglot.ItemStateRepository;
import de.hybris.platform.persistence.polyglot.PolyglotPersistence;
import de.hybris.platform.persistence.polyglot.TypeInfoFactory;
import de.hybris.platform.persistence.polyglot.config.RepositoryResult;
import de.hybris.platform.persistence.polyglot.config.TypeInfo;
import de.hybris.platform.persistence.polyglot.model.Identity;
import de.hybris.platform.persistence.polyglot.model.ItemState;
import de.hybris.platform.persistence.polyglot.model.Key;
import de.hybris.platform.persistence.polyglot.model.LocalizedKey;
import de.hybris.platform.persistence.polyglot.model.SingleAttributeKey;
import de.hybris.platform.persistence.polyglot.search.FindResult;
import de.hybris.platform.persistence.polyglot.search.StandardFindResult;
import de.hybris.platform.persistence.polyglot.search.criteria.ConditionVisitor;
import de.hybris.platform.persistence.polyglot.search.criteria.Criteria;
import de.hybris.platform.persistence.polyglot.search.dialect.CriteriaExtractor;
import de.hybris.platform.persistence.polyglot.search.dialect.PolyglotDialect;
import de.hybris.platform.persistence.polyglot.view.ItemStateView;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;

public final class PolyglotPersistenceFlexibleSearchSupport
{
    public static Optional<Criteria> tryToConvertToPolyglotCriteria(QueryOptions query)
    {
        if(!isQueryStringInPolyglotDialect(query))
        {
            return Optional.empty();
        }
        return Optional.of(convertToPolyglotCriteria(query));
    }


    public static String convertToFlexibleSearchQueryString(Criteria criteria)
    {
        PK typePK = PolyglotPersistenceGenericItemSupport.PolyglotJaloConverter.toJaloLayer(criteria.getTypeIdentity());
        ComposedType composedType = (ComposedType)JaloSession.lookupItem(typePK);
        StringBuilder result = new StringBuilder("SELECT {PK}");
        for(int i = 1; i < criteria.getRequestedKeys().size(); i++)
        {
            String qualifier = ((SingleAttributeKey)criteria.getRequestedKeys().get(i)).getQualifier();
            if(StringUtils.isNotEmpty(qualifier))
            {
                result.append(", {").append(qualifier).append("}");
            }
        }
        result.append(" FROM ").append("{").append(composedType.getCode());
        if(criteria.isTypeExclusive())
        {
            result.append("!");
        }
        result.append("}");
        if(criteria.hasCondition())
        {
            ToFlexibleSearchConditionVisitor fsConditionsVisitor = new ToFlexibleSearchConditionVisitor();
            criteria.getCondition().visit((ConditionVisitor)fsConditionsVisitor);
            String fsCondition = fsConditionsVisitor.getString();
            result.append(" WHERE ").append(fsCondition);
        }
        if(criteria.hasOrderBy())
        {
            Criteria.OrderBy orderBy = criteria.getOrderBy();
            result.append(" ORDER BY ");
            int numberOfElements = orderBy.getElementCount();
            for(int j = 0; j < numberOfElements; j++)
            {
                if(j > 0)
                {
                    result.append(", ");
                }
                Criteria.OrderByElement element = orderBy.getElement(j);
                StringBuilder orderByBuffer = new StringBuilder(toFlexibleSearchAttribute(element.getKey()));
                GenericSearchQueryAdjuster.getDefault().adjustQueryForOrderBy(result, orderByBuffer, composedType.getCode(), element.getKey().getQualifier());
                result.append(" ").append(element.getDirection().toString());
            }
        }
        return result.toString();
    }


    private static boolean isQueryStringInPolyglotDialect(QueryOptions query)
    {
        return query.getQuery().toUpperCase(LocaleHelper.getPersistenceLocale()).startsWith("GET");
    }


    private static Criteria convertToPolyglotCriteria(QueryOptions query)
    {
        String queryString = query.getQuery();
        Criteria.CriteriaBuilder extractor = PolyglotDialect.prepareCriteriaBuilder(queryString, (CriteriaExtractor.TypeNameConverter)new ComposedTypeNameConverter());
        return getCriteria(extractor, query);
    }


    private static Criteria getCriteria(Criteria.CriteriaBuilder criteriaBuilder, QueryOptions query)
    {
        List<SingleAttributeKey> requestedKeys = (List<SingleAttributeKey>)query.getResultAttributes().stream().map(a -> PolyglotPersistence.getNonlocalizedKey(a.getKey())).collect(Collectors.toList());
        Map<String, Object> polyglotParams = (Map<String, Object>)query.getValues().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> PolyglotPersistenceGenericItemSupport.PolyglotJaloConverter.toPolyglotLayer(entry.getValue())));
        return criteriaBuilder.withParameters(polyglotParams).withRequestedKeys(requestedKeys).withCount(query.getCount())
                        .withStart(query.getStart()).withNeedTotal(!query.isDontNeedTotal()).withTypeExclusive(query.isTypeExclusive()).build();
    }


    private static String toFlexibleSearchAttribute(SingleAttributeKey key)
    {
        LocalizedKey localizedKey = (LocalizedKey)PolyglotPersistence.as(LocalizedKey.class, (Key)key);
        String qualifier = key.getQualifier() + key.getQualifier();
        return "{" + qualifier + "}";
    }


    public static PolyglotFsResult search(Tenant tenant, FlexibleSearch flexibleSearch, QueryOptions options)
    {
        Criteria criteria = options.getPolyglotQueryCriteria();
        TypeInfo typeInfo = TypeInfoFactory.getTypeInfo(criteria);
        RepositoryResult repositoryResult = PolyglotPersistence.getRepository(typeInfo);
        if(repositoryResult.isNotSupported())
        {
            return PolyglotFsResult.unknown();
        }
        if(repositoryResult.isFullySupported())
        {
            ItemStateRepository repo = repositoryResult.requireSingleRepositoryOnly();
            FindResult findResult = repo.find(criteria);
            return convertToPolyglotFsResult(findResult);
        }
        return convertToPolyglotFsResult(
                        getFindResultForItemStateRepositories(repositoryResult.getRepositories(), flexibleSearch, options));
    }


    private static PolyglotFsResult convertToPolyglotFsResult(FindResult fResult)
    {
        PolyglotFsResult fsResult = PolyglotFsResult.full(fResult.getResult());
        fsResult.setTotalCount(fResult.getTotalCount());
        return fsResult;
    }


    private static FindResult returnFindResultWithCalculatedCount(FindResult findResult)
    {
        if(findResult.getCount() < 0)
        {
            List<ItemStateView> totalList = (List<ItemStateView>)findResult.getResult().collect(Collectors.toList());
            return (FindResult)StandardFindResult.buildFromStream(totalList.stream()).withCount(totalList.size())
                            .withTotalCount(findResult.getTotalCount()).build();
        }
        return findResult;
    }


    private static FindResult getFindResultForItemStateRepositories(Set<ItemStateRepository> repositories, FlexibleSearch flexibleSearch, QueryOptions options)
    {
        Criteria origCriteria = options.getPolyglotQueryCriteria();
        QueryOptions updatedQueryOptions = updateQueryOptionsWithResultAttributesBuilder(options, origCriteria);
        Criteria modifiedCriteria = updatedQueryOptions.getPolyglotQueryCriteria();
        List<ItemStateRepository> allRepos = getAllPolyglotRepositoriesAndDBRepo(repositories, flexibleSearch, updatedQueryOptions);
        if(canRetriveDataWithOptimisation(options, origCriteria.hasOrderBy()))
        {
            return
                            (FindResult)StandardFindResult.buildFromFindResults(
                                                            findResultFromRepositoriesWithOptimisation(allRepos, modifiedCriteria, options.getCount()))
                                            .withCriteria(origCriteria).build();
        }
        return (FindResult)StandardFindResult.buildFromFindResults(findResultFromAllRepositories(allRepos, modifiedCriteria))
                        .withCriteria(origCriteria).build();
    }


    private static List<FindResult> findResultFromRepositoriesWithOptimisation(List<ItemStateRepository> itemStateRepositories, Criteria modifiedCriteria, int countSize)
    {
        List<FindResult> findResultToMerge = new ArrayList<>();
        Iterator<ItemStateRepository> repoIterator = itemStateRepositories.iterator();
        int actualCountSize = 0;
        while(actualCountSize < countSize && repoIterator.hasNext())
        {
            ItemStateRepository repo = repoIterator.next();
            FindResult result = repo.find(modifiedCriteria);
            result = returnFindResultWithCalculatedCount(result);
            actualCountSize += result.getCount();
            findResultToMerge.add(result);
        }
        return findResultToMerge;
    }


    private static List<FindResult> findResultFromAllRepositories(List<ItemStateRepository> itemStateRepositories, Criteria modifiedCriteria)
    {
        return (List<FindResult>)itemStateRepositories.stream().map(repo -> repo.find(modifiedCriteria)).collect(Collectors.toList());
    }


    private static boolean canRetriveDataWithOptimisation(QueryOptions options, boolean hasOrderBy)
    {
        return (!hasOrderBy && options.getStart() == 0 && options.getCount() > -1 && options.isDontNeedTotal());
    }


    private static List<ItemStateRepository> getAllPolyglotRepositoriesAndDBRepo(Set<ItemStateRepository> repositories, FlexibleSearch flexibleSearch, QueryOptions options)
    {
        List<ItemStateRepository> itemStateRepositoryList = new ArrayList<>(repositories);
        ItemStateRepositoryWrapperForDB dbWrapper = new ItemStateRepositoryWrapperForDB(flexibleSearch, options);
        itemStateRepositoryList.add(dbWrapper);
        return itemStateRepositoryList;
    }


    private static QueryOptions updateQueryOptionsWithResultAttributesBuilder(QueryOptions options, Criteria criteria)
    {
        QueryOptions.Builder newOptionsBuilder = QueryOptions.newBuilderFromTemplate(options);
        ModelService modelService = (ModelService)Registry.getApplicationContext().getBean("modelService", ModelService.class);
        ComposedTypeModel type = (ComposedTypeModel)modelService.get(PK.fromLong(criteria.getTypeId().getIdentity().toLongValue()));
        List<QueryOptions.Attribute> attributes = new ArrayList<>();
        attributes.add(new QueryOptions.Attribute(PK.class, PolyglotPersistence.getNonlocalizedKey("pk").getQualifier()));
        if(criteria.hasOrderBy())
        {
            for(int i = 0; i < criteria.getOrderBy().getElementCount(); i++)
            {
                String key = criteria.getOrderBy().getElement(i).getKey().getQualifier();
                type.getDeclaredattributedescriptors()
                                .stream()
                                .filter(descriptor -> descriptor.getQualifier().equalsIgnoreCase(key))
                                .findFirst()
                                .ifPresent(descriptor -> attributes.add(new QueryOptions.Attribute(descriptor.getPersistenceClass(), key)));
            }
        }
        newOptionsBuilder.withResultAttributes(attributes);
        newOptionsBuilder.withStart(0);
        if(options.getCount() >= 0)
        {
            if(criteria.hasOrderBy() || options.getStart() > 0)
            {
                newOptionsBuilder.withCount(options.getStart() + options.getCount());
            }
            else
            {
                newOptionsBuilder.withCount(options.getCount());
            }
        }
        else
        {
            newOptionsBuilder.withCount(-1);
        }
        return newOptionsBuilder.build();
    }


    private static Stream<ItemStateView> convertToPolyglotResult(SearchResult searchResult, List<SingleAttributeKey> keys)
    {
        SingleAttributeKey pkKey = PolyglotPersistence.getNonlocalizedKey("pk");
        if(keys.size() == 1 && keys.contains(pkKey))
        {
            return (Stream)createItemStateStreamWithIdentityOnly(searchResult.getResult(), pkKey);
        }
        return (Stream)createItemStateStreamWithManyKeys(searchResult.getResult(), keys);
    }


    private static Stream<ItemState> createItemStateStreamWithIdentityOnly(List<PK> searchResult, SingleAttributeKey pkKey)
    {
        List<ItemState> resultItemStates = new ArrayList<>();
        for(PK row : searchResult)
        {
            Map<SingleAttributeKey, Object> singleItemStateAttributes = new HashMap<>();
            Identity id = PolyglotPersistence.identityFromLong(row.getLongValue());
            singleItemStateAttributes.put(pkKey, id);
            FlexibleSearchResultItemState flexibleSearchResultItemState = new FlexibleSearchResultItemState(singleItemStateAttributes);
            resultItemStates.add(flexibleSearchResultItemState);
        }
        return resultItemStates.stream();
    }


    private static Stream<ItemState> createItemStateStreamWithManyKeys(List<List<Object>> searchResult, List<SingleAttributeKey> keys)
    {
        List<ItemState> resultItemStates = new ArrayList<>();
        for(List<Object> row : searchResult)
        {
            Iterator<Object> rowIterator = row.iterator();
            Map<SingleAttributeKey, Object> singleItemStateAttributes = new HashMap<>();
            for(SingleAttributeKey key : keys)
            {
                if(!rowIterator.hasNext())
                {
                    throw new IllegalStateException("Search result does not match requested keys");
                }
                Object value = rowIterator.next();
                if(value instanceof PK)
                {
                    value = PolyglotPersistence.identityFromLong(((PK)value).getLongValue());
                }
                singleItemStateAttributes.put(key, value);
            }
            FlexibleSearchResultItemState flexibleSearchResultItemState = new FlexibleSearchResultItemState(singleItemStateAttributes);
            resultItemStates.add(flexibleSearchResultItemState);
        }
        return resultItemStates.stream();
    }
}
