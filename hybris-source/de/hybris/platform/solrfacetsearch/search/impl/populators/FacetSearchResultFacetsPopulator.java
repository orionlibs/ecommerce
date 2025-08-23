package de.hybris.platform.solrfacetsearch.search.impl.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.FacetSortProvider;
import de.hybris.platform.solrfacetsearch.config.FacetType;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.SearchConfig;
import de.hybris.platform.solrfacetsearch.config.ValueRange;
import de.hybris.platform.solrfacetsearch.config.ValueRangeSet;
import de.hybris.platform.solrfacetsearch.provider.FacetDisplayNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FacetTopValuesProvider;
import de.hybris.platform.solrfacetsearch.provider.FacetValueDisplayNameProvider;
import de.hybris.platform.solrfacetsearch.search.Facet;
import de.hybris.platform.solrfacetsearch.search.FacetField;
import de.hybris.platform.solrfacetsearch.search.FacetValue;
import de.hybris.platform.solrfacetsearch.search.FacetValueField;
import de.hybris.platform.solrfacetsearch.search.FieldNameTranslator;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.context.FacetSearchContext;
import de.hybris.platform.solrfacetsearch.search.impl.SearchResultConverterData;
import de.hybris.platform.solrfacetsearch.search.impl.SolrSearchResult;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.solr.client.solrj.response.GroupCommand;
import org.apache.solr.client.solrj.response.GroupResponse;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.json.BucketBasedJsonFacet;
import org.apache.solr.client.solrj.response.json.BucketJsonFacet;
import org.apache.solr.client.solrj.response.json.NestableJsonFacet;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Required;

public class FacetSearchResultFacetsPopulator implements Populator<SearchResultConverterData, SolrSearchResult>, BeanFactoryAware
{
    public static final String RESPONSE_HEADERS_PARAMS = "params";
    protected static final int DEFAULT_PRIORITY = 0;
    private FieldNameTranslator fieldNameTranslator;
    private BeanFactory beanFactory;


    public void populate(SearchResultConverterData source, SolrSearchResult target)
    {
        QueryResponse queryResponse = source.getQueryResponse();
        if(queryResponse == null)
        {
            return;
        }
        List<Facet> facets = new ArrayList<>();
        FacetSearchContext facetSearchContext = source.getFacetSearchContext();
        SearchQuery searchQuery = facetSearchContext.getSearchQuery();
        Map<String, FacetInfo> facetInfos = buildFacetInfos(searchQuery);
        FieldNameTranslator.FieldInfosMapping fieldInfosMapping = this.fieldNameTranslator.getFieldInfos(source.getFacetSearchContext());
        Map<String, FieldNameTranslator.FieldInfo> fieldInfos = fieldInfosMapping.getInvertedFieldInfos();
        NestableJsonFacet facetingResponse = queryResponse.getJsonFacetingResponse();
        if(facetingResponse == null)
        {
            return;
        }
        long numberOfDocuments = extractNumberOfDocuments(queryResponse);
        long numberOfGroups = extractNumberOfGroups(queryResponse);
        Collection<String> facetNames = facetingResponse.getBucketBasedFacetNames();
        if(CollectionUtils.isNotEmpty(facetNames))
        {
            for(String facetName : facetNames)
            {
                FieldNameTranslator.FieldInfo fieldInfo = fieldInfos.get(facetName);
                FacetInfo facetInfo = facetInfos.get((fieldInfo != null) ? fieldInfo.getFieldName() : facetName);
                if(facetInfo != null)
                {
                    BucketBasedJsonFacet sourceFacet = facetingResponse.getBucketBasedFacets(facetName);
                    facets.add(buildFacet(searchQuery, facetInfo, queryResponse, numberOfDocuments, numberOfGroups, sourceFacet));
                }
            }
            Collections.sort(facets, this::compareFacets);
            Objects.requireNonNull(target);
            facets.forEach(target::addFacet);
        }
    }


