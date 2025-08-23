package de.hybris.platform.warehousing.stock.services.impl;

import com.google.common.base.Preconditions;
import de.hybris.platform.basecommerce.enums.InStockStatus;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.stock.StockService;
import de.hybris.platform.stock.impl.StockLevelDao;
import de.hybris.platform.warehousing.atp.strategy.impl.WarehousingAvailabilityCalculationStrategy;
import de.hybris.platform.warehousing.enums.AsnStatus;
import de.hybris.platform.warehousing.stock.daos.WarehouseStockLevelDao;
import de.hybris.platform.warehousing.stock.services.WarehouseStockService;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

public class DefaultWarehouseStockService implements WarehouseStockService
{
    private StockService stockService;
    private StockLevelDao stockLevelDao;
    private WarehousingAvailabilityCalculationStrategy commerceStockLevelCalculationStrategy;
    private ModelService modelService;
    private WarehouseStockLevelDao warehouseStockLevelDao;


    public Long getStockLevelForProductCodeAndWarehouse(String productCode, WarehouseModel warehouse)
    {
        Preconditions.checkArgument((productCode != null), "productCode cannot be null.");
        Preconditions.checkArgument((warehouse != null), "Warehouse cannot be null.");
        Collection<StockLevelModel> stockLevels = getStockLevelDao().findStockLevels(productCode, Collections.singleton(warehouse));
        if(CollectionUtils.isNotEmpty(stockLevels))
        {
            return getCommerceStockLevelCalculationStrategy().calculateAvailability(stockLevels);
        }
        return Long.valueOf(0L);
    }


    public StockLevelModel createStockLevel(String productCode, WarehouseModel warehouse, int initialQuantityOnHand, InStockStatus status, Date releaseDate, String bin)
    {
        StockLevelModel stockLevel = (StockLevelModel)getModelService().create(StockLevelModel.class);
        stockLevel.setProductCode(productCode);
        stockLevel.setWarehouse(warehouse);
        stockLevel.setAvailable(initialQuantityOnHand);
        stockLevel.setInStockStatus(status);
        stockLevel.setReleaseDate(releaseDate);
        stockLevel.setBin(bin);
        getModelService().save(stockLevel);
        return stockLevel;
    }


    public StockLevelModel getUniqueStockLevel(String productCode, String warehouseCode, String binCode, Date releaseDate)
    {
        List<StockLevelModel> stockLevelResults = getWarehouseStockLevelDao().getStockLevels(productCode, warehouseCode, binCode, releaseDate);
        if(releaseDate == null && stockLevelResults.isEmpty())
        {
            stockLevelResults = getWarehouseStockLevelDao().getFutureStockLevels(productCode, warehouseCode, binCode);
            stockLevelResults = (List<StockLevelModel>)stockLevelResults.stream().filter(sl -> (sl.getAsnEntry() != null && AsnStatus.RECEIVED.equals(sl.getAsnEntry().getAsn().getStatus()))).collect(Collectors.toList());
        }
        Assert.notNull(stockLevelResults,
                        String.format("No StockLevel can be found for product code [%s] and warehouse [%s].", new Object[] {productCode, warehouseCode}));
        Assert.notEmpty(stockLevelResults,
                        String.format("No StockLevel can be found for product code [%s] and warehouse [%s].", new Object[] {productCode, warehouseCode}));
        Assert.isTrue((stockLevelResults.size() <= 1), String.format("More than one StockLevels have been found for product code [%s] and warehouse [%s]. You might want to be more specific and provide bin code and/or release date", new Object[] {productCode, warehouseCode}));
        return stockLevelResults.iterator().next();
    }


    protected StockService getStockService()
    {
        return this.stockService;
    }


    @Required
    public void setStockService(StockService stockService)
    {
        this.stockService = stockService;
    }


    protected WarehousingAvailabilityCalculationStrategy getCommerceStockLevelCalculationStrategy()
    {
        return this.commerceStockLevelCalculationStrategy;
    }


    @Required
    public void setCommerceStockLevelCalculationStrategy(WarehousingAvailabilityCalculationStrategy commerceStockLevelCalculationStrategy)
    {
        this.commerceStockLevelCalculationStrategy = commerceStockLevelCalculationStrategy;
    }


    protected StockLevelDao getStockLevelDao()
    {
        return this.stockLevelDao;
    }


    @Required
    public void setStockLevelDao(StockLevelDao stockLevelDao)
    {
        this.stockLevelDao = stockLevelDao;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected WarehouseStockLevelDao getWarehouseStockLevelDao()
    {
        return this.warehouseStockLevelDao;
    }


    @Required
    public void setWarehouseStockLevelDao(WarehouseStockLevelDao warehouseStockLevelDao)
    {
        this.warehouseStockLevelDao = warehouseStockLevelDao;
    }
}
