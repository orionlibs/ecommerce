package de.hybris.platform.warehousing.externalfulfillment.dao.impl;

import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.warehousing.externalfulfillment.dao.WarehousingFulfillmentConfigDao;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultWarehousingFulfillmentConfigDao implements WarehousingFulfillmentConfigDao
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultWarehousingFulfillmentConfigDao.class);
    protected static final String GET_WAREHOUSES = "getWarehouses";
    private FlexibleSearchService flexibleSearchService;
    private List<String> warehouseFulfillmentProcessConfigs;


    public Object getConfiguration(WarehouseModel warehouse)
    {
        List configsList = new ArrayList();
        Object result = null;
        if(!CollectionUtils.isEmpty(getWarehouseFulfillmentProcessConfigs()))
        {
            getWarehouseFulfillmentProcessConfigs().forEach(warehouseFulfillmentConfig -> {
                String warehouseFulfillmentProcessConfigQuery = "SELECT {pk} FROM {" + warehouseFulfillmentConfig + "}";
                FlexibleSearchQuery fsQuery = new FlexibleSearchQuery(warehouseFulfillmentProcessConfigQuery);
                SearchResult searchResult = getFlexibleSearchService().search(fsQuery);
                if(searchResult != null && !CollectionUtils.isEmpty(searchResult.getResult()))
                {
                    searchResult.getResult().forEach(());
                }
            });
        }
        if(configsList.size() == 1)
        {
            result = configsList.iterator().next();
        }
        else if(configsList.size() > 1)
        {
            throw new AmbiguousIdentifierException("More than one config contains the warehouse: [" + warehouse.getCode() + "]");
        }
        return result;
    }


    protected void collectRelatedConfigs(WarehouseModel warehouse, List<Object> configsList, Object config)
    {
        try
        {
            Collection<WarehouseModel> warehouses = (Collection<WarehouseModel>)config.getClass().getMethod("getWarehouses", new Class[0]).invoke(config, new Object[0]);
            if(warehouses.contains(warehouse))
            {
                configsList.add(config);
            }
        }
        catch(NoSuchMethodException | IllegalAccessException | java.lang.reflect.InvocationTargetException e)
        {
            LOGGER.warn("No method 'getWarehouses()' to retrieve the warehouses linked to [{}].", config
                            .getClass().getSimpleName());
        }
    }


    protected FlexibleSearchService getFlexibleSearchService()
    {
        return this.flexibleSearchService;
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }


    protected List<String> getWarehouseFulfillmentProcessConfigs()
    {
        return this.warehouseFulfillmentProcessConfigs;
    }


    @Required
    public void setWarehouseFulfillmentProcessConfigs(List<String> warehouseFulfillmentProcessConfigs)
    {
        this.warehouseFulfillmentProcessConfigs = warehouseFulfillmentProcessConfigs;
    }
}
