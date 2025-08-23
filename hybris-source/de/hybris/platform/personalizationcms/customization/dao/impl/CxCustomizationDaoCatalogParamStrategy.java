package de.hybris.platform.personalizationcms.customization.dao.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.personalizationcms.dao.CxMulticountryCatalogSupport;
import de.hybris.platform.personalizationservices.customization.dao.CxCustomizationDaoParamStrategy;
import java.util.Map;

public class CxCustomizationDaoCatalogParamStrategy implements CxCustomizationDaoParamStrategy
{
    private static final String SINGLE_CATALOG = "catalogVersion";


    public Map<String, Object> expandParams(Map<String, Object> params, Map<String, String> externalParams)
    {
        CatalogVersionModel catalogVersion = (CatalogVersionModel)params.get("catalogVersion");
        Map<String, Object> result = CxMulticountryCatalogSupport.createCatalogParams(catalogVersion, externalParams);
        result.putAll(params);
        return result;
    }
}
