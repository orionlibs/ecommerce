package de.hybris.platform.promotionengineservices.promotionengine.impl;

import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.promotionengineservices.promotionengine.PromotionMessageParameterResolutionStrategy;
import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCategoryResolutionStrategy implements PromotionMessageParameterResolutionStrategy
{
    private CategoryService categoryService;
    private static final Logger LOG = LoggerFactory.getLogger(DefaultCategoryResolutionStrategy.class);


    public String getValue(RuleParameterData data, PromotionResultModel promotionResult, Locale locale)
    {
        String categoryCode = (String)data.getValue();
        CategoryModel category = getCategory(categoryCode);
        if(category != null)
        {
            return getCategoryRepresentation(category);
        }
        return categoryCode;
    }


    protected CategoryModel getCategory(String categoryCode)
    {
        try
        {
            return getCategoryService().getCategoriesForCode(categoryCode).stream().filter(this::isSupportedCategory).findFirst()
                            .orElse(null);
        }
        catch(IllegalArgumentException e)
        {
            LOG.error("Cannot resolve category code: {} to a category.", categoryCode, e);
            return null;
        }
    }


    protected boolean isSupportedCategory(CategoryModel categoryModel)
    {
        return !(categoryModel.getCatalogVersion() instanceof de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel);
    }


    protected String getCategoryRepresentation(CategoryModel category)
    {
        return category.getName();
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
