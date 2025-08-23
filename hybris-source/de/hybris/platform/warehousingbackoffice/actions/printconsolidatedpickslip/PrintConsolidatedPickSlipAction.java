package de.hybris.platform.warehousingbackoffice.actions.printconsolidatedpickslip;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.warehousingbackoffice.labels.strategy.impl.ConsolidatedConsignmentPrintPickSlipStrategy;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;

public class PrintConsolidatedPickSlipAction implements CockpitAction<Collection<Object>, Collection<Object>>
{
    protected static final String PICKING_STATUS = "NPR_Picking";
    @Resource
    protected ConsolidatedConsignmentPrintPickSlipStrategy consignmentsPrintPickSlipStrategy;


    public boolean canPerform(ActionContext<Collection<Object>> actionContext)
    {
        boolean returnValue = false;
        if(actionContext != null && CollectionUtils.isNotEmpty((Collection)actionContext.getData()))
        {
            Collection<Object> items = (Collection<Object>)actionContext.getData();
            if(items.iterator().next() instanceof WorkflowActionModel)
            {
                Collection<WorkflowActionModel> workflowActions = convertItemCollection(items);
                BaseSiteModel firstSite = ((ConsignmentModel)((WorkflowActionModel)workflowActions.iterator().next()).getAttachmentItems().iterator().next()).getOrder().getSite();
                boolean isFulfillmentInternal = workflowActions.stream().allMatch(workflowActionModel -> (((ConsignmentModel)workflowActionModel.getAttachmentItems().iterator().next()).getFulfillmentSystemConfig() == null));
                returnValue = (workflowActions.stream().allMatch(workflowActionModel ->
                                ("NPR_Picking".equals(workflowActionModel.getTemplate().getCode()) && ((ConsignmentModel)workflowActionModel.getAttachmentItems().iterator().next()).getOrder().getSite().equals(firstSite))) && isFulfillmentInternal);
            }
            else if(items.iterator().next() instanceof ConsignmentModel)
            {
                Collection<ConsignmentModel> consignments = convertItemCollection(items);
                BaseSiteModel firstSite = ((ConsignmentModel)consignments.iterator().next()).getOrder().getSite();
                boolean isFulfillmentInternal = consignments.stream().allMatch(consignment -> (consignment.getFulfillmentSystemConfig() == null));
                returnValue = (consignments.stream().allMatch(consignment -> consignment.getOrder().getSite().equals(firstSite)) && isFulfillmentInternal);
            }
        }
        return returnValue;
    }


    public String getConfirmationMessage(ActionContext<Collection<Object>> actionContext)
    {
        return null;
    }


    public boolean needsConfirmation(ActionContext<Collection<Object>> actionContext)
    {
        return false;
    }


    public ActionResult<Collection<Object>> perform(ActionContext<Collection<Object>> actionContext)
    {
        List<ConsignmentModel> consignments = new ArrayList<>();
        if(((Collection)actionContext.getData()).iterator().next() instanceof WorkflowActionModel)
        {
            Collection<WorkflowActionModel> workflowActions = convertItemCollection((Collection)actionContext.getData());
            workflowActions.forEach(workflowActionModel -> consignments.add(workflowActionModel.getAttachmentItems().iterator().next()));
        }
        else
        {
            consignments.addAll(convertItemCollection((Collection)actionContext.getData()));
        }
        getConsignmentsPrintPickSlipStrategy().printDocument(consignments);
        return new ActionResult("success");
    }


    protected <T> Collection<T> convertItemCollection(Collection<Object> itemCollection)
    {
        List<T> convertedCollection = new ArrayList<>();
        itemCollection.forEach(item -> convertedCollection.add(item));
        return convertedCollection;
    }


    protected ConsolidatedConsignmentPrintPickSlipStrategy getConsignmentsPrintPickSlipStrategy()
    {
        return this.consignmentsPrintPickSlipStrategy;
    }
}
