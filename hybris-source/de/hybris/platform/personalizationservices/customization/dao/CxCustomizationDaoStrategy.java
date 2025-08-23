package de.hybris.platform.personalizationservices.customization.dao;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.personalizationservices.dao.CxDaoStrategy;
import de.hybris.platform.personalizationservices.dao.CxMulticountryParamSupport;
import java.util.List;
import java.util.Map;

public interface CxCustomizationDaoStrategy extends CxDaoStrategy
{
    public static final String CATALOGS = "catalogVersions";


    default boolean isMulticountryEnabled(Map<String, Object> queryParams)
    {
        return CxMulticountryParamSupport.isMulticountryEnabled(queryParams);
    }


    default String getMulticountryWhereOperator(Map<String, Object> queryParams)
    {
        return CxMulticountryParamSupport.getMulticountryWhereOperator(queryParams);
    }


    default String buildOrderByForMulticountry(Map<String, Object> queryParams)
    {
        return CxMulticountryParamSupport.buildOrderByForMulticountry(queryParams);
    }


    default String buildOrderByForMulticountry(Map<String, Object> queryParams, String prefix)
    {
        return CxMulticountryParamSupport.buildOrderByForMulticountry(queryParams, prefix);
    }


    default String buildCaseFroMulticountry(Map<String, Object> queryParams, String prefix)
    {
        return CxMulticountryParamSupport.buildCaseFroMulticountry(queryParams, prefix);
    }


    default String buildCaseFroMulticountry(List<CatalogVersionModel> catalogVersions, String prefix)
    {
        return CxMulticountryParamSupport.buildCaseFroMulticountry(catalogVersions, prefix);
    }
}
