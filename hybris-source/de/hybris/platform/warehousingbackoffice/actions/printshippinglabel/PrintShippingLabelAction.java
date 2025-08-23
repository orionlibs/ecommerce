package de.hybris.platform.warehousingbackoffice.actions.printshippinglabel;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.warehousingbackoffice.labels.strategy.ConsignmentPrintDocumentStrategy;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Messagebox;

public class PrintShippingLabelAction implements CockpitAction<ConsignmentModel, ConsignmentModel>
{
    private static final Logger LOG = LoggerFactory.getLogger(PrintShippingLabelAction.class);
    protected static final String POPUP_UNSAVED_CONSIGNMENT_MESSAGE = "shippinglabelpopup.unsaved.consignment.message.question";
    protected static final String POPUP_UNSAVED_CONSIGNMENT_TITLE = "shippinglabelpopup.unsaved.consignment.title";
    @Resource
    private ConsignmentPrintDocumentStrategy consignmentPrintShippingLabelStrategy;
    @Resource
    private ModelService modelService;


    public ActionResult<ConsignmentModel> perform(ActionContext<ConsignmentModel> consignmentModelActionContext)
    {
        ConsignmentModel consignment = (ConsignmentModel)consignmentModelActionContext.getData();
        ActionResult<ConsignmentModel> actionResult = new ActionResult("success");
        Map<String, Object> parentWidgetMap = (Map<String, Object>)consignmentModelActionContext.getParameter("parentWidgetModel");
        if(consignment.getShippingLabel() == null && ((Boolean)parentWidgetMap.get("valueChanged")).booleanValue())
        {
            Messagebox.show(consignmentModelActionContext.getLabel("shippinglabelpopup.unsaved.consignment.message.question"), consignmentModelActionContext
                                            .getLabel("shippinglabelpopup.unsaved.consignment.title"), new Messagebox.Button[] {Messagebox.Button.NO, Messagebox.Button.YES}, "z-messagebox-icon z-messagebox-question",
                            clickEvent -> processMessageboxEvent((Event)clickEvent, consignmentModelActionContext, parentWidgetMap));
        }
        else
        {
            printShippingLabel(consignment);
            actionResult.getStatusFlags().add(ActionResult.StatusFlag.OBJECT_PERSISTED);
        }
        return actionResult;
    }


    public boolean canPerform(ActionContext<ConsignmentModel> consignmentModelActionContext)
    {
        Object data = consignmentModelActionContext.getData();
        return (data instanceof ConsignmentModel &&
                        !(((ConsignmentModel)data).getDeliveryMode() instanceof de.hybris.platform.commerceservices.model.PickUpDeliveryModeModel) && ((ConsignmentModel)consignmentModelActionContext
                        .getData()).getFulfillmentSystemConfig() == null);
    }


    public boolean needsConfirmation(ActionContext<ConsignmentModel> consignmentModelActionContext)
    {
        return false;
    }


    public String getConfirmationMessage(ActionContext<ConsignmentModel> consignmentModelActionContext)
    {
        return null;
    }


    protected void processMessageboxEvent(Event clickEvent, ActionContext<ConsignmentModel> consignmentModelActionContext, Map<String, Object> parentWidgetMap)
    {
        if(Messagebox.Button.YES.event.equals(clickEvent.getName()))
        {
            ConsignmentModel consignment = (ConsignmentModel)consignmentModelActionContext.getData();
            getModelService().save(consignment);
            printShippingLabel(consignment);
            getModelService().refresh(consignment);
            parentWidgetMap.put("currentObject", consignment);
        }
    }


    protected void printShippingLabel(ConsignmentModel consignment)
    {
        LOG.info("Generate Print shipping label for consignment {}", consignment.getCode());
        getConsignmentPrintShippingLabelStrategy().printDocument(consignment);
    }


    protected ConsignmentPrintDocumentStrategy getConsignmentPrintShippingLabelStrategy()
    {
        return this.consignmentPrintShippingLabelStrategy;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }
}
