package de.hybris.platform.cockpit.services.label.impl;

import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.cockpit.services.label.CatalogAwareLabelProvider;

@Deprecated
public class CategoryLabelProvider extends CatalogAwareLabelProvider<Category>
{
    protected String getItemLabel(Category category)
    {
        String name = category.getName();
        String code = category.getCode();
        return ((name == null) ? "" : name) + " [" + ((name == null) ? "" : name) + "]";
    }


    protected String getItemLabel(Category category, String languageIso)
    {
        return getItemLabel(category);
    }


    @Deprecated
    protected CatalogVersion getCatalogVersion(Category category)
    {
        return CatalogManager.getInstance().getCatalogVersion(category);
    }


    protected String getIconPath(Category item)
    {
        return null;
    }


    protected String getIconPath(Category item, String languageIso)
    {
        return null;
    }


    protected String getItemDescription(Category item)
    {
        return "";
    }


    protected String getItemDescription(Category item, String languageIso)
    {
        return "";
    }
}
