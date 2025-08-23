package de.hybris.platform.personalizationcms.setup;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.personalizationcms.model.CxCmsActionModel;
import de.hybris.platform.personalizationcms.model.CxCmsComponentContainerModel;
import de.hybris.platform.personalizationservices.model.CxAbstractActionModel;
import de.hybris.platform.personalizationservices.model.config.CxConfigModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@SystemSetup(extension = "personalizationcms")
public class PersonalizationcmsSystemSetup
{
    private static final String CONTAINER_CLEANUP_ENABLED_PROPERTY = "personalizationcms.containers.cleanup.enabled";
    private FlexibleSearchService flexibleSearchService;
    private ModelService modelService;
    private ConfigurationService configurationService;
    private static final int PAGE_SIZE = 100;


    @SystemSetup(type = SystemSetup.Type.ESSENTIAL, process = SystemSetup.Process.UPDATE)
    public void updateCatalogInCxCmsAction()
    {
        List<CxCmsActionModel> results = getActionsForUpdate();
        results.forEach(this::updateAction);
        this.modelService.saveAll(results);
    }


    protected List<CxCmsActionModel> getActionsForUpdate()
    {
        String query = "SELECT {pk}  FROM {CxCmsAction}  WHERE {componentCatalog} IS NULL ";
        SearchResult<CxCmsActionModel> searchResult = this.flexibleSearchService.search("SELECT {pk}  FROM {CxCmsAction}  WHERE {componentCatalog} IS NULL ");
        return searchResult.getResult();
    }


    protected void updateAction(CxCmsActionModel model)
    {
        Objects.requireNonNull(model);
        Optional.<CxCmsActionModel>of(model).map(CxAbstractActionModel::getCatalogVersion).map(CatalogVersionModel::getCatalog).map(CatalogModel::getId).ifPresent(model::setComponentCatalog);
    }


    @SystemSetup(type = SystemSetup.Type.ESSENTIAL, process = SystemSetup.Process.UPDATE)
    public void updateSourceIdInCxContainer()
    {
        int start = 0;
        boolean isMore = true;
        do
        {
            SearchResult<CxCmsComponentContainerModel> searchResults = getCxContainers(start);
            isMore = (searchResults.getCount() == 100);
            start += 100;
            List<CxCmsComponentContainerModel> results = searchResults.getResult();
            results.forEach(this::updateCxCmsContainer);
            this.modelService.saveAll(results);
        }
        while(isMore);
    }


    protected SearchResult<CxCmsComponentContainerModel> getCxContainers(int start)
    {
        String query = "SELECT {pk}  FROM {CxCmsComponentContainer}  WHERE {sourceId} IS NULL ";
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {pk}  FROM {CxCmsComponentContainer}  WHERE {sourceId} IS NULL ");
        fQuery.setCount(100);
        fQuery.setStart(start);
        return this.flexibleSearchService.search(fQuery);
    }


    protected void updateCxCmsContainer(CxCmsComponentContainerModel model)
    {
        model.setSourceId(model.getUid());
    }


    @SystemSetup(type = SystemSetup.Type.ESSENTIAL, process = SystemSetup.Process.UPDATE)
    public void updateContainerCleanupEnabled()
    {
        boolean defaultValue = defaultContainerCleanupEnabled();
        List<CxConfigModel> results = getConfigForUpdate();
        results.forEach(c -> c.setContainerCleanupEnabled(defaultValue));
        this.modelService.saveAll(results);
    }


    protected List<CxConfigModel> getConfigForUpdate()
    {
        String query = "SELECT {pk}  FROM {CxConfig}  WHERE {containerCleanupEnabled} IS NULL ";
        SearchResult<CxConfigModel> searchResult = this.flexibleSearchService.search("SELECT {pk}  FROM {CxConfig}  WHERE {containerCleanupEnabled} IS NULL ");
        return searchResult.getResult();
    }


    protected boolean defaultContainerCleanupEnabled()
    {
        return this.configurationService.getConfiguration().getBoolean("personalizationcms.containers.cleanup.enabled", Boolean.TRUE.booleanValue());
    }


    protected FlexibleSearchService getFlexibleSearchService()
    {
        return this.flexibleSearchService;
    }


    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected ConfigurationService getConfigurationService()
    {
        return this.configurationService;
    }


    public void setConfigurationService(ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }
}
