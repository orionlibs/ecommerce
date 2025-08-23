package de.hybris.platform.ruleengineservices.converters.populator;

import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.ruleengineservices.rao.OrderEntryRAO;
import de.hybris.platform.ruleengineservices.util.ProductUtils;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class OrderEntryRaoProductPopulator implements Populator<ProductModel, OrderEntryRAO>
{
    private CategoryService categoryService;
    private ProductUtils productUtils;


    public void populate(ProductModel source, OrderEntryRAO target)
    {
        if(Objects.nonNull(source))
        {
            target.setProductCode(source.getCode());
            Set<ProductModel> baseProducts = getProductUtils().getAllBaseProducts(source);
            target.setBaseProductCodes((Set)baseProducts.stream().map(ProductModel::getCode).collect(Collectors.toSet()));
            HashSet<String> categoryCodes = new HashSet<>(getAllCategoryCodes(source.getSupercategories()));
            baseProducts.stream().forEach(bp -> categoryCodes.addAll(getAllCategoryCodes(bp.getSupercategories())));
            target.setCategoryCodes(categoryCodes);
        }
    }


    protected Collection<String> getAllCategoryCodes(Collection<CategoryModel> categories)
    {
        if(CollectionUtils.isNotEmpty(categories))
        {
            Set<CategoryModel> allCategories = new HashSet<>(categories);
            for(CategoryModel category : categories)
            {
                allCategories.addAll(getCategoryService().getAllSupercategoriesForCategory(category));
            }
            return (Collection<String>)allCategories.stream().map(CategoryModel::getCode).collect(Collectors.toSet());
        }
        return Collections.emptySet();
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


    protected ProductUtils getProductUtils()
    {
        return this.productUtils;
    }


    @Required
    public void setProductUtils(ProductUtils productUtils)
    {
        this.productUtils = productUtils;
    }
}
