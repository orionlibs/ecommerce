package de.hybris.platform.cmscockpit.services.label.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cockpit.services.label.CatalogAwareModelLabelProvider;

public class CMSItemModelLabelProvider extends CatalogAwareModelLabelProvider<CMSItemModel>
{
    protected String getItemLabel(CMSItemModel item)
    {
        String name = item.getName();
        String code = item.getUid();
        return ((name == null) ? "" : name) + " [" + ((name == null) ? "" : name) + "]";
    }


    protected String getItemLabel(CMSItemModel item, String languageIso)
    {
        return getItemLabel(item);
    }


    protected CatalogVersionModel getCatalogVersionModel(CMSItemModel item)
    {
        return item.getCatalogVersion();
    }


    protected String getIconPath(CMSItemModel item)
    {
        return null;
    }


    protected String getIconPath(CMSItemModel item, String languageIso)
    {
        return null;
    }


    protected String getItemDescription(CMSItemModel item)
    {
        return "";
    }


    protected String getItemDescription(CMSItemModel item, String languageIso)
    {
        return "";
    }
}
