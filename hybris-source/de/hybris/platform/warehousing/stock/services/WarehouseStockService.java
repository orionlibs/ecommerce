package de.hybris.platform.warehousing.stock.services;

import de.hybris.platform.basecommerce.enums.InStockStatus;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import java.util.Date;

public interface WarehouseStockService
{
    Long getStockLevelForProductCodeAndWarehouse(String paramString, WarehouseModel paramWarehouseModel);


    StockLevelModel createStockLevel(String paramString1, WarehouseModel paramWarehouseModel, int paramInt, InStockStatus paramInStockStatus, Date paramDate, String paramString2);


    StockLevelModel getUniqueStockLevel(String paramString1, String paramString2, String paramString3, Date paramDate);
}
