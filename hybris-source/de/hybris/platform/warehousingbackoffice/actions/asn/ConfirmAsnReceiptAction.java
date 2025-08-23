package de.hybris.platform.warehousingbackoffice.actions.asn;

import com.hybris.backoffice.widgets.notificationarea.NotificationService;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import de.hybris.platform.warehousing.asn.service.AsnService;
import de.hybris.platform.warehousing.enums.AsnStatus;
import de.hybris.platform.warehousing.model.AdvancedShippingNoticeModel;
import java.util.Objects;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfirmAsnReceiptAction implements CockpitAction<AdvancedShippingNoticeModel, AdvancedShippingNoticeModel>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfirmAsnReceiptAction.class);
    private static final String SUCCESS_MESSAGE = "warehousingbackoffice.confirm.asn.receipt.success";
    private static final String FAILURE_MESSAGE = "warehousingbackoffice.confirm.asn.receipt.failure";
    @Resource
    private AsnService asnService;
    @Resource
    private NotificationService notificationService;


    public ActionResult<AdvancedShippingNoticeModel> perform(ActionContext<AdvancedShippingNoticeModel> actionContext)
    {
        ActionResult<AdvancedShippingNoticeModel> result;
        AdvancedShippingNoticeModel asn = (AdvancedShippingNoticeModel)actionContext.getData();
        try
        {
            getAsnService().confirmAsnReceipt(asn.getInternalId());
            getNotificationService()
                            .notifyUser("", "JustMessage", NotificationEvent.Level.SUCCESS, new Object[] {actionContext.getLabel("warehousingbackoffice.confirm.asn.receipt.success")});
            result = new ActionResult("success");
        }
        catch(IllegalArgumentException e)
        {
            LOGGER.info(
                            String.format("Unable to Confirm Receipt of ASN: [%s] with status: [%s]", new Object[] {asn.getInternalId(), asn.getStatus()}));
            getNotificationService()
                            .notifyUser("", "JustMessage", NotificationEvent.Level.FAILURE, new Object[] {actionContext.getLabel("warehousingbackoffice.confirm.asn.receipt.failure")});
            result = new ActionResult("error");
        }
        result.getStatusFlags().add(ActionResult.StatusFlag.OBJECT_PERSISTED);
        return result;
    }


    public boolean canPerform(ActionContext<AdvancedShippingNoticeModel> actionContext)
    {
        AdvancedShippingNoticeModel asn = (AdvancedShippingNoticeModel)actionContext.getData();
        return (Objects.nonNull(asn) && AsnStatus.CREATED.equals(asn.getStatus()));
    }


    public boolean needsConfirmation(ActionContext<AdvancedShippingNoticeModel> actionContext)
    {
        return false;
    }


    public String getConfirmationMessage(ActionContext<AdvancedShippingNoticeModel> actionContext)
    {
        return null;
    }


    protected AsnService getAsnService()
    {
        return this.asnService;
    }


    protected NotificationService getNotificationService()
    {
        return this.notificationService;
    }
}
