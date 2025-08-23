package de.hybris.platform.warehousing.util.builder;

import com.google.common.collect.Sets;
import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;

public class ConsignmentModelBuilder
{
    private final ConsignmentModel model = new ConsignmentModel();


    private ConsignmentModel getModel()
    {
        return this.model;
    }


    public static ConsignmentModelBuilder aModel()
    {
        return new ConsignmentModelBuilder();
    }


    public ConsignmentModel build()
    {
        return getModel();
    }


    public ConsignmentModelBuilder withCode(String code)
    {
        getModel().setCode(code);
        return this;
    }


    public ConsignmentModelBuilder withWorkflowCode(String code)
    {
        getModel().setTaskAssignmentWorkflow(code);
        return this;
    }


    public ConsignmentModelBuilder withDeliveryMode(DeliveryModeModel deliveryMode)
    {
        getModel().setDeliveryMode(deliveryMode);
        return this;
    }


    public ConsignmentModelBuilder withShippingAddress(AddressModel address)
    {
        getModel().setShippingAddress(address);
        return this;
    }


    public ConsignmentModelBuilder withStatus(ConsignmentStatus status)
    {
        getModel().setStatus(status);
        return this;
    }


    public ConsignmentModelBuilder withWarehouse(WarehouseModel warehouse)
    {
        getModel().setWarehouse(warehouse);
        return this;
    }


    public ConsignmentModelBuilder withEntries(ConsignmentEntryModel... entries)
    {
        getModel().setConsignmentEntries(Sets.newHashSet((Object[])entries));
        return this;
    }


    public ConsignmentModelBuilder withOrder(OrderModel order)
    {
        getModel().setOrder((AbstractOrderModel)order);
        return this;
    }
}
