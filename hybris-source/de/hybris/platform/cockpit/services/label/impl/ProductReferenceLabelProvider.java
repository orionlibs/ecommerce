package de.hybris.platform.cockpit.services.label.impl;

import de.hybris.platform.catalog.jalo.ProductReference;
import de.hybris.platform.cockpit.services.label.AbstractObjectLabelProvider;
import de.hybris.platform.jalo.product.Product;

@Deprecated
public class ProductReferenceLabelProvider extends AbstractObjectLabelProvider<ProductReference>
{
    protected String getItemLabel(ProductReference productReference)
    {
        Product product = productReference.getTarget();
        if(product == null)
        {
            return productReference.toString();
        }
        String name = product.getName();
        String code = product.getCode();
        return ((name == null) ? "" : name) + " [" + ((name == null) ? "" : name) + "]";
    }


    protected String getItemLabel(ProductReference productReference, String languageIso)
    {
        return getItemLabel(productReference);
    }


    protected String getIconPath(ProductReference item)
    {
        return null;
    }


    protected String getIconPath(ProductReference item, String languageIso)
    {
        return null;
    }


    protected String getItemDescription(ProductReference item)
    {
        return "";
    }


    protected String getItemDescription(ProductReference item, String languageIso)
    {
        return "";
    }
}
