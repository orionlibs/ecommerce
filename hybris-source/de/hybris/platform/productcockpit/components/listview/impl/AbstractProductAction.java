package de.hybris.platform.productcockpit.components.listview.impl;

import de.hybris.platform.cockpit.components.listview.AbstractListViewAction;
import de.hybris.platform.productcockpit.services.product.ProductService;
import org.zkoss.spring.SpringUtil;

public abstract class AbstractProductAction extends AbstractListViewAction
{
    private ProductService productCockpitProductService = null;


    public ProductService getProductCockpitProductService()
    {
        if(this.productCockpitProductService == null)
        {
            this.productCockpitProductService = (ProductService)SpringUtil.getBean("productCockpitProductService");
        }
        return this.productCockpitProductService;
    }
}
