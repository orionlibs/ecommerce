package de.hybris.platform.omsbackoffice.widgets.returns.createreturnrequest;

import com.hybris.backoffice.i18n.BackofficeLocaleService;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.core.events.CockpitEventQueue;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.cockpitng.util.notifications.NotificationService;
import de.hybris.platform.basecommerce.enums.RefundReason;
import de.hybris.platform.basecommerce.enums.ReturnAction;
import de.hybris.platform.basecommerce.enums.ReturnStatus;
import de.hybris.platform.commerceservices.event.CreateReturnEvent;
import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.omsbackoffice.widgets.returns.dtos.ReturnEntryToCreateDto;
import de.hybris.platform.refund.RefundService;
import de.hybris.platform.returns.OrderReturnRecordsHandlerException;
import de.hybris.platform.returns.ReturnService;
import de.hybris.platform.returns.model.RefundEntryModel;
import de.hybris.platform.returns.model.ReturnRequestModel;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.util.TaxValue;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.apache.log4j.Logger;
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
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.impl.InputElement;

public class CreateReturnRequestController extends DefaultWidgetController
{
    private static final Logger LOG = Logger.getLogger(CreateReturnRequestController.class.getName());
    private static final long serialVersionUID = 1L;
    private static final int DEFAULT_NUMBER_OF_DIGITS = 2;
    protected static final String IN_SOCKET = "inputObject";
    protected static final String OUT_CONFIRM = "confirm";
    protected static final Object COMPLETED = "completed";
    protected static final int COLUMN_INDEX_RETURNABLE_QUANTITY = 5;
    protected static final int COLUMN_INDEX_RETURN_QUANTITY = 6;
    protected static final int COLUMN_INDEX_RETURN_AMOUNT = 7;
    protected static final int COLUMN_INDEX_RETURN_REASON = 8;
    protected static final int COLUMN_INDEX_RETURN_COMMENT = 9;
    protected static final int DEFAULT_AMOUNT_SCALE = 2;
    private final List<String> refundReasons = new ArrayList<>();
    private transient Set<ReturnEntryToCreateDto> returnEntriesToCreate;
    private OrderModel order;
    @Wire
    private Textbox orderCode;
    @Wire
    private Textbox customer;
    @Wire
    private Doublebox totalDiscounts;
    @Wire
    private Doublebox orderTotal;
    @Wire
    private Combobox globalReason;
    @Wire
    private Textbox globalComment;
    @Wire
    private Grid returnEntries;
    @Wire
    private Checkbox isReturnInstore;
    @Wire
    private Checkbox refundDeliveryCost;
    @Wire
    private Checkbox globalReturnEntriesSelection;
    @Wire
    private Doublebox totalRefundAmount;
    @Wire
    private Doublebox estimatedTax;
    @Wire
    private Doublebox deliveryCost;
    @WireVariable
    private transient ReturnService returnService;
    @WireVariable
    private transient RefundService refundService;
    @WireVariable
    private transient EventService eventService;
    @WireVariable
    private transient EnumerationService enumerationService;
    @WireVariable
    private transient ModelService modelService;
    @WireVariable
    private transient BackofficeLocaleService cockpitLocaleService;
    @WireVariable
    private transient CockpitEventQueue cockpitEventQueue;
    @WireVariable
    private transient NotificationService notificationService;


