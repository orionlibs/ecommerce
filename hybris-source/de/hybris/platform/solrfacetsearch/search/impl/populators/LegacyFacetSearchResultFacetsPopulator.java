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
import org.apache.solr.common.util.SimpleOrderedMap;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Required;

@Deprecated(since = "2105")
public class LegacyFacetSearchResultFacetsPopulator implements Populator<SearchResultConverterData, SolrSearchResult>, BeanFactoryAware
{
    public static final String RESPONSE_HEADERS_PARAMS = "params";
    public static final String GROUP_PARAM = "group";
    public static final String GROUP_FACET_PARAM = "group.facet";
    protected static final int DEFAULT_PRIORITY = 0;
    private FieldNameTranslator fieldNameTranslator;
    private BeanFactory beanFactory;


    public void populate(SearchResultConverterData source, SolrSearchResult target)
    {
        QueryResponse queryResponse = source.getQueryResponse();
        if(queryResponse == null || CollectionUtils.isEmpty(queryResponse.getFacetFields()))
        {
            return;
        }
        List<Facet> facets = new ArrayList<>();
        FacetSearchContext facetSearchContext = source.getFacetSearchContext();
        SearchQuery searchQuery = facetSearchContext.getSearchQuery();
        Map<String, FacetInfo> facetInfos = buildFacetInfos(searchQuery);
        FieldNameTranslator.FieldInfosMapping fieldInfosMapping = this.fieldNameTranslator.getFieldInfos(source.getFacetSearchContext());
        Map<String, FieldNameTranslator.FieldInfo> fieldInfos = fieldInfosMapping.getInvertedFieldInfos();
        long maxFacetValueCount = getMaxFacetValueCount(queryResponse);
        for(FacetField sourceFacet : queryResponse.getFacetFields())
        {
            FieldNameTranslator.FieldInfo fieldInfo = fieldInfos.get(sourceFacet.getName());
            String field = (fieldInfo != null) ? fieldInfo.getFieldName() : sourceFacet.getName();
            FacetInfo facetInfo = facetInfos.get(field);
            if(facetInfo != null)
            {
                facets.add(buildFacet(searchQuery, facetInfo, maxFacetValueCount, sourceFacet));
            }
        }
        Collections.sort(facets, this::compareFacets);
        Objects.requireNonNull(target);
        facets.forEach(target::addFacet);
    }


    protected Map<String, FacetInfo> buildFacetInfos(SearchQuery searchQuery)
    {
        Map<String, FacetInfo> facetInfos = new HashMap<>();
        IndexedType indexedType = searchQuery.getIndexedType();
        for(String facet : indexedType.getTypeFacets())
        {
            IndexedProperty indexedProperty = (IndexedProperty)indexedType.getIndexedProperties().get(facet);
            FacetField facetField = new FacetField(facet);
            if(indexedProperty != null)
            {
                facetField.setPriority(Integer.valueOf(indexedProperty.getPriority()));
                facetField.setFacetType(indexedProperty.getFacetType());
                facetField.setDisplayNameProvider(indexedProperty.getFacetDisplayNameProvider());
                facetField.setSortProvider(indexedProperty.getFacetSortProvider());
                facetField.setTopValuesProvider(indexedProperty.getTopValuesProvider());
            }
            FacetInfo facetInfo = new FacetInfo(facetField, indexedProperty);
            facetInfos.put(facet, facetInfo);
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


    protected Facet buildFacet(SearchQuery searchQuery, FacetInfo facetInfo, long maxFacetValueCount, FacetField sourceFacet)
    {
        FacetField facetField = facetInfo.getFacetField();
        IndexedProperty indexedProperty = facetInfo.getIndexedProperty();
        String facetDisplayName = (indexedProperty != null) ? indexedProperty.getDisplayName() : null;
        int facetPriority = (facetField.getPriority() != null) ? facetField.getPriority().intValue() : 0;
        FacetType facetType = facetField.getFacetType();
        int size = sourceFacet.getValueCount();
        List<FacetValue> topFacetValues = new ArrayList<>();
        List<FacetValue> facetValues = new ArrayList<>(size);
        List<FacetValue> selectedFacetValues = new ArrayList<>();
        List<FacetValue> allFacetValues = new ArrayList<>(size);
        if(CollectionUtils.isNotEmpty(sourceFacet.getValues()))
        {
            boolean showFacet = (facetInfo.isMultiselect() || isAllFacetValuesInResponse(searchQuery));
            Object facetValueDisplayNameProvider = resolveFacetValuesDisplayNameProvider(facetInfo);
            Map<String, FacetValue> valuesMapping = new LinkedHashMap<>();
            for(FacetField.Count sourceFacetValue : sourceFacet.getValues())
            {
                boolean showFacetValue = (showFacet || sourceFacetValue.getCount() < maxFacetValueCount);
                boolean selected = isFacetValueSelected(facetInfo, sourceFacetValue);
                String displayName = resolveFacetValueDisplayName(searchQuery, facetInfo, facetValueDisplayNameProvider, sourceFacetValue
                                .getName());
                FacetValue facetValue = new FacetValue(sourceFacetValue.getName(), displayName, sourceFacetValue.getCount(), selected);
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


    protected long getMaxFacetValueCount(QueryResponse queryResponse)
    {
        GroupResponse groupResponse = queryResponse.getGroupResponse();
        if(groupResponse != null)
        {
            GroupCommand groupCommand = groupResponse.getValues().get(0);
            SimpleOrderedMap<String> params = (SimpleOrderedMap<String>)queryResponse.getResponseHeader().get("params");
            boolean groupFacets = Boolean.parseBoolean((String)params.get("group.facet"));
            return groupFacets ? groupCommand.getNGroups().intValue() : groupCommand.getMatches();
        }
        return queryResponse.getResults().getNumFound();
    }


    protected boolean isAllFacetValuesInResponse(SearchQuery searchQuery)
    {
        FacetSearchConfig facetSearchConfig = searchQuery.getFacetSearchConfig();
        SearchConfig searchConfig = facetSearchConfig.getSearchConfig();
        return searchConfig.isAllFacetValuesInResponse();
    }


    protected boolean isFacetValueSelected(FacetInfo facetInfo, FacetField.Count sourceFacetValue)
    {
        return facetInfo.getSelectedValues().contains(sourceFacetValue.getName());
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
