package de.hybris.platform.ordersplitting.strategy.impl;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.ordersplitting.WarehouseService;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.ordersplitting.strategy.SplittingStrategy;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class SplitByWarehouse implements SplittingStrategy
{
    private static final Logger LOG = Logger.getLogger(SplitByWarehouse.class.getName());
    private WarehouseService warehouseService;
    private static final String WAREHOUSE_LIST_NAME = "WAREHOUSE_LIST";
    private static final String RANDOM_ALGORITHM = "SHA1PRNG";


    protected WarehouseModel getWarehouse(OrderEntryGroup orderEntries)
    {
        return chooseBestWarehouse(orderEntries);
    }


    protected List<OrderEntryGroup> splitForWarehouses(OrderEntryGroup orderEntryList)
    {
        List<OrderEntryGroup> result = new ArrayList<>();
        OrderEntryGroup todoEntryList = orderEntryList.getEmpty();
        OrderEntryGroup workingOrderEntryList = sortOrderEntryBeforeWarehouseSplitting(orderEntryList);
        OrderEntryGroup emptyOrderEntryList = orderEntryList.getEmpty();
        do
        {
            todoEntryList.clear();
            List<WarehouseModel> tmpWarehouseResult = null;
            OrderEntryGroup tmpOrderEntryResult = orderEntryList.getEmpty();
            for(AbstractOrderEntryModel orderEntry : workingOrderEntryList)
            {
                List<WarehouseModel> currentPossibleWarehouses = getPossibleWarehouses(orderEntry);
                if(currentPossibleWarehouses.isEmpty())
                {
                    emptyOrderEntryList.add(orderEntry);
                    continue;
                }
                if(tmpWarehouseResult != null)
                {
                    currentPossibleWarehouses.retainAll(tmpWarehouseResult);
                }
                if(currentPossibleWarehouses.isEmpty())
                {
                    todoEntryList.add(orderEntry);
                    continue;
                }
                tmpWarehouseResult = currentPossibleWarehouses;
                tmpOrderEntryResult.add(orderEntry);
            }
            if(!tmpOrderEntryResult.isEmpty())
            {
                tmpOrderEntryResult.setParameter("WAREHOUSE_LIST", tmpWarehouseResult);
                result.add(tmpOrderEntryResult);
            }
            workingOrderEntryList = todoEntryList.getEmpty();
            workingOrderEntryList.addAll((Collection)todoEntryList);
        }
        while(!todoEntryList.isEmpty());
        if(!emptyOrderEntryList.isEmpty())
        {
            result.add(emptyOrderEntryList);
        }
        return result;
    }


    protected List<WarehouseModel> getPossibleWarehouses(AbstractOrderEntryModel orderEntry)
    {
        return new ArrayList<>(this.warehouseService.getWarehousesWithProductsInStock(orderEntry));
    }


    protected WarehouseModel chooseBestWarehouse(OrderEntryGroup orderEntries)
    {
        try
        {
            List<WarehouseModel> warehouses = (List<WarehouseModel>)orderEntries.getParameter("WAREHOUSE_LIST");
            if(warehouses == null || warehouses.isEmpty())
            {
                return null;
            }
            SecureRandom sRnd = SecureRandom.getInstance("SHA1PRNG");
            return warehouses.get(sRnd.nextInt(warehouses.size()));
        }
        catch(NoSuchAlgorithmException ex)
        {
            LOG.error("Choose best warehouse failed!!", ex);
            return null;
        }
    }


    protected OrderEntryGroup sortOrderEntryBeforeWarehouseSplitting(OrderEntryGroup listOrderEntry)
    {
        return listOrderEntry;
    }


    public List<OrderEntryGroup> perform(List<OrderEntryGroup> orderEntryGroup)
    {
        List<OrderEntryGroup> result = new ArrayList<>();
        for(OrderEntryGroup orderEntry : orderEntryGroup)
        {
            List<OrderEntryGroup> tmpList = splitForWarehouses(orderEntry);
            for(OrderEntryGroup tmpOrderEntryGroup : tmpList)
            {
                result.add(tmpOrderEntryGroup);
            }
        }
        return result;
    }


    public void afterSplitting(OrderEntryGroup group, ConsignmentModel createdOne)
    {
        createdOne.setWarehouse(chooseBestWarehouse(group));
    }


    @Required
    public void setWarehouseService(WarehouseService warehouseService)
    {
        this.warehouseService = warehouseService;
    }


    protected WarehouseService getWarehouseService()
    {
        return this.warehouseService;
    }
}
