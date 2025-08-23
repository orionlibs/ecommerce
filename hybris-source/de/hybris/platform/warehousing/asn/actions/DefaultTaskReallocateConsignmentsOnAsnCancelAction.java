package de.hybris.platform.warehousing.asn.actions;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.processengine.model.BusinessProcessParameterModel;
import de.hybris.platform.warehousing.asn.service.AsnService;
import de.hybris.platform.warehousing.data.allocation.DeclineEntries;
import de.hybris.platform.warehousing.data.allocation.DeclineEntry;
import de.hybris.platform.warehousing.enums.DeclineReason;
import de.hybris.platform.warehousing.inventoryevent.service.InventoryEventService;
import de.hybris.platform.warehousing.model.AdvancedShippingNoticeModel;
import de.hybris.platform.warehousing.model.AllocationEventModel;
import de.hybris.platform.warehousing.process.impl.DefaultConsignmentProcessService;
import de.hybris.platform.warehousing.taskassignment.actions.AbstractTaskAssignmentActions;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowDecisionModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

@Deprecated(since = "18.11", forRemoval = true)
public class DefaultTaskReallocateConsignmentsOnAsnCancelAction extends AbstractTaskAssignmentActions
{
    protected static final String CONSIGNMENT_ACTION_EVENT_NAME = "ConsignmentActionEvent";
    protected static final String REALLOCATE_CONSIGNMENT_CHOICE = "reallocateConsignment";
    protected static final String DECLINE_ENTRIES = "declineEntries";
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultTaskReallocateConsignmentsOnAsnCancelAction.class);
    private AsnService asnService;
    private InventoryEventService inventoryEventService;
    private List<ConsignmentStatus> reallocableConsignmentStatusList;


    public WorkflowDecisionModel perform(WorkflowActionModel workflowActionModel)
    {
        return workflowActionModel.getDecisions().isEmpty() ? null : workflowActionModel.getDecisions().iterator().next();
    }


    protected void reallocateConsignments(AdvancedShippingNoticeModel attachedAsn, Collection<AllocationEventModel> allocationEvents, Set<String> alreadyReallocatedConsignments)
    {
        allocationEvents.forEach(allocationEvent -> {
            ConsignmentModel consignment = allocationEvent.getConsignmentEntry().getConsignment();
            if(getReallocableConsignmentStatusList().contains(consignment.getStatus()) && !alreadyReallocatedConsignments.contains(consignment.getCode()))
            {
                List<DeclineEntry> declineEntries = new ArrayList<>();
                populateConsignmentEntries(consignment, declineEntries);
                BusinessProcessModel businessProcess = ((DefaultConsignmentProcessService)getConsignmentBusinessProcessService()).getConsignmentProcess(consignment);
                buildDeclineParam((ConsignmentProcessModel)businessProcess, declineEntries);
                getConsignmentBusinessProcessService().triggerChoiceEvent((ItemModel)consignment, "ConsignmentActionEvent", "reallocateConsignment");
                LOGGER.info("Consignment: {} is being reallocated because ASN: {} got cancelled", consignment.getCode(), attachedAsn.getInternalId());
                alreadyReallocatedConsignments.add(consignment.getCode());
            }
        });
    }


    protected void populateConsignmentEntries(ConsignmentModel consignment, List<DeclineEntry> declineEntries)
    {
        consignment.getConsignmentEntries().forEach(consignmentEntryModel -> {
            DeclineEntry declineEntry = new DeclineEntry();
            declineEntry.setQuantity(consignmentEntryModel.getQuantity());
            declineEntry.setConsignmentEntry(consignmentEntryModel);
            declineEntry.setReason(DeclineReason.ASNCANCELLATION);
            declineEntries.add(declineEntry);
        });
    }


    protected void buildDeclineParam(ConsignmentProcessModel processModel, List<DeclineEntry> entriesToReallocate)
    {
        cleanDeclineParam(processModel);
        DeclineEntries declinedEntries = new DeclineEntries();
        declinedEntries.setEntries(entriesToReallocate);
        BusinessProcessParameterModel declineParam = new BusinessProcessParameterModel();
        declineParam.setName("declineEntries");
        declineParam.setValue(declinedEntries);
        declineParam.setProcess((BusinessProcessModel)processModel);
        processModel.setContextParameters(Collections.singleton(declineParam));
        getModelService().save(processModel);
    }


    protected void cleanDeclineParam(ConsignmentProcessModel processModel)
    {
        Collection<BusinessProcessParameterModel> contextParams = new ArrayList<>();
        processModel.getContextParameters().forEach(param -> contextParams.add(param));
        if(CollectionUtils.isNotEmpty(contextParams))
        {
            Optional<BusinessProcessParameterModel> declineEntriesParamOptional = contextParams.stream().filter(param -> param.getName().equals("declineEntries")).findFirst();
            if(declineEntriesParamOptional.isPresent())
            {
                BusinessProcessParameterModel declineEntriesParam = declineEntriesParamOptional.get();
                contextParams.remove(declineEntriesParam);
                getModelService().remove(declineEntriesParam);
            }
        }
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


    protected List<ConsignmentStatus> getReallocableConsignmentStatusList()
    {
        return this.reallocableConsignmentStatusList;
    }


    @Required
    public void setReallocableConsignmentStatusList(List<ConsignmentStatus> reallocableConsignmentStatusList)
    {
        this.reallocableConsignmentStatusList = reallocableConsignmentStatusList;
    }
}
