package de.hybris.platform.ordersplitting.impl;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.ordersplitting.ConsignmentCreationException;
import de.hybris.platform.ordersplitting.ConsignmentService;
import de.hybris.platform.ordersplitting.WarehouseService;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.servicelayer.model.ModelService;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;

public class DefaultConsignmentService implements ConsignmentService
{
    private static final Logger LOG = Logger.getLogger(DefaultConsignmentService.class.getName());
    private ModelService modelService;
    private WarehouseService warehouseService;
    private static final String RANDOM_ALGORITHM = "SHA1PRNG";


    public ConsignmentModel createConsignment(AbstractOrderModel order, String code, List<AbstractOrderEntryModel> orderEntries) throws ConsignmentCreationException
    {
        ConsignmentModel cons = (ConsignmentModel)this.modelService.create(ConsignmentModel.class);
        cons.setStatus(ConsignmentStatus.READY);
        cons.setConsignmentEntries(new HashSet());
        cons.setCode(code);
        if(order != null)
        {
            cons.setShippingAddress(order.getDeliveryAddress());
        }
        for(AbstractOrderEntryModel orderEntry : orderEntries)
        {
            ConsignmentEntryModel entry = (ConsignmentEntryModel)this.modelService.create(ConsignmentEntryModel.class);
            entry.setOrderEntry(orderEntry);
            entry.setQuantity(orderEntry.getQuantity());
            entry.setConsignment(cons);
            cons.getConsignmentEntries().add(entry);
            cons.setDeliveryMode(orderEntry.getDeliveryMode());
        }
        List<WarehouseModel> warehouses = this.warehouseService.getWarehouses(orderEntries);
        if(warehouses.isEmpty())
        {
            throw new ConsignmentCreationException("No default warehouse found for consignment");
        }
        WarehouseModel warehouse = warehouses.iterator().next();
        cons.setWarehouse(warehouse);
        cons.setOrder(order);
        return cons;
    }


    public WarehouseModel getWarehouse(List<AbstractOrderEntryModel> orderEntries)
    {
        try
        {
            SecureRandom sRnd = SecureRandom.getInstance("SHA1PRNG");
            Set<WarehouseModel> warehouses = ((AbstractOrderEntryModel)orderEntries.get(sRnd.nextInt(orderEntries.size()))).getChosenVendor().getWarehouses();
            WarehouseModel[] warehouse = warehouses.<WarehouseModel>toArray(new WarehouseModel[warehouses.size()]);
            return warehouse[sRnd.nextInt(warehouse.length)];
        }
        catch(NoSuchAlgorithmException ex)
        {
            LOG.error("Get warehouse failed!!", ex);
            return null;
        }
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public void setWarehouseService(WarehouseService warehouseService)
    {
        this.warehouseService = warehouseService;
    }
}