    @SocketEvent(socketId = "inputObject")
    public void initCreateReturnRequestForm(OrderModel inputOrder)
    {
        setOrder(inputOrder);
        this.refundReasons.clear();
        this.isReturnInstore.setChecked(false);
        this.refundDeliveryCost.setChecked(false);
        this.globalReturnEntriesSelection.setChecked(false);
        this.deliveryCost.setValue(getOrder().getDeliveryCost());
        this.refundDeliveryCost.setDisabled(getReturnService().getReturnRequests(getOrder().getCode()).stream().anyMatch(returnRequest ->
                        (returnRequest.getRefundDeliveryCost().booleanValue() && returnRequest.getStatus() != ReturnStatus.CANCELED)));
        getWidgetInstanceManager().setTitle(
                        getWidgetInstanceManager().getLabel("customersupportbackoffice.createreturnrequest.confirm.title") + " " + getWidgetInstanceManager().getLabel("customersupportbackoffice.createreturnrequest.confirm.title"));
        this.orderCode.setValue(getOrder().getCode());
        this.customer.setValue(getOrder().getUser().getDisplayName());
        this.orderTotal.setValue(getOrder().getTotalPrice());
        setTotalDiscounts();
        Locale locale = getCockpitLocaleService().getCurrentLocale();
        getEnumerationService().getEnumerationValues(RefundReason.class)
                        .forEach(reason -> this.refundReasons.add(getEnumerationService().getEnumerationName((HybrisEnumValue)reason, locale)));
        this.globalReason.setModel((ListModel)new ListModelArray(this.refundReasons));
        Map<AbstractOrderEntryModel, Long> returnableOrderEntries = getReturnService().getAllReturnableEntries(inputOrder);
        this.returnEntriesToCreate = new HashSet<>();
        returnableOrderEntries.forEach((orderEntry, returnableQty) -> this.returnEntriesToCreate.add(new ReturnEntryToCreateDto(orderEntry, returnableQty.intValue(), this.refundReasons)));
        getReturnEntries().setModel((ListModel)new ListModelList(this.returnEntriesToCreate));
        getReturnEntries().renderAll();
        addListeners();
    }


    protected void addListeners()
    {
        List<Component> rows = this.returnEntries.getRows().getChildren();
        for(Iterator<Component> iterator = rows.iterator(); iterator.hasNext(); )
        {
            Component row = iterator.next();
            for(Component myComponent : row.getChildren())
            {
                if(myComponent instanceof Combobox)
                {
                    myComponent.addEventListener("onCustomChange", event -> Events.echoEvent("onLaterCustomChange", myComponent, event.getData()));
                    myComponent.addEventListener("onLaterCustomChange", event -> {
                        Clients.clearWrongValue(myComponent);
                        myComponent.invalidate();
                        handleIndividualRefundReason(event);
                    });
                    continue;
                }
                if(myComponent instanceof Checkbox)
                {
                    myComponent.addEventListener("onCheck", event -> {
                        handleRow((Row)event.getTarget().getParent());
                        calculateTotalRefundAmount();
                    });
                    continue;
                }
                if(myComponent instanceof Intbox)
                {
                    myComponent.addEventListener("onChanging", this::handleIndividualQuantityToReturn);
                    continue;
                }
                if(myComponent instanceof Doublebox)
                {
                    myComponent.addEventListener("onChanging", this::handleIndividualAmountToReturn);
                    myComponent.addEventListener("onFocus", this::handleIndividualAmountToReturnFocus);
                    myComponent.addEventListener("onBlur", this::handleIndividualAmountToReturnBlur);
                    continue;
                }
                if(myComponent instanceof Textbox)
                {
                    myComponent.addEventListener("onChanging", event -> {
                        autoSelect(event);
                        ((ReturnEntryToCreateDto)((Row)event.getTarget().getParent()).getValue()).setRefundEntryComment(((InputEvent)event).getValue());
                    });
                }
            }
        }
        this.globalReason.addEventListener("onSelect", this::handleGlobalReason);
        this.globalComment.addEventListener("onChanging", this::handleGlobalComment);
        this.globalReturnEntriesSelection.addEventListener("onCheck", event -> selectAllEntries());
        this.refundDeliveryCost.addEventListener("onClick", event -> calculateTotalRefundAmount());
    }


    protected void handleIndividualAmountToReturn(Event event)
    {
        ((Checkbox)event.getTarget().getParent().getChildren().iterator().next()).setChecked(true);
        Row myRow = (Row)event.getTarget().getParent();
        ReturnEntryToCreateDto returnEntryDto = (ReturnEntryToCreateDto)myRow.getValue();
        String refundAmountStr = ((InputEvent)event).getValue();
        BigDecimal newAmount = (refundAmountStr != null && !refundAmountStr.isEmpty()) ? BigDecimal.valueOf(Double.parseDouble(refundAmountStr)) : BigDecimal.ZERO;
        returnEntryDto.getRefundEntry().setAmount(newAmount);
        calculateIndividualTaxEstimate(returnEntryDto);
        calculateTotalRefundAmount();
    }


    protected void handleIndividualAmountToReturnFocus(Event event)
    {
        applyAmountToRefundInput(event, false);
    }


