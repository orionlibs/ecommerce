package de.hybris.platform.omsbackoffice.widgets.order.cancelorder;

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
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.omsbackoffice.dto.OrderEntryToCancelDto;
import de.hybris.platform.ordercancel.OrderCancelEntry;
import de.hybris.platform.ordercancel.OrderCancelRequest;
import de.hybris.platform.ordercancel.OrderCancelService;
import de.hybris.platform.ordercancel.model.OrderCancelRecordEntryModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CancellationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.impl.InputElement;

public class CancelOrderController extends DefaultWidgetController
{
    private static final Logger LOGGER = LoggerFactory.getLogger(CancelOrderController.class);
    private static final long serialVersionUID = 1L;
    protected static final String IN_SOCKET = "inputObject";
    protected static final String CONFIRM_ID = "confirmcancellation";
    protected static final Object COMPLETED = "completed";
    protected static final String CUSTOMERSUPPORTBACKOFFICE_CANCELORDER_PICKUP = "customersupportbackoffice.cancelorder.pickup";
    protected static final String CUSTOMERSUPPORTBACKOFFICE_CANCELORDER_CONFIRM_TITLE = "customersupportbackoffice.cancelorder.confirm.title";
    protected static final String CUSTOMERSUPPORTBACKOFFICE_CANCELORDER_CONFIRM_ERROR = "customersupportbackoffice.cancelorder.confirm.error";
    protected static final String CUSTOMERSUPPORTBACKOFFICE_CANCELORDER_CONFIRM_MSG = "customersupportbackoffice.cancelorder.confirm.msg";
    protected static final String CUSTOMERSUPPORTBACKOFFICE_CANCELORDER_ERROR_QTYCANCELLED_INVALID = "customersupportbackoffice.cancelorder.error.qtycancelled.invalid";
    protected static final String CUSTOMERSUPPORTBACKOFFICE_CANCELORDER_MISSING_QUANTITY = "customersupportbackoffice.cancelorder.missing.quantity";
    protected static final String CUSTOMERSUPPORTBACKOFFICE_CANCELORDER_ERROR_REASON = "customersupportbackoffice.cancelorder.error.reason";
    protected static final String CUSTOMERSUPPORTBACKOFFICE_CANCELORDER_MISSING_SELECTED_LINE = "customersupportbackoffice.cancelorder.missing.selectedLine";
    protected static final String CANCELORDER_CONFIRM_ICON = "oms-widget-cancelorder-confirm-icon";
    protected static final int COLUMN_INDEX_PENDING_QUANTITY = 4;
    protected static final int COLUMN_INDEX_CANCEL_QUANTITY = 5;
    protected static final int COLUMN_INDEX_CANCEL_REASON = 6;
    protected static final int COLUMN_INDEX_CANCEL_COMMENT = 7;
    private final List<String> cancelReasons = new ArrayList<>();
    private transient Map<AbstractOrderEntryModel, Long> orderCancellableEntries;
    private transient Set<OrderEntryToCancelDto> orderEntriesToCancel;
    private OrderModel orderModel;
    @Wire
    private Textbox orderNumber;
    @Wire
    private Textbox customerName;
    @Wire
    private Combobox globalCancelReasons;
    @Wire
    private Textbox globalCancelComment;
    @Wire
    private Grid orderEntries;
    @Wire
    private Checkbox globalCancelEntriesSelection;
    @WireVariable
    private transient BackofficeLocaleService cockpitLocaleService;
    @WireVariable
    private transient OrderCancelService orderCancelService;
    @WireVariable
    private transient EnumerationService enumerationService;
    @WireVariable
    private transient ModelService modelService;
    @WireVariable
    private transient CockpitEventQueue cockpitEventQueue;
    @WireVariable
    private transient UserService userService;
    @WireVariable
    private transient NotificationService notificationService;


    @ViewEvent(componentID = "confirmcancellation", eventName = "onClick")
    public void confirmCancellation()
    {
        validateRequest();
        showMessageBox();
    }


