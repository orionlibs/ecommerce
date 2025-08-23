package de.hybris.platform.adaptivesearch.services.impl;

import de.hybris.platform.adaptivesearch.context.AsSearchProfileContext;
import de.hybris.platform.adaptivesearch.daos.AsSearchConfigurationDao;
import de.hybris.platform.adaptivesearch.data.AsSearchConfigurationInfoData;
import de.hybris.platform.adaptivesearch.model.AbstractAsSearchConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AbstractAsSearchProfileModel;
import de.hybris.platform.adaptivesearch.services.AsSearchConfigurationService;
import de.hybris.platform.adaptivesearch.strategies.AsCloneStrategy;
import de.hybris.platform.adaptivesearch.strategies.AsSearchConfigurationStrategy;
import de.hybris.platform.adaptivesearch.strategies.AsSearchProfileMapping;
import de.hybris.platform.adaptivesearch.strategies.AsSearchProfileRegistry;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Required;

public class DefaultAsSearchConfigurationService implements AsSearchConfigurationService
{
    protected static final String UID_PARAM = "uid";
    protected static final String CONTEXT_PARAM = "context";
    protected static final String SEARCH_PROFILE_PARAM = "searchProfile";
    private AsSearchConfigurationDao asSearchConfigurationDao;
    private AsSearchProfileRegistry asSearchProfileRegistry;
    private AsCloneStrategy asCloneStrategy;


    public List<AbstractAsSearchConfigurationModel> getAllSearchConfigurations()
    {
        return this.asSearchConfigurationDao.findAllSearchConfigurations();
    }


    public List<AbstractAsSearchConfigurationModel> getSearchConfigurationsForCatalogVersion(CatalogVersionModel catalogVersion)
    {
        return this.asSearchConfigurationDao.findSearchConfigurationsByCatalogVersion(catalogVersion);
    }


    public <T extends AbstractAsSearchConfigurationModel> Optional<T> getSearchConfigurationForUid(CatalogVersionModel catalogVersion, String uid)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("uid", uid);
        return this.asSearchConfigurationDao.findSearchConfigurationByUid(catalogVersion, uid);
    }


    public Optional<AbstractAsSearchConfigurationModel> getSearchConfigurationForContext(AsSearchProfileContext context, AbstractAsSearchProfileModel searchProfile)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("context", context);
        ServicesUtil.validateParameterNotNullStandardMessage("searchProfile", searchProfile);
        AsSearchProfileMapping strategyMapping = this.asSearchProfileRegistry.getSearchProfileMapping(searchProfile);
        AsSearchConfigurationStrategy searchConfigurationStrategy = strategyMapping.getSearchConfigurationStrategy();
        return searchConfigurationStrategy.getForContext(context, searchProfile);
    }


    public AbstractAsSearchConfigurationModel getOrCreateSearchConfigurationForContext(AsSearchProfileContext context, AbstractAsSearchProfileModel searchProfile)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("context", context);
        ServicesUtil.validateParameterNotNullStandardMessage("searchProfile", searchProfile);
        AsSearchProfileMapping strategyMapping = this.asSearchProfileRegistry.getSearchProfileMapping(searchProfile);
        AsSearchConfigurationStrategy searchConfigurationStrategy = strategyMapping.getSearchConfigurationStrategy();
        return searchConfigurationStrategy.getOrCreateForContext(context, searchProfile);
    }


    public AsSearchConfigurationInfoData getSearchConfigurationInfoForContext(AsSearchProfileContext context, AbstractAsSearchProfileModel searchProfile)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("context", context);
        ServicesUtil.validateParameterNotNullStandardMessage("searchProfile", searchProfile);
        AsSearchProfileMapping strategyMapping = this.asSearchProfileRegistry.getSearchProfileMapping(searchProfile);
        AsSearchConfigurationStrategy searchConfigurationStrategy = strategyMapping.getSearchConfigurationStrategy();
        return searchConfigurationStrategy.getInfoForContext(context, searchProfile);
    }


    public <T extends AbstractAsSearchConfigurationModel> T cloneSearchConfiguration(T searchConfiguration)
    {
        return (T)this.asCloneStrategy.clone((ItemModel)searchConfiguration);
    }


    public AsSearchConfigurationDao getAsSearchConfigurationDao()
    {
        return this.asSearchConfigurationDao;
    }


    @Required
    public void setAsSearchConfigurationDao(AsSearchConfigurationDao asSearchConfigurationDao)
    {
        this.asSearchConfigurationDao = asSearchConfigurationDao;
    }


    public AsSearchProfileRegistry getAsSearchProfileRegistry()
    {
        return this.asSearchProfileRegistry;
    }


    @Required
    public void setAsSearchProfileRegistry(AsSearchProfileRegistry asSearchProfileRegistry)
    {
        this.asSearchProfileRegistry = asSearchProfileRegistry;
    }


    public AsCloneStrategy getAsCloneStrategy()
    {
        return this.asCloneStrategy;
    }


    @Required
    public void setAsCloneStrategy(AsCloneStrategy asCloneStrategy)
    {
        this.asCloneStrategy = asCloneStrategy;
    }


    public Set<String> getSearchConfigurationQualifiers(AbstractAsSearchProfileModel searchProfile)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("searchProfile", searchProfile);
        AsSearchProfileMapping strategyMapping = this.asSearchProfileRegistry.getSearchProfileMapping(searchProfile);
        AsSearchConfigurationStrategy searchConfigurationStrategy = strategyMapping.getSearchConfigurationStrategy();
        return searchConfigurationStrategy.getQualifiers(searchProfile);
    }
}