    protected Map<String, FacetInfo> buildFacetInfos(SearchQuery searchQuery)
    {
        Map<String, FacetInfo> facetInfos = new HashMap<>();
        IndexedType indexedType = searchQuery.getIndexedType();
        for(FacetField facet : searchQuery.getFacets())
        {
            IndexedProperty indexedProperty = (IndexedProperty)indexedType.getIndexedProperties().get(facet.getField());
            FacetInfo facetInfo = new FacetInfo(facet, indexedProperty);
            facetInfos.put(facet.getField(), facetInfo);
        }
        for(FacetValueField facetValue : searchQuery.getFacetValues())
        {
            FacetInfo facetInfo = facetInfos.get(facetValue.getField());
            if(facetInfo != null && CollectionUtils.isNotEmpty(facetValue.getValues()))
            {
                facetInfo.getSelectedValues().addAll(facetValue.getValues());
            }
        }
        return facetInfos;
    }


    protected long extractNumberOfDocuments(QueryResponse queryResponse)
    {
        GroupResponse groupResponse = queryResponse.getGroupResponse();
        if(groupResponse != null)
        {
            return ((GroupCommand)groupResponse.getValues().get(0)).getMatches();
        }
        NestableJsonFacet facetingResponse = queryResponse.getJsonFacetingResponse();
        NestableJsonFacet documentCountFacet = facetingResponse.getQueryFacet("documentCount");
        if(documentCountFacet != null)
        {
            return documentCountFacet.getCount();
        }
        return queryResponse.getResults().getNumFound();
    }


    protected long extractNumberOfGroups(QueryResponse queryResponse)
    {
        GroupResponse groupResponse = queryResponse.getGroupResponse();
        if(groupResponse != null)
        {
            return ((GroupCommand)groupResponse.getValues().get(0)).getNGroups().intValue();
        }
        return queryResponse.getResults().getNumFound();
    }


    protected Facet buildFacet(SearchQuery searchQuery, FacetInfo facetInfo, QueryResponse queryResponse, long numberOfDocuments, long numberOfGroups, BucketBasedJsonFacet sourceFacet)
    {
        FacetField facetField = facetInfo.getFacetField();
        IndexedProperty indexedProperty = facetInfo.getIndexedProperty();
        String facetDisplayName = (indexedProperty != null) ? indexedProperty.getDisplayName() : null;
        int facetPriority = (facetField.getPriority() != null) ? facetField.getPriority().intValue() : 0;
        FacetType facetType = facetField.getFacetType();
        List<FacetValue> topFacetValues = new ArrayList<>();
        List<FacetValue> facetValues = new ArrayList<>();
        List<FacetValue> selectedFacetValues = new ArrayList<>();
        List<FacetValue> allFacetValues = new ArrayList<>();
        List<BucketJsonFacet> sourceFacetBuckets = sourceFacet.getBuckets();
        if(CollectionUtils.isNotEmpty(sourceFacetBuckets))
        {
            boolean showAllFacetValues = isShowAllFacetValues(searchQuery, facetInfo);
            Object facetValueDisplayNameProvider = resolveFacetValuesDisplayNameProvider(facetInfo);
            Map<String, FacetValue> valuesMapping = new LinkedHashMap<>();
            for(BucketJsonFacet sourceFacetBucket : sourceFacetBuckets)
            {
                String bucketName = String.valueOf(sourceFacetBucket.getVal());
                Number groupCount = (Number)sourceFacetBucket.getStatValue("groupCount");
                long bucketCount = (groupCount != null) ? groupCount.longValue() : (int)sourceFacetBucket.getCount();
                boolean showFacetValue = isShowFacetValue(searchQuery, facetInfo, queryResponse, numberOfDocuments, numberOfGroups, sourceFacet, sourceFacetBucket, showAllFacetValues);
                boolean selected = isFacetValueSelected(facetInfo, bucketName);
                String displayName = resolveFacetValueDisplayName(searchQuery, facetInfo, facetValueDisplayNameProvider, bucketName);
                FacetValue facetValue = new FacetValue(bucketName, displayName, bucketCount, selected);
                if(showFacetValue)
                {
                    valuesMapping.put(facetValue.getName(), facetValue);
                }
                if(selected)
                {
                    selectedFacetValues.add(facetValue);
                }
                allFacetValues.add(facetValue);
            }
            List<FacetValue> promotedValues = new ArrayList<>();
            removeFacetValues(facetInfo.getFacetField().getPromotedValues(), valuesMapping, value -> {
                value.addTag("promoted");
                promotedValues.add(value);
            });
            removeFacetValues(facetInfo.getFacetField().getExcludedValues(), valuesMapping, value -> value.addTag("excluded"));
            facetValues.addAll(valuesMapping.values());
            sortFacetValues(facetInfo, searchQuery, facetValues);
            facetValues.addAll(0, promotedValues);
            buildTopFacetValues(facetInfo, facetValues, topFacetValues);
        }
        Facet facet = new Facet(facetField.getField(), facetDisplayName, facetValues, topFacetValues, selectedFacetValues, facetType, facetPriority);
        facet.setAllFacetValues(allFacetValues);
        facet.setMultiselect(facetInfo.isMultiselect());
        return facet;
    }


