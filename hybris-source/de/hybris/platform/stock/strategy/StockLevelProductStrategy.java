package de.hybris.platform.stock.strategy;

import de.hybris.platform.core.model.product.ProductModel;

public interface StockLevelProductStrategy
{
    String convert(ProductModel paramProductModel);


    ProductModel convert(String paramString);
}
