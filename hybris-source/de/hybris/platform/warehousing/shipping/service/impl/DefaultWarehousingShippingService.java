package de.hybris.platform.warehousing.shipping.service.impl;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.processengine.enums.ProcessState;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.warehousing.process.BusinessProcessException;
import de.hybris.platform.warehousing.shipping.service.WarehousingShippingService;
import de.hybris.platform.warehousing.taskassignment.services.WarehousingConsignmentWorkflowService;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

public class DefaultWarehousingShippingService implements WarehousingShippingService
{
    protected static final String CONFIRM_SHIP_CONSIGNMENT_CHOICE = "confirmShipConsignment";
    protected static final String CONFIRM_PICKUP_CONSIGNMENT_CHOICE = "confirmPickupConsignment";
    protected static final String SHIPPING_TEMPLATE_CODE = "NPR_Shipping";
    protected static final String PICKUP_TEMPLATE_CODE = "NPR_Pickup";
    private ModelService modelService;
    private List<OrderStatus> validConsConfirmOrderStatusList;
    private List<ConsignmentStatus> validConsConfirmConsignmentStatusList;
    private WarehousingConsignmentWorkflowService warehousingConsignmentWorkflowService;


    public boolean isConsignmentConfirmable(ConsignmentModel consignment)
    {
        ConsignmentModel upToDateConsignment;
        ServicesUtil.validateParameterNotNullStandardMessage("consignment", consignment);
        if(getModelService().isUpToDate(consignment))
        {
            upToDateConsignment = consignment;
        }
        else
        {
            upToDateConsignment = (ConsignmentModel)getModelService().get(consignment.getPk());
        }
        Assert.notNull(upToDateConsignment.getOrder(),
                        String.format("Order cannot be null for the Consignment with code: [%s]", new Object[] {upToDateConsignment.getCode()}));
        AbstractOrderModel order = upToDateConsignment.getOrder();
        Assert.notNull(order.getStatus(),
                        String.format("Order Status cannot be null for the Order with code: [%s].", new Object[] {order.getCode()}));
        String consignmentProcessCode = upToDateConsignment.getCode() + "_ordermanagement";
        Optional<ConsignmentProcessModel> consignmentProcess = upToDateConsignment.getConsignmentProcesses().stream().filter(consProcess -> consProcess.getCode().equals(consignmentProcessCode)).findFirst();
        Assert.isTrue(consignmentProcess.isPresent(),
                        String.format("No process found for the Consignment with the code: [%s].", new Object[] {upToDateConsignment.getCode()}));
        return (getValidConsConfirmConsignmentStatusList().contains(upToDateConsignment.getStatus()) &&
                        getValidConsConfirmOrderStatusList().contains(order.getStatus()) &&
                        !ProcessState.SUCCEEDED.equals(((ConsignmentProcessModel)consignmentProcess.get()).getState()) && upToDateConsignment
                        .getConsignmentEntries().stream().filter(entry -> Objects.nonNull(entry.getQuantityPending()))
                        .mapToLong(ConsignmentEntryModel::getQuantityPending).sum() > 0L);
    }


    public void confirmShipConsignment(ConsignmentModel consignment) throws BusinessProcessException
    {
        getWarehousingConsignmentWorkflowService()
                        .decideWorkflowAction(consignment, "NPR_Shipping", "confirmShipConsignment");
    }


    public void confirmPickupConsignment(ConsignmentModel consignment) throws BusinessProcessException
    {
        getWarehousingConsignmentWorkflowService()
                        .decideWorkflowAction(consignment, "NPR_Pickup", "confirmPickupConsignment");
    }


    protected List<ConsignmentStatus> getValidConsConfirmConsignmentStatusList()
    {
        return this.validConsConfirmConsignmentStatusList;
    }


    @Required
    public void setValidConsConfirmConsignmentStatusList(List<ConsignmentStatus> validConsConfirmConsignmentStatusList)
    {
        this.validConsConfirmConsignmentStatusList = validConsConfirmConsignmentStatusList;
    }


    protected List<OrderStatus> getValidConsConfirmOrderStatusList()
    {
        return this.validConsConfirmOrderStatusList;
    }


    @Required
    public void setValidConsConfirmOrderStatusList(List<OrderStatus> validConsConfirmOrderStatusList)
    {
        this.validConsConfirmOrderStatusList = validConsConfirmOrderStatusList;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected WarehousingConsignmentWorkflowService getWarehousingConsignmentWorkflowService()
    {
        return this.warehousingConsignmentWorkflowService;
    }


    @Required
    public void setWarehousingConsignmentWorkflowService(WarehousingConsignmentWorkflowService warehousingConsignmentWorkflowService)
    {
        this.warehousingConsignmentWorkflowService = warehousingConsignmentWorkflowService;
    }
}
