package de.hybris.platform.cockpit.services.label.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cockpit.services.label.CatalogAwareModelLabelProvider;
import de.hybris.platform.core.model.product.ProductModel;
import java.util.Locale;

public class ProductModelLabelProvider extends CatalogAwareModelLabelProvider<ProductModel>
{
    protected String getItemLabel(ProductModel product)
    {
        String name = product.getName();
        String code = product.getCode();
        return ((name == null) ? "" : name) + " [" + ((name == null) ? "" : name) + "]";
    }


    protected String getItemLabel(ProductModel product, String languageIso)
    {
        return getItemLabel(product);
    }


    protected CatalogVersionModel getCatalogVersionModel(ProductModel itemModel)
    {
        return itemModel.getCatalogVersion();
    }


    protected String getIconPath(ProductModel item)
    {
        return getIconPath(item, null);
    }


    protected String getIconPath(ProductModel item, String languageIso)
    {
        if(item.getThumbnail() != null)
        {
            return item.getThumbnail().getURL();
        }
        return null;
    }


    protected String getItemDescription(ProductModel item)
    {
        return getItemDescription(item, null);
    }


    protected String getItemDescription(ProductModel item, String languageIso)
    {
        if(languageIso == null)
        {
            return item.getDescription();
        }
        return item.getDescription(new Locale(languageIso));
    }
}
