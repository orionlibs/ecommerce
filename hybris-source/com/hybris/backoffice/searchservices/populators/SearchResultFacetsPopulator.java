package com.hybris.backoffice.searchservices.populators;

import com.hybris.backoffice.searchservices.providers.impl.CategoryFacetValueDisplayNameProvider;
import com.hybris.cockpitng.i18n.CockpitLocaleService;
import com.hybris.cockpitng.search.data.FullTextSearchData;
import com.hybris.cockpitng.search.data.facet.FacetData;
import com.hybris.cockpitng.search.data.facet.FacetType;
import com.hybris.cockpitng.search.data.facet.FacetValueData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.searchservices.search.data.AbstractSnBucketResponse;
import de.hybris.platform.searchservices.search.data.AbstractSnBucketsFacetResponse;
import de.hybris.platform.searchservices.search.data.AbstractSnFacetResponse;
import de.hybris.platform.searchservices.search.data.SnRangeBucketResponse;
import de.hybris.platform.searchservices.search.data.SnRangeBucketsFacetResponse;
import de.hybris.platform.searchservices.search.data.SnSearchResult;
import de.hybris.platform.searchservices.search.data.SnTermBucketResponse;
import de.hybris.platform.searchservices.search.data.SnTermBucketsFacetResponse;
import de.hybris.platform.searchservices.util.ConverterUtils;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.collections4.CollectionUtils;

public class SearchResultFacetsPopulator implements Populator<SnSearchResultSourceData, SnSearchResultConverterData>
{
    private CockpitLocaleService cockpitLocaleService;
    private CategoryFacetValueDisplayNameProvider categoryFacetValueDisplayNameProvider;


    public void populate(SnSearchResultSourceData snSearchResultSourceData, SnSearchResultConverterData snSearchResultConverterData)
    {
        SnSearchResult snSearchResult = snSearchResultSourceData.getSnSearchResult();
        List<AbstractSnFacetResponse> searchResultFacets = snSearchResult.getFacets();
        Objects.requireNonNull(AbstractSnBucketsFacetResponse.class);
        Objects.requireNonNull(AbstractSnBucketsFacetResponse.class);
        List<AbstractSnBucketsFacetResponse> bucketResponses = (List<AbstractSnBucketsFacetResponse>)searchResultFacets.stream().filter(AbstractSnBucketsFacetResponse.class::isInstance).map(AbstractSnBucketsFacetResponse.class::cast).collect(Collectors.toList());
        List<FacetData> convertedFacets = ConverterUtils.convertAll(bucketResponses, bResp -> convertBucketsFacetResponse(bResp));
        FullTextSearchData fullTextSearchData = new FullTextSearchData(convertedFacets, "");
        snSearchResultConverterData.setFullTextSearchData(fullTextSearchData);
    }


    private FacetData convertBucketsFacetResponse(AbstractSnBucketsFacetResponse source)
    {
        List<FacetValueData> values = null;
        if(source instanceof SnTermBucketsFacetResponse)
        {
            SnTermBucketsFacetResponse termBucketsFacetResponse = (SnTermBucketsFacetResponse)source;
            Set<String> selectedBucketIds = (Set<String>)CollectionUtils.emptyIfNull(termBucketsFacetResponse.getSelectedBuckets()).stream().map(AbstractSnBucketResponse::getId).collect(Collectors.toSet());
            List<SnTermBucketResponse> buckets = getSnTermBuckets(termBucketsFacetResponse);
            values = ConverterUtils.convertAll(buckets, bucket -> convertBucket((AbstractSnBucketResponse)bucket, selectedBucketIds, source.getId()));
        }
        else if(source instanceof SnRangeBucketsFacetResponse)
        {
            SnRangeBucketsFacetResponse rangeBucketsFacetResponse = (SnRangeBucketsFacetResponse)source;
            Set<String> selectedBucketIds = (Set<String>)CollectionUtils.emptyIfNull(rangeBucketsFacetResponse.getSelectedBuckets()).stream().map(AbstractSnBucketResponse::getId).collect(Collectors.toSet());
            List<SnRangeBucketResponse> buckets = getSnRangeBuckets(rangeBucketsFacetResponse);
            values = ConverterUtils.convertAll(buckets, bucket -> convertBucket((AbstractSnBucketResponse)bucket, selectedBucketIds, source.getId()));
        }
        return new FacetData(source.getId(), source.getName(), extractFacetType(source), values);
    }


    private List<SnTermBucketResponse> getSnTermBuckets(SnTermBucketsFacetResponse termBucketsFacetResponse)
    {
        List<SnTermBucketResponse> newTermBuckets;
        if(CollectionUtils.isNotEmpty(termBucketsFacetResponse.getSelectedBuckets()))
        {
            newTermBuckets = (List<SnTermBucketResponse>)Stream.<List>of(new List[] {termBucketsFacetResponse.getBuckets(), termBucketsFacetResponse.getSelectedBuckets()}).flatMap(Collection::stream).collect(Collectors.toList());
        }
        else
        {
            newTermBuckets = termBucketsFacetResponse.getBuckets();
        }
        return newTermBuckets;
    }


    private List<SnRangeBucketResponse> getSnRangeBuckets(SnRangeBucketsFacetResponse rangeBucketsFacetResponse)
    {
        List<SnRangeBucketResponse> newRangeBuckets;
        if(CollectionUtils.isNotEmpty(rangeBucketsFacetResponse.getSelectedBuckets()))
        {
            newRangeBuckets = (List<SnRangeBucketResponse>)Stream.<List>of(new List[] {rangeBucketsFacetResponse.getBuckets(), rangeBucketsFacetResponse.getSelectedBuckets()}).flatMap(Collection::stream).collect(Collectors.toList());
        }
        else
        {
            newRangeBuckets = rangeBucketsFacetResponse.getBuckets();
        }
        return newRangeBuckets;
    }


    private FacetValueData convertBucket(AbstractSnBucketResponse bucket, Set<String> selectedBucketIds, String facetId)
    {
        boolean isSelected = selectedBucketIds.contains(bucket.getId());
        String facetValueDisplayName = bucket.getName();
        if(facetId.equals("categoryLabel"))
        {
            facetValueDisplayName = this.categoryFacetValueDisplayNameProvider.getDisplayName(facetValueDisplayName, this.cockpitLocaleService.getCurrentLocale());
        }
        return new FacetValueData(bucket.getId(), facetValueDisplayName, bucket.getCount().intValue(), isSelected);
    }


    protected FacetType extractFacetType(AbstractSnBucketsFacetResponse source)
    {
        switch(null.$SwitchMap$de$hybris$platform$searchservices$search$data$SnFacetFilterMode[source.getFilterMode().ordinal()])
        {
            case 1:
                facetType = FacetType.REFINE;
                return facetType;
            case 2:
                facetType = FacetType.MULTISELECTOR;
                return facetType;
        }
        FacetType facetType = FacetType.REFINE;
        return facetType;
    }


    public void setCockpitLocaleService(CockpitLocaleService cockpitLocaleService)
    {
        this.cockpitLocaleService = cockpitLocaleService;
    }


    public void setCategoryFacetValueDisplayNameProvider(CategoryFacetValueDisplayNameProvider categoryFacetValueDisplayNameProvider)
    {
        this.categoryFacetValueDisplayNameProvider = categoryFacetValueDisplayNameProvider;
    }
}
