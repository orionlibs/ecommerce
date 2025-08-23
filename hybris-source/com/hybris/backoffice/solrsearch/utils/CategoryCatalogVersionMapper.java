package com.hybris.backoffice.solrsearch.utils;

import de.hybris.platform.category.model.CategoryModel;

@Deprecated(since = "2105")
public class CategoryCatalogVersionMapper
{
    private static final String DELIMITER = "@@";


    public String encode(CategoryModel category)
    {
        return category.getCode() + "@@" + category.getCode() + "@@" + category.getCatalogVersion().getCatalog().getId();
    }


    public CategoryWithCatalogVersion decode(String categoryWithCatVersion)
    {
        if(categoryWithCatVersion.contains("@@"))
        {
            String[] decodedCategory = categoryWithCatVersion.split("@@");
            if(decodedCategory.length != 3)
            {
                throw new IllegalArgumentException("Can't decode: " + categoryWithCatVersion);
            }
            return new CategoryWithCatalogVersion(decodedCategory);
        }
        throw new IllegalArgumentException("Can't decode: " + categoryWithCatVersion);
    }
}
