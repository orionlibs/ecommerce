package de.hybris.platform.adaptivesearchbackoffice.editors.excludedfacets;

import de.hybris.platform.adaptivesearch.data.AbstractAsFacetConfiguration;
import de.hybris.platform.adaptivesearch.data.AsConfigurationHolder;
import de.hybris.platform.adaptivesearch.data.AsExcludedFacet;
import de.hybris.platform.adaptivesearch.data.AsSearchProfileResult;
import de.hybris.platform.adaptivesearch.data.AsSearchResultData;
import de.hybris.platform.adaptivesearch.enums.AsFacetType;
import de.hybris.platform.adaptivesearch.model.AbstractAsConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AbstractAsSearchProfileModel;
import de.hybris.platform.adaptivesearch.model.AsExcludedFacetModel;
import de.hybris.platform.adaptivesearch.util.MergeMap;
import de.hybris.platform.adaptivesearchbackoffice.common.AsFacetUtils;
import de.hybris.platform.adaptivesearchbackoffice.data.ExcludedFacetEditorData;
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

public class AsExcludedFacetsDataHandler extends AbstractDataHandler<ExcludedFacetEditorData, AsExcludedFacetModel>
{
    private AsFacetUtils asFacetUtils;


    public String getTypeCode()
    {
        return "AsExcludedFacet";
    }


    protected ExcludedFacetEditorData createEditorData()
    {
        ExcludedFacetEditorData editorData = new ExcludedFacetEditorData();
        editorData.setValid(true);
        return editorData;
    }


    protected void loadDataFromSearchResult(Map<Object, ExcludedFacetEditorData> mapping, SearchResultData searchResult, Map<String, Object> parameters)
    {
        if(searchResult == null || searchResult.getAsSearchResult() == null)
        {
            return;
        }
        AsSearchResultData asSearchResult = searchResult.getAsSearchResult();
        AsSearchProfileResult searchProfileResult = asSearchResult.getSearchProfileResult();
        if(searchProfileResult != null && MapUtils.isNotEmpty(searchProfileResult.getExcludedFacets()))
        {
            AbstractAsSearchProfileModel searchProfile = (AbstractAsSearchProfileModel)parameters.get("searchProfile");
            for(AsConfigurationHolder<AsExcludedFacet, AbstractAsFacetConfiguration> excludedFacetHolder : (Iterable<AsConfigurationHolder<AsExcludedFacet, AbstractAsFacetConfiguration>>)((MergeMap)searchProfileResult
                            .getExcludedFacets()).orderedValues())
            {
                AsExcludedFacet excludedFacet = (AsExcludedFacet)excludedFacetHolder.getConfiguration();
                ExcludedFacetEditorData editorData = (ExcludedFacetEditorData)getOrCreateEditorData(mapping, excludedFacet.getIndexProperty());
                convertFromSearchProfileResult(excludedFacetHolder, editorData, searchProfile);
            }
        }
    }


    protected void loadDataFromInitialValue(Map<Object, ExcludedFacetEditorData> mapping, Collection<AsExcludedFacetModel> initialValue, Map<String, Object> parameters)
    {
        if(CollectionUtils.isNotEmpty(initialValue))
        {
            for(AsExcludedFacetModel excludedFacet : initialValue)
            {
                ExcludedFacetEditorData editorData = (ExcludedFacetEditorData)getOrCreateEditorData(mapping, excludedFacet.getIndexProperty());
                convertFromModel(excludedFacet, editorData);
            }
        }
    }


    protected void convertFromSearchProfileResult(AsConfigurationHolder<AsExcludedFacet, AbstractAsFacetConfiguration> source, ExcludedFacetEditorData target, AbstractAsSearchProfileModel searchProfile)
    {
        AsExcludedFacet excludedFacet = (AsExcludedFacet)source.getConfiguration();
        String indexProperty = excludedFacet.getIndexProperty();
        target.setUid(excludedFacet.getUid());
        target.setLabel(indexProperty);
        target.setIndexProperty(indexProperty);
        target.setPriority(excludedFacet.getPriority());
        target.setMultiselect((excludedFacet.getFacetType() == AsFacetType.MULTISELECT_AND || excludedFacet
                        .getFacetType() == AsFacetType.MULTISELECT_OR));
        target.setFacetConfiguration((AbstractAsFacetConfiguration)excludedFacet);
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


    protected void convertFromModel(AsExcludedFacetModel source, ExcludedFacetEditorData target)
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


    protected void postLoadData(Collection<AsExcludedFacetModel> initialValue, SearchResultData searchResult, Map<String, Object> parameters, ListModelList<ExcludedFacetEditorData> data)
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
