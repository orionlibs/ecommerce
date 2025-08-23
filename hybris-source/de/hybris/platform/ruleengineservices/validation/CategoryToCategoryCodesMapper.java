package de.hybris.platform.ruleengineservices.validation;

import com.google.common.base.Preconditions;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Required;

public class CategoryToCategoryCodesMapper implements Function<CategoryModel, Set<String>>
{
    private CategoryService categoryService;


    public Set<String> apply(CategoryModel category)
    {
        Preconditions.checkArgument((category != null), "Category is required to perform this operation, null given");
        Set<String> categoryCodes = (Set<String>)getCategoryService().getAllSubcategoriesForCategory(category).stream().map(CategoryModel::getCode).collect(Collectors.toSet());
        categoryCodes.add(category.getCode());
        return categoryCodes;
    }


    protected CategoryService getCategoryService()
    {
        return this.categoryService;
    }


    @Required
    public void setCategoryService(CategoryService categoryService)
    {
        this.categoryService = categoryService;
    }
}