    protected boolean isShowAllFacetValues(SearchQuery searchQuery, FacetInfo facetInfo)
    {
        if(facetInfo.isMultiselect())
        {
            return true;
        }
        FacetSearchConfig facetSearchConfig = searchQuery.getFacetSearchConfig();
        SearchConfig searchConfig = facetSearchConfig.getSearchConfig();
        return searchConfig.isAllFacetValuesInResponse();
    }


    protected boolean isShowFacetValue(SearchQuery searchQuery, FacetInfo facetInfo, QueryResponse queryResponse, long numberOfDocuments, long numberOfGroups, BucketBasedJsonFacet sourceFacet, BucketJsonFacet sourceFacetBucket, boolean showAllFacetValues)
    {
        if(showAllFacetValues)
        {
            return true;
        }
        Number groupCount = (Number)sourceFacetBucket.getStatValue("groupCount");
        if(groupCount != null)
        {
            return (groupCount.longValue() < numberOfGroups);
        }
        return (sourceFacetBucket.getCount() < numberOfDocuments);
    }


    protected boolean isFacetValueSelected(FacetInfo facetInfo, String facetBucketName)
    {
        return facetInfo.getSelectedValues().contains(facetBucketName);
    }


    protected String resolveFacetValueDisplayName(SearchQuery searchQuery, FacetInfo facetInfo, Object facetDisplayNameProvider, String facetValue)
    {
        if(facetDisplayNameProvider != null)
        {
            if(facetDisplayNameProvider instanceof FacetValueDisplayNameProvider)
            {
                return ((FacetValueDisplayNameProvider)facetDisplayNameProvider).getDisplayName(searchQuery, facetInfo
                                .getIndexedProperty(), facetValue);
            }
            if(facetDisplayNameProvider instanceof FacetDisplayNameProvider)
            {
                return ((FacetDisplayNameProvider)facetDisplayNameProvider).getDisplayName(searchQuery, facetValue);
            }
        }
        return facetValue;
    }


    protected void removeFacetValues(List<String> keys, Map<String, FacetValue> mapping, Consumer<FacetValue> consumer)
    {
        if(CollectionUtils.isNotEmpty(keys))
        {
            for(String key : keys)
            {
                FacetValue value = mapping.remove(key);
                if(value != null)
                {
                    consumer.accept(value);
                }
            }
        }
    }