    @SocketEvent(socketId = "inputObject")
    public void initCancellationOrderForm(OrderModel inputObject)
    {
        this.cancelReasons.clear();
        this.globalCancelEntriesSelection.setChecked(false);
        setOrderModel(inputObject);
        getWidgetInstanceManager().setTitle(
                        getWidgetInstanceManager().getLabel("customersupportbackoffice.cancelorder.confirm.title") + " " + getWidgetInstanceManager().getLabel("customersupportbackoffice.cancelorder.confirm.title"));
        this.orderNumber.setValue(getOrderModel().getCode());
        this.customerName.setValue(getOrderModel().getUser().getDisplayName());
        Locale locale = getLocale();
        getEnumerationService().getEnumerationValues(CancelReason.class)
                        .forEach(reason -> this.cancelReasons.add(getEnumerationService().getEnumerationName((HybrisEnumValue)reason, locale)));
        this.globalCancelReasons.setModel((ListModel)new ListModelArray(this.cancelReasons));
        this.orderEntriesToCancel = new HashSet<>();
        this
                        .orderCancellableEntries = getOrderCancelService().getAllCancelableEntries(getOrderModel(), (PrincipalModel)getUserService().getCurrentUser());
        if(!this.orderCancellableEntries.isEmpty())
        {
            this.orderCancellableEntries.forEach((entry, cancellableQty) -> this.orderEntriesToCancel.add(new OrderEntryToCancelDto(entry, this.cancelReasons, cancellableQty, determineDeliveryMode(entry))));
        }
        getOrderEntries().setModel((ListModel)new ListModelList(this.orderEntriesToCancel));
        getOrderEntries().renderAll();
        addListeners();
    }


    protected String determineDeliveryMode(AbstractOrderEntryModel orderEntry)
    {
        String deliveryModeResult;
        if(orderEntry.getDeliveryMode() != null)
        {
            deliveryModeResult = orderEntry.getDeliveryMode().getName();
        }
        else if(orderEntry.getDeliveryPointOfService() != null)
        {
            deliveryModeResult = getLabel("customersupportbackoffice.cancelorder.pickup");
        }
        else
        {
            deliveryModeResult = (orderEntry.getOrder().getDeliveryMode() != null) ? ((orderEntry.getOrder().getDeliveryMode().getName() != null) ? orderEntry.getOrder().getDeliveryMode().getName() : orderEntry.getOrder().getDeliveryMode().getCode()) : null;
        }
        return deliveryModeResult;
    }


    @ViewEvent(componentID = "undocancellation", eventName = "onClick")
    public void reset()
    {
        this.globalCancelReasons.setSelectedItem(null);
        this.globalCancelComment.setValue("");
        initCancellationOrderForm(getOrderModel());
    }


    protected void addListeners()
    {
        List<Component> rows = getOrderEntries().getRows().getChildren();
        for(Iterator<Component> iterator = rows.iterator(); iterator.hasNext(); )
        {
            Component row = iterator.next();
            for(Component myComponent : row.getChildren())
            {
                if(myComponent instanceof Checkbox)
                {
                    myComponent.addEventListener("onCheck", event -> handleRow((Row)event.getTarget().getParent()));
                    continue;
                }
                if(myComponent instanceof Combobox)
                {
                    myComponent.addEventListener("onCustomChange", event -> Events.echoEvent("onLaterCustomChange", myComponent, event.getData()));
                    myComponent.addEventListener("onLaterCustomChange", event -> {
                        Clients.clearWrongValue(myComponent);
                        myComponent.invalidate();
                        handleIndividualCancelReason(event);
                    });
                    continue;
                }
                if(myComponent instanceof Intbox)
                {
                    myComponent.addEventListener("onChange", event -> {
                        autoSelect(event);
                        ((OrderEntryToCancelDto)((Row)event.getTarget().getParent()).getValue()).setQuantityToCancel(Long.valueOf(((InputEvent)event).getValue()));
                    });
                    continue;
                }
                if(myComponent instanceof Textbox)
                {
                    myComponent.addEventListener("onChanging", event -> {
                        autoSelect(event);
                        ((OrderEntryToCancelDto)((Row)event.getTarget().getParent()).getValue()).setCancelOrderEntryComment(((InputEvent)event).getValue());
                    });
                }
            }
        }
        this.globalCancelReasons.addEventListener("onSelect", this::handleGlobalCancelReason);
        this.globalCancelComment.addEventListener("onChanging", this::handleGlobalCancelComment);
        this.globalCancelEntriesSelection.addEventListener("onCheck", event -> selectAllEntries());
    }


    protected void applyToGrid(Object data, int childrenIndex)
    {
        getOrderEntriesGridRows().stream().filter(entry -> ((Checkbox)entry.getChildren().iterator().next()).isChecked())
                        .forEach(entry -> applyToRow(data, childrenIndex, entry));
    }


