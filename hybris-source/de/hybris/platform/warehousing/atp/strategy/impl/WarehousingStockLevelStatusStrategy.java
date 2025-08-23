package de.hybris.platform.warehousing.atp.strategy.impl;

import de.hybris.platform.basecommerce.enums.InStockStatus;
import de.hybris.platform.basecommerce.enums.StockLevelStatus;
import de.hybris.platform.commerceservices.stock.strategies.impl.CommerceStockLevelStatusStrategy;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import java.util.Collection;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WarehousingStockLevelStatusStrategy extends CommerceStockLevelStatusStrategy
{
    private static final Logger LOGGER = LoggerFactory.getLogger(WarehousingStockLevelStatusStrategy.class);


    public StockLevelStatus checkStatus(Collection<StockLevelModel> stockLevels)
    {
        StockLevelStatus resultStatus = StockLevelStatus.OUTOFSTOCK;
        if(CollectionUtils.isEmpty(stockLevels))
        {
            LOGGER.debug("No stocklevel passed to check for their status. Returning OUTOFSTOCK as default.");
            return resultStatus;
        }
        boolean isInStockStatusLevel = stockLevels.stream().anyMatch(stockLevel -> InStockStatus.FORCEINSTOCK.equals(stockLevel.getInStockStatus()));
        if(isInStockStatusLevel)
        {
            resultStatus = StockLevelStatus.INSTOCK;
        }
        else
        {
            Collection<StockLevelModel> filteredStockLevels = (Collection<StockLevelModel>)stockLevels.stream().filter(stockLevel -> !InStockStatus.FORCEOUTOFSTOCK.equals(stockLevel.getInStockStatus())).collect(Collectors.toSet());
            Long availability = getCommerceStockLevelCalculationStrategy().calculateAvailability(filteredStockLevels);
            if(availability.longValue() <= 0L)
            {
                resultStatus = StockLevelStatus.OUTOFSTOCK;
            }
            else if(availability.longValue() > getDefaultLowStockThreshold())
            {
                resultStatus = StockLevelStatus.INSTOCK;
            }
            else
            {
                resultStatus = StockLevelStatus.LOWSTOCK;
            }
        }
        return resultStatus;
    }
}
