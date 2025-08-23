package de.hybris.platform.personalizationservices.customization.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.personalizationservices.customization.CxCustomizationService;
import de.hybris.platform.personalizationservices.customization.dao.CxCustomizationDao;
import de.hybris.platform.personalizationservices.customization.dao.CxCustomizationGroupDao;
import de.hybris.platform.personalizationservices.model.CxCustomizationModel;
import de.hybris.platform.personalizationservices.model.CxCustomizationsGroupModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DefaultCxCustomizationService implements CxCustomizationService
{
    private ModelService modelService;
    private CxCustomizationDao cxCustomziationDao;
    private CxCustomizationGroupDao cxCustomizationGroupDao;


    public Optional<CxCustomizationModel> getCustomization(String code, CatalogVersionModel catalogVersion)
    {
        ServicesUtil.validateParameterNotNull(catalogVersion, "CatalogVersion must not be null");
        ServicesUtil.validateParameterNotNull(code, "code must not be null");
        return this.cxCustomziationDao.findCustomizationByCode(code, catalogVersion);
    }


    public List<CxCustomizationModel> getCustomizations(CatalogVersionModel catalogVersion)
    {
        ServicesUtil.validateParameterNotNull(catalogVersion, "CatalogVersion must not be null");
        return this.cxCustomziationDao.findCustomizations(catalogVersion);
    }


    public SearchPageData<CxCustomizationModel> getCustomizations(CatalogVersionModel catalogVersion, Map<String, String> params, SearchPageData<?> pagination)
    {
        ServicesUtil.validateParameterNotNull(catalogVersion, "CatalogVersion must not be null");
        ServicesUtil.validateParameterNotNull(params, "params must not be null");
        ServicesUtil.validateParameterNotNull(pagination, "pagination must not be null");
        return this.cxCustomziationDao.findCustomizations(catalogVersion, params, pagination);
    }


    public CxCustomizationsGroupModel getDefaultGroup(CatalogVersionModel catalogVersion)
    {
        return this.cxCustomizationGroupDao.getDefaultGroup(catalogVersion);
    }


    public CxCustomizationModel createCustomization(CxCustomizationModel customization, CxCustomizationsGroupModel customizationGroup, Integer rank)
    {
        ServicesUtil.validateParameterNotNull(customization, "Customization must not be null");
        ServicesUtil.validateParameterNotNull(customization.getCode(), "Customization code must not be null");
        ServicesUtil.validateParameterNotNull(customization.getName(), "Customization name must not be null");
        ServicesUtil.validateParameterNotNull(customizationGroup, "Customization group must not be null");
        ServicesUtil.validateParameterNotNull(customizationGroup.getCatalogVersion(), "Customization group must belong to some catalog version");
        customization.setCatalogVersion(customizationGroup.getCatalogVersion());
        customization.setGroup(customizationGroup);
        getModelService().save(customization);
        customization.setRank(rank);
        getModelService().save(customization.getGroup());
        return customization;
    }


    public boolean isDefaultGroup(CatalogVersionModel catalogVersion)
    {
        return this.cxCustomizationGroupDao.isDefaultGroup(catalogVersion);
    }


    public void setCxCustomizationGroupDao(CxCustomizationGroupDao cxCustomizationGroupDao)
    {
        this.cxCustomizationGroupDao = cxCustomizationGroupDao;
    }


    protected CxCustomizationGroupDao getCxCustomizationGroupDao()
    {
        return this.cxCustomizationGroupDao;
    }


    public void setCxCustomziationDao(CxCustomizationDao cxCustomziationDao)
    {
        this.cxCustomziationDao = cxCustomziationDao;
    }


    protected CxCustomizationDao getCxCustomziationDao()
    {
        return this.cxCustomziationDao;
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }
}
