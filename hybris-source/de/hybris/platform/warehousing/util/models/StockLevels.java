package de.hybris.platform.warehousing.util.models;

import de.hybris.platform.basecommerce.enums.InStockStatus;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.stock.impl.StockLevelDao;
import de.hybris.platform.warehousing.util.builder.StockLevelModelBuilder;
import java.util.Collections;
import java.util.Date;

public class StockLevels extends AbstractItems<StockLevelModel>
{
    private StockLevelDao stockLevelDao;
    private Warehouses warehouses;
    private Products products;


    public StockLevelModel Camera(WarehouseModel warehouse, int quantity)
    {
        return (StockLevelModel)getFromCollectionOrSaveAndReturn(() -> getStockLevelDao().findStockLevels("camera", Collections.singletonList(warehouse)),
                        () -> StockLevelModelBuilder.aModel().withAvailable(quantity).withMaxPreOrder(0).withPreOrder(0).withMaxStockLevelHistoryCount(-1).withReserved(0).withWarehouse(warehouse).withProduct(getProducts().Camera()).withInStockStatus(InStockStatus.NOTSPECIFIED).build());
    }


    public StockLevelModel MemoryCard(WarehouseModel warehouse, int quantity)
    {
        return (StockLevelModel)getFromCollectionOrSaveAndReturn(() -> getStockLevelDao().findStockLevels("memorycard", Collections.singletonList(warehouse)),
                        () -> StockLevelModelBuilder.aModel().withAvailable(quantity).withMaxPreOrder(0).withPreOrder(0).withMaxStockLevelHistoryCount(-1).withReserved(0).withWarehouse(warehouse).withProduct(getProducts().MemoryCard()).withInStockStatus(InStockStatus.NOTSPECIFIED).build());
    }


    public StockLevelModel NewStockLevel(ProductModel product, WarehouseModel warehouse, int quantity, Date releaseDate)
    {
        StockLevelModel stockLevelModel = StockLevelModelBuilder.aModel().withAvailable(quantity).withMaxPreOrder(0).withPreOrder(0).withMaxStockLevelHistoryCount(-1).withReserved(0).withWarehouse(warehouse).withProduct(product).withInStockStatus(InStockStatus.NOTSPECIFIED)
                        .withReleaseDate(releaseDate).build();
        getModelService().save(stockLevelModel);
        return stockLevelModel;
    }


    public StockLevelModel Lens(WarehouseModel warehouse, int quantity)
    {
        return (StockLevelModel)getFromCollectionOrSaveAndReturn(() -> getStockLevelDao().findStockLevels("lens", Collections.singletonList(warehouse)),
                        () -> StockLevelModelBuilder.aModel().withAvailable(quantity).withMaxPreOrder(0).withPreOrder(0).withMaxStockLevelHistoryCount(-1).withReserved(0).withWarehouse(warehouse).withProduct(getProducts().Lens()).withInStockStatus(InStockStatus.NOTSPECIFIED).build());
    }


    public StockLevelDao getStockLevelDao()
    {
        return this.stockLevelDao;
    }


    public void setStockLevelDao(StockLevelDao stockLevelDao)
    {
        this.stockLevelDao = stockLevelDao;
    }


    public Warehouses getWarehouses()
    {
        return this.warehouses;
    }


    public void setWarehouses(Warehouses warehouses)
    {
        this.warehouses = warehouses;
    }


    public Products getProducts()
    {
        return this.products;
    }


    public void setProducts(Products products)
    {
        this.products = products;
    }
}
