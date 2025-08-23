package de.hybris.platform.futurestock;

import de.hybris.platform.core.model.product.ProductModel;
import java.util.Date;
import java.util.Map;

public interface FutureStockService
{
    public static final String BEAN_NAME = "futureStockService";


    Map<Date, Integer> getFutureAvailability(ProductModel paramProductModel);
}