    protected void applyToRow(Object data, int childrenIndex, Component row)
    {
        int index = 0;
        for(Component myComponent : row.getChildren())
        {
            if(index != childrenIndex)
            {
                index++;
                continue;
            }
            if(myComponent instanceof Checkbox && data != null)
            {
                ((Checkbox)myComponent).setChecked(((Boolean)data).booleanValue());
            }
            if(myComponent instanceof Combobox)
            {
                if(data == null)
                {
                    ((Combobox)myComponent).setSelectedItem(null);
                }
                else
                {
                    ((Combobox)myComponent).setSelectedIndex(((Integer)data).intValue());
                }
            }
            else if(myComponent instanceof Intbox)
            {
                ((Intbox)myComponent).setValue((Integer)data);
            }
            else if(myComponent instanceof Textbox)
            {
                ((Textbox)myComponent).setValue((String)data);
            }
            index++;
        }
    }


    protected void autoSelect(Event event)
    {
        ((Checkbox)event.getTarget().getParent().getChildren().iterator().next()).setChecked(true);
    }


    protected OrderCancelRequest buildCancelRequest()
    {
        if(getOrderModel() != null)
        {
            List<OrderCancelEntry> orderCancelEntries = new ArrayList<>();
            getOrderEntriesGridRows().stream().filter(entry -> ((Checkbox)entry.getFirstChild()).isChecked())
                            .forEach(entry -> createOrderCancelEntry(orderCancelEntries, ((Row)entry).getValue()));
            OrderCancelRequest orderCancelRequest = new OrderCancelRequest(getOrderModel(), orderCancelEntries);
            orderCancelRequest.setCancelReason(matchingComboboxCancelReason(this.globalCancelReasons.getValue()).orElse(null));
            orderCancelRequest.setNotes(this.globalCancelComment.getValue());
            return orderCancelRequest;
        }
        return null;
    }


    protected void createOrderCancelEntry(List<OrderCancelEntry> orderCancelEntries, Object entry)
    {
        OrderEntryToCancelDto orderEntryToCancel = (OrderEntryToCancelDto)entry;
        OrderCancelEntry orderCancelEntry = new OrderCancelEntry(orderEntryToCancel.getOrderEntry(), orderEntryToCancel.getQuantityToCancel().longValue(), orderEntryToCancel.getCancelOrderEntryComment(), orderEntryToCancel.getSelectedReason());
        orderCancelEntries.add(orderCancelEntry);
    }


    protected int getReasonIndex(CancelReason cancelReason)
    {
        int index = 0;
        String myReason = getEnumerationService().getEnumerationName((HybrisEnumValue)cancelReason, getCockpitLocaleService().getCurrentLocale());
        for(String reason : this.cancelReasons)
        {
            if(myReason.equals(reason))
            {
                break;
            }
            index++;
        }
        return index;
    }


    protected Optional<CancelReason> getSelectedCancelReason(Event event)
    {
        Optional<CancelReason> result = Optional.empty();
        if(!((SelectEvent)event).getSelectedItems().isEmpty())
        {
            Object selectedValue = ((Comboitem)((SelectEvent)event).getSelectedItems().iterator().next()).getValue();
            result = matchingComboboxCancelReason(selectedValue.toString());
        }
        return result;
    }


    protected void handleGlobalCancelComment(Event event)
    {
        applyToGrid(((InputEvent)event).getValue(), 7);
        getOrderEntriesGridRows().stream().filter(entry -> ((Checkbox)entry.getChildren().iterator().next()).isChecked())
                        .forEach(entry -> {
                            OrderEntryToCancelDto myEntry = (OrderEntryToCancelDto)((Row)entry).getValue();
                            myEntry.setCancelOrderEntryComment(((InputEvent)event).getValue());
                        });
    }


    protected void handleGlobalCancelReason(Event event)
    {
        Optional<CancelReason> cancelReason = getSelectedCancelReason(event);
        if(cancelReason.isPresent())
        {
            applyToGrid(Integer.valueOf(getReasonIndex(cancelReason.get())), 6);
            getOrderEntriesGridRows().stream().filter(entry -> ((Checkbox)entry.getChildren().iterator().next()).isChecked())
                            .forEach(entry -> {
                                OrderEntryToCancelDto myEntry = (OrderEntryToCancelDto)((Row)entry).getValue();
                                myEntry.setSelectedReason(cancelReason.get());
                            });
        }
    }


