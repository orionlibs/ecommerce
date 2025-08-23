package com.hybris.backoffice.solrsearch.converters.impl;

import com.google.common.collect.Lists;
import com.hybris.backoffice.solrsearch.converters.FullTextSearchDataConverter;
import com.hybris.cockpitng.search.data.facet.FacetData;
import com.hybris.cockpitng.search.data.facet.FacetType;
import com.hybris.cockpitng.search.data.facet.FacetValueData;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.search.Breadcrumb;
import de.hybris.platform.solrfacetsearch.search.Facet;
import de.hybris.platform.solrfacetsearch.search.FacetValue;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.collections4.CollectionUtils;

public class DefaultFullTextSearchDataConverter implements FullTextSearchDataConverter
{
    public Collection<FacetData> convertFacets(Collection<Facet> facets, List<Breadcrumb> breadcrumbs, IndexedType indexedType)
    {
        List<FacetData> convertedFacets = (List<FacetData>)facets.stream().map(f -> convertFacet(f, (IndexedProperty)indexedType.getIndexedProperties().get(f.getName()))).collect(Collectors.toList());
        return mergeFacetsWithBreadcrumbs(convertedFacets, breadcrumbs, indexedType);
    }


    protected Collection<FacetData> mergeFacetsWithBreadcrumbs(List<FacetData> facets, Collection<Breadcrumb> breadcrumbs, IndexedType indexedType)
    {
        Map<String, FacetData> facetsMap = new LinkedHashMap<>();
        facets.forEach(facet -> facetsMap.put(facet.getName(), facet));
        for(Breadcrumb breadcrumb : breadcrumbs)
        {
            FacetData facetData = facetsMap.get(breadcrumb.getFieldName());
            if(facetData == null)
            {
                IndexedProperty indexedProperty = (IndexedProperty)indexedType.getIndexedProperties().get(breadcrumb.getFieldName());
                if(indexedProperty != null)
                {
                    FacetValueData facetValueData = new FacetValueData(breadcrumb.getValue(), breadcrumb.getDisplayValue(), 0L, true);
                    facetData = new FacetData(breadcrumb.getFieldName(), indexedProperty.getBackofficeDisplayName(), extractFacetType(indexedProperty), Lists.newArrayList((Object[])new FacetValueData[] {facetValueData}));
                    facetsMap.put(facetData.getName(), facetData);
                }
                continue;
            }
            FacetValueData facetValue = facetData.getFacetValue(breadcrumb.getValue());
            if(facetValue == null)
            {
                facetData.addFacetValue(new FacetValueData(breadcrumb.getValue(), breadcrumb.getDisplayValue(), -1L, true));
                continue;
            }
            if(!facetValue.isSelected())
            {
                facetData.addFacetValue(new FacetValueData(breadcrumb
                                .getValue(), breadcrumb.getDisplayValue(), facetValue.getCount(), true));
            }
        }
        return facetsMap.values();
    }


    protected FacetData convertFacet(Facet facet, IndexedProperty indexedProperty)
    {
        List<FacetValue> facetValues = getAllFacetValues(facet);
        List<FacetValueData> values = (List<FacetValueData>)facetValues.stream().map(this::convertFacetValue).collect(Collectors.toList());
        FacetType facetType = extractFacetType(indexedProperty);
        return new FacetData(facet.getName(), indexedProperty.getBackofficeDisplayName(), facetType, values);
    }


    private List<FacetValue> getAllFacetValues(Facet facet)
    {
        List<FacetValue> facetValues;
        ToIntFunction<List<FacetValue>> getSize = col -> CollectionUtils.emptyIfNull(col).size();
        if(getSize.applyAsInt(facet.getFacetValues()) == getSize.applyAsInt(facet.getAllFacetValues()))
        {
            return facet.getFacetValues();
        }
        if(CollectionUtils.isNotEmpty(facet.getSelectedFacetValues()))
        {
            facetValues = (List<FacetValue>)Stream.<List>of(new List[] {facet.getFacetValues(), facet.getSelectedFacetValues()}).flatMap(Collection::stream).collect(Collectors.toList());
        }
        else if(facet.getAllFacetValues() != null)
        {
            facetValues = facet.getAllFacetValues();
        }
        else
        {
            facetValues = facet.getFacetValues();
        }
        return facetValues;
    }


    protected FacetValueData convertFacetValue(FacetValue facetValue)
    {
        return new FacetValueData(facetValue.getName(), facetValue.getDisplayName(), facetValue.getCount(), facetValue
                        .isSelected());
    }


    protected FacetType extractFacetType(IndexedProperty indexedProperty)
    {
        if(indexedProperty != null)
        {
            FacetType facetType = indexedProperty.getFacetType();
            if(facetType != null)
            {
                switch(null.$SwitchMap$de$hybris$platform$solrfacetsearch$config$FacetType[facetType.ordinal()])
                {
                    case 1:
                        return FacetType.MULTISELECTAND;
                    case 2:
                        return FacetType.MULTISELECTOR;
                }
                return FacetType.REFINE;
            }
        }
        return FacetType.REFINE;
    }
}
