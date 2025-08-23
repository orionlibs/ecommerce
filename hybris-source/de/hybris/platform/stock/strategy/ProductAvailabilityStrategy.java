package de.hybris.platform.stock.strategy;

import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ProductAvailabilityStrategy
{
    Map<WarehouseModel, Integer> getAvailability(String paramString, List<WarehouseModel> paramList, Date paramDate);


    Map<WarehouseModel, Date> getAvailability(String paramString, List<WarehouseModel> paramList, int paramInt);


    String parse(Map<WarehouseModel, Integer> paramMap, String paramString, Date paramDate, LanguageModel paramLanguageModel);


    String parse(Map<WarehouseModel, Date> paramMap, String paramString, int paramInt, LanguageModel paramLanguageModel);


    WarehouseModel getBestMatchOfQuantity(Map<WarehouseModel, Integer> paramMap);


    WarehouseModel getBestMatchOfAvailability(Map<WarehouseModel, Date> paramMap);
}
