package de.hybris.platform.warehousing.allocation.decline.action.impl;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.warehousing.allocation.decline.action.DeclineActionStrategy;
import de.hybris.platform.warehousing.data.allocation.DeclineEntry;
import de.hybris.platform.warehousing.enums.AsnStatus;
import de.hybris.platform.warehousing.inventoryevent.service.InventoryEventService;
import de.hybris.platform.warehousing.model.WastageEventModel;
import de.hybris.platform.warehousing.stock.services.impl.DefaultWarehouseStockService;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultDamagedStockDeclineStrategy implements DeclineActionStrategy
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultDamagedStockDeclineStrategy.class);
    private InventoryEventService inventoryEventService;
    private DefaultWarehouseStockService warehouseStockService;


    public void execute(DeclineEntry declineEntry)
    {
        ServicesUtil.validateParameterNotNull(declineEntry, "Decline Entry cannot be null");
        ProductModel product = declineEntry.getConsignmentEntry().getOrderEntry().getProduct();
        WarehouseModel warehouse = declineEntry.getConsignmentEntry().getConsignment().getWarehouse();
        Long totalQuantity = getWarehouseStockService().getStockLevelForProductCodeAndWarehouse(product.getCode(), warehouse);
        totalQuantity = Long.valueOf((totalQuantity == null) ? 0L : totalQuantity.longValue());
        Long unReceivedQuantity = Long.valueOf(warehouse.getStockLevels().stream().filter(stockLevel ->
                                        (stockLevel.getAsnEntry() != null && stockLevel.getAsnEntry().getAsn() != null && AsnStatus.CREATED.equals(stockLevel.getAsnEntry().getAsn().getStatus())))
                        .mapToLong(stockLevel -> stockLevel.getAvailable())
                        .sum());
        Long quantityToWaste = Long.valueOf(totalQuantity.longValue() - unReceivedQuantity.longValue());
        if(quantityToWaste.longValue() > 0L)
        {
            WastageEventModel wastageEventModel = new WastageEventModel();
            wastageEventModel
                            .setStockLevel(getWarehouseStockService().getUniqueStockLevel(product.getCode(), warehouse.getCode(), null, null));
            wastageEventModel.setQuantity(quantityToWaste.longValue());
            getInventoryEventService().createWastageEvent(wastageEventModel);
            LOGGER.debug("Damaged Strategy is being invoked, Wastage event is being created for the amount: {}", quantityToWaste);
        }
    }


    public void execute(Collection<DeclineEntry> declineEntries)
    {
        ServicesUtil.validateIfAnyResult(declineEntries, "Nothing to decline");
        declineEntries.forEach(this::execute);
    }


    protected InventoryEventService getInventoryEventService()
    {
        return this.inventoryEventService;
    }


    @Required
    public void setInventoryEventService(InventoryEventService inventoryEventService)
    {
        this.inventoryEventService = inventoryEventService;
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