    protected void handleIndividualAmountToReturnBlur(Event event)
    {
        applyAmountToRefundInput(event, true);
    }


    protected int getDigitsNumber(RefundEntryModel refundEntryModel)
    {
        int digitsNumber = 2;
        if(refundEntryModel != null)
        {
            digitsNumber = refundEntryModel.getOrderEntry().getOrder().getCurrency().getDigits().intValue();
        }
        return digitsNumber;
    }


    private void applyAmountToRefundInput(Event event, boolean rounded)
    {
        Row myRow = (Row)event.getTarget().getParent();
        ReturnEntryToCreateDto returnEntryDto = (ReturnEntryToCreateDto)myRow.getValue();
        BigDecimal refundAmountTemp = (returnEntryDto.getRefundEntry().getAmount() != null) ? returnEntryDto.getRefundEntry().getAmount() : BigDecimal.ZERO;
        if(rounded)
        {
            applyToRow(Double.valueOf(refundAmountTemp.setScale(getDigitsNumber(returnEntryDto.getRefundEntry()), RoundingMode.HALF_DOWN).doubleValue()), 7, (Component)myRow);
        }
        else
        {
            applyToRow(Double.valueOf(refundAmountTemp.doubleValue()), 7, (Component)myRow);
        }
    }


    protected void calculateIndividualTaxEstimate(ReturnEntryToCreateDto returnEntryDto)
    {
        if(returnEntryDto.getQuantityToReturn() <= returnEntryDto.getReturnableQuantity())
        {
            RefundEntryModel refundEntry = returnEntryDto.getRefundEntry();
            Optional<TaxValue> orderEntryOptional = refundEntry.getOrderEntry().getTaxValues().stream().findFirst();
            BigDecimal orderEntryTax = BigDecimal.ZERO;
            if(orderEntryOptional.isPresent())
            {
                orderEntryTax = BigDecimal.valueOf(((TaxValue)orderEntryOptional.get()).getAppliedValue());
            }
            BigDecimal alignedRefund = refundEntry.getAmount().setScale(refundEntry.getOrderEntry().getOrder().getCurrency().getDigits().intValue(), RoundingMode.HALF_UP);
            if(alignedRefund.compareTo(BigDecimal.valueOf(refundEntry.getOrderEntry().getTotalPrice().doubleValue())) >= 0)
            {
                returnEntryDto.setTax(orderEntryTax);
            }
            else
            {
                BigDecimal returnEntryTax = orderEntryTax.multiply(refundEntry.getAmount()).divide(BigDecimal.valueOf(refundEntry.getOrderEntry().getTotalPrice().doubleValue()), RoundingMode.HALF_UP)
                                .setScale(refundEntry.getOrderEntry().getOrder().getCurrency().getDigits().intValue(), RoundingMode.HALF_UP);
                returnEntryDto.setTax(returnEntryTax);
            }
        }
    }


    protected void handleIndividualQuantityToReturn(Event event)
    {
        autoSelect(event);
        Row myRow = (Row)event.getTarget().getParent();
        ReturnEntryToCreateDto myReturnEntry = (ReturnEntryToCreateDto)myRow.getValue();
        String returnQuantityStr = ((InputEvent)event).getValue();
        int amountEntered = (returnQuantityStr != null && !returnQuantityStr.isEmpty()) ? Integer.parseInt(returnQuantityStr) : 0;
        calculateRowAmount(myRow, myReturnEntry, amountEntered);
    }


    protected void handleIndividualRefundReason(Event event)
    {
        Optional<RefundReason> refundReason = getCustomSelectedRefundReason(event);
        if(refundReason.isPresent())
        {
            autoSelect(event);
            ((ReturnEntryToCreateDto)((Row)event.getTarget().getParent()).getValue()).getRefundEntry()
                            .setReason(refundReason.get());
        }
    }


    protected void handleGlobalComment(Event event)
    {
        applyToGrid(((InputEvent)event).getValue(), 9);
        this.returnEntries.getRows().getChildren().stream()
                        .filter(entry -> ((Checkbox)entry.getChildren().iterator().next()).isChecked()).forEach(entry -> ((ReturnEntryToCreateDto)((Row)entry).getValue()).setRefundEntryComment(((InputEvent)event).getValue()));
    }


