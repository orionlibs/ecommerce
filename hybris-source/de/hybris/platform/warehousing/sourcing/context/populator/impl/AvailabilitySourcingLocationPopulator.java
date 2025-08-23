package de.hybris.platform.warehousing.sourcing.context.populator.impl;

import com.google.common.base.Preconditions;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.warehousing.data.sourcing.SourcingLocation;
import de.hybris.platform.warehousing.sourcing.context.populator.SourcingLocationPopulator;
import de.hybris.platform.warehousing.stock.services.impl.DefaultWarehouseStockService;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

public class AvailabilitySourcingLocationPopulator implements SourcingLocationPopulator
{
    private DefaultWarehouseStockService warehouseStockService;


    public void populate(WarehouseModel source, SourcingLocation target)
    {
        Preconditions.checkArgument((source != null), "Point of service model (source) cannot be null.");
        Preconditions.checkArgument((target != null), "Sourcing location (target) cannot be null.");
        Map<ProductModel, Long> availability = new HashMap<>();
        if(target.getContext() != null && target.getContext().getOrderEntries() != null)
        {
            target.getContext().getOrderEntries().forEach(entry -> setAvailability(source, entry, availability));
        }
        target.setAvailability(availability);
    }


    protected void setAvailability(WarehouseModel source, AbstractOrderEntryModel entry, Map<ProductModel, Long> availability)
    {
        ProductModel product = entry.getProduct();
        Assert.isTrue((product != null), "no product found for the order entry");
        Long stock = getWarehouseStockService().getStockLevelForProductCodeAndWarehouse(product.getCode(), source);
        if(stock == null)
        {
            stock = entry.getQuantity();
        }
        availability.put(product, stock);
    }


    protected DefaultWarehouseStockService getWarehouseStockService()
    {
        return this.warehouseStockService;
    }


    @Required
    public void setWarehouseStockService(DefaultWarehouseStockService warehouseStockService)
    {
        this.warehouseStockService = warehouseStockService;
    }
}
