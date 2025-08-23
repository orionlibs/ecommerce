package de.hybris.platform.omsbackoffice.widgets.order.orderprocessexecutor;

import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.util.notifications.NotificationService;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.processengine.BusinessProcessEvent;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.util.localization.Localization;
import org.zkoss.zk.ui.select.annotation.WireVariable;

public class OrderProcessExecutor
{
    protected static final String TRIGGER_SOURCING_IN_SOCKET = "triggerSourcing";
    protected static final String PUT_ON_HOLD_IN_SOCKET = "putOnHold";
    protected static final String ORDER_ACTION_EVENT_NAME = "OrderActionEvent";
    protected static final String RE_SOURCE_CHOICE = "reSource";
    protected static final String PUT_ON_HOLD_CHOICE = "putOnHold";
    protected static final String TRIGGER_SOURCING_SUCCESS = "customersupportbackoffice.order.trigger.sourcing.success";
    protected static final String PUT_ORDER_ON_HOLD_SUCCESS = "customersupportbackoffice.order.on.hold.success";
    @WireVariable
    private BusinessProcessService businessProcessService;
    @WireVariable
    private NotificationService notificationService;


    @SocketEvent(socketId = "triggerSourcing")
    public void triggerSourcing(OrderModel order)
    {
        triggerBusinessProcessEvent(order, "reSource", "customersupportbackoffice.order.trigger.sourcing.success");
    }


    @SocketEvent(socketId = "putOnHold")
    public void putOnHold(OrderModel order)
    {
        triggerBusinessProcessEvent(order, "putOnHold", "customersupportbackoffice.order.on.hold.success");
    }


    protected void triggerBusinessProcessEvent(OrderModel order, String choice, String notificationMessage)
    {
        order.getOrderProcess().stream()
                        .filter(process -> process.getCode().startsWith(order.getStore().getSubmitOrderProcessCode()))
                        .forEach(filteredProcess -> getBusinessProcessService().triggerEvent(BusinessProcessEvent.builder(filteredProcess.getCode() + "_OrderActionEvent").withChoice(choice).withEventTriggeringInTheFutureDisabled().build()));
        getNotificationService().notifyUser((String)null, "JustMessage", NotificationEvent.Level.SUCCESS, new Object[] {Localization.getLocalizedString(notificationMessage)});
    }


    protected BusinessProcessService getBusinessProcessService()
    {
        return this.businessProcessService;
    }


    protected NotificationService getNotificationService()
    {
        return this.notificationService;
    }
}
