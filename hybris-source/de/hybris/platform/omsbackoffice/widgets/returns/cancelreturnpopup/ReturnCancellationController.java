package de.hybris.platform.omsbackoffice.widgets.returns.cancelreturnpopup;

import com.hybris.backoffice.i18n.BackofficeLocaleService;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.core.events.CockpitEvent;
import com.hybris.cockpitng.core.events.CockpitEventQueue;
import com.hybris.cockpitng.core.events.impl.DefaultCockpitEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.cockpitng.util.notifications.NotificationService;
import de.hybris.platform.basecommerce.enums.CancelReason;
import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.returns.OrderReturnException;
import de.hybris.platform.returns.ReturnActionResponse;
import de.hybris.platform.returns.ReturnCallbackService;
import de.hybris.platform.returns.model.ReturnRequestModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

public class ReturnCancellationController extends DefaultWidgetController
{
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(ReturnCancellationController.class);
    protected static final String IN_SOCKET = "cancelReturnContextInput";
    protected static final String OUT_CONFIRM = "cancelReturnContext";
    protected static final Object COMPLETED = "completed";
    private ReturnRequestModel returnRequest;
    private final List<String> cancellationReasons = new ArrayList<>();
    @Wire
    private Textbox returnRequestCode;
    @Wire
    private Textbox customerName;
    @Wire
    private Combobox globalCancelReasons;
    @Wire
    private Textbox globalCancelComment;
    @WireVariable
    private transient EnumerationService enumerationService;
    @WireVariable
    private transient BackofficeLocaleService cockpitLocaleService;
    @WireVariable
    private transient CockpitEventQueue cockpitEventQueue;
    @WireVariable
    private transient ReturnCallbackService returnCallbackService;
    @WireVariable
    private transient NotificationService notificationService;


    @SocketEvent(socketId = "cancelReturnContextInput")
    public void initCancelReturnForm(ReturnRequestModel inputObject)
    {
        this.cancellationReasons.clear();
        getReturnRequestCode().setValue(inputObject.getRMA());
        getCustomerName().setValue(inputObject.getOrder().getUser().getName());
        setReturnRequest(inputObject);
        getWidgetInstanceManager().setTitle(
                        getWidgetInstanceManager().getLabel("customersupportbackoffice.cancelreturnpopup.title") + " " + getWidgetInstanceManager().getLabel("customersupportbackoffice.cancelreturnpopup.title"));
        Locale locale = getCockpitLocaleService().getCurrentLocale();
        getEnumerationService().getEnumerationValues(CancelReason.class).forEach(reason -> this.cancellationReasons.add(getEnumerationService().getEnumerationName((HybrisEnumValue)reason, locale)));
        this.globalCancelReasons.setModel((ListModel)new ListModelArray(this.cancellationReasons));
        this.globalCancelReasons.addEventListener("onCustomChange", event -> Events.echoEvent("onLaterCustomChange", (Component)this.globalCancelReasons, event.getData()));
        this.globalCancelReasons.addEventListener("onLaterCustomChange", event -> {
            Clients.clearWrongValue((Component)this.globalCancelReasons);
            this.globalCancelReasons.invalidate();
            handleGlobalCancelReason(event);
        });
    }


    protected void handleGlobalCancelReason(Event event)
    {
        getSelectedCancelReason(event);
    }


    protected Optional<CancelReason> matchingComboboxCancelReason(String cancelReasonLabel)
    {
        return getEnumerationService()
                        .getEnumerationValues(CancelReason.class)
                        .stream()
                        .filter(reason -> getEnumerationService().getEnumerationName((HybrisEnumValue)reason, getCockpitLocaleService().getCurrentLocale()).equals(cancelReasonLabel))
                        .findFirst();
    }


    protected Optional<CancelReason> getSelectedCancelReason(Event event)
    {
        Optional<CancelReason> reason = Optional.empty();
        if(event.getTarget() instanceof Combobox)
        {
            Object selectedValue = event.getData();
            reason = matchingComboboxCancelReason(selectedValue.toString());
        }
        return reason;
    }


    @ViewEvent(componentID = "undocancelreturn", eventName = "onClick")
    public void undoCancelReturn()
    {
        this.globalCancelReasons.setSelectedItem(null);
        this.globalCancelComment.setValue("");
        initCancelReturnForm(getReturnRequest());
    }


    @ViewEvent(componentID = "confirmcancelreturn", eventName = "onClick")
    public void confirmCancelReturn()
    {
        validateRequest();
        Messagebox.show(getWidgetInstanceManager().getLabel("customersupportbackoffice.cancelreturnpopup.confirm.message.question"),
                        getWidgetInstanceManager().getLabel("customersupportbackoffice.cancelreturnpopup.title") + " " + getWidgetInstanceManager().getLabel("customersupportbackoffice.cancelreturnpopup.title"),
                        new Messagebox.Button[] {Messagebox.Button.NO, Messagebox.Button.YES}, "z-messagebox-icon z-messagebox-question", this::processCancellation);
    }


    protected void processCancellation(Event event)
    {
        if(Messagebox.Button.YES.event.equals(event.getName()))
        {
            ReturnActionResponse returnActionResponse = new ReturnActionResponse(getReturnRequest());
            try
            {
                getReturnCallbackService().onReturnCancelResponse(returnActionResponse);
                getNotificationService().notifyUser("", "JustMessage", NotificationEvent.Level.SUCCESS, new Object[] {getWidgetInstanceManager().getLabel("customersupportbackoffice.cancelreturnpopup.success.message")});
            }
            catch(OrderReturnException e)
            {
                LOGGER.error(e.getMessage(), (Throwable)e);
                getNotificationService().notifyUser("", "JustMessage", NotificationEvent.Level.FAILURE, new Object[] {getWidgetInstanceManager().getLabel("customersupportbackoffice.cancelreturnpopup.error.message")});
            }
            getCockpitEventQueue()
                            .publishEvent((CockpitEvent)new DefaultCockpitEvent("objectsUpdated", getReturnRequest(), null));
            getWidgetInstanceManager().sendOutput("cancelReturnContext", COMPLETED);
        }
    }


    protected void validateRequest()
    {
        if(this.globalCancelReasons.getSelectedItem() == null)
        {
            throw new WrongValueException(this.globalCancelReasons,
                            getLabel("customersupportbackoffice.cancelreturnpopup.decline.validation.missing.reason"));
        }
    }


    protected ReturnRequestModel getReturnRequest()
    {
        return this.returnRequest;
    }


    public void setReturnRequest(ReturnRequestModel returnRequest)
    {
        this.returnRequest = returnRequest;
    }


    protected EnumerationService getEnumerationService()
    {
        return this.enumerationService;
    }


    protected BackofficeLocaleService getCockpitLocaleService()
    {
        return this.cockpitLocaleService;
    }


    protected CockpitEventQueue getCockpitEventQueue()
    {
        return this.cockpitEventQueue;
    }


    protected ReturnCallbackService getReturnCallbackService()
    {
        return this.returnCallbackService;
    }


    protected NotificationService getNotificationService()
    {
        return this.notificationService;
    }


    public Textbox getCustomerName()
    {
        return this.customerName;
    }


    public Textbox getReturnRequestCode()
    {
        return this.returnRequestCode;
    }
}
