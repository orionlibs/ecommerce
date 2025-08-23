package de.hybris.platform.stock.strategy.impl;

import de.hybris.platform.basecommerce.messages.ResourceBundleProvider;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.stock.impl.StockLevelDao;
import de.hybris.platform.stock.strategy.BestMatchStrategy;
import de.hybris.platform.stock.strategy.ProductAvailabilityStrategy;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import org.springframework.beans.factory.annotation.Required;

public class DefaultProductAvailabilityStrategy implements ProductAvailabilityStrategy
{
    private I18NService i18nService;
    private ResourceBundleProvider bundleProvider;
    private BestMatchStrategy bestMatchStrategy;
    private StockLevelDao stockLevelDao;


    public String parse(Map<WarehouseModel, Integer> quantities, String productCode, Date date, LanguageModel language)
    {
        StringBuilder parsedResult = new StringBuilder();
        int total = 0;
        for(Iterator<Map.Entry<WarehouseModel, Integer>> it = quantities.entrySet().iterator(); it.hasNext(); )
        {
            Map.Entry<WarehouseModel, Integer> entry = it.next();
            Map<String, Object> map = new HashMap<>();
            map.put("date", date);
            map.put("product", productCode);
            map.put("warehouse", ((WarehouseModel)entry.getKey()).getCode());
            map.put("availability", entry.getValue());
            total += ((Integer)entry.getValue()).intValue();
            parsedResult.append(getLocalizedString("de.hybris.platform.validation.services.impl.DefaultProductAvailabilityStrategy.availability",
                            (language != null) ? getLocale(language.getIsocode()) : this.i18nService.getCurrentLocale(), map));
            parsedResult.append('\n');
        }
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("total", Integer.valueOf(total));
        parsedResult.append(getLocalizedString("de.hybris.platform.validation.services.impl.DefaultProductAvailabilityStrategy.total",
                        (language != null) ? getLocale(language.getIsocode()) : this.i18nService.getCurrentLocale(), attributes));
        return parsedResult.toString();
    }


    private Locale getLocale(String isoCode)
    {
        String[] splitCode = isoCode.split("_");
        if(splitCode.length == 2)
        {
            return new Locale(splitCode[0], splitCode[1]);
        }
        return new Locale(isoCode);
    }


    public String parse(Map<WarehouseModel, Date> quantities, String productCode, int quantity, LanguageModel language)
    {
        StringBuilder parsedResult = new StringBuilder();
        int total = 0;
        for(Iterator<Map.Entry<WarehouseModel, Date>> it = quantities.entrySet().iterator(); it.hasNext(); )
        {
            Map.Entry<WarehouseModel, Date> entry = it.next();
            Map<String, Object> map = new HashMap<>();
            map.put("availability", Integer.valueOf(quantity));
            map.put("product", productCode);
            map.put("warehouse", ((WarehouseModel)entry.getKey()).getCode());
            map.put("date", entry.getValue());
            total += quantity;
            parsedResult.append(getLocalizedString("de.hybris.platform.validation.services.impl.DefaultProductAvailabilityStrategy.availability",
                            (language != null) ? getLocale(language.getIsocode()) : this.i18nService.getCurrentLocale(), map));
            parsedResult.append('\n');
        }
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("total", Integer.valueOf(total));
        parsedResult.append(getLocalizedString("de.hybris.platform.validation.services.impl.DefaultProductAvailabilityStrategy.total",
                        (language != null) ? getLocale(language.getIsocode()) : this.i18nService.getCurrentLocale(), attributes));
        return parsedResult.toString();
    }


    public Map<WarehouseModel, Integer> getAvailability(String productCode, List<WarehouseModel> warehouses, Date date)
    {
        Map<WarehouseModel, Integer> results = new HashMap<>();
        for(WarehouseModel warehouse : warehouses)
        {
            Integer quantity = this.stockLevelDao.getAvailableQuantity(warehouse, productCode);
            results.put(warehouse, quantity);
        }
        return Collections.unmodifiableMap(results);
    }


    public Map<WarehouseModel, Date> getAvailability(String productCode, List<WarehouseModel> warehouses, int preOrderQuantity)
    {
        Map<WarehouseModel, Date> results = new HashMap<>();
        Collection<StockLevelModel> stockLevels = this.stockLevelDao.findStockLevels(productCode, warehouses, preOrderQuantity);
        for(StockLevelModel stockLevel : stockLevels)
        {
            results.put(stockLevel.getWarehouse(), stockLevel.getNextDeliveryTime());
        }
        return Collections.unmodifiableMap(results);
    }


    private String getLocalizedString(String key, Locale locale, Map<String, Object> placeholders)
    {
        ResourceBundle bundle = this.bundleProvider.getResourceBundle(locale);
        String result = bundle.getString(key);
        if(placeholders != null)
        {
            result = replacePlaceholders(result, placeholders);
        }
        return result;
    }


    public WarehouseModel getBestMatchOfQuantity(Map<WarehouseModel, Integer> map)
    {
        return this.bestMatchStrategy.getBestMatchOfQuantity(map);
    }


    public WarehouseModel getBestMatchOfAvailability(Map<WarehouseModel, Date> map)
    {
        return this.bestMatchStrategy.getBestMatchOfAvailability(map);
    }


    private String replacePlaceholders(String value, Map<String, Object> placeholders)
    {
        String result = value;
        for(Map.Entry<String, Object> entry : placeholders.entrySet())
        {
            String varKey = entry.getKey();
            Object varValue = (entry.getValue() == null) ? "" : entry.getValue();
            result = result.replaceAll("\\{" + varKey + "\\}", varValue.toString());
        }
        return result;
    }


    @Required
    public void setI18nService(I18NService i18nService)
    {
        this.i18nService = i18nService;
    }


    @Required
    public void setBundleProvider(ResourceBundleProvider bundleProvider)
    {
        this.bundleProvider = bundleProvider;
    }


    @Required
    public void setBestMatchStrategy(BestMatchStrategy bestMatchStrategy)
    {
        this.bestMatchStrategy = bestMatchStrategy;
    }


    @Required
    public void setStockLevelDao(StockLevelDao stockLevelDao)
    {
        this.stockLevelDao = stockLevelDao;
    }
}
