package de.hybris.platform.solrfacetsearch.search.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.solrfacetsearch.config.FacetType;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.IndexedTypeFieldsValuesProvider;
import de.hybris.platform.solrfacetsearch.search.CoupledQueryField;
import de.hybris.platform.solrfacetsearch.search.FacetSearchException;
import de.hybris.platform.solrfacetsearch.search.FacetValueField;
import de.hybris.platform.solrfacetsearch.search.FieldNameTranslator;
import de.hybris.platform.solrfacetsearch.search.QueryField;
import de.hybris.platform.solrfacetsearch.search.RawQuery;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.SolrQueryConverter;
import de.hybris.platform.solrfacetsearch.search.SolrQueryPostProcessor;
import de.hybris.platform.solrfacetsearch.search.context.FacetSearchContext;
import de.hybris.platform.solrfacetsearch.search.context.FacetSearchContextFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Required;

public class DefaultSolrQueryConverter implements SolrQueryConverter, BeanFactoryAware
{
    private static final Logger LOG = Logger.getLogger(DefaultSolrQueryConverter.class);
    protected static final String ALL_QUERY = "*:*";
    protected static final SearchQuery.Operator DEFAULT_FIELD_OPERATOR = SearchQuery.Operator.AND;
    protected static final int DEFAULT_LIMIT = 50;
    private FieldNameTranslator fieldNameTranslator;
    private List<SolrQueryPostProcessor> queryPostProcessors;
    private FacetSort facetSort;
    private SearchQuery.Operator fieldOperator;
    private Integer defaultLimit = Integer.valueOf(50);
    private String forbiddenChar = "_";
    private FacetSearchContextFactory<FacetSearchContext> facetSearchContextFactory;
    private Converter<SearchQueryConverterData, SolrQuery> legacyFacetSearchQueryConverter;
    private BeanFactory beanFactory;


    protected FieldNameTranslator getFieldNameTranslator()
    {
        return this.fieldNameTranslator;
    }


    @Required
    public void setFieldNameTranslator(FieldNameTranslator fieldNameTranslator)
    {
        this.fieldNameTranslator = fieldNameTranslator;
    }


    public List<SolrQueryPostProcessor> getQueryPostProcessors()
    {
        return this.queryPostProcessors;
    }


    public void setQueryPostProcessors(List<SolrQueryPostProcessor> queryPostProcessors)
    {
        this.queryPostProcessors = queryPostProcessors;
    }


    public FacetSort getFacetSort()
    {
        return this.facetSort;
    }


    @Required
    public void setFacetSort(FacetSort facetSort)
    {
        this.facetSort = facetSort;
    }


    public SearchQuery.Operator getFieldOperator()
    {
        return this.fieldOperator;
    }


    public void setFieldOperator(SearchQuery.Operator fieldOperator)
    {
        this.fieldOperator = fieldOperator;
    }


    public Integer getDefaultLimit()
    {
        return this.defaultLimit;
    }


    public void setDefaultLimit(Integer defaultLimit)
    {
        if(defaultLimit == null)
        {
            this.defaultLimit = Integer.valueOf(50);
        }
        else
        {
            this.defaultLimit = defaultLimit;
        }
    }


    public String getForbiddenChar()
    {
        return this.forbiddenChar;
    }


    public void setForbiddenChar(String forbiddenChar)
    {
        if(forbiddenChar == null)
        {
            this.forbiddenChar = "_";
        }
        else
        {
            this.forbiddenChar = forbiddenChar;
        }
    }


    public FacetSearchContextFactory<FacetSearchContext> getFacetSearchContextFactory()
    {
        return this.facetSearchContextFactory;
    }


    @Required
    public void setFacetSearchContextFactory(FacetSearchContextFactory<FacetSearchContext> facetSearchContextFactory)
    {
        this.facetSearchContextFactory = facetSearchContextFactory;
    }


    public Converter<SearchQueryConverterData, SolrQuery> getLegacyFacetSearchQueryConverter()
    {
        return this.legacyFacetSearchQueryConverter;
    }


    @Required
    public void setLegacyFacetSearchQueryConverter(Converter<SearchQueryConverterData, SolrQuery> legacyFacetSearchQueryConverter)
    {
        this.legacyFacetSearchQueryConverter = legacyFacetSearchQueryConverter;
    }


    public BeanFactory getBeanFactory()
    {
        return this.beanFactory;
    }


    public void setBeanFactory(BeanFactory beanFactory)
    {
        this.beanFactory = beanFactory;
    }


