package de.hybris.platform.warehousing.atp.dao;

import java.util.Map;

public interface AvailableToPromiseDao
{
    Long getAvailabilityForStockLevels(Map<String, Object> paramMap);


    Long getAllocationQuantityForStockLevels(Map<String, Object> paramMap);


    Long getCancellationQuantityForStockLevels(Map<String, Object> paramMap);


    Long getReservedQuantityForStockLevels(Map<String, Object> paramMap);


    Long getShrinkageQuantityForStockLevels(Map<String, Object> paramMap);


    Long getWastageQuantityForStockLevels(Map<String, Object> paramMap);


    Long getIncreaseQuantityForStockLevels(Map<String, Object> paramMap);
}