    protected void handleGlobalReason(Event event)
    {
        Optional<RefundReason> refundReason = getSelectedRefundReason(event);
        if(refundReason.isPresent())
        {
            applyToGrid(Integer.valueOf(getReasonIndex(refundReason.get())), 8);
            this.returnEntries.getRows().getChildren().stream()
                            .filter(entry -> ((Checkbox)entry.getChildren().iterator().next()).isChecked()).forEach(entry -> ((ReturnEntryToCreateDto)((Row)entry).getValue()).getRefundEntry().setReason(refundReason.get()));
        }
    }


    protected void calculateRowAmount(Row myRow, ReturnEntryToCreateDto myReturnEntry, int qtyEntered)
    {
        BigDecimal newAmount = myReturnEntry.isDiscountApplied() ? BigDecimal.ZERO : BigDecimal.valueOf(myReturnEntry.getRefundEntry().getOrderEntry().getBasePrice().doubleValue() * qtyEntered);
        myReturnEntry.setQuantityToReturn(qtyEntered);
        myReturnEntry.getRefundEntry().setAmount(newAmount);
        applyToRow(Double.valueOf(newAmount.setScale(2, RoundingMode.HALF_EVEN).doubleValue()), 7, (Component)myRow);
        calculateIndividualTaxEstimate(myReturnEntry);
        calculateTotalRefundAmount();
    }


    protected void calculateTotalRefundAmount()
    {
        calculateEstimatedTax();
        Double calculatedRefundAmount = Double.valueOf(this.refundDeliveryCost.isChecked() ? getOrder().getDeliveryCost().doubleValue() : 0.0D);
        calculatedRefundAmount = Double.valueOf(calculatedRefundAmount.doubleValue() + ((BigDecimal)this.returnEntriesToCreate.stream().map(entry -> entry.getRefundEntry().getAmount())
                        .reduce(BigDecimal.ZERO, BigDecimal::add)).doubleValue());
        double totalValue = getOrder().getNet().booleanValue() ? BigDecimal.valueOf(calculatedRefundAmount.doubleValue()).add(BigDecimal.valueOf(this.estimatedTax.doubleValue())).doubleValue() : BigDecimal.valueOf(calculatedRefundAmount.doubleValue()).doubleValue();
        this.totalRefundAmount.setValue(totalValue);
    }


    protected void calculateEstimatedTax()
    {
        BigDecimal totalTax = this.returnEntriesToCreate.stream().filter(returnEntryToCreate -> (returnEntryToCreate.getQuantityToReturn() > 0 && returnEntryToCreate.getTax() != null)).map(ReturnEntryToCreateDto::getTax).reduce(BigDecimal.ZERO, BigDecimal::add);
        if(this.refundDeliveryCost.isChecked())
        {
            BigDecimal deliveryCostTax = BigDecimal.valueOf(
                            getOrder().getTotalTax().doubleValue() - getOrder().getEntries().stream().filter(entry -> !entry.getTaxValues().isEmpty())
                                            .mapToDouble(entry -> ((TaxValue)entry.getTaxValues().stream().findFirst().get()).getAppliedValue()).sum());
            totalTax = totalTax.add(deliveryCostTax);
        }
        double totalTaxChecked = Double.min(totalTax.doubleValue(), getOrder().getTotalTax().doubleValue());
        this.estimatedTax.setValue(totalTaxChecked);
    }


    protected void autoSelect(Event event)
    {
        ((Checkbox)event.getTarget().getParent().getChildren().iterator().next()).setChecked(true);
    }


