package de.hybris.platform.personalizationcms.dao;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.personalizationservices.exceptions.EmptyResultParameterCombinationException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

public final class CxMulticountryCatalogSupport
{
    private static final String MULTI_CATALOG = "catalogVersions";
    private static final String CATALOGS = "catalogs";
    private static final String CURRENT = "current";
    private static final String PARENTS = "parents";
    private static final String ALL = "all";
    private static final String NO_PARENTS_MESSAGE = "Catalog with id: [{0}] has no parent catalogs but was called with parents parameter";
    private static final String ERROR_MESSAGE = "Unknown value [{0}], accepted values: [current, parents, all]";


    public static Map<String, Object> createCatalogParams(CatalogVersionModel catalogVersion, Map<String, String> externalParams)
    {
        Map<String, Object> result = new HashMap<>();
        String setting = externalParams.get("catalogs");
        boolean inputIsValid = (StringUtils.isNotBlank(setting) && catalogVersion != null && catalogVersion.getCatalog() instanceof ContentCatalogModel);
        if(!inputIsValid)
        {
            externalParams.remove("catalogs");
            return result;
        }
        List<CatalogVersionModel> targetCatalogVersions = new ArrayList<>();
        switch(setting.toLowerCase(Locale.ROOT))
        {
            case "all":
                targetCatalogVersions = buildParentCatalogs(catalogVersion);
                targetCatalogVersions.add(0, catalogVersion);
                break;
            case "parents":
                targetCatalogVersions = buildParentCatalogs(catalogVersion);
                checkIfCatalogVersionHasParents(catalogVersion, targetCatalogVersions);
                break;
            case "current":
                break;
            default:
                throw new IllegalArgumentException(MessageFormat.format("Unknown value [{0}], accepted values: [current, parents, all]", new Object[] {setting}));
        }
        if(targetCatalogVersions.isEmpty())
        {
            externalParams.remove("catalogs");
            return result;
        }
        return buildExtraCatalogs(targetCatalogVersions);
    }


    private static void checkIfCatalogVersionHasParents(CatalogVersionModel catalogVersion, List<CatalogVersionModel> targetCatalogVersions)
    {
        if(targetCatalogVersions.isEmpty())
        {
            throw new EmptyResultParameterCombinationException(
                            MessageFormat.format("Catalog with id: [{0}] has no parent catalogs but was called with parents parameter", new Object[] {catalogVersion.getCatalog().getId()}));
        }
    }


    private static List<CatalogVersionModel> buildParentCatalogs(CatalogVersionModel catalogVersion)
    {
        List<CatalogVersionModel> result = new ArrayList<>();
        ContentCatalogModel current = (ContentCatalogModel)catalogVersion.getCatalog();
        while(current.getSuperCatalog() != null)
        {
            current = current.getSuperCatalog();
            result.add(current.getActiveCatalogVersion());
        }
        return result;
    }


    private static Map<String, Object> buildExtraCatalogs(List<CatalogVersionModel> targetCatalogs)
    {
        Map<String, Object> result = new HashMap<>();
        result.put("catalogVersions", targetCatalogs);
        for(int i = 0; i < targetCatalogs.size(); i++)
        {
            String key = "catalogVersions" + i;
            result.put(key, targetCatalogs.get(i));
        }
        return result;
    }
}
