package de.hybris.platform.adaptivesearchbackoffice.editors.promotedfacets;

import de.hybris.platform.adaptivesearch.data.AbstractAsFacetConfiguration;
import de.hybris.platform.adaptivesearch.data.AsConfigurationHolder;
import de.hybris.platform.adaptivesearch.data.AsFacetData;
import de.hybris.platform.adaptivesearch.data.AsPromotedFacet;
import de.hybris.platform.adaptivesearch.data.AsSearchProfileResult;
import de.hybris.platform.adaptivesearch.data.AsSearchResultData;
import de.hybris.platform.adaptivesearch.enums.AsFacetType;
import de.hybris.platform.adaptivesearch.model.AbstractAsConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AbstractAsSearchProfileModel;
import de.hybris.platform.adaptivesearch.model.AsPromotedFacetModel;
import de.hybris.platform.adaptivesearch.util.MergeMap;
import de.hybris.platform.adaptivesearchbackoffice.common.AsFacetUtils;
import de.hybris.platform.adaptivesearchbackoffice.data.PromotedFacetEditorData;
import de.hybris.platform.adaptivesearchbackoffice.data.SearchResultData;
import de.hybris.platform.adaptivesearchbackoffice.editors.configurablemultireference.AbstractDataHandler;
import de.hybris.platform.core.model.ItemModel;
import java.util.Collection;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zul.ListModelList;

public class AsPromotedFacetsDataHandler extends AbstractDataHandler<PromotedFacetEditorData, AsPromotedFacetModel>
{
    private AsFacetUtils asFacetUtils;


    public String getTypeCode()
    {
        return "AsPromotedFacet";
    }


    protected PromotedFacetEditorData createEditorData()
    {
        PromotedFacetEditorData editorData = new PromotedFacetEditorData();
        editorData.setValid(true);
        return editorData;
    }


    protected void loadDataFromSearchResult(Map<Object, PromotedFacetEditorData> mapping, SearchResultData searchResult, Map<String, Object> parameters)
    {
        if(searchResult == null || searchResult.getAsSearchResult() == null)
        {
            return;
        }
        AsSearchResultData asSearchResult = searchResult.getAsSearchResult();
        AsSearchProfileResult searchProfileResult = asSearchResult.getSearchProfileResult();
        if(searchProfileResult != null && MapUtils.isNotEmpty(searchProfileResult.getPromotedFacets()))
        {
            AbstractAsSearchProfileModel searchProfile = (AbstractAsSearchProfileModel)parameters.get("searchProfile");
            for(AsConfigurationHolder<AsPromotedFacet, AbstractAsFacetConfiguration> promotedFacetHolder : (Iterable<AsConfigurationHolder<AsPromotedFacet, AbstractAsFacetConfiguration>>)((MergeMap)searchProfileResult
                            .getPromotedFacets()).orderedValues())
            {
                AsPromotedFacet promotedFacet = (AsPromotedFacet)promotedFacetHolder.getConfiguration();
                PromotedFacetEditorData editorData = (PromotedFacetEditorData)getOrCreateEditorData(mapping, promotedFacet.getIndexProperty());
                convertFromSearchProfileResult(promotedFacetHolder, editorData, searchProfile);
            }
        }
        if(CollectionUtils.isNotEmpty(asSearchResult.getFacets()))
        {
            for(AsFacetData facet : asSearchResult.getFacets())
            {
                PromotedFacetEditorData editorData = mapping.get(facet.getIndexProperty());
                if(editorData != null)
                {
                    convertFromSearchResult(facet, editorData);
                }
            }
        }
    }


    protected void loadDataFromInitialValue(Map<Object, PromotedFacetEditorData> mapping, Collection<AsPromotedFacetModel> initialValue, Map<String, Object> parameters)
    {
        if(CollectionUtils.isNotEmpty(initialValue))
        {
            for(AsPromotedFacetModel promotedFacet : initialValue)
            {
                PromotedFacetEditorData editorData = (PromotedFacetEditorData)getOrCreateEditorData(mapping, promotedFacet.getIndexProperty());
                convertFromModel(promotedFacet, editorData);
            }
        }
    }


    protected void convertFromSearchProfileResult(AsConfigurationHolder<AsPromotedFacet, AbstractAsFacetConfiguration> source, PromotedFacetEditorData target, AbstractAsSearchProfileModel searchProfile)
    {
        AsPromotedFacet promotedFacet = (AsPromotedFacet)source.getConfiguration();
        String indexProperty = promotedFacet.getIndexProperty();
        target.setUid(promotedFacet.getUid());
        target.setLabel(indexProperty);
        target.setIndexProperty(indexProperty);
        target.setPriority(promotedFacet.getPriority());
        target.setMultiselect((promotedFacet.getFacetType() == AsFacetType.MULTISELECT_AND || promotedFacet
                        .getFacetType() == AsFacetType.MULTISELECT_OR));
        target.setFacetConfiguration((AbstractAsFacetConfiguration)promotedFacet);
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


    protected void convertFromSearchResult(AsFacetData source, PromotedFacetEditorData target)
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


    protected void convertFromModel(AsPromotedFacetModel source, PromotedFacetEditorData target)
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


    protected void postLoadData(Collection<AsPromotedFacetModel> initialValue, SearchResultData searchResult, Map<String, Object> parameters, ListModelList<PromotedFacetEditorData> data)
    {
        if(searchResult != null && data != null)
        {
            this.asFacetUtils.localizeFacets(searchResult.getNavigationContext(), searchResult.getSearchContext(), data.getInnerList());
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
