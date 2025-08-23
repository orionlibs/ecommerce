package de.hybris.platform.adaptivesearchbackoffice.editors.facets;

import de.hybris.platform.adaptivesearch.data.AbstractAsFacetConfiguration;
import de.hybris.platform.adaptivesearch.data.AsConfigurationHolder;
import de.hybris.platform.adaptivesearch.data.AsFacet;
import de.hybris.platform.adaptivesearch.data.AsFacetData;
import de.hybris.platform.adaptivesearch.data.AsSearchProfileResult;
import de.hybris.platform.adaptivesearch.data.AsSearchResultData;
import de.hybris.platform.adaptivesearch.enums.AsFacetType;
import de.hybris.platform.adaptivesearch.model.AbstractAsConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AbstractAsSearchProfileModel;
import de.hybris.platform.adaptivesearch.model.AsFacetModel;
import de.hybris.platform.adaptivesearch.util.MergeMap;
import de.hybris.platform.adaptivesearchbackoffice.common.AsFacetUtils;
import de.hybris.platform.adaptivesearchbackoffice.data.AbstractFacetConfigurationEditorData;
import de.hybris.platform.adaptivesearchbackoffice.data.FacetEditorData;
import de.hybris.platform.adaptivesearchbackoffice.data.SearchResultData;
import de.hybris.platform.adaptivesearchbackoffice.editors.configurablemultireference.AbstractDataHandler;
import de.hybris.platform.core.model.ItemModel;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zul.ListModelList;

public class AsFacetsDataHandler extends AbstractDataHandler<FacetEditorData, AsFacetModel>
{
    private AsFacetUtils asFacetUtils;


    public String getTypeCode()
    {
        return "AsFacet";
    }


    protected FacetEditorData createEditorData()
    {
        FacetEditorData editorData = new FacetEditorData();
        editorData.setValid(true);
        return editorData;
    }


    protected void loadDataFromSearchResult(Map<Object, FacetEditorData> mapping, SearchResultData searchResult, Map<String, Object> parameters)
    {
        if(searchResult == null || searchResult.getAsSearchResult() == null)
        {
            return;
        }
        AsSearchResultData asSearchResult = searchResult.getAsSearchResult();
        AsSearchProfileResult searchProfileResult = asSearchResult.getSearchProfileResult();
        if(searchProfileResult != null && MapUtils.isNotEmpty(searchProfileResult.getFacets()))
        {
            AbstractAsSearchProfileModel searchProfile = (AbstractAsSearchProfileModel)parameters.get("searchProfile");
            for(AsConfigurationHolder<AsFacet, AbstractAsFacetConfiguration> facetHolder : (Iterable<AsConfigurationHolder<AsFacet, AbstractAsFacetConfiguration>>)((MergeMap)searchProfileResult
                            .getFacets()).orderedValues())
            {
                AsFacet facet = (AsFacet)facetHolder.getConfiguration();
                FacetEditorData editorData = (FacetEditorData)getOrCreateEditorData(mapping, facet.getIndexProperty());
                convertFromSearchProfileResult(facetHolder, editorData, searchProfile);
            }
        }
        if(CollectionUtils.isNotEmpty(asSearchResult.getFacets()))
        {
            for(AsFacetData facet : asSearchResult.getFacets())
            {
                FacetEditorData editorData = mapping.get(facet.getIndexProperty());
                if(editorData != null)
                {
                    convertFromSearchResult(facet, editorData);
                }
            }
        }
    }


    protected void loadDataFromInitialValue(Map<Object, FacetEditorData> mapping, Collection<AsFacetModel> initialValue, Map<String, Object> parameters)
    {
        if(CollectionUtils.isNotEmpty(initialValue))
        {
            for(AsFacetModel facet : initialValue)
            {
                FacetEditorData editorData = (FacetEditorData)getOrCreateEditorData(mapping, facet.getIndexProperty());
                convertFromModel(facet, editorData);
            }
        }
    }