    protected void handleIndividualCancelReason(Event event)
    {
        Optional<CancelReason> cancelReason = getCustomSelectedCancelReason(event);
        if(cancelReason.isPresent())
        {
            autoSelect(event);
            ((OrderEntryToCancelDto)((Row)event.getTarget().getParent()).getValue()).setSelectedReason(cancelReason.get());
        }
    }


    protected void handleRow(Row row)
    {
        OrderEntryToCancelDto myEntry = (OrderEntryToCancelDto)row.getValue();
        if(!((Checkbox)row.getChildren().iterator().next()).isChecked())
        {
            applyToRow(Integer.valueOf(0), 5, (Component)row);
            applyToRow(null, 6, (Component)row);
            applyToRow(null, 7, (Component)row);
            myEntry.setQuantityToCancel(Long.valueOf(0L));
            myEntry.setSelectedReason(null);
            myEntry.setCancelOrderEntryComment(null);
        }
        else
        {
            applyToRow(Integer.valueOf(this.globalCancelReasons.getSelectedIndex()), 6, (Component)row);
            applyToRow(this.globalCancelComment.getValue(), 7, (Component)row);
            Optional<CancelReason> reason = matchingComboboxCancelReason(
                            (this.globalCancelReasons.getSelectedItem() != null) ? this.globalCancelReasons.getSelectedItem().getLabel() : null);
            myEntry.setSelectedReason(reason.orElse(null));
            myEntry.setCancelOrderEntryComment(this.globalCancelComment.getValue());
        }
    }


    protected Optional<CancelReason> getCustomSelectedCancelReason(Event event)
    {
        Optional<CancelReason> reason = Optional.empty();
        if(event.getTarget() instanceof Combobox)
        {
            Object selectedValue = event.getData();
            reason = matchingComboboxCancelReason(selectedValue.toString());
        }
        return reason;
    }


    protected Optional<CancelReason> matchingComboboxCancelReason(String cancelReasonLabel)
    {
        return getEnumerationService().getEnumerationValues(CancelReason.class).stream()
                        .filter(reason -> getEnumerationService().getEnumerationName((HybrisEnumValue)reason, getLocale()).equals(cancelReasonLabel))
                        .findFirst();
    }


    protected void processCancellation(Event obj)
    {
        if(Messagebox.Button.YES.event.equals(obj.getName()))
        {
            try
            {
                OrderCancelRecordEntryModel orderCancelRecordEntry = getOrderCancelService().requestOrderCancel(buildCancelRequest(), (PrincipalModel)getUserService().getCurrentUser());
                switch(null.$SwitchMap$de$hybris$platform$basecommerce$enums$OrderCancelEntryStatus[orderCancelRecordEntry.getCancelResult().ordinal()])
                {
                    case 1:
                    case 2:
                        getNotificationService().notifyUser("", "JustMessage", NotificationEvent.Level.SUCCESS, new Object[] {getLabel("customersupportbackoffice.cancelorder.confirm.success")});
                        break;
                    case 3:
                        getNotificationService().notifyUser("", "JustMessage", NotificationEvent.Level.FAILURE, new Object[] {getLabel("customersupportbackoffice.cancelorder.confirm.error")});
                        break;
                }
            }
            catch(CancellationException | de.hybris.platform.ordercancel.OrderCancelException e)
            {
                LOGGER.info(e.getMessage(), e);
                getNotificationService().notifyUser("", "JustMessage", NotificationEvent.Level.FAILURE, new Object[] {getLabel("customersupportbackoffice.cancelorder.confirm.error")});
            }
            OrderModel object = (OrderModel)getModelService().get(getOrderModel().getPk());
            object.getEntries().forEach(entry -> getCockpitEventQueue().publishEvent((CockpitEvent)new DefaultCockpitEvent("objectsUpdated", entry, null)));
            sendOutput("confirmcancellation", COMPLETED);
        }
    }


    protected void selectAllEntries()
    {
        applyToGrid(Boolean.TRUE, 0);
        for(Component row : getOrderEntriesGridRows())
        {
            Component firstComponent = row.getChildren().iterator().next();
            if(firstComponent instanceof Checkbox)
            {
                ((Checkbox)firstComponent).setChecked(this.globalCancelEntriesSelection.isChecked());
            }
            handleRow((Row)row);
            if(this.globalCancelEntriesSelection.isChecked())
            {
                int cancellableQuantity = Integer.parseInt(((Label)row.getChildren().get(4)).getValue());
                applyToRow(Integer.valueOf(cancellableQuantity), 5, row);
            }
        }
        if(this.globalCancelEntriesSelection.isChecked())
        {
            this.orderEntriesToCancel.forEach(entry -> entry.setQuantityToCancel(this.orderCancellableEntries.get(entry.getOrderEntry())));
        }
    }


