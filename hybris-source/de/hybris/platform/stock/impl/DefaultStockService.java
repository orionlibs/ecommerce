package de.hybris.platform.stock.impl;

import de.hybris.platform.basecommerce.enums.InStockStatus;
import de.hybris.platform.basecommerce.enums.StockLevelStatus;
import de.hybris.platform.basecommerce.enums.StockLevelUpdateType;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.stock.StockService;
import de.hybris.platform.stock.exception.InsufficientStockLevelException;
import de.hybris.platform.stock.exception.StockLevelNotFoundException;
import de.hybris.platform.stock.model.StockLevelHistoryEntryModel;
import de.hybris.platform.stock.strategy.ProductAvailabilityStrategy;
import de.hybris.platform.stock.strategy.StockLevelProductStrategy;
import de.hybris.platform.stock.strategy.StockLevelStatusStrategy;
import de.hybris.platform.util.Utilities;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class DefaultStockService implements StockService
{
    private static final Logger LOG = Logger.getLogger(DefaultStockService.class.getName());
    private static final String NO_STOCKLEVEL_FOR_PRODUCT_TEMPLATE = "no stock level for product [%s] found.";
    private ModelService modelService;
    private StockLevelDao stockLevelDao;
    private StockLevelStatusStrategy stockLevelStatusStrategy;
    private ProductAvailabilityStrategy productAvailabilityStrategy;
    private StockLevelProductStrategy stockLevelProductStrategy;


    public StockLevelStatus getProductStatus(ProductModel product, WarehouseModel warehouse)
    {
        StockLevelModel stockLevel = getStockLevelDao().findStockLevel(getStockLevelProductStrategy().convert(product), warehouse);
        return getStockLevelStatusStrategy().checkStatus(stockLevel);
    }


    public StockLevelStatus getProductStatus(ProductModel product, Collection<WarehouseModel> warehouses)
    {
        List<StockLevelModel> stockLevels = new ArrayList<>(getStockLevelDao().findStockLevels(getStockLevelProductStrategy().convert(product), warehouses));
        return getStockLevelStatusStrategy().checkStatus(stockLevels);
    }


    protected StockLevelModel createStockLevel(ProductModel product, WarehouseModel warehouse, int available)
    {
        return createStockLevel(product, warehouse, available, 0, 0, InStockStatus.NOTSPECIFIED, 0, true);
    }


    protected StockLevelModel createStockLevel(ProductModel product, WarehouseModel warehouse, int available, int overSelling, int reserved, InStockStatus status, int maxStockLevelHistoryCount, boolean treatNegativeAsZero)
    {
        if(available < 0)
        {
            throw new IllegalArgumentException("available amount cannot be negative.");
        }
        if(overSelling < 0)
        {
            throw new IllegalArgumentException("overSelling amount cannot be negative.");
        }
        StockLevelModel stockLevel = getStockLevelDao().findStockLevel(getStockLevelProductStrategy().convert(product), warehouse);
        if(stockLevel != null)
        {
            throw new JaloSystemException("product [" + product + "] in warehouse [" + warehouse.getName() + "] already exists. the same product cannot be created in the same warehouse again.");
        }
        stockLevel = (StockLevelModel)getModelService().create(StockLevelModel.class);
        stockLevel.setProductCode(getStockLevelProductStrategy().convert(product));
        stockLevel.setWarehouse(warehouse);
        stockLevel.setAvailable(available);
        stockLevel.setOverSelling(overSelling);
        stockLevel.setReserved(reserved);
        stockLevel.setInStockStatus(status);
        stockLevel.setMaxStockLevelHistoryCount(maxStockLevelHistoryCount);
        stockLevel.setTreatNegativeAsZero(treatNegativeAsZero);
        if(maxStockLevelHistoryCount != 0)
        {
            List<StockLevelHistoryEntryModel> historyEntries = new ArrayList<>();
            StockLevelHistoryEntryModel entry = createStockLevelHistoryEntry(stockLevel, available, 0, StockLevelUpdateType.WAREHOUSE, "new in stock");
            historyEntries.add(entry);
            stockLevel.setStockLevelHistoryEntries(historyEntries);
        }
        getModelService().save(stockLevel);
        return stockLevel;
    }


    private StockLevelHistoryEntryModel createStockLevelHistoryEntry(StockLevelModel stockLevel, StockLevelUpdateType updateType, int reserved, String comment)
    {
        if(stockLevel.getMaxStockLevelHistoryCount() != 0)
        {
            StockLevelHistoryEntryModel historyEntry = (StockLevelHistoryEntryModel)getModelService().create(StockLevelHistoryEntryModel.class);
            historyEntry.setStockLevel(stockLevel);
            historyEntry.setActual(stockLevel.getAvailable());
            historyEntry.setReserved(reserved);
            historyEntry.setUpdateType(updateType);
            if(comment != null)
            {
                historyEntry.setComment(comment);
            }
            historyEntry.setUpdateDate(new Date());
            getModelService().save(historyEntry);
            return historyEntry;
        }
        return null;
    }


    protected StockLevelHistoryEntryModel createStockLevelHistoryEntry(StockLevelModel stockLevel, int actual, int reserved, StockLevelUpdateType updateType, String comment)
    {
        if(stockLevel == null)
        {
            throw new IllegalArgumentException("stock level cannot be null.");
        }
        if(actual < 0)
        {
            throw new IllegalArgumentException("actual amount cannot be negative.");
        }
        if(stockLevel.getMaxStockLevelHistoryCount() != 0)
        {
            StockLevelHistoryEntryModel historyEntry = (StockLevelHistoryEntryModel)getModelService().create(StockLevelHistoryEntryModel.class);
            historyEntry.setStockLevel(stockLevel);
            historyEntry.setActual(actual);
            historyEntry.setReserved(reserved);
            historyEntry.setUpdateType(updateType);
            if(comment != null)
            {
                historyEntry.setComment(comment);
            }
            historyEntry.setUpdateDate(new Date());
            getModelService().save(historyEntry);
            return historyEntry;
        }
        return null;
    }


    public int getStockLevelAmount(ProductModel product, WarehouseModel warehouse)
    {
        StockLevelModel stockLevel = checkAndGetStockLevel(product, warehouse);
        return stockLevel.getAvailable() - stockLevel.getReserved();
    }


    public int getTotalStockLevelAmount(ProductModel product)
    {
        List<StockLevelModel> stockLevels = new ArrayList<>(getStockLevelDao().findAllStockLevels(getStockLevelProductStrategy().convert(product)));
        if(stockLevels.isEmpty())
        {
            throw new StockLevelNotFoundException(String.format("no stock level for product [%s] found.", new Object[] {product.toString()}));
        }
        return calculateTotalActualAmount(stockLevels);
    }


    public int getTotalStockLevelAmount(ProductModel product, Collection<WarehouseModel> warehouses)
    {
        List<StockLevelModel> stockLevels = new ArrayList<>(getStockLevelDao().findStockLevels(getStockLevelProductStrategy().convert(product), warehouses));
        if(stockLevels.isEmpty())
        {
            throw new StockLevelNotFoundException(String.format("no stock level for product [%s] found.", new Object[] {product.toString()}));
        }
        return calculateTotalActualAmount(stockLevels);
    }


    private int calculateTotalActualAmount(List<StockLevelModel> stockLevels)
    {
        int totalActualAmount = 0;
        for(StockLevelModel stockLevel : stockLevels)
        {
            int actualAmount = stockLevel.getAvailable() - stockLevel.getReserved();
            if(actualAmount > 0 || !stockLevel.isTreatNegativeAsZero())
            {
                totalActualAmount += actualAmount;
            }
        }
        return totalActualAmount;
    }


    public void setInStockStatus(ProductModel product, Collection<WarehouseModel> warehouses, InStockStatus status)
    {
        Collection<StockLevelModel> stockLevels = new ArrayList<>(getStockLevelDao().findStockLevels(getStockLevelProductStrategy().convert(product), warehouses));
        if(!stockLevels.isEmpty())
        {
            for(StockLevelModel level : stockLevels)
            {
                level.setInStockStatus(status);
            }
            getModelService().saveAll(stockLevels);
        }
    }


    public InStockStatus getInStockStatus(ProductModel product, WarehouseModel warehouse)
    {
        StockLevelModel stockLevel = checkAndGetStockLevel(product, warehouse);
        return stockLevel.getInStockStatus();
    }


    private void clearCacheForItem(StockLevelModel stockLevel)
    {
        Utilities.invalidateCache(stockLevel.getPk());
        getModelService().refresh(stockLevel);
    }


    public void reserve(ProductModel product, WarehouseModel warehouse, int amount, String comment) throws InsufficientStockLevelException
    {
        if(amount <= 0)
        {
            throw new IllegalArgumentException("amount must be greater than zero.");
        }
        StockLevelModel currentStockLevel = checkAndGetStockLevel(product, warehouse);
        Integer reserved = getStockLevelDao().reserve(currentStockLevel, amount);
        if(reserved == null)
        {
            throw new InsufficientStockLevelException("insufficient available amount for stock level [" + currentStockLevel
                            .getPk() + "]");
        }
        clearCacheForItem(currentStockLevel);
        createStockLevelHistoryEntry(currentStockLevel, StockLevelUpdateType.CUSTOMER_RESERVE, reserved.intValue(), comment);
    }


    public void release(ProductModel product, WarehouseModel warehouse, int amount, String comment)
    {
        if(amount <= 0)
        {
            throw new IllegalArgumentException("amount must be greater than zero.");
        }
        StockLevelModel currentStockLevel = checkAndGetStockLevel(product, warehouse);
        Integer reserved = getStockLevelDao().release(currentStockLevel, amount);
        if(reserved == null)
        {
            throw new SystemException("release failed for stock level [" + currentStockLevel.getPk() + "]!");
        }
        clearCacheForItem(currentStockLevel);
        createStockLevelHistoryEntry(currentStockLevel, StockLevelUpdateType.CUSTOMER_RELEASE, reserved.intValue(), comment);
    }


    public void updateActualStockLevel(ProductModel product, WarehouseModel warehouse, int actualAmount, String comment)
    {
        StockLevelModel stockLevel;
        try
        {
            stockLevel = checkAndGetStockLevel(product, warehouse);
        }
        catch(StockLevelNotFoundException e)
        {
            createStockLevel(product, warehouse, actualAmount);
            return;
        }
        try
        {
            int amount;
            if(actualAmount < 0)
            {
                amount = 0;
                LOG.warn("actual amount is negative, changing amount to 0");
            }
            else
            {
                amount = actualAmount;
            }
            getStockLevelDao().updateActualAmount(stockLevel, amount);
            clearCacheForItem(stockLevel);
            createStockLevelHistoryEntry(stockLevel, StockLevelUpdateType.WAREHOUSE, 0, comment);
        }
        catch(Exception e)
        {
            LOG.error("update not successful: " + e.getMessage());
            throw new SystemException(e);
        }
    }


    public Map<WarehouseModel, Integer> getAvailability(List<WarehouseModel> warehouses, ProductModel product, Date date)
    {
        return getProductAvailabilityStrategy().getAvailability(getStockLevelProductStrategy().convert(product), warehouses, date);
    }


    public Map<WarehouseModel, Date> getAvailability(List<WarehouseModel> warehouses, ProductModel product, int quantity)
    {
        return getProductAvailabilityStrategy().getAvailability(getStockLevelProductStrategy().convert(product), warehouses, quantity);
    }


    public String getAvailability(ProductModel product, WarehouseModel warehouse, Date date, LanguageModel language)
    {
        List<WarehouseModel> warehouses = new ArrayList<>();
        warehouses.add(warehouse);
        return getAvailability(product, warehouses, date, language);
    }


    public String getAvailability(ProductModel product, List<WarehouseModel> warehouses, Date date, LanguageModel language)
    {
        Map<WarehouseModel, Integer> mappedAvalabilities = getProductAvailabilityStrategy().getAvailability(getStockLevelProductStrategy().convert(product), warehouses, date);
        return getProductAvailabilityStrategy().parse(mappedAvalabilities, getStockLevelProductStrategy().convert(product), date, language);
    }


    public String getAvailability(ProductModel product, WarehouseModel warehouse, int quantity, LanguageModel language)
    {
        List<WarehouseModel> warehouses = new ArrayList<>();
        warehouses.add(warehouse);
        return getAvailability(product, warehouses, quantity, language);
    }


    public String getAvailability(ProductModel product, List<WarehouseModel> warehouses, int quantity, LanguageModel language)
    {
        Map<WarehouseModel, Date> mappedAvalabilities = getProductAvailabilityStrategy().getAvailability(getStockLevelProductStrategy().convert(product), warehouses, quantity);
        return getProductAvailabilityStrategy().parse(mappedAvalabilities, getStockLevelProductStrategy().convert(product), quantity, language);
    }


    public WarehouseModel getBestMatchOfQuantity(Map<WarehouseModel, Integer> map)
    {
        return getProductAvailabilityStrategy().getBestMatchOfQuantity(map);
    }


    public WarehouseModel getBestMatchOfAvailability(Map<WarehouseModel, Date> map)
    {
        return getProductAvailabilityStrategy().getBestMatchOfAvailability(map);
    }


    public StockLevelModel getStockLevel(ProductModel product, WarehouseModel warehouse)
    {
        return getStockLevelDao().findStockLevel(getStockLevelProductStrategy().convert(product), warehouse);
    }


    public Collection<StockLevelModel> getAllStockLevels(ProductModel product)
    {
        return getStockLevelDao().findAllStockLevels(getStockLevelProductStrategy().convert(product));
    }


    public Collection<StockLevelModel> getStockLevels(ProductModel product, Collection<WarehouseModel> warehouses)
    {
        return getStockLevelDao().findStockLevels(getStockLevelProductStrategy().convert(product), warehouses);
    }


    private StockLevelModel checkAndGetStockLevel(ProductModel product, WarehouseModel warehouse)
    {
        StockLevelModel stockLevel = getStockLevelDao().findStockLevel(getStockLevelProductStrategy().convert(product), warehouse);
        if(stockLevel == null)
        {
            throw new StockLevelNotFoundException(
                            String.format("no stock level for product [%s] found.", new Object[] {product.toString()}) + " in warehouse [" + String.format("no stock level for product [%s] found.", new Object[] {product.toString()}));
        }
        return stockLevel;
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


    protected StockLevelDao getStockLevelDao()
    {
        return this.stockLevelDao;
    }


    @Required
    public void setStockLevelDao(StockLevelDao stockLevelDao)
    {
        this.stockLevelDao = stockLevelDao;
    }


    protected StockLevelStatusStrategy getStockLevelStatusStrategy()
    {
        return this.stockLevelStatusStrategy;
    }


    @Required
    public void setStockLevelStatusStrategy(StockLevelStatusStrategy stockLevelStatusStrategy)
    {
        this.stockLevelStatusStrategy = stockLevelStatusStrategy;
    }


    protected ProductAvailabilityStrategy getProductAvailabilityStrategy()
    {
        return this.productAvailabilityStrategy;
    }


    @Required
    public void setProductAvailabilityStrategy(ProductAvailabilityStrategy productAvailabilityStrategy)
    {
        this.productAvailabilityStrategy = productAvailabilityStrategy;
    }


    protected StockLevelProductStrategy getStockLevelProductStrategy()
    {
        return this.stockLevelProductStrategy;
    }


    @Required
    public void setStockLevelProductStrategy(StockLevelProductStrategy stockLevelProductStrategy)
    {
        this.stockLevelProductStrategy = stockLevelProductStrategy;
    }
}
