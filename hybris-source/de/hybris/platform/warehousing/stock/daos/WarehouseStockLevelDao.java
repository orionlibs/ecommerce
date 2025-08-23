package de.hybris.platform.warehousing.stock.daos;

import de.hybris.platform.ordersplitting.model.StockLevelModel;
import java.util.Date;
import java.util.List;

public interface WarehouseStockLevelDao
{
    List<StockLevelModel> getStockLevels(String paramString1, String paramString2, String paramString3, Date paramDate);


    List<StockLevelModel> getFutureStockLevels(String paramString1, String paramString2, String paramString3);
}
