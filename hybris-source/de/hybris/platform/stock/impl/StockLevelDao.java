package de.hybris.platform.stock.impl;

import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.servicelayer.internal.dao.Dao;
import java.util.Collection;

public interface StockLevelDao extends Dao
{
    StockLevelModel findStockLevel(String paramString, WarehouseModel paramWarehouseModel);


    Collection<StockLevelModel> findAllStockLevels(String paramString);


    Collection<StockLevelModel> findStockLevels(String paramString, Collection<WarehouseModel> paramCollection);


    Collection<StockLevelModel> findStockLevels(String paramString, Collection<WarehouseModel> paramCollection, int paramInt);


    Integer getAvailableQuantity(WarehouseModel paramWarehouseModel, String paramString);


    Integer reserve(StockLevelModel paramStockLevelModel, int paramInt);


    Integer release(StockLevelModel paramStockLevelModel, int paramInt);


    void updateActualAmount(StockLevelModel paramStockLevelModel, int paramInt);
}
