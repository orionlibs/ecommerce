package de.hybris.platform.warehousing.asn.actions;

import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.warehousing.asn.service.AsnService;
import de.hybris.platform.warehousing.inventoryevent.service.InventoryEventService;
import de.hybris.platform.warehousing.model.AdvancedShippingNoticeModel;
import de.hybris.platform.warehousing.model.CancellationEventModel;
import de.hybris.platform.warehousing.taskassignment.actions.AbstractTaskAssignmentActions;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowDecisionModel;
import java.util.Collection;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultTaskDeleteCancellationEventsOnAsnCancelAction extends AbstractTaskAssignmentActions
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultTaskDeleteCancellationEventsOnAsnCancelAction.class);
    private InventoryEventService inventoryEventService;
    private AsnService asnService;


    public WorkflowDecisionModel perform(WorkflowActionModel workflowActionModel)
    {
        if(getAttachedAsn(workflowActionModel).isPresent())
        {
            AdvancedShippingNoticeModel attachedAsn = getAttachedAsn(workflowActionModel).get();
            List<StockLevelModel> stockLevels = getAsnService().getStockLevelsForAsn(attachedAsn);
            stockLevels.forEach(stockLevelModel -> {
                Collection<CancellationEventModel> cancellationEvents = getInventoryEventService().getInventoryEventsForStockLevel(stockLevelModel, CancellationEventModel.class);
                getModelService().removeAll(cancellationEvents);
                LOGGER.info("{} Cancellation Events linked to {} are being removed because ASN: {} got cancelled", new Object[] {Integer.valueOf(cancellationEvents.size()), stockLevelModel.getProductCode(), attachedAsn.getInternalId()});
            });
        }
        return workflowActionModel.getDecisions().isEmpty() ? null : workflowActionModel.getDecisions().iterator().next();
    }


    protected AsnService getAsnService()
    {
        return this.asnService;
    }


    @Required
    public void setAsnService(AsnService asnService)
    {
        this.asnService = asnService;
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
