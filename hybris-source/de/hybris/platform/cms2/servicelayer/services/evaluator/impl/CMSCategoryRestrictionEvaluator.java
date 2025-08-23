package de.hybris.platform.cms2.servicelayer.services.evaluator.impl;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.model.restrictions.CMSCategoryRestrictionModel;
import de.hybris.platform.cms2.servicelayer.data.RestrictionData;
import de.hybris.platform.cms2.servicelayer.services.evaluator.CMSRestrictionEvaluator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.log4j.Logger;

public class CMSCategoryRestrictionEvaluator implements CMSRestrictionEvaluator<CMSCategoryRestrictionModel>
{
    private static final Logger LOG = Logger.getLogger(CMSCategoryRestrictionEvaluator.class);


    public boolean evaluate(CMSCategoryRestrictionModel categoryRestrictionModel, RestrictionData context)
    {
        if(context == null)
        {
            return true;
        }
        List<CategoryModel> categories = new ArrayList<>();
        if(context.hasProduct())
        {
            Collection<CategoryModel> productCategories = context.getProduct().getSupercategories();
            categories = new ArrayList<>(productCategories);
            if(categoryRestrictionModel.isRecursive())
            {
                for(CategoryModel category : productCategories)
                {
                    categories.addAll(category.getAllSupercategories());
                }
            }
        }
        else if(context.hasCategory())
        {
            CategoryModel category = context.getCategory();
            categories.add(category);
            if(categoryRestrictionModel.isRecursive())
            {
                categories.addAll(category.getAllSupercategories());
            }
        }
        else
        {
            LOG.warn("Could not evaluate CMSCategoryRestriction. RestrictionData contains neither a category or a product. Returning false.");
            return false;
        }
        List<String> codes = getCategoryCodes(categories);
        for(String code : categoryRestrictionModel.getCategoryCodes())
        {
            if(codes.contains(code))
            {
                return true;
            }
        }
        return false;
    }


    protected List<String> getCategoryCodes(List<CategoryModel> categories)
    {
        List<String> codes = new ArrayList<>(categories.size());
        for(CategoryModel category : categories)
        {
            codes.add(category.getCode());
        }
        return codes;
    }
}