    public SolrQuery convertSolrQuery(SearchQuery searchQuery) throws FacetSearchException
    {
        checkQuery(searchQuery);
        SolrQuery solrQuery = createSolrQuery(searchQuery);
        List<QueryField> queries = new ArrayList<>();
        List<QueryField> filterQueries = new ArrayList<>();
        Map<String, IndexedFacetInfo> facetInfoMap = getFacetInfo(searchQuery);
        List<CoupledQueryField> catalogVersionFilters = includeCatalogVersionFields(searchQuery);
        splitQueryFields(prepareQueryFields(searchQuery), queries, filterQueries, facetInfoMap);
        String[] convertedQueryFields = convertQueryFields(queries, null);
        String[] convertedCoupledQueryFields = convertCoupledQueryFields(searchQuery, searchQuery.getCoupledFields());
        String[] convertedRawQueries = convertRawQueries(searchQuery, searchQuery.getRawQueries());
        List<String> combinedQueryFields = new ArrayList<>();
        combinedQueryFields.addAll(Arrays.asList(convertedQueryFields));
        combinedQueryFields.addAll(Arrays.asList(convertedCoupledQueryFields));
        combinedQueryFields.addAll(Arrays.asList(convertedRawQueries));
        String query = buildQuery(combinedQueryFields.<String>toArray(new String[combinedQueryFields.size()]), searchQuery);
        solrQuery.setQuery(query);
        IndexedType indexedType = searchQuery.getIndexedType();
        if(indexedType.isGroup())
        {
            IndexedProperty groupIndexedProperty = (IndexedProperty)indexedType.getIndexedProperties().get(indexedType.getGroupFieldName());
            if(groupIndexedProperty == null)
            {
                throw new FacetSearchException("Grouping is enabled but no groupFieldName is configured in the indexed type");
            }
            String groupFieldName = getFieldNameTranslator().translate(searchQuery, groupIndexedProperty.getName(), FieldNameProvider.FieldType.INDEX);
            solrQuery.add("group", new String[] {"true"});
            solrQuery.add("group.field", new String[] {groupFieldName});
            solrQuery.add("group.limit", new String[] {Integer.toString(indexedType.getGroupLimit())});
            solrQuery.add("group.facet", new String[] {Boolean.toString(indexedType.isGroupFacets())});
            solrQuery.add("group.ngroups", new String[] {"true"});
        }
        if(searchQuery.isEnableSpellcheck() && StringUtils.isNotBlank(searchQuery.getUserQuery()))
        {
            solrQuery.add("spellcheck", new String[] {"true"});
            solrQuery.add("spellcheck.dictionary", new String[] {searchQuery.getLanguage()});
            solrQuery.add("spellcheck.collate", new String[] {Boolean.TRUE.toString()});
            solrQuery.add("spellcheck.q", new String[] {searchQuery.getUserQuery()});
        }
        String[] convertedQueryFilters = convertQueryFields(filterQueries, facetInfoMap);
        String[] convertedCatalogVersionFilters = convertCoupledQueryFields(searchQuery, catalogVersionFilters);
        String[] combinedFilterFields = (String[])ArrayUtils.addAll((Object[])convertedQueryFilters, (Object[])convertedCatalogVersionFilters);
        if(ArrayUtils.isNotEmpty((Object[])combinedFilterFields))
        {
            solrQuery.addFilterQuery(combinedFilterFields);
        }
        int start = searchQuery.getOffset() * searchQuery.getPageSize();
        solrQuery.setStart(Integer.valueOf(start));
        solrQuery.setRows(Integer.valueOf(searchQuery.getPageSize()));
        solrQuery.setFacet(true);
        addFacetFields(solrQuery, facetInfoMap);
        solrQuery.setFacetMinCount(1);
        solrQuery.setFacetLimit(this.defaultLimit.intValue());
        solrQuery.setFacetSort(this.facetSort.getName());
        if(searchQuery.getRawParams().size() > 0)
        {
            addSolrParams(solrQuery, searchQuery);
        }
        return applyPostProcessorsInOrder(solrQuery, searchQuery);
    }


    protected SolrQuery createSolrQuery(SearchQuery searchQuery)
    {
        FacetSearchContext facetSearchContext = this.facetSearchContextFactory.getContext();
        SearchQueryConverterData searchQueryConverterData = new SearchQueryConverterData();
        searchQueryConverterData.setFacetSearchContext(facetSearchContext);
        searchQueryConverterData.setSearchQuery(searchQuery);
        return (SolrQuery)this.legacyFacetSearchQueryConverter.convert(searchQueryConverterData);
    }


