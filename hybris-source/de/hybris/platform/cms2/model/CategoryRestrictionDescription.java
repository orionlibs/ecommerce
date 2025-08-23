package de.hybris.platform.cms2.model;

import com.google.common.base.Strings;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.model.restrictions.CMSCategoryRestrictionModel;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;
import de.hybris.platform.util.localization.Localization;
import java.util.Collection;

public class CategoryRestrictionDescription implements DynamicAttributeHandler<String, CMSCategoryRestrictionModel>
{
    public String get(CMSCategoryRestrictionModel model)
    {
        Collection<CategoryModel> categories = model.getCategories();
        StringBuilder result = new StringBuilder();
        if(categories != null && !categories.isEmpty())
        {
            String localizedString = Localization.getLocalizedString("type.CMSCategoryRestriction.description.text");
            result.append((localizedString == null) ? "Display for categories:" : localizedString);
            for(CategoryModel category : categories)
            {
                if(!Strings.isNullOrEmpty(category.getName()))
                {
                    result.append(" ").append(category.getName());
                }
                result.append(" (").append(category.getCode()).append(");");
            }
        }
        return result.toString();
    }


    public void set(CMSCategoryRestrictionModel model, String value)
    {
        throw new UnsupportedOperationException();
    }
}
