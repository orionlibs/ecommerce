package de.hybris.platform.personalizationservices.dao;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import java.util.List;
import java.util.Map;

public final class CxMulticountryParamSupport
{
    public static final String CATALOGS = "catalogVersions";


    public static boolean isMulticountryEnabled(Map<String, Object> queryParams)
    {
        return (queryParams != null && queryParams.containsKey("catalogVersions"));
    }


    public static String getMulticountryWhereOperator(Map<String, Object> queryParams)
    {
        if(isMulticountryEnabled(queryParams))
        {
            return " IN (?catalogVersions) ";
        }
        return " = ?catalogVersion ";
    }


    public static String buildOrderByForMulticountry(Map<String, Object> queryParams)
    {
        return buildOrderByForMulticountry(queryParams, null);
    }


    public static String buildOrderByForMulticountry(Map<String, Object> queryParams, String prefix)
    {
        if(isMulticountryEnabled(queryParams))
        {
            List<CatalogVersionModel> catalogVersions = (List<CatalogVersionModel>)queryParams.get("catalogVersions");
            return " " + buildCaseFroMulticountry(catalogVersions, prefix) + " ASC,";
        }
        return "";
    }


    public static String buildCaseFroMulticountry(Map<String, Object> queryParams, String prefix)
    {
        if(isMulticountryEnabled(queryParams))
        {
            List<CatalogVersionModel> catalogVersions = (List<CatalogVersionModel>)queryParams.get("catalogVersions");
            return buildCaseFroMulticountry(catalogVersions, prefix);
        }
        return "1";
    }


    public static String buildCaseFroMulticountry(List<CatalogVersionModel> catalogVersions, String prefix)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(" (CASE {");
        if(prefix != null)
        {
            sb.append(prefix).append(".");
        }
        sb.append("catalogVersion").append("} ");
        for(int i = 0; i < catalogVersions.size(); i++)
        {
            sb.append(" WHEN ?").append("catalogVersions").append(i).append(" THEN ").append(i).append(" ");
        }
        sb.append(" END) ");
        return sb.toString();
    }
}
