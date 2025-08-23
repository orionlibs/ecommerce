package de.hybris.platform.warehousingbackoffice.actions.util;

import com.hybris.backoffice.widgets.notificationarea.NotificationService;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.warehousing.process.BusinessProcessException;
import javax.annotation.Resource;

public abstract class AbstractConsignmentWorkflow
{
    protected static final int RETRIES = 500;
    @Resource
    private ModelService modelService;
    @Resource
    private NotificationService notificationService;


    protected ActionResult<ConsignmentModel> getConsignmentActionResult(ActionContext<ConsignmentModel> actionContext, String successMessage, String failedMessage, ConsignmentStatus expectedStatus)
    {
        String result;
        ConsignmentModel consignment = (ConsignmentModel)actionContext.getData();
        try
        {
            int count = 0;
            while(!expectedStatus.equals(getUpdatedConsignmentStatus(consignment)) && count < 500)
            {
                count++;
            }
            getNotificationService()
                            .notifyUser("", "JustMessage", NotificationEvent.Level.SUCCESS, new Object[] {actionContext.getLabel(successMessage)});
            result = "success";
        }
        catch(BusinessProcessException | de.hybris.platform.workflow.exceptions.WorkflowActionDecideException e)
        {
            getNotificationService()
                            .notifyUser("", "JustMessage", NotificationEvent.Level.FAILURE, new Object[] {actionContext.getLabel(failedMessage)});
            result = "error";
        }
        ActionResult<ConsignmentModel> actionResult = new ActionResult(result);
        actionResult.getStatusFlags().add(ActionResult.StatusFlag.OBJECT_PERSISTED);
        return actionResult;
    }


    protected ConsignmentStatus getUpdatedConsignmentStatus(ConsignmentModel consignmentModel)
    {
        return ((ConsignmentModel)getModelService().get(consignmentModel.getPk())).getStatus();
    }


    protected boolean isFulfillmentExternal(ConsignmentModel consignmentModel)
    {
        return (consignmentModel.getFulfillmentSystemConfig() != null);
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    protected NotificationService getNotificationService()
    {
        return this.notificationService;
    }
}
