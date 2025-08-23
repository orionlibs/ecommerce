package com.hybris.backoffice.search.utils;

import de.hybris.platform.category.model.CategoryModel;
import java.util.regex.Pattern;

public class CategoryCatalogVersionMapper
{
    private static final String DELIMITER = "@@";
    private static final Pattern DELIMITER_PATTERN = Pattern.compile("@@");
    private static final int DEFAULT_CATEGORY_LENGTH = 3;


    public String encode(CategoryModel category)
    {
        return category.getCode() + "@@" + category.getCode() + "@@" + category.getCatalogVersion().getCatalog().getId();
    }


    public CategoryWithCatalogVersion decode(String categoryWithCatVersion)
    {
        if(categoryWithCatVersion.contains("@@"))
        {
            String[] decodedCategory = DELIMITER_PATTERN.split(categoryWithCatVersion);
            if(decodedCategory.length != 3)
            {
                throw new IllegalArgumentException("Can't decode: " + categoryWithCatVersion);
            }
            return new CategoryWithCatalogVersion(decodedCategory);
        }
        throw new IllegalArgumentException("Can't decode: " + categoryWithCatVersion);
    }
}