    protected void sortFacetValues(FacetInfo facetInfo, SearchQuery searchQuery, List<FacetValue> facetValues)
    {
        FacetSortProvider sortProvider = resolveFacetValuesSortProvider(facetInfo);
        if(sortProvider != null)
        {
            Comparator<FacetValue> comparator = sortProvider.getComparatorForTypeAndProperty(searchQuery.getIndexedType(), facetInfo
                            .getIndexedProperty());
            Collections.sort(facetValues, comparator);
        }
        else if(facetInfo.isRanged())
        {
            IndexedProperty indexedProperty = facetInfo.getIndexedProperty();
            List<ValueRange> valueRanges = resolveFacetValueRanges(indexedProperty,
                            indexedProperty.isCurrency() ? searchQuery.getCurrency() : null);
            Map<String, FacetValue> facetValuesMap = (Map<String, FacetValue>)facetValues.stream().collect(Collectors.toMap(FacetValue::getName, Function.identity()));
            facetValues.clear();
            for(ValueRange valueRange : valueRanges)
            {
                FacetValue facetValue = facetValuesMap.get(valueRange.getName());
                if(facetValue != null)
                {
                    facetValues.add(facetValue);
                }
            }
        }
    }


    protected List<ValueRange> resolveFacetValueRanges(IndexedProperty property, String qualifier)
    {
        ValueRangeSet valueRangeSet;
        if(qualifier == null)
        {
            valueRangeSet = (ValueRangeSet)property.getValueRangeSets().get("default");
        }
        else
        {
            valueRangeSet = (ValueRangeSet)property.getValueRangeSets().get(qualifier);
            if(valueRangeSet == null)
            {
                valueRangeSet = (ValueRangeSet)property.getValueRangeSets().get("default");
            }
        }
        if(valueRangeSet != null)
        {
            return valueRangeSet.getValueRanges();
        }
        return Collections.emptyList();
    }


    protected void buildTopFacetValues(FacetInfo facetInfo, List<FacetValue> facetValues, List<FacetValue> topFacetValues)
    {
        FacetTopValuesProvider topValuesProvider = resolveFacetTopValuesProvider(facetInfo);
        if(topValuesProvider != null && !facetInfo.isRanged())
        {
            topFacetValues.addAll(topValuesProvider.getTopValues(facetInfo.getIndexedProperty(), facetValues));
        }
    }


    protected int compareFacets(Facet facet1, Facet facet2)
    {
        int result = Integer.compare(facet2.getPriority(), facet1.getPriority());
        if(result == 0)
        {
            result = facet2.getDisplayName().compareToIgnoreCase(facet1.getDisplayName());
            if(result == 0)
            {
                result = facet2.getName().compareToIgnoreCase(facet1.getName());
            }
        }
        return result;
    }


    protected Object resolveFacetValuesDisplayNameProvider(FacetInfo facetInfo)
    {
        String beanName = facetInfo.getFacetField().getDisplayNameProvider();
        return (beanName != null) ? this.beanFactory.getBean(beanName) : null;
    }


    protected FacetSortProvider resolveFacetValuesSortProvider(FacetInfo facetInfo)
    {
        String beanName = facetInfo.getFacetField().getSortProvider();
        Object bean = (beanName != null) ? this.beanFactory.getBean(beanName) : null;
        if(bean instanceof FacetSortProvider)
        {
            return (FacetSortProvider)bean;
        }
        return null;
    }


    protected FacetTopValuesProvider resolveFacetTopValuesProvider(FacetInfo facetInfo)
    {
        String beanName = facetInfo.getFacetField().getTopValuesProvider();
        return (beanName != null) ? (FacetTopValuesProvider)this.beanFactory.getBean(beanName, FacetTopValuesProvider.class) : null;
    }


    public FieldNameTranslator getFieldNameTranslator()
    {
        return this.fieldNameTranslator;
    }


    @Required
    public void setFieldNameTranslator(FieldNameTranslator fieldNameTranslator)
    {
        this.fieldNameTranslator = fieldNameTranslator;
    }


    public BeanFactory getBeanFactory()
    {
        return this.beanFactory;
    }


    public void setBeanFactory(BeanFactory beanFactory)
    {
        this.beanFactory = beanFactory;
    }
}
