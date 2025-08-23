package de.hybris.platform.cms2.model;

import com.google.common.base.Strings;
import de.hybris.platform.cms2.model.restrictions.CMSProductRestrictionModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;
import de.hybris.platform.util.localization.Localization;
import java.util.Collection;

public class ProductRestrictionDescription implements DynamicAttributeHandler<String, CMSProductRestrictionModel>
{
    public String get(CMSProductRestrictionModel model)
    {
        Collection<ProductModel> products = model.getProducts();
        StringBuilder result = new StringBuilder();
        if(products != null && !products.isEmpty())
        {
            String localizedString = Localization.getLocalizedString("type.CMSProductRestriction.description.text");
            result.append((localizedString == null) ? "Display for products:" : localizedString);
            for(ProductModel product : products)
            {
                if(!Strings.isNullOrEmpty(product.getName()))
                {
                    result.append(" ").append(product.getName());
                }
                result.append(" (").append(product.getCode()).append(");");
            }
        }
        return result.toString();
    }


    public void set(CMSProductRestrictionModel model, String value)
    {
        throw new UnsupportedOperationException();
    }
}
