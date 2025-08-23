package de.hybris.platform.adaptivesearchbackoffice.editors.excludeditems;

import com.hybris.cockpitng.labels.LabelService;
import de.hybris.platform.adaptivesearch.data.AbstractAsBoostItemConfiguration;
import de.hybris.platform.adaptivesearch.data.AsConfigurationHolder;
import de.hybris.platform.adaptivesearch.data.AsExcludedItem;
import de.hybris.platform.adaptivesearch.data.AsSearchProfileResult;
import de.hybris.platform.adaptivesearch.data.AsSearchResultData;
import de.hybris.platform.adaptivesearch.model.AbstractAsConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AbstractAsSearchProfileModel;
import de.hybris.platform.adaptivesearch.model.AsExcludedItemModel;
import de.hybris.platform.adaptivesearch.util.MergeMap;
import de.hybris.platform.adaptivesearchbackoffice.data.ExcludedItemEditorData;
import de.hybris.platform.adaptivesearchbackoffice.data.SearchResultData;
import de.hybris.platform.adaptivesearchbackoffice.editors.configurablemultireference.AbstractDataHandler;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.exceptions.ModelLoadingException;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.Collection;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class AsExcludedItemsDataHandler extends AbstractDataHandler<ExcludedItemEditorData, AsExcludedItemModel>
{
    private static final Logger LOG = LoggerFactory.getLogger(AsExcludedItemsDataHandler.class);
    private ModelService modelService;
    private LabelService labelService;


    public String getTypeCode()
    {
        return "AsExcludedItem";
    }


    protected ExcludedItemEditorData createEditorData()
    {
        ExcludedItemEditorData editorData = new ExcludedItemEditorData();
        editorData.setValid(true);
        return editorData;
    }


    protected void loadDataFromSearchResult(Map<Object, ExcludedItemEditorData> mapping, SearchResultData searchResult, Map<String, Object> parameters)
    {
        if(searchResult == null || searchResult.getAsSearchResult() == null)
        {
            return;
        }
        AsSearchResultData asSearchResult = searchResult.getAsSearchResult();
        AsSearchProfileResult searchProfileResult = asSearchResult.getSearchProfileResult();
        if(searchProfileResult != null && MapUtils.isNotEmpty(searchProfileResult.getExcludedItems()))
        {
            AbstractAsSearchProfileModel searchProfile = (AbstractAsSearchProfileModel)parameters.get("searchProfile");
            for(AsConfigurationHolder<AsExcludedItem, AbstractAsBoostItemConfiguration> excludedItemHolder : (Iterable<AsConfigurationHolder<AsExcludedItem, AbstractAsBoostItemConfiguration>>)((MergeMap)searchProfileResult
                            .getExcludedItems()).orderedValues())
            {
                AsExcludedItem excludedItem = (AsExcludedItem)excludedItemHolder.getConfiguration();
                ExcludedItemEditorData editorData = (ExcludedItemEditorData)getOrCreateEditorData(mapping, excludedItem.getUid());
                convertFromSearchProfileResult(excludedItemHolder, editorData, searchProfile);
            }
        }
    }


    protected void loadDataFromInitialValue(Map<Object, ExcludedItemEditorData> mapping, Collection<AsExcludedItemModel> initialValue, Map<String, Object> parameters)
    {
        if(CollectionUtils.isNotEmpty(initialValue))
        {
            for(AsExcludedItemModel excludedItem : initialValue)
            {
                ExcludedItemEditorData editorData = (ExcludedItemEditorData)getOrCreateEditorData(mapping, excludedItem.getUid());
                convertFromModel(excludedItem, editorData);
            }
        }
    }


    protected void convertFromSearchProfileResult(AsConfigurationHolder<AsExcludedItem, AbstractAsBoostItemConfiguration> source, ExcludedItemEditorData target, AbstractAsSearchProfileModel searchProfile)
    {
        AsExcludedItem excludedItem = (AsExcludedItem)source.getConfiguration();
        PK itemPk = excludedItem.getItemPk();
        ItemModel item = null;
        try
        {
            item = (ItemModel)this.modelService.get(itemPk);
        }
        catch(ModelLoadingException e)
        {
            LOG.warn("Failed to load excluded item", (Throwable)e);
        }
        target.setUid(excludedItem.getUid());
        target.setLabel(this.labelService.getObjectLabel(item));
        target.setItemPk(itemPk);
        target.setBoostItemConfiguration((AbstractAsBoostItemConfiguration)excludedItem);
        target.setFromSearchProfile(isConfigurationFromSearchProfile((AbstractAsBoostItemConfiguration)source.getConfiguration(), searchProfile));
        AbstractAsBoostItemConfiguration replacedConfiguration = CollectionUtils.isNotEmpty(source.getReplacedConfigurations()) ? source.getReplacedConfigurations().get(0) : null;
        target.setOverride((replacedConfiguration != null));
        target.setOverrideFromSearchProfile(isConfigurationFromSearchProfile(replacedConfiguration, searchProfile));
    }


    protected boolean isConfigurationFromSearchProfile(AbstractAsBoostItemConfiguration configuration, AbstractAsSearchProfileModel searchProfile)
    {
        if(configuration == null || searchProfile == null)
        {
            return false;
        }
        return StringUtils.equals(searchProfile.getCode(), configuration.getSearchProfileCode());
    }


    protected void convertFromModel(AsExcludedItemModel source, ExcludedItemEditorData target)
    {
        if(StringUtils.isBlank(source.getUid()))
        {
            source.setUid(getAsUidGenerator().generateUid());
        }
        ItemModel item = source.getItem();
        target.setUid(source.getUid());
        target.setLabel(this.labelService.getObjectLabel(item));
        target.setValid(getAsConfigurationService().isValid((AbstractAsConfigurationModel)source));
        target.setItemPk((item == null) ? null : item.getPk());
        target.setModel((ItemModel)source);
        target.setFromSearchProfile(true);
        target.setFromSearchConfiguration(true);
    }


    public ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public LabelService getLabelService()
    {
        return this.labelService;
    }


    @Required
    public void setLabelService(LabelService labelService)
    {
        this.labelService = labelService;
    }
}
