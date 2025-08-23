package de.hybris.platform.personalizationcms.strategy;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.personalizationservices.enums.CxCatalogLookupType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class AllCatalogsHierarchyLookupStrategy extends LeavesOrClosestAncestorsCatalogLookupStrategy
{
    public CxCatalogLookupType getType()
    {
        return CxCatalogLookupType.ALL_CATALOGS;
    }


    public List<CatalogVersionModel> getCatalogVersionsForCalculation()
    {
        Collection<CatalogVersionModel> allCatalogsVersions = getCatalogVersionService().getSessionCatalogVersions();
        List<CatalogModel> sessionCatalogs = getSessionCatalogs();
        List<CatalogVersionModel> result = new ArrayList<>();
        do
        {
            Set<CatalogModel> leafCatalogs = getLeafCatalogs(sessionCatalogs);
            sessionCatalogs.removeAll(leafCatalogs);
            result.addAll(getCatalogVersionsFromLeafCatalogs(allCatalogsVersions, leafCatalogs));
        }
        while(!sessionCatalogs.isEmpty());
        return result;
    }
}