    protected void showMessageBox()
    {
        Messagebox.show(getLabel("customersupportbackoffice.cancelorder.confirm.msg"),
                        getLabel("customersupportbackoffice.cancelorder.confirm.title") + " " + getLabel("customersupportbackoffice.cancelorder.confirm.title"), new Messagebox.Button[] {Messagebox.Button.NO, Messagebox.Button.YES}, "oms-widget-cancelorder-confirm-icon", this::processCancellation);
    }


    protected Component targetFieldToApplyValidation(String stringToValidate, int indexLabelToCheck, int indexTargetComponent)
    {
        for(Component component : getOrderEntriesGridRows())
        {
            Label label = component.getChildren().get(indexLabelToCheck);
            if(label.getValue().equals(stringToValidate))
            {
                return component.getChildren().get(indexTargetComponent);
            }
        }
        return null;
    }


    protected void validateOrderEntry(OrderEntryToCancelDto entry)
    {
        if(entry.getQuantityToCancel().longValue() > ((Long)this.orderCancellableEntries.get(entry.getOrderEntry())).longValue())
        {
            InputElement quantity = (InputElement)targetFieldToApplyValidation(entry.getOrderEntry().getProduct().getCode(), 1, 5);
            throw new WrongValueException(quantity, getLabel("customersupportbackoffice.cancelorder.error.qtycancelled.invalid"));
        }
        if(entry.getSelectedReason() != null && entry.getQuantityToCancel().longValue() == 0L)
        {
            InputElement quantity = (InputElement)targetFieldToApplyValidation(entry.getOrderEntry().getProduct().getCode(), 1, 5);
            throw new WrongValueException(quantity, getLabel("customersupportbackoffice.cancelorder.missing.quantity"));
        }
        if(entry.getSelectedReason() == null && entry.getQuantityToCancel().longValue() > 0L)
        {
            Combobox reason = (Combobox)targetFieldToApplyValidation(entry.getOrderEntry().getProduct().getCode(), 1, 6);
            throw new WrongValueException(reason, getLabel("customersupportbackoffice.cancelorder.error.reason"));
        }
    }


    protected void validateRequest()
    {
        for(Component row : getOrderEntriesGridRows())
        {
            if(((Checkbox)row.getChildren().iterator().next()).isChecked())
            {
                InputElement cancelQty = row.getChildren().get(5);
                if(cancelQty.getRawValue().equals(Integer.valueOf(0)))
                {
                    throw new WrongValueException(cancelQty, getLabel("customersupportbackoffice.cancelorder.missing.quantity"));
                }
            }
        }
        ListModelList<OrderEntryToCancelDto> modelList = (ListModelList<OrderEntryToCancelDto>)getOrderEntries().getModel();
        if(modelList.stream().allMatch(entry -> (entry.getQuantityToCancel().longValue() == 0L)))
        {
            throw new WrongValueException(this.globalCancelEntriesSelection,
                            getLabel("customersupportbackoffice.cancelorder.missing.selectedLine"));
        }
        modelList.forEach(this::validateOrderEntry);
    }


    protected List<Component> getOrderEntriesGridRows()
    {
        return getOrderEntries().getRows().getChildren();
    }


    protected Locale getLocale()
    {
        return getCockpitLocaleService().getCurrentLocale();
    }


    protected BackofficeLocaleService getCockpitLocaleService()
    {
        return this.cockpitLocaleService;
    }


    protected Grid getOrderEntries()
    {
        return this.orderEntries;
    }


    protected OrderModel getOrderModel()
    {
        return this.orderModel;
    }


    public void setOrderModel(OrderModel orderModel)
    {
        this.orderModel = orderModel;
    }


    protected OrderCancelService getOrderCancelService()
    {
        return this.orderCancelService;
    }


    protected EnumerationService getEnumerationService()
    {
        return this.enumerationService;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    protected CockpitEventQueue getCockpitEventQueue()
    {
        return this.cockpitEventQueue;
    }


    protected UserService getUserService()
    {
        return this.userService;
    }


    protected NotificationService getNotificationService()
    {
        return this.notificationService;
    }
}
