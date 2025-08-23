package de.hybris.platform.warehousing.atp.strategy.impl;

import de.hybris.platform.basecommerce.enums.InStockStatus;
import de.hybris.platform.commerceservices.stock.strategies.impl.DefaultCommerceAvailabilityCalculationStrategy;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import de.hybris.platform.warehousing.atp.formula.services.AtpFormulaService;
import de.hybris.platform.warehousing.enums.AsnStatus;
import de.hybris.platform.warehousing.model.AtpFormulaModel;
import de.hybris.platform.warehousing.returns.service.RestockConfigService;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class WarehousingAvailabilityCalculationStrategy extends DefaultCommerceAvailabilityCalculationStrategy
{
    private AtpFormulaService atpFormulaService;
    private RestockConfigService restockConfigService;
    private BaseStoreService baseStoreService;
    private static final String STOCK_LEVELS = "stockLevels";
    private static final Logger LOGGER = LoggerFactory.getLogger(WarehousingAvailabilityCalculationStrategy.class);


    public Long calculateAvailability(Collection<StockLevelModel> stockLevels)
    {
        Long availability = Long.valueOf(0L);
        if(!stockLevels.isEmpty())
        {
            if(stockLevels.stream().anyMatch(stockLevel -> InStockStatus.FORCEINSTOCK.equals(stockLevel.getInStockStatus())))
            {
                return null;
            }
            AtpFormulaModel atpFormula = getDefaultAtpFormula(stockLevels);
            if(atpFormula != null)
            {
                Map<String, Object> params = filterStocks(stockLevels, atpFormula);
                availability = getAtpFormulaService().getAtpValueFromFormula(atpFormula, params);
            }
            else
            {
                LOGGER.debug("No AtpFormula found, The availability is set to 0 by default");
            }
        }
        return availability;
    }


    protected AtpFormulaModel getDefaultAtpFormula(Collection<StockLevelModel> stockLevels)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("stocklevels", stockLevels);
        BaseStoreModel currentBaseStore = getBaseStoreService().getCurrentBaseStore();
        if(currentBaseStore == null && !stockLevels.isEmpty())
        {
            WarehouseModel warehouse = ((StockLevelModel)stockLevels.iterator().next()).getWarehouse();
            Collection<BaseStoreModel> basestores = warehouse.getBaseStores();
            Collection<PointOfServiceModel> posList = warehouse.getPointsOfService();
            if(!CollectionUtils.isEmpty(basestores))
            {
                currentBaseStore = basestores.iterator().next();
            }
            else if(!CollectionUtils.isEmpty(posList))
            {
                currentBaseStore = ((PointOfServiceModel)posList.iterator().next()).getBaseStore();
            }
        }
        return (currentBaseStore != null) ? currentBaseStore.getDefaultAtpFormula() : null;
    }


    protected Map<String, Object> filterStocks(Collection<StockLevelModel> stockLevels, AtpFormulaModel atpFormula)
    {
        Collection<StockLevelModel> stockLevelsFiltered = filterStockLevels(stockLevels);
        if(atpFormula.getExternal() == null || !atpFormula.getExternal().booleanValue())
        {
            stockLevelsFiltered.removeAll(filterStockLevelsExternal(stockLevelsFiltered));
        }
        if(atpFormula.getReturned() == null || !atpFormula.getReturned().booleanValue())
        {
            stockLevelsFiltered.removeAll(filterStockLevelsReturned(stockLevelsFiltered));
        }
        Map<String, Object> params = new HashMap<>();
        params.put("stockLevels", stockLevelsFiltered);
        return params;
    }


    protected Collection<StockLevelModel> filterStockLevels(Collection<StockLevelModel> stockLevels)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("stocklevels", stockLevels);
        return (Collection<StockLevelModel>)stockLevels.stream().filter(level ->
                                        (!InStockStatus.FORCEOUTOFSTOCK.equals(level.getInStockStatus()) && (level.getAsnEntry() == null || !AsnStatus.CANCELLED.equals(level.getAsnEntry().getAsn().getStatus()))))
                        .collect(Collectors.toList());
    }


    protected Collection<StockLevelModel> filterStockLevelsReturned(Collection<StockLevelModel> stockLevels)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("stocklevels", stockLevels);
        String returnedBinCode = getRestockConfigService().getReturnedBinCode();
        return stockLevels.isEmpty() ?
                        Collections.<StockLevelModel>emptyList() :
                        (Collection<StockLevelModel>)stockLevels.stream().filter(level -> returnedBinCode.equals(level.getBin())).collect(Collectors.toList());
    }


    protected Collection<StockLevelModel> filterStockLevelsExternal(Collection<StockLevelModel> stockLevels)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("stocklevels", stockLevels);
        return stockLevels.isEmpty() ?
                        Collections.<StockLevelModel>emptyList() :
                        (Collection<StockLevelModel>)stockLevels.stream().filter(level -> level.getWarehouse().isExternal()).collect(Collectors.toList());
    }


    protected AtpFormulaService getAtpFormulaService()
    {
        return this.atpFormulaService;
    }


    @Required
    public void setAtpFormulaService(AtpFormulaService atpFormulaService)
    {
        this.atpFormulaService = atpFormulaService;
    }


    protected RestockConfigService getRestockConfigService()
    {
        return this.restockConfigService;
    }


    @Required
    public void setRestockConfigService(RestockConfigService restockConfigService)
    {
        this.restockConfigService = restockConfigService;
    }


    protected BaseStoreService getBaseStoreService()
    {
        return this.baseStoreService;
    }


    @Required
    public void setBaseStoreService(BaseStoreService baseStoreService)
    {
        this.baseStoreService = baseStoreService;
    }
}
