package de.hybris.platform.configurablebundlecockpits.services.label.impl;

import de.hybris.platform.cockpit.services.label.AbstractModelLabelProvider;
import de.hybris.platform.configurablebundleservices.model.AbstractBundleRuleModel;
import de.hybris.platform.core.model.product.ProductModel;
import java.util.Collection;
import java.util.List;

public abstract class AbstractBundleRuleLabelProvider<R extends AbstractBundleRuleModel> extends AbstractModelLabelProvider<R>
{
    protected String getIconPath(R bundleRule)
    {
        return null;
    }


    protected String getIconPath(R bundleRule, String languageIso)
    {
        return null;
    }


    protected String getItemDescription(R bundleRule)
    {
        return null;
    }


    protected String getItemDescription(R bundleRule, String languageIso)
    {
        return null;
    }


    protected String getProductNames(Collection<ProductModel> associatedProducts)
    {
        StringBuilder productsBuffer = new StringBuilder();
        if(!((List)associatedProducts).isEmpty())
        {
            for(ProductModel curProduct : associatedProducts)
            {
                if(productsBuffer.length() == 0)
                {
                    productsBuffer.append("'" + curProduct.getName() + "'");
                    continue;
                }
                productsBuffer.append(", '" + curProduct.getName() + "'");
            }
        }
        return productsBuffer.toString();
    }


    protected String getItemLabel(R bundleRule, String languageIso)
    {
        return getItemLabel(bundleRule);
    }
}
