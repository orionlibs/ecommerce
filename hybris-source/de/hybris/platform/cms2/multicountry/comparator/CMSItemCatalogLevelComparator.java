package de.hybris.platform.cms2.multicountry.comparator;

import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.cms2.multicountry.service.CatalogLevelService;
import java.util.Comparator;
import org.springframework.beans.factory.annotation.Required;

public class CMSItemCatalogLevelComparator<T extends CMSItemModel> implements Comparator<T>
{
    private CatalogLevelService cmsCatalogLevelService;


    public int compare(CMSItemModel item1, CMSItemModel item2)
    {
        if(item1 == null)
        {
            return 1;
        }
        if(item2 == null)
        {
            return -1;
        }
        int item1CatalogLevel = getCmsCatalogLevelService().getCatalogLevel((ContentCatalogModel)item1.getCatalogVersion().getCatalog());
        int item2CatalogLevel = getCmsCatalogLevelService().getCatalogLevel((ContentCatalogModel)item2.getCatalogVersion().getCatalog());
        Integer result = Integer.valueOf(Integer.compare(item1CatalogLevel, item2CatalogLevel));
        return (result.intValue() == 0) ? compareItemsByCatalogVersions(item1, item2) : result.intValue();
    }


    protected int compareItemsByCatalogVersions(CMSItemModel item1, CMSItemModel item2)
    {
        if(item1.getCatalogVersion().getActive().equals(item2.getCatalogVersion().getActive()))
        {
            return 0;
        }
        if(item1.getCatalogVersion().getActive().booleanValue())
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
