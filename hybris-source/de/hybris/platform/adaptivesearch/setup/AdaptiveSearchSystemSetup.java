package de.hybris.platform.adaptivesearch.setup;

import de.hybris.platform.adaptivesearch.enums.AsGroupMergeMode;
import de.hybris.platform.adaptivesearch.enums.AsSortsMergeMode;
import de.hybris.platform.adaptivesearch.model.AbstractAsConfigurableSearchConfigurationModel;
import de.hybris.platform.adaptivesearch.util.ConfigurationUtils;
import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

@SystemSetup(extension = "adaptivesearch")
public class AdaptiveSearchSystemSetup
{
    private FlexibleSearchService flexibleSearchService;
    private ModelService modelService;


    @SystemSetup(type = SystemSetup.Type.ESSENTIAL, process = SystemSetup.Process.UPDATE)
    public void update()
    {
        updateSortsMergeMode();
        updateGroupMergeMode();
    }


    public void updateSortsMergeMode()
    {
        AsSortsMergeMode defaultSortsMergeMode = ConfigurationUtils.getDefaultSortsMergeMode();
        String query = "SELECT {pk} FROM {AbstractAsConfigurableSearchConfiguration} WHERE {sortsMergeMode} IS NULL";
        SearchResult<AbstractAsConfigurableSearchConfigurationModel> searchResult = this.flexibleSearchService.search("SELECT {pk} FROM {AbstractAsConfigurableSearchConfiguration} WHERE {sortsMergeMode} IS NULL");
        List<AbstractAsConfigurableSearchConfigurationModel> searchConfigurations = searchResult.getResult();
        if(CollectionUtils.isNotEmpty(searchConfigurations))
        {
            searchConfigurations.forEach(searchConfiguration -> searchConfiguration.setSortsMergeMode(defaultSortsMergeMode));
            this.modelService.saveAll(searchConfigurations);
        }
    }


    public void updateGroupMergeMode()
    {
        AsGroupMergeMode defaultGroupMergeMode = ConfigurationUtils.getDefaultGroupMergeMode();
        String query = "SELECT {pk} FROM {AbstractAsConfigurableSearchConfiguration} WHERE {groupMergeMode} IS NULL";
        SearchResult<AbstractAsConfigurableSearchConfigurationModel> searchResult = this.flexibleSearchService.search("SELECT {pk} FROM {AbstractAsConfigurableSearchConfiguration} WHERE {groupMergeMode} IS NULL");
        List<AbstractAsConfigurableSearchConfigurationModel> searchConfigurations = searchResult.getResult();
        if(CollectionUtils.isNotEmpty(searchConfigurations))
        {
            searchConfigurations.forEach(searchConfiguration -> searchConfiguration.setGroupMergeMode(defaultGroupMergeMode));
            this.modelService.saveAll(searchConfigurations);
        }
    }


    public FlexibleSearchService getFlexibleSearchService()
    {
        return this.flexibleSearchService;
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
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
}
