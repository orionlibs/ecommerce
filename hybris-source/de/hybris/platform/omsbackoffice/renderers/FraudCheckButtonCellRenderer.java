package de.hybris.platform.omsbackoffice.renderers;

import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.cockpitng.core.config.impl.jaxb.listview.ListColumn;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.util.notifications.NotificationService;
import com.hybris.cockpitng.widgets.common.WidgetComponentRenderer;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.fraud.model.FraudReportModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Button;
import org.zkoss.zul.Listcell;

public class FraudCheckButtonCellRenderer implements WidgetComponentRenderer<Listcell, ListColumn, FraudReportModel>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(FraudCheckButtonCellRenderer.class);
    protected static final String FRAUD_BUTTON = "fraudbutton";
    protected static final String REJECT_FRAUD_ITEM = "rejectfrauditem";
    protected static final String ACCEPT_FRAUD_ITEM = "acceptfrauditem";
    protected static final String ORDER_EVENT_NAME = "CSAOrderVerified";
    protected static final String DISABLED = "disabled";
    private BusinessProcessService businessProcessService;
    private ModelService modelService;
    private NotificationService notificationService;
    private WidgetInstanceManager widgetInstanceManager;


    public void render(Listcell parent, ListColumn columnConfiguration, FraudReportModel object, DataType dataType, WidgetInstanceManager widgetInstanceManger)
    {
        LOGGER.debug("Rendering accept and reject potentially fraudulent order button.");
        OrderModel order = object.getOrder();
        setWidgetInstanceManager(widgetInstanceManger);
        Button acceptButton = new Button();
        String acceptButtonClass = "fraudbutton acceptfrauditem";
        acceptButton.setParent((Component)parent);
        acceptButton.addEventListener("onClick", event -> acceptPotentiallyFraudulentOrder(order));
        Button rejectButton = new Button();
        String rejectButtonClass = "fraudbutton rejectfrauditem";
        rejectButton.setParent((Component)parent);
        rejectButton.addEventListener("onClick", event -> rejectPotentiallyFraudulentOrder(order));
        if(!canPerformOperation(order))
        {
            acceptButton.setDisabled(true);
            acceptButtonClass = acceptButtonClass + " disabled";
            rejectButton.setDisabled(true);
            rejectButtonClass = rejectButtonClass + " disabled";
        }
        acceptButton.setSclass(acceptButtonClass);
        rejectButton.setSclass(rejectButtonClass);
    }


    protected void rejectPotentiallyFraudulentOrder(OrderModel order)
    {
        if(canPerformOperation(order))
        {
            try
            {
                if(LOGGER.isDebugEnabled())
                {
                    LOGGER.debug(String.format("Rejected potentially fraudulent order with code: %s", new Object[] {order.getCode()}));
                }
                order.setFraudulent(Boolean.TRUE);
                executeFraudulentOperation(order);
                getNotificationService().notifyUser("", "JustMessage", NotificationEvent.Level.SUCCESS, new Object[] {Labels.getLabel("customersupportbackoffice.order.fraud.rejected.success")});
            }
            catch(ModelSavingException e)
            {
                LOGGER.warn(e.getMessage(), (Throwable)e);
                getNotificationService().notifyUser("", "JustMessage", NotificationEvent.Level.FAILURE, new Object[] {Labels.getLabel("customersupportbackoffice.order.fraud.rejected.failure")});
            }
        }
    }


    protected void acceptPotentiallyFraudulentOrder(OrderModel order)
    {
        if(canPerformOperation(order))
        {
            try
            {
                if(LOGGER.isDebugEnabled())
                {
                    LOGGER.debug(String.format("Accepted potentially fraudulent order with code: %s", new Object[] {order.getCode()}));
                }
                order.setFraudulent(Boolean.FALSE);
                executeFraudulentOperation(order);
                this.notificationService.notifyUser("", "JustMessage", NotificationEvent.Level.SUCCESS, new Object[] {Labels.getLabel("customersupportbackoffice.order.fraud.accepted.success")});
            }
            catch(ModelSavingException e)
            {
                LOGGER.info(e.getMessage(), (Throwable)e);
                this.notificationService.notifyUser("", "JustMessage", NotificationEvent.Level.FAILURE, new Object[] {Labels.getLabel("customersupportbackoffice.order.fraud.accepted.failure")});
            }
        }
    }


    protected boolean canPerformOperation(OrderModel order)
    {
        return (order.getStatus() == OrderStatus.WAIT_FRAUD_MANUAL_CHECK);
    }


    protected void executeFraudulentOperation(OrderModel order)
    {
        getModelService().save(order);
        order.getOrderProcess().stream()
                        .filter(process -> process.getCode().startsWith(order.getStore().getSubmitOrderProcessCode())).forEach(filteredProcess -> getBusinessProcessService().triggerEvent(filteredProcess.getCode() + "_CSAOrderVerified"));
    }


    protected BusinessProcessService getBusinessProcessService()
    {
        return this.businessProcessService;
    }


    @Required
    public void setBusinessProcessService(BusinessProcessService businessProcessService)
    {
        this.businessProcessService = businessProcessService;
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


    protected NotificationService getNotificationService()
    {
        return this.notificationService;
    }


    @Required
    public void setNotificationService(NotificationService notificationService)
    {
        this.notificationService = notificationService;
    }


    public WidgetInstanceManager getWidgetInstanceManager()
    {
        return this.widgetInstanceManager;
    }


    public void setWidgetInstanceManager(WidgetInstanceManager widgetInstanceManager)
    {
        this.widgetInstanceManager = widgetInstanceManager;
    }
}
