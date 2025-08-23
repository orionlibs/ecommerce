package de.hybris.platform.personalizationcms.action.interceptor;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.personalizationcms.model.CxCmsActionModel;
import de.hybris.platform.personalizationcms.model.CxCmsComponentContainerModel;
import de.hybris.platform.personalizationservices.configuration.CxConfigurationService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.RemoveInterceptor;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class CxActionRemoveInterceptor implements RemoveInterceptor<CxCmsActionModel>
{
    private static final Logger LOG = Logger.getLogger(CxActionRemoveInterceptor.class);
    private static final String CATALOG_LINKING_PROPERTY_ROOT = "personalizationcms.containers.cleanup.";
    private static final int CATALOG_SPLIT_STRING_EXPECTED_LENGTH = 2;
    private ModelService modelService;
    private FlexibleSearchService flexibleSearchService;
    private ConfigurationService configurationService;
    private CatalogVersionService catalogVersionService;
    private CxConfigurationService cxConfigurationService;


    public void onRemove(CxCmsActionModel cmsAction, InterceptorContext ctx) throws InterceptorException
    {
        try
        {
            if(isCleanupEnabled(cmsAction))
            {
                cleanCxContainer(cmsAction);
            }
        }
        catch(RuntimeException e)
        {
            LOG.error("Could not clean up the associated container to action : " + cmsAction.getCode(), e);
        }
    }


    protected boolean isCleanupEnabled(CxCmsActionModel cmsAction)
    {
        Optional<CatalogVersionModel> contentCatalog = getContentCatalog(cmsAction);
        if(contentCatalog.isPresent())
        {
            return ((Boolean)this.cxConfigurationService.getValue(contentCatalog.get(), config -> Boolean.valueOf(config.isContainerCleanupEnabled()),
                            isCleanupEnabled())).booleanValue();
        }
        return false;
    }


    protected Boolean isCleanupEnabled()
    {
        return this.configurationService.getConfiguration().getBoolean("personalizationcms.containers.cleanup.enabled", Boolean.FALSE);
    }


    protected void cleanCxContainer(CxCmsActionModel cmsAction)
    {
        List<CxCmsComponentContainerModel> containers = getContainers(cmsAction);
        if(containers.isEmpty())
        {
            return;
        }
        if(isLastCmsAction(cmsAction))
        {
            containers.forEach(this::removeContainer);
        }
    }


    protected List<CxCmsComponentContainerModel> getContainers(CxCmsActionModel cmsAction)
    {
        Optional<CatalogVersionModel> contentCatalog = getContentCatalog(cmsAction);
        if(contentCatalog.isPresent())
        {
            CxCmsComponentContainerModel example = new CxCmsComponentContainerModel();
            example.setSourceId(cmsAction.getContainerId());
            example.setCatalogVersion(contentCatalog.get());
            List<CxCmsComponentContainerModel> modelsByExample = this.flexibleSearchService.getModelsByExample(example);
            if(modelsByExample.isEmpty())
            {
                LOG.warn("Cleaning cx container attached to CxCmsAction failed because it cannot be found : " + cmsAction
                                .getContainerId());
            }
            return modelsByExample;
        }
        return Collections.emptyList();
    }


    protected void removeContainer(CxCmsComponentContainerModel container)
    {
        for(ContentSlotModel slot : container.getSlots())
        {
            slot.setCmsComponents(new ArrayList(slot.getCmsComponents()));
            Collections.replaceAll(slot.getCmsComponents(), container, container.getDefaultCmsComponent());
        }
        this.modelService.saveAll(container.getSlots());
        this.modelService.remove(container);
    }


    protected Optional<CatalogVersionModel> getContentCatalog(CxCmsActionModel action)
    {
        String key = "personalizationcms.containers.cleanup.cxcatalogtocmscatalog." + action.getCatalogVersion().getCatalog().getId() + "." + action.getCatalogVersion().getVersion();
        String catalogFromConfiguration = this.configurationService.getConfiguration().getString(key);
        if(StringUtils.isEmpty(catalogFromConfiguration))
        {
            return Optional.of(action.getCatalogVersion());
        }
        String[] splitConfig = catalogFromConfiguration.split(",");
        if(splitConfig.length != 2)
        {
            LOG.error(String.format("Wrong catalog format was set for property %s. Could not find the catalog to cleanup.", new Object[] {key}));
            return Optional.empty();
        }
        return Optional.of(this.catalogVersionService.getCatalogVersion(splitConfig[0], splitConfig[1]));
    }


    protected boolean isLastCmsAction(CxCmsActionModel cmsAction)
    {
        CxCmsActionModel example = new CxCmsActionModel();
        example.setContainerId(cmsAction.getContainerId());
        example.setCatalogVersion(cmsAction.getCatalogVersion());
        List<CxCmsActionModel> modelsByExample = this.flexibleSearchService.getModelsByExample(example);
        return (modelsByExample.isEmpty() || (modelsByExample.size() == 1 && modelsByExample.contains(cmsAction)));
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    protected FlexibleSearchService getFlexibleSearchService()
    {
        return this.flexibleSearchService;
    }


    protected ConfigurationService getConfigurationService()
    {
        return this.configurationService;
    }


    protected CatalogVersionService getCatalogVersionService()
    {
        return this.catalogVersionService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }


    @Required
    public void setCatalogVersionService(CatalogVersionService catalogVersionService)
    {
        this.catalogVersionService = catalogVersionService;
    }


    @Required
    public void setConfigurationService(ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }


    protected CxConfigurationService getCxConfigurationService()
    {
        return this.cxConfigurationService;
    }


    @Required
    public void setCxConfigurationService(CxConfigurationService cxConfigurationService)
    {
        this.cxConfigurationService = cxConfigurationService;
    }
}
