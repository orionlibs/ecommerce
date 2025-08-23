package de.hybris.platform.warehousing.util.builder;

import de.hybris.platform.basecommerce.enums.InStockStatus;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import java.util.Date;

public class StockLevelModelBuilder
{
    private final StockLevelModel model = new StockLevelModel();


    private StockLevelModel getModel()
    {
        return this.model;
    }


    public static StockLevelModelBuilder aModel()
    {
        return new StockLevelModelBuilder();
    }


    public StockLevelModel build()
    {
        return getModel();
    }


    public StockLevelModelBuilder withAvailable(int available)
    {
        getModel().setAvailable(available);
        return this;
    }


    public StockLevelModelBuilder withBin(String bin)
    {
        getModel().setBin(bin);
        return this;
    }


    public StockLevelModelBuilder withInStockStatus(InStockStatus inStockStatus)
    {
        getModel().setInStockStatus(inStockStatus);
        return this;
    }


    public StockLevelModelBuilder withMaxPreOrder(int maxPreOrder)
    {
        getModel().setMaxPreOrder(maxPreOrder);
        return this;
    }


    public StockLevelModelBuilder withWarehouse(WarehouseModel warehouse)
    {
        getModel().setWarehouse(warehouse);
        return this;
    }


    public StockLevelModelBuilder withMaxStockLevelHistoryCount(int maxStockLevelHistoryCount)
    {
        getModel().setMaxStockLevelHistoryCount(maxStockLevelHistoryCount);
        return this;
    }


    public StockLevelModelBuilder withOverselling(int overselling)
    {
        getModel().setOverSelling(overselling);
        return this;
    }


    public StockLevelModelBuilder withPreOrder(int preOrder)
    {
        getModel().setPreOrder(preOrder);
        return this;
    }


    public StockLevelModelBuilder withReserved(int reserved)
    {
        getModel().setReserved(reserved);
        return this;
    }


    public StockLevelModelBuilder withProduct(ProductModel product)
    {
        getModel().setProductCode(product.getCode());
        getModel().setProduct(product);
        return this;
    }


    public StockLevelModelBuilder withReleaseDate(Date releaseDate)
    {
        getModel().setReleaseDate(releaseDate);
        return this;
    }
}