    protected List<CoupledQueryField> includeCatalogVersionFields(SearchQuery searchQuery)
    {
        List<CatalogVersionModel> catalogVersions = searchQuery.getCatalogVersions();
        if(catalogVersions != null && !catalogVersions.isEmpty())
        {
            List<CoupledQueryField> catalogVersionCoupledFields = new ArrayList<>(catalogVersions.size());
            for(CatalogVersionModel catalogVersion : searchQuery.getCatalogVersions())
            {
                QueryField catalogField = new QueryField("catalogId", new String[] {"\"" + escape(catalogVersion.getCatalog().getId()) + "\""});
                QueryField catalogVersionField = new QueryField("catalogVersion", new String[] {escape(catalogVersion.getVersion())});
                CoupledQueryField catalogCouple = new CoupledQueryField("catalogVersionCouple", catalogField, catalogVersionField, SearchQuery.Operator.AND, SearchQuery.Operator.OR);
                catalogVersionCoupledFields.add(catalogCouple);
            }
            return catalogVersionCoupledFields;
        }
        return Collections.emptyList();
    }


    protected String buildQuery(String[] queries, SearchQuery searchQuery)
    {
        String query;
        if(queries.length == 0)
        {
            query = "*:*";
        }
        else
        {
            SearchQuery.Operator operator = resolveOperator(searchQuery);
            query = combine(queries, operator.getName());
        }
        if(LOG.isDebugEnabled())
        {
            LOG.debug("FIELDS : " + query);
        }
        return query;
    }


    protected void splitQueryFields(List<QueryField> source, List<QueryField> queries, List<QueryField> filterQueries, Map<String, IndexedFacetInfo> facetInfoMap)
    {
        for(QueryField queryField : source)
        {
            if(isFilterQueryField(queryField, facetInfoMap))
            {
                if(facetInfoMap.containsKey(queryField.getField()) && ((IndexedFacetInfo)facetInfoMap.get(queryField.getField())).isMultiSelectOr())
                {
                    queryField.setOperator(SearchQuery.Operator.OR);
                }
                filterQueries.add(queryField);
                continue;
            }
            queries.add(queryField);
        }
    }


    protected Map<String, IndexedFacetInfo> getFacetInfo(SearchQuery searchQuery)
    {
        Map<String, IndexedFacetInfo> results = new HashMap<>();
        int index = 0;
        IndexedType indexedType = searchQuery.getIndexedType();
        Set<String> facets = indexedType.getTypeFacets();
        for(String facetName : facets)
        {
            IndexedProperty indexedProperty = (IndexedProperty)indexedType.getIndexedProperties().get(facetName);
            if(indexedProperty != null)
            {
                IndexedFacetInfo facetInfo = new IndexedFacetInfo();
                FacetType facetType = indexedProperty.getFacetType();
                if(FacetType.MULTISELECTAND.equals(facetType))
                {
                    facetInfo.setMultiSelect(true);
                    facetInfo.setMultiSelectAnd(true);
                }
                else if(FacetType.MULTISELECTOR.equals(facetType))
                {
                    facetInfo.setMultiSelect(true);
                    facetInfo.setMultiSelectOr(true);
                }
                facetInfo.setTranslatedFieldName(getFieldNameTranslator().translate(searchQuery, facetName, FieldNameProvider.FieldType.INDEX));
                facetInfo.setKey("fk" + index);
                results.put(facetInfo.getTranslatedFieldName(), facetInfo);
            }
            index++;
        }
        return results;
    }


    protected String escape(String text)
    {
        return ClientUtils.escapeQueryChars(text);
    }


    protected boolean isFilterQueryField(QueryField queryField, Map<String, IndexedFacetInfo> facetInfoMap)
    {
        String field = queryField.getField();
        return ("catalogId".equals(field) || "catalogVersion".equals(field) || facetInfoMap.containsKey(field));
    }


    protected void checkQuery(SearchQuery solrSearchQuery) throws FacetSearchException
    {
        if(solrSearchQuery.getLanguage() == null)
        {
            throw new FacetSearchException("query language must not be null");
        }
        if(solrSearchQuery.getCurrency() == null)
        {
            throw new FacetSearchException("query currency must not be null");
        }
    }


