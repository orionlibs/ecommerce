package de.hybris.platform.cockpit.services.label.impl;

import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.cockpit.services.label.CatalogAwareLabelProvider;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.product.Product;

@Deprecated
public class ProductLabelProvider extends CatalogAwareLabelProvider<Product>
{
    protected String getItemLabel(Product product)
    {
        String name = product.getName();
        String code = product.getCode();
        return ((name == null) ? "" : name) + " [" + ((name == null) ? "" : name) + "]";
    }


    protected String getItemLabel(Product product, String languageIso)
    {
        return getItemLabel(product);
    }


    protected CatalogVersion getCatalogVersion(Product product)
    {
        return CatalogManager.getInstance().getCatalogVersion(product);
    }


    protected String getIconPath(Product item)
    {
        return getIconPath(item, null);
    }


    protected String getIconPath(Product item, String languageIso)
    {
        if(item.getThumbnail() != null)
        {
            return item.getThumbnail().getURL();
        }
        return null;
    }


    protected String getItemDescription(Product item)
    {
        return getItemDescription(item, null);
    }


    protected String getItemDescription(Product item, String languageIso)
    {
        if(languageIso == null)
        {
            return item.getDescription();
        }
        SessionContext ctx = JaloSession.getCurrentSession().createSessionContext();
        return item.getDescription(ctx);
    }
}
