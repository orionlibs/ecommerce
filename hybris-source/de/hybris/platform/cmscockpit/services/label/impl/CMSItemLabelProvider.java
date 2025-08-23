package de.hybris.platform.cmscockpit.services.label.impl;

import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.cms2.common.annotations.HybrisDeprecation;
import de.hybris.platform.cms2.jalo.contents.CMSItem;
import de.hybris.platform.cockpit.services.label.CatalogAwareLabelProvider;
import de.hybris.platform.jalo.Item;

@Deprecated(since = "4.5", forRemoval = true)
@HybrisDeprecation(sinceVersion = "4.5")
public class CMSItemLabelProvider extends CatalogAwareLabelProvider<CMSItem>
{
    protected String getItemLabel(CMSItem item)
    {
        String name = item.getName();
        String code = item.getUid();
        return ((name == null) ? "" : name) + " [" + ((name == null) ? "" : name) + "]";
    }


    protected String getItemLabel(CMSItem item, String languageIso)
    {
        return getItemLabel(item);
    }


    protected CatalogVersion getCatalogVersion(CMSItem item)
    {
        return CatalogManager.getInstance().getCatalogVersion((Item)item);
    }


    protected String getIconPath(CMSItem item)
    {
        return null;
    }


    protected String getIconPath(CMSItem item, String languageIso)
    {
        return null;
    }


    protected String getItemDescription(CMSItem item)
    {
        return "";
    }


    protected String getItemDescription(CMSItem item, String languageIso)
    {
        return "";
    }
}
