package de.hybris.platform.cockpit.services.label.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cockpit.services.label.CatalogAwareModelLabelProvider;

public class CategoryModelLabelProvider extends CatalogAwareModelLabelProvider<CategoryModel>
{
    protected String getItemLabel(CategoryModel category)
    {
        String name = category.getName();
        String code = category.getCode();
        return ((name == null) ? "" : name) + " [" + ((name == null) ? "" : name) + "]";
    }


    protected String getItemLabel(CategoryModel category, String languageIso)
    {
        return getItemLabel(category);
    }


    protected CatalogVersionModel getCatalogVersionModel(CategoryModel itemModel)
    {
        return itemModel.getCatalogVersion();
    }


    protected String getIconPath(CategoryModel item)
    {
        return null;
    }


    protected String getIconPath(CategoryModel item, String languageIso)
    {
        return null;
    }


    protected String getItemDescription(CategoryModel item)
    {
        return "";
    }


    protected String getItemDescription(CategoryModel item, String languageIso)
    {
        return "";
    }
}