    protected void convertFromSearchProfileResult(AsConfigurationHolder<AsFacet, AbstractAsFacetConfiguration> source, FacetEditorData target, AbstractAsSearchProfileModel searchProfile)
    {
        AsFacet facet = (AsFacet)source.getConfiguration();
        String indexProperty = facet.getIndexProperty();
        target.setUid(facet.getUid());
        target.setLabel(indexProperty);
        target.setIndexProperty(indexProperty);
        target.setPriority(facet.getPriority());
        target.setMultiselect((facet
                        .getFacetType() == AsFacetType.MULTISELECT_AND || facet.getFacetType() == AsFacetType.MULTISELECT_OR));
        target.setFacetConfiguration((AbstractAsFacetConfiguration)facet);
        target.setFromSearchProfile(isConfigurationFromSearchProfile((AbstractAsFacetConfiguration)source.getConfiguration(), searchProfile));
        AbstractAsFacetConfiguration replacedConfiguration = CollectionUtils.isNotEmpty(source.getReplacedConfigurations()) ? source.getReplacedConfigurations().get(0) : null;
        target.setOverride((replacedConfiguration != null));
        target.setOverrideFromSearchProfile(isConfigurationFromSearchProfile(replacedConfiguration, searchProfile));
    }


    protected boolean isConfigurationFromSearchProfile(AbstractAsFacetConfiguration configuration, AbstractAsSearchProfileModel searchProfile)
    {
        if(configuration == null || searchProfile == null)
        {
            return false;
        }
        return StringUtils.equals(searchProfile.getCode(), configuration.getSearchProfileCode());
    }


    protected void convertFromSearchResult(AsFacetData source, FacetEditorData target)
    {
        boolean inSearchResult = (CollectionUtils.isNotEmpty(source.getValues()) || CollectionUtils.isNotEmpty(source.getSelectedValues()));
        target.setInSearchResult(inSearchResult);
        target.setOpen(this.asFacetUtils.isOpen(source));
        target.setFacet(source);
        if(CollectionUtils.isNotEmpty(source.getSelectedValues()))
        {
            target.setFacetFiltersCount(Integer.valueOf(source.getSelectedValues().size()));
        }
    }


    protected void convertFromModel(AsFacetModel source, FacetEditorData target)
    {
        if(StringUtils.isBlank(source.getUid()))
        {
            source.setUid(getAsUidGenerator().generateUid());
        }
        String indexProperty = source.getIndexProperty();
        target.setUid(source.getUid());
        target.setLabel(indexProperty);
        target.setValid(getAsConfigurationService().isValid((AbstractAsConfigurationModel)source));
        target.setIndexProperty(indexProperty);
        target.setPriority(source.getPriority());
        target.setModel((ItemModel)source);
        target.setFromSearchProfile(true);
        target.setFromSearchConfiguration(true);
    }


    protected void postLoadData(Collection<AsFacetModel> initialValue, SearchResultData searchResult, Map<String, Object> parameters, ListModelList<FacetEditorData> data)
    {
        if(data != null)
        {
            if(searchResult != null)
            {
                this.asFacetUtils.localizeFacets(searchResult.getNavigationContext(), searchResult.getSearchContext(), data
                                .getInnerList());
            }
            updateRankAttributes(data.getInnerList());
        }
    }


    protected void updateRankAttributes(List<? extends AbstractFacetConfigurationEditorData> facets)
    {
        AbstractFacetConfigurationEditorData previousFacet = null;
        for(AbstractFacetConfigurationEditorData facet : facets)
        {
            if(previousFacet != null && previousFacet.getModel() != null && facet.getModel() != null &&
                            ObjectUtils.equals(previousFacet.getPriority(), facet.getPriority()))
            {
                previousFacet.setRankDownAllowed(true);
                facet.setRankUpAllowed(true);
            }
            previousFacet = facet;
        }
    }


    public AsFacetUtils getAsFacetUtils()
    {
        return this.asFacetUtils;
    }


    @Required
    public void setAsFacetUtils(AsFacetUtils asFacetUtils)
    {
        this.asFacetUtils = asFacetUtils;
    }
}
