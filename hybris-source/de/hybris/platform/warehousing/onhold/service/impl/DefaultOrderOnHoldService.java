package de.hybris.platform.warehousing.onhold.service.impl;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.warehousing.inventoryevent.service.InventoryEventService;
import de.hybris.platform.warehousing.onhold.service.OrderOnHoldService;
import de.hybris.platform.warehousing.process.WarehousingBusinessProcessService;
import de.hybris.platform.warehousing.taskassignment.services.WarehousingConsignmentWorkflowService;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;

public class DefaultOrderOnHoldService implements OrderOnHoldService
{
    protected static final String CONSIGNMENT_ACTION_EVENT_NAME = "ConsignmentActionEvent";
    protected static final String CANCEL_CONSIGNMENT_CHOICE = "cancelConsignment";
    private ModelService modelService;
    private List<ConsignmentStatus> nonCancellableConsignmentStatus;
    private WarehousingBusinessProcessService<ConsignmentModel> consignmentBusinessProcessService;
    private WarehousingConsignmentWorkflowService warehousingConsignmentWorkflowService;
    private InventoryEventService inventoryEventService;


    public void processOrderOnHold(OrderModel order)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("order", order);
        order.setStatus(OrderStatus.ON_HOLD);
        getModelService().save(order);
        order.getConsignments().stream()
                        .filter(consignment -> !getNonCancellableConsignmentStatus().contains(consignment.getStatus()))
                        .forEach(consignment -> {
                            consignment.setStatus(ConsignmentStatus.CANCELLED);
                            getModelService().save(consignment);
                            getConsignmentBusinessProcessService().triggerChoiceEvent((ItemModel)consignment, "ConsignmentActionEvent", "cancelConsignment");
                            getWarehousingConsignmentWorkflowService().terminateConsignmentWorkflow(consignment);
                            getModelService().removeAll(getInventoryEventService().getAllocationEventsForConsignment(consignment));
                        });
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


    protected List<ConsignmentStatus> getNonCancellableConsignmentStatus()
    {
        return this.nonCancellableConsignmentStatus;
    }


    @Required
    public void setNonCancellableConsignmentStatus(List<ConsignmentStatus> nonCancellableConsignmentStatus)
    {
        this.nonCancellableConsignmentStatus = nonCancellableConsignmentStatus;
    }


    protected WarehousingBusinessProcessService<ConsignmentModel> getConsignmentBusinessProcessService()
    {
        return this.consignmentBusinessProcessService;
    }


    @Required
    public void setConsignmentBusinessProcessService(WarehousingBusinessProcessService<ConsignmentModel> consignmentBusinessProcessService)
    {
        this.consignmentBusinessProcessService = consignmentBusinessProcessService;
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


    protected InventoryEventService getInventoryEventService()
    {
        return this.inventoryEventService;
    }


    @Required
    public void setInventoryEventService(InventoryEventService inventoryEventService)
    {
        this.inventoryEventService = inventoryEventService;
    }
}