    protected String[] convertQueryFields(List<QueryField> queryFields, Map<String, IndexedFacetInfo> facetInfoMap)
    {
        List<String> joinedQueries = new ArrayList<>();
        for(QueryField qf : queryFields)
        {
            IndexedFacetInfo indexedFacetInfo = (facetInfoMap == null) ? null : facetInfoMap.get(qf.getField());
            String fieldPrefix = (indexedFacetInfo == null || !indexedFacetInfo.isMultiSelect()) ? "" : ("{!tag=" + indexedFacetInfo.getKey() + "}");
            if(qf.getValues().size() == 1)
            {
                if("fulltext".equals(qf.getField()))
                {
                    joinedQueries.add(fieldPrefix + "(" + fieldPrefix + ")");
                    continue;
                }
                joinedQueries.add(fieldPrefix + "(" + fieldPrefix + ":" + escape(qf.getField()) + ")");
                continue;
            }
            if("fulltext".equals(qf.getField()))
            {
                joinedQueries.add(fieldPrefix + "(" + fieldPrefix + "))");
                continue;
            }
            joinedQueries.add(fieldPrefix + "(" + fieldPrefix + ":(" + escape(qf.getField()) + "))");
        }
        return joinedQueries.<String>toArray(new String[joinedQueries.size()]);
    }


    protected String[] convertCoupledQueryFields(SearchQuery searchQuery, List<CoupledQueryField> coupledQueryFields)
    {
        if(CollectionUtils.isEmpty(coupledQueryFields))
        {
            return new String[0];
        }
        List<String> joinedQueries = new ArrayList<>();
        Map<String, List<String>> couples = new HashMap<>();
        Map<String, SearchQuery.Operator> operatorMapping = new HashMap<>(coupledQueryFields.size());
        for(CoupledQueryField qf : coupledQueryFields)
        {
            StringBuilder couple = new StringBuilder();
            couple.append('(').append(prepareQueryField(qf.getField1())).append(qf.getInnerCouplingOperator().getName())
                            .append(prepareQueryField(qf.getField2())).append(')');
            List<String> joinedCouples = couples.get(qf.getCoupleId());
            if(joinedCouples == null)
            {
                joinedCouples = new ArrayList<>();
            }
            joinedCouples.add(couple.toString());
            couples.put(qf.getCoupleId(), joinedCouples);
            operatorMapping.put(qf.getCoupleId(), qf.getOuterCouplingOperator());
        }
        for(Map.Entry<String, List<String>> entry : couples.entrySet())
        {
            String coupleId = entry.getKey();
            List<String> list = entry.getValue();
            joinedQueries.add("(" + combine(list.<String>toArray(new String[list.size()]), ((SearchQuery.Operator)operatorMapping.get(coupleId)).getName()) + ")");
        }
        return joinedQueries.<String>toArray(new String[joinedQueries.size()]);
    }


    protected String[] convertRawQueries(SearchQuery searchQuery, List<RawQuery> rawQueries)
    {
        if(CollectionUtils.isEmpty(rawQueries))
        {
            return new String[0];
        }
        String[] queries = new String[rawQueries.size()];
        int index = 0;
        for(RawQuery rawQuery : rawQueries)
        {
            StringBuilder query = new StringBuilder();
            query.append('(');
            if(rawQuery.getField() != null)
            {
                String convertedField = this.fieldNameTranslator.translate(searchQuery, rawQuery.getField(), FieldNameProvider.FieldType.INDEX);
                query.append(convertedField);
                query.append(":(");
            }
            query.append(rawQuery.getQuery());
            if(rawQuery.getField() != null)
            {
                query.append(')');
            }
            query.append(')');
            queries[index] = query.toString();
            index++;
        }
        return queries;
    }


    protected String prepareQueryField(QueryField field)
    {
        StringBuilder result = new StringBuilder("(" + escape(field.getField()) + ":");
        if(field.getValues().size() == 1)
        {
            result.append(field.getValues().iterator().next()).append(")");
        }
        else
        {
            result.append("(")
                            .append(combine((String[])field.getValues().toArray((Object[])new String[field.getValues().size()]), field.getOperator().getName()))
                            .append("))");
        }
        return result.toString();
    }


    protected IndexedTypeFieldsValuesProvider getFieldsValuesProvider(IndexedType indexType)
    {
        String name = indexType.getFieldsValuesProvider();
        return (name == null) ? null : (IndexedTypeFieldsValuesProvider)this.beanFactory.getBean(name, IndexedTypeFieldsValuesProvider.class);
    }


    protected void addFacetFields(SolrQuery solrQuery, SearchQuery solrSearchQuery, FieldNameProvider solrFieldNameProvider)
    {
        Set<String> facets = solrSearchQuery.getIndexedType().getTypeFacets();
        for(String facetName : facets)
        {
            solrQuery.addFacetField(new String[] {getFieldNameTranslator().translate(solrSearchQuery, facetName, FieldNameProvider.FieldType.INDEX)});
        }
    }