    protected void handleRow(Row row)
    {
        ReturnEntryToCreateDto myEntry = (ReturnEntryToCreateDto)row.getValue();
        if(row.getChildren().iterator().next() instanceof Checkbox)
        {
            if(!((Checkbox)row.getChildren().iterator().next()).isChecked())
            {
                applyToRow(Integer.valueOf(0), 6, (Component)row);
                applyToRow(null, 8, (Component)row);
                applyToRow(Double.valueOf(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_EVEN).doubleValue()), 7, (Component)row);
                applyToRow(null, 9, (Component)row);
                myEntry.setQuantityToReturn(0);
                myEntry.getRefundEntry().setAmount(BigDecimal.ZERO);
                myEntry.getRefundEntry().setReason(null);
                myEntry.setRefundEntryComment(null);
            }
            else
            {
                applyToRow(Integer.valueOf(this.globalReason.getSelectedIndex()), 8, (Component)row);
                applyToRow(this.globalComment.getValue(), 9, (Component)row);
                Optional<RefundReason> reason = matchingComboboxReturnReason(
                                (this.globalReason.getSelectedItem() != null) ? this.globalReason.getSelectedItem().getLabel() : null);
                myEntry.getRefundEntry().setReason(reason.isPresent() ? reason.get() : null);
                myEntry.setRefundEntryComment(this.globalComment.getValue());
            }
        }
        calculateTotalRefundAmount();
    }


    protected void selectAllEntries()
    {
        applyToGrid(Boolean.TRUE, 0);
        for(Component row : this.returnEntries.getRows().getChildren())
        {
            Component firstComponent = row.getChildren().iterator().next();
            if(firstComponent instanceof Checkbox)
            {
                ((Checkbox)firstComponent).setChecked(this.globalReturnEntriesSelection.isChecked());
            }
            handleRow((Row)row);
            if(this.globalReturnEntriesSelection.isChecked())
            {
                int returnableQty = Integer.parseInt(((Label)row.getChildren().get(5)).getValue());
                applyToRow(Integer.valueOf(returnableQty), 6, row);
                calculateRowAmount((Row)row, (ReturnEntryToCreateDto)((Row)row).getValue(), returnableQty);
            }
        }
        if(this.globalReturnEntriesSelection.isChecked())
        {
            this.returnEntriesToCreate.forEach(entry -> entry.setQuantityToReturn(entry.getReturnableQuantity()));
            calculateTotalRefundAmount();
        }
    }


    protected int getReasonIndex(RefundReason refundReason)
    {
        int index = 0;
        String myReason = getEnumerationService().getEnumerationName((HybrisEnumValue)refundReason, getCockpitLocaleService().getCurrentLocale());
        for(String reason : this.refundReasons)
        {
            if(myReason.equals(reason))
            {
                break;
            }
            index++;
        }
        return index;
    }


    protected Optional<RefundReason> getSelectedRefundReason(Event event)
    {
        Optional<RefundReason> result = Optional.empty();
        if(!((SelectEvent)event).getSelectedItems().isEmpty())
        {
            Object selectedValue = ((Comboitem)((SelectEvent)event).getSelectedItems().iterator().next()).getValue();
            result = matchingComboboxReturnReason(selectedValue.toString());
        }
        return result;
    }


    protected Optional<RefundReason> getCustomSelectedRefundReason(Event event)
    {
        Optional<RefundReason> reason = Optional.empty();
        if(event.getTarget() instanceof Combobox)
        {
            Object selectedValue = event.getData();
            reason = matchingComboboxReturnReason(selectedValue.toString());
        }
        return reason;
    }


    protected void applyToGrid(Object data, int childrenIndex)
    {
        for(Component row : this.returnEntries.getRows().getChildren())
        {
            Component firstComponent = row.getChildren().iterator().next();
            if(firstComponent instanceof Checkbox && ((Checkbox)firstComponent).isChecked())
            {
                applyToRow(data, childrenIndex, row);
            }
        }
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
                if(!(data instanceof Integer))
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
            else if(myComponent instanceof Doublebox)
            {
                ((Doublebox)myComponent).setValue((Double)data);
            }
            else if(myComponent instanceof Textbox)
            {
                ((Textbox)myComponent).setValue((String)data);
            }
            index++;
        }
    }


    @ViewEvent(componentID = "resetcreatereturnrequest", eventName = "onClick")
    public void reset()
    {
        this.globalReason.setSelectedItem(null);
        this.globalComment.setValue("");
        initCreateReturnRequestForm(getOrder());
        calculateTotalRefundAmount();
    }


    @ViewEvent(componentID = "confirmcreatereturnrequest", eventName = "onClick")
    public void confirmCreation()
    {
        validateRequest();
        try
        {
            ReturnRequestModel returnRequest = getReturnService().createReturnRequest(getOrder());
            returnRequest.setRefundDeliveryCost(Boolean.valueOf(this.refundDeliveryCost.isChecked()));
            ReturnStatus status = this.isReturnInstore.isChecked() ? ReturnStatus.RECEIVED : ReturnStatus.APPROVAL_PENDING;
            returnRequest.setStatus(status);
            getModelService().save(returnRequest);
            this.returnEntriesToCreate.stream().filter(entry -> (entry.getQuantityToReturn() != 0))
                            .forEach(entry -> createRefundWithCustomAmount(returnRequest, entry));
            applyReturnRequest(returnRequest);
            CreateReturnEvent createReturnEvent = new CreateReturnEvent();
            createReturnEvent.setReturnRequest(returnRequest);
            getEventService().publishEvent((AbstractEvent)createReturnEvent);
            getNotificationService()
                            .notifyUser("", "JustMessage", NotificationEvent.Level.SUCCESS,
                                            new Object[] {getWidgetInstanceManager().getLabel("customersupportbackoffice.createreturnrequest.confirm.success") + " - " + getWidgetInstanceManager().getLabel("customersupportbackoffice.createreturnrequest.confirm.success")});
        }
        catch(Exception e)
        {
            LOG.info(e.getMessage(), e);
            getNotificationService()
                            .notifyUser("", "JustMessage", NotificationEvent.Level.FAILURE, new Object[] {getWidgetInstanceManager().getLabel("customersupportbackoffice.createreturnrequest.confirm.error")});
        }
        sendOutput("confirm", COMPLETED);
    }


    private void applyReturnRequest(ReturnRequestModel returnRequest) throws OrderReturnRecordsHandlerException
    {
        try
        {
            getRefundService().apply(returnRequest.getOrder(), returnRequest);
        }
        catch(IllegalStateException ise)
        {
            LOG.info("Order " + getOrder().getCode() + " Return record already in progress");
        }
    }


    protected RefundEntryModel createRefundWithCustomAmount(ReturnRequestModel returnRequest, ReturnEntryToCreateDto entry)
    {
        ReturnAction actionToExecute = this.isReturnInstore.isChecked() ? ReturnAction.IMMEDIATE : ReturnAction.HOLD;
        RefundEntryModel refundEntryToBeCreated = getReturnService().createRefund(returnRequest, entry.getRefundEntry().getOrderEntry(), entry.getRefundEntryComment(),
                        Long.valueOf(entry.getQuantityToReturn()), actionToExecute, entry.getRefundEntry().getReason());
        refundEntryToBeCreated.setAmount(entry.getRefundEntry().getAmount());
        returnRequest.setSubtotal(returnRequest.getSubtotal().add(entry.getRefundEntry().getAmount()));
        getModelService().save(refundEntryToBeCreated);
        return refundEntryToBeCreated;
    }


    protected void validateReturnEntry(ReturnEntryToCreateDto entry)
    {
        if(entry.getQuantityToReturn() > entry.getReturnableQuantity())
        {
            InputElement quantity = (InputElement)targetFieldToApplyValidation(entry
                            .getRefundEntry().getOrderEntry().getProduct().getCode(), 1, 6);
            throw new WrongValueException(quantity,
                            getLabel("customersupportbackoffice.createreturnrequest.validation.invalid.quantity"));
        }
        if(entry.getRefundEntry().getReason() != null && entry.getQuantityToReturn() == 0)
        {
            InputElement quantity = (InputElement)targetFieldToApplyValidation(entry
                            .getRefundEntry().getOrderEntry().getProduct().getCode(), 1, 6);
            throw new WrongValueException(quantity,
                            getLabel("customersupportbackoffice.createreturnrequest.validation.missing.quantity"));
        }
        if(entry.getRefundEntry().getReason() == null && entry.getQuantityToReturn() > 0)
        {
            Combobox combobox = (Combobox)targetFieldToApplyValidation(entry
                            .getRefundEntry().getOrderEntry().getProduct().getCode(), 1, 8);
            throw new WrongValueException(combobox,
                            getLabel("customersupportbackoffice.createreturnrequest.validation.missing.reason"));
        }
        if(entry.getQuantityToReturn() > 0 && entry.getRefundEntry().getAmount().compareTo(BigDecimal.ZERO) <= 0)
        {
            InputElement amountInput = (InputElement)targetFieldToApplyValidation(entry
                            .getRefundEntry().getOrderEntry().getProduct().getCode(), 1, 7);
            throw new WrongValueException(amountInput,
                            getLabel("customersupportbackoffice.createreturnrequest.validation.invalid.amount"));
        }
    }


    protected void validateRequest()
    {
        for(Component row : getReturnEntries().getRows().getChildren())
        {
            Component firstComponent = row.getChildren().iterator().next();
            if(firstComponent instanceof Checkbox && ((Checkbox)firstComponent).isChecked())
            {
                InputElement returnQty = row.getChildren().get(6);
                if(returnQty.getRawValue().equals(Integer.valueOf(0)))
                {
                    throw new WrongValueException(returnQty,
                                    getLabel("customersupportbackoffice.createreturnrequest.validation.missing.quantity"));
                }
            }
        }
        ListModelList<ReturnEntryToCreateDto> modelList = (ListModelList<ReturnEntryToCreateDto>)getReturnEntries().getModel();
        if(modelList.stream().allMatch(entry -> (entry.getQuantityToReturn() == 0)))
        {
            throw new WrongValueException(this.globalReturnEntriesSelection,
                            getLabel("customersupportbackoffice.createreturnrequest.validation.missing.selectedLine"));
        }
        modelList.forEach(this::validateReturnEntry);
    }


    protected Component targetFieldToApplyValidation(String stringToValidate, int indexLabelToCheck, int indexTargetComponent)
    {
        for(Component component : this.returnEntries.getRows().getChildren())
        {
            Label label = component.getChildren().get(indexLabelToCheck);
            if(label.getValue().equals(stringToValidate))
            {
                return component.getChildren().get(indexTargetComponent);
            }
        }
        return null;
    }


    protected Optional<RefundReason> matchingComboboxReturnReason(String refundReasonLabel)
    {
        return getEnumerationService().getEnumerationValues(RefundReason.class).stream().filter(reason -> getEnumerationService().getEnumerationName((HybrisEnumValue)reason, getCockpitLocaleService().getCurrentLocale()).equals(refundReasonLabel))
                        .findFirst();
    }


    protected void setTotalDiscounts()
    {
        Double totalDiscount = Double.valueOf((getOrder().getTotalDiscounts() != null) ? getOrder().getTotalDiscounts().doubleValue() : 0.0D);
        totalDiscount = Double.valueOf(totalDiscount.doubleValue() + getOrder().getEntries().stream()
                        .mapToDouble(entry -> entry.getDiscountValues().stream().mapToDouble(DiscountValue::getAppliedValue).sum()).sum());
        this.totalDiscounts.setValue(totalDiscount);
    }


    protected OrderModel getOrder()
    {
        return this.order;
    }


    public void setOrder(OrderModel order)
    {
        this.order = order;
    }


    public Grid getReturnEntries()
    {
        return this.returnEntries;
    }


    public void setReturnEntries(Grid returnEntries)
    {
        this.returnEntries = returnEntries;
    }


    public ReturnService getReturnService()
    {
        return this.returnService;
    }


    public void setReturnService(ReturnService returnService)
    {
        this.returnService = returnService;
    }


    public EventService getEventService()
    {
        return this.eventService;
    }


    public void setEventService(EventService eventService)
    {
        this.eventService = eventService;
    }


    protected EnumerationService getEnumerationService()
    {
        return this.enumerationService;
    }


    public void setEnumerationService(EnumerationService enumerationService)
    {
        this.enumerationService = enumerationService;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected BackofficeLocaleService getCockpitLocaleService()
    {
        return this.cockpitLocaleService;
    }


    public void setCockpitLocaleService(BackofficeLocaleService cockpitLocaleService)
    {
        this.cockpitLocaleService = cockpitLocaleService;
    }


    protected CockpitEventQueue getCockpitEventQueue()
    {
        return this.cockpitEventQueue;
    }


    public void setCockpitEventQueue(CockpitEventQueue cockpitEventQueue)
    {
        this.cockpitEventQueue = cockpitEventQueue;
    }


    protected RefundService getRefundService()
    {
        return this.refundService;
    }


    public void setRefundService(RefundService refundService)
    {
        this.refundService = refundService;
    }


    protected NotificationService getNotificationService()
    {
        return this.notificationService;
    }


    public void setNotificationService(NotificationService notificationService)
    {
        this.notificationService = notificationService;
    }
}
