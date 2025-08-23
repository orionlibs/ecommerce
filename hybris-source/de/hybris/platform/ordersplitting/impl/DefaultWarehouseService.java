package de.hybris.platform.ordersplitting.impl;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.ordersplitting.WarehouseService;
import de.hybris.platform.ordersplitting.daos.WarehouseDao;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;

public class DefaultWarehouseService implements WarehouseService
{
    private WarehouseDao warehouseDao;


    public List<WarehouseModel> getWarehouses(Collection<? extends AbstractOrderEntryModel> orderEntries)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("orderEntries", orderEntries);
        List<WarehouseModel> result = null;
        for(AbstractOrderEntryModel entry : orderEntries)
        {
            if(result == null)
            {
                result = new ArrayList<>(this.warehouseDao.getWarehouses(entry.getProduct().getCode()));
            }
            else
            {
                result.retainAll(this.warehouseDao.getWarehouses(entry.getProduct().getCode()));
            }
            if(result.isEmpty())
            {
                return getDefWarehouse();
            }
        }
        return result;
    }


    public List<WarehouseModel> getWarehousesWithProductsInStock(AbstractOrderEntryModel orderEntry)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("orderEntry", orderEntry);
        List<WarehouseModel> result = this.warehouseDao.getWarehousesWithProductsInStock(orderEntry.getProduct().getCode(), orderEntry
                        .getQuantity().longValue(), orderEntry.getChosenVendor());
        if(result.isEmpty())
        {
            return getDefWarehouse();
        }
        return result;
    }


    public List<WarehouseModel> getDefWarehouse()
    {
        return this.warehouseDao.getDefWarehouse();
    }


    public WarehouseModel getWarehouseForCode(String code)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("code", code);
        List<WarehouseModel> res = this.warehouseDao.getWarehouseForCode(code);
        ServicesUtil.validateIfSingleResult(res, WarehouseModel.class, "code", code);
        return res.get(0);
    }


    @Required
    public void setWarehouseDao(WarehouseDao warehouseDao)
    {
        this.warehouseDao = warehouseDao;
    }
}