    protected void addFacetFields(SolrQuery solrQuery, SearchQuery solrSearchQuery)
    {
        Set<String> facets = solrSearchQuery.getIndexedType().getTypeFacets();
        for(String facetName : facets)
        {
            solrQuery.addFacetField(new String[] {getFieldNameTranslator().translate(solrSearchQuery, facetName, FieldNameProvider.FieldType.INDEX)});
        }
    }


    protected void addFacetFields(SolrQuery solrQuery, Map<String, IndexedFacetInfo> facetInfoMap)
    {
        for(IndexedFacetInfo facetInfo : facetInfoMap.values())
        {
            if(facetInfo.isMultiSelect())
            {
                solrQuery.addFacetField(new String[] {"{!ex=" + facetInfo.getKey() + "}" + facetInfo.getTranslatedFieldName()});
                continue;
            }
            solrQuery.addFacetField(new String[] {facetInfo.getTranslatedFieldName()});
        }
    }


    protected void addSolrParams(SolrQuery solrQuery, SearchQuery solrSearchQuery)
    {
        Map<String, String[]> params = solrSearchQuery.getRawParams();
        Set<String> keys = params.keySet();
        for(String key : keys)
        {
            solrQuery.remove(key);
            solrQuery.add(key, params.get(key));
        }
    }


    protected SolrQuery applyPostProcessorsInOrder(SolrQuery solrQuery, SearchQuery solrSearchQuery)
    {
        if(solrQuery == null)
        {
            throw new IllegalArgumentException("SolrQuery may not be null!");
        }
        SolrQuery processedSolrQuery = solrQuery;
        if(this.queryPostProcessors != null)
        {
            for(SolrQueryPostProcessor processor : this.queryPostProcessors)
            {
                processedSolrQuery = processor.process(processedSolrQuery, solrSearchQuery);
            }
        }
        return processedSolrQuery;
    }


    protected String combine(String[] values, String separator)
    {
        return StringUtils.join((Object[])values, separator);
    }


    protected List<QueryField> prepareQueryFields(SearchQuery searchQuery)
    {
        Map<String, QueryField> queryFields = new HashMap<>();
        prepareQueryFieldsFromFacetValues(searchQuery, queryFields);
        prepareQueryFieldsFromQueries(searchQuery, queryFields);
        for(QueryField queryField : queryFields.values())
        {
            String field = queryField.getField();
            if("fulltext".equals(field))
            {
                field = field + field + this.forbiddenChar;
            }
            else
            {
                field = getFieldNameTranslator().translate(searchQuery, field, FieldNameProvider.FieldType.INDEX);
            }
            queryField.setField(field);
        }
        return new ArrayList<>(queryFields.values());
    }


    protected void prepareQueryFieldsFromFacetValues(SearchQuery searchQuery, Map<String, QueryField> queryFields)
    {
        for(FacetValueField facetValue : searchQuery.getFacetValues())
        {
            QueryField queryField = queryFields.get(facetValue.getField());
            if(queryField == null)
            {
                queryField = new QueryField(facetValue.getField(), SearchQuery.Operator.AND, new HashSet());
                queryFields.put(facetValue.getField(), queryField);
            }
            if(CollectionUtils.isNotEmpty(facetValue.getValues()))
            {
                for(String value : facetValue.getValues())
                {
                    queryField.getValues().add(ClientUtils.escapeQueryChars(value));
                }
            }
        }
    }


    protected void prepareQueryFieldsFromQueries(SearchQuery searchQuery, Map<String, QueryField> queryFields)
    {
        for(QueryField query : searchQuery.getQueries())
        {
            QueryField queryField = queryFields.get(query.getField());
            if(queryField == null)
            {
                queryField = new QueryField(query.getField(), query.getOperator(), new HashSet());
                queryFields.put(query.getField(), queryField);
            }
            if(CollectionUtils.isNotEmpty(query.getValues()))
            {
                for(String value : query.getValues())
                {
                    queryField.getValues().add(ClientUtils.escapeQueryChars(value));
                }
            }
        }
    }


    protected SearchQuery.Operator resolveOperator(SearchQuery searchQuery)
    {
        if(searchQuery.getDefaultOperator() != null)
        {
            return searchQuery.getDefaultOperator();
        }
        if(getFieldOperator() != null)
        {
            return getFieldOperator();
        }
        return DEFAULT_FIELD_OPERATOR;
    }
}
