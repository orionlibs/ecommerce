package de.hybris.platform.adaptivesearch.services.impl;

import de.hybris.platform.adaptivesearch.daos.AsSearchProfileDao;
import de.hybris.platform.adaptivesearch.services.AsSearchProfileService;
import de.hybris.platform.adaptivesearch.strategies.AsCloneStrategy;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Required;

public class DefaultAsSearchProfileService implements AsSearchProfileService
{
    private AsSearchProfileDao asSearchProfileDao;
    private AsCloneStrategy asCloneStrategy;


    public <T extends de.hybris.platform.adaptivesearch.model.AbstractAsSearchProfileModel> List<T> getAllSearchProfiles()
    {
        return this.asSearchProfileDao.findAllSearchProfiles();
    }


    public <T extends de.hybris.platform.adaptivesearch.model.AbstractAsSearchProfileModel> List<T> getSearchProfilesForIndexTypesAndCatalogVersions(List<String> indexTypes, List<CatalogVersionModel> catalogVersions)
    {
        return this.asSearchProfileDao.findSearchProfilesByIndexTypesAndCatalogVersions(indexTypes, catalogVersions);
    }


    public <T extends de.hybris.platform.adaptivesearch.model.AbstractAsSearchProfileModel> List<T> getSearchProfilesForCatalogVersion(CatalogVersionModel catalogVersion)
    {
        return this.asSearchProfileDao.findSearchProfilesByCatalogVersion(catalogVersion);
    }


    public <T extends de.hybris.platform.adaptivesearch.model.AbstractAsSearchProfileModel> Optional<T> getSearchProfileForCode(CatalogVersionModel catalogVersion, String code)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("code", code);
        return this.asSearchProfileDao.findSearchProfileByCode(catalogVersion, code);
    }


    public <T extends de.hybris.platform.adaptivesearch.model.AbstractAsSearchProfileModel> List<T> getSearchProfiles(String query, Map<String, Object> filters)
    {
        ServicesUtil.validateParameterNotNull(filters, "filters must not be null");
        return this.asSearchProfileDao.getSearchProfiles(query, filters);
    }


    public <T extends de.hybris.platform.adaptivesearch.model.AbstractAsSearchProfileModel> SearchPageData<T> getSearchProfiles(String query, Map<String, Object> filters, SearchPageData<?> pagination)
    {
        ServicesUtil.validateParameterNotNull(filters, "filters must not be null");
        ServicesUtil.validateParameterNotNull(pagination, "pagination must not be null");
        return this.asSearchProfileDao.getSearchProfiles(query, filters, pagination);
    }


    public <T extends de.hybris.platform.adaptivesearch.model.AbstractAsSearchProfileModel> T cloneSearchProfile(T original)
    {
        return (T)this.asCloneStrategy.clone((ItemModel)original);
    }


    public AsSearchProfileDao getAsSearchProfileDao()
    {
        return this.asSearchProfileDao;
    }


    @Required
    public void setAsSearchProfileDao(AsSearchProfileDao asSearchProfileDao)
    {
        this.asSearchProfileDao = asSearchProfileDao;
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
}
