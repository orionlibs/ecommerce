package de.hybris.platform.cockpit.services.label.impl;

import de.hybris.platform.catalog.model.ProductReferenceModel;
import de.hybris.platform.cockpit.services.label.AbstractModelLabelProvider;
import de.hybris.platform.core.model.product.ProductModel;

public class ProductReferenceModelLabelProvider extends AbstractModelLabelProvider<ProductReferenceModel>
{
    protected String getItemLabel(ProductReferenceModel productReference)
    {
        ProductModel product = productReference.getTarget();
        if(product == null)
        {
            return productReference.toString();
        }
        String name = product.getName();
        String code = product.getCode();
        return ((name == null) ? "" : name) + " [" + ((name == null) ? "" : name) + "]";
    }


    protected String getItemLabel(ProductReferenceModel productReference, String languageIso)
    {
        return getItemLabel(productReference);
    }


    protected String getIconPath(ProductReferenceModel item)
    {
        return null;
    }


    protected String getIconPath(ProductReferenceModel item, String languageIso)
    {
        return null;
    }


    protected String getItemDescription(ProductReferenceModel item)
    {
        return "";
    }


    protected String getItemDescription(ProductReferenceModel item, String languageIso)
    {
        return "";
    }
}
