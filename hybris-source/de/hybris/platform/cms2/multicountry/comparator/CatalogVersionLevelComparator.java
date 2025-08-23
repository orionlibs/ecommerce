package de.hybris.platform.cms2.multicountry.comparator;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.cms2.multicountry.service.CatalogLevelService;
import java.util.Comparator;
import org.springframework.beans.factory.annotation.Required;

public class CatalogVersionLevelComparator implements Comparator<CatalogVersionModel>
{
    private CatalogLevelService cmsCatalogLevelService;


    public int compare(CatalogVersionModel catalogVersion1, CatalogVersionModel catalogVersion2)
    {
        if(catalogVersion1 == null)
        {
            return 1;
        }
        if(catalogVersion2 == null)
        {
            return -1;
        }
        int entry1CatalogLevel = getCmsCatalogLevelService().getCatalogLevel((ContentCatalogModel)catalogVersion1.getCatalog());
        int entry2CatalogLevel = getCmsCatalogLevelService().getCatalogLevel((ContentCatalogModel)catalogVersion2.getCatalog());
        Integer result = Integer.valueOf(Integer.compare(entry1CatalogLevel, entry2CatalogLevel));
        return (result.intValue() == 0) ? compareSameLevelCatalogVersions(catalogVersion1, catalogVersion2) : result.intValue();
    }


    protected int compareSameLevelCatalogVersions(CatalogVersionModel catalogVersion1, CatalogVersionModel catalogVersion2)
    {
        if(catalogVersion1.getActive().equals(catalogVersion2.getActive()))
        {
            return 0;
        }
        if(catalogVersion1.getActive().booleanValue())
        {
            return -1;
        }
        return 1;
    }


    protected CatalogLevelService getCmsCatalogLevelService()
    {
        return this.cmsCatalogLevelService;
    }


    @Required
    public void setCmsCatalogLevelService(CatalogLevelService cmsCatalogLevelService)
    {
        this.cmsCatalogLevelService = cmsCatalogLevelService;
    }
}
