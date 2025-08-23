package de.hybris.platform.stock.strategy.impl;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContextImpl;
import de.hybris.platform.servicelayer.model.ModelContextUtils;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.stock.strategy.StockLevelProductStrategy;

public class DefaultStockLevelProductStrategy implements StockLevelProductStrategy
{
    private String qualifier;
    private ProductService productService;


    public String convert(ProductModel model)
    {
        ServicesUtil.validateParameterNotNull(model, "Parameter 'model' was null.");
        ItemModelContextImpl internalContext = (ItemModelContextImpl)ModelContextUtils.getItemModelContext((AbstractItemModel)model);
        Object value = internalContext.getAttributeProvider().getAttribute(this.qualifier);
        return (value == null) ? null : value.toString();
    }


    public ProductModel convert(String productCode)
    {
        ServicesUtil.validateParameterNotNull(productCode, "Parameter 'productCode' was null.");
        return this.productService.getProductForCode(productCode);
    }


    public void setQualifier(String qualifier)
    {
        this.qualifier = qualifier;
    }
}
