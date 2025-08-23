package de.hybris.platform.commercefacades.catalog.data;

import java.util.Collection;

public class CatalogVersionData extends AbstractCatalogItemData
{
    private Collection<CategoryHierarchyData> categoriesHierarchyData;


    public void setCategoriesHierarchyData(Collection<CategoryHierarchyData> categoriesHierarchyData)
    {
        this.categoriesHierarchyData = categoriesHierarchyData;
    }


    public Collection<CategoryHierarchyData> getCategoriesHierarchyData()
    {
        return this.categoriesHierarchyData;
    }
}
