package de.hybris.platform.warehousingbackoffice.widgets.consignment;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.hybris.backoffice.i18n.BackofficeLocaleService;
import com.hybris.backoffice.widgets.notificationarea.NotificationService;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.core.events.CockpitEvent;
import com.hybris.cockpitng.core.events.CockpitEventQueue;
import com.hybris.cockpitng.core.events.impl.DefaultCockpitEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;
import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.ordersplitting.WarehouseService;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.processengine.model.BusinessProcessParameterModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.warehousing.data.allocation.DeclineEntries;
import de.hybris.platform.warehousing.data.allocation.DeclineEntry;
import de.hybris.platform.warehousing.enums.DeclineReason;
import de.hybris.platform.warehousing.process.WarehousingBusinessProcessService;
import de.hybris.platform.warehousing.sourcing.filter.SourcingFilterProcessor;
import de.hybris.platform.warehousing.stock.services.impl.DefaultWarehouseStockService;
import de.hybris.platform.warehousingbackoffice.dtos.ConsignmentEntryToReallocateDto;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.collections4.CollectionUtils;
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
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.impl.InputElement;

public class ConsignmentToReallocateController extends DefaultWidgetController
{
    private static final long serialVersionUID = 1L;
    protected static final String IN_SOCKET = "consignmentInput";
    protected static final String OUT_CONFIRM = "confirmOutput";
    protected static final Object COMPLETED = "completed";
    protected static final String CONSIGNMENT_ACTION_EVENT_NAME = "ConsignmentActionEvent";
    protected static final String REALLOCATE_CONSIGNMENT_CHOICE = "reallocateConsignment";
    protected static final String DECLINE_ENTRIES = "declineEntries";
    protected static final int COLUMN_INDEX_REALLOCATION_QUANTITY = 4;
    protected static final int COLUMN_INDEX_REALLOCATION_REASON = 5;
    protected static final int COLUMN_INDEX_REALLOCATION_LOCATION = 6;
    protected static final int COLUMN_INDEX_REALLOCATION_COMMENT = 7;
    private transient Set<ConsignmentEntryToReallocateDto> consignmentsEntriesToReallocate;
    private ConsignmentModel consignment;
    @Wire
    private Textbox consignmentCode;
    @Wire
    private Textbox customerName;
    @Wire
    private Combobox globalDeclineReasons;
    @Wire
    private Textbox globalDeclineComment;
    @Wire
    private Grid consignmentEntries;
    @Wire
    private Combobox globalPossibleLocations;
    @Wire
    private Checkbox globalDeclineEntriesSelection;
    private final List<String> declineReasons = new ArrayList<>();
    private final Set<WarehouseModel> locations = Sets.newHashSet();
    @WireVariable
    private transient SourcingFilterProcessor sourcingFilterProcessor;
    @WireVariable
    private transient WarehouseService warehouseService;
    @WireVariable
    private transient EnumerationService enumerationService;
    @WireVariable
    private transient WarehousingBusinessProcessService<ConsignmentModel> consignmentBusinessProcessService;
    @WireVariable
    private transient ModelService modelService;
    @WireVariable
    private transient BackofficeLocaleService cockpitLocaleService;
    @WireVariable
    private transient CockpitEventQueue cockpitEventQueue;
    @WireVariable
    private transient DefaultWarehouseStockService warehouseStockService;
    @WireVariable
    private transient NotificationService notificationService;


    @SocketEvent(socketId = "consignmentInput")
    public void initReallocationConsignmentForm(ConsignmentModel inputObject)
    {
        this.declineReasons.clear();
        this.locations.clear();
        this.globalDeclineEntriesSelection.setChecked(false);
        setConsignment(inputObject);
        getWidgetInstanceManager().setTitle(
                        getWidgetInstanceManager().getLabel("warehousingbackoffice.reallocationconsignment.title") + " " + getWidgetInstanceManager().getLabel("warehousingbackoffice.reallocationconsignment.title"));
        this.consignmentCode.setValue(getConsignment().getCode());
        this.customerName.setValue(getConsignment().getOrder().getUser().getDisplayName());
        Locale locale = getCockpitLocaleService().getCurrentLocale();
        getEnumerationService().getEnumerationValues(DeclineReason.class).stream()
                        .filter(reason -> !reason.equals(DeclineReason.ASNCANCELLATION))
                        .forEach(reason -> this.declineReasons.add(getEnumerationService().getEnumerationName((HybrisEnumValue)reason, locale)));
        this.sourcingFilterProcessor.filterLocations(inputObject.getOrder(), this.locations);
        if(this.locations.contains(getConsignment().getWarehouse()))
        {
            this.locations.remove(getConsignment().getWarehouse());
        }
        this.globalDeclineReasons.setModel((ListModel)new ListModelArray(this.declineReasons));
        this.globalPossibleLocations.setModel((ListModel)new ListModelArray(this.locations.toArray()));
        this.consignmentsEntriesToReallocate = new HashSet<>();
        getConsignment().getConsignmentEntries().stream().filter(entry -> (entry.getQuantityPending().longValue() > 0L)).forEach(entry -> this.consignmentsEntriesToReallocate.add(new ConsignmentEntryToReallocateDto(entry, this.declineReasons, this.locations)));
        getConsignmentEntries().setModel((ListModel)new ListModelList(this.consignmentsEntriesToReallocate));
        getConsignmentEntries().renderAll();
        addListeners();
    }


    @ViewEvent(componentID = "confirmreallocation", eventName = "onClick")
    public void confirmReallocation() throws InterruptedException
    {
        validateRequest();
        String consignmentProcessCode = this.consignment.getCode() + "_ordermanagement";
        Optional<ConsignmentProcessModel> myConsignmentProcess = this.consignment.getConsignmentProcesses().stream().filter(consignmentProcess -> consignmentProcess.getCode().equals(consignmentProcessCode)).findFirst();
        Collection<DeclineEntry> entriesToReallocate = new ArrayList<>();
        if(myConsignmentProcess.isPresent())
        {
            List<Component> rows = this.consignmentEntries.getRows().getChildren();
            rows.stream().filter(entry -> ((Checkbox)entry.getFirstChild()).isChecked())
                            .forEach(entry -> createDeclineEntry(entriesToReallocate, entry));
        }
        if(!entriesToReallocate.isEmpty())
        {
            buildDeclineParam(myConsignmentProcess.isPresent() ? myConsignmentProcess.get() : null, entriesToReallocate);
            getConsignmentBusinessProcessService()
                            .triggerChoiceEvent((ItemModel)getConsignment(), "ConsignmentActionEvent", "reallocateConsignment");
            ConsignmentModel refreshedConsignment = (ConsignmentModel)getModelService().get(getConsignment().getPk());
            int iterationLimit = 500000;
            int iterationCount = 0;
            while(!isDeclineProcessDone(refreshedConsignment, entriesToReallocate) && iterationCount < 500000)
            {
                getModelService().refresh(refreshedConsignment);
                iterationCount++;
            }
            refreshedConsignment.getConsignmentEntries().forEach(entry -> getCockpitEventQueue().publishEvent((CockpitEvent)new DefaultCockpitEvent("objectsUpdated", entry, null)));
            setConsignment(refreshedConsignment);
            getNotificationService()
                            .notifyUser("", "JustMessage", NotificationEvent.Level.SUCCESS, new Object[] {getLabel("warehousingbackoffice.reallocationconsignment.success.message")});
        }
        else
        {
            getNotificationService()
                            .notifyUser("", "JustMessage", NotificationEvent.Level.FAILURE, new Object[] {getLabel("warehousingbackoffice.reallocationconsignment.error.message")});
        }
        sendOutput("confirmOutput", COMPLETED);
    }


    protected void createDeclineEntry(Collection<DeclineEntry> entriesToReallocate, Component component)
    {
        ConsignmentEntryToReallocateDto consignmentEntryToReallocate = (ConsignmentEntryToReallocateDto)((Row)component).getValue();
        Long qtyToReallocate = consignmentEntryToReallocate.getQuantityToReallocate();
        Long qtyAvailableForReallocation = consignmentEntryToReallocate.getConsignmentEntry().getQuantityPending();
        if(qtyToReallocate.longValue() > 0L && qtyToReallocate.longValue() <= qtyAvailableForReallocation.longValue())
        {
            DeclineEntry newEntry = new DeclineEntry();
            newEntry.setQuantity(qtyToReallocate);
            newEntry.setConsignmentEntry(consignmentEntryToReallocate.getConsignmentEntry());
            newEntry.setNotes(consignmentEntryToReallocate.getDeclineConsignmentEntryComment());
            newEntry.setReallocationWarehouse(consignmentEntryToReallocate.getSelectedLocation());
            newEntry.setReason(consignmentEntryToReallocate.getSelectedReason());
            entriesToReallocate.add(newEntry);
        }
    }


    @ViewEvent(componentID = "undoreallocation", eventName = "onClick")
    public void reset()
    {
        this.globalDeclineReasons.setSelectedItem(null);
        this.globalPossibleLocations.setSelectedItem(null);
        this.globalDeclineComment.setValue("");
        initReallocationConsignmentForm(getConsignment());
    }


    protected void addListeners()
    {
        List<Component> rows = this.consignmentEntries.getRows().getChildren();
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
                    myComponent.addEventListener("onSelect", this::handleIndividualLocation);
                    myComponent.addEventListener("onCustomChange", event -> Events.echoEvent("onLaterCustomChange", myComponent, event.getData()));
                    myComponent.addEventListener("onLaterCustomChange", event -> {
                        Clients.clearWrongValue(myComponent);
                        myComponent.invalidate();
                        handleIndividualReason(event);
                    });
                    continue;
                }
                if(myComponent instanceof Intbox)
                {
                    myComponent.addEventListener("onChange", event -> {
                        autoSelect(event);
                        ((ConsignmentEntryToReallocateDto)((Row)event.getTarget().getParent()).getValue()).setQuantityToReallocate(Long.valueOf(Long.parseLong(((InputEvent)event).getValue())));
                    });
                    continue;
                }
                if(myComponent instanceof Textbox)
                {
                    myComponent.addEventListener("onChanging", event -> {
                        autoSelect(event);
                        ((ConsignmentEntryToReallocateDto)((Row)event.getTarget().getParent()).getValue()).setDeclineConsignmentEntryComment(((InputEvent)event).getValue());
                    });
                }
            }
        }
        this.globalDeclineReasons.addEventListener("onSelect", this::handleGlobalReason);
        this.globalPossibleLocations.addEventListener("onSelect", this::handleGlobalLocation);
        this.globalDeclineComment.addEventListener("onChanging", this::handleGlobalComment);
        this.globalDeclineEntriesSelection.addEventListener("onCheck", event -> selectAllEntries());
    }


    protected void applyToGrid(Object data, int childrenIndex)
    {
        this.consignmentEntries.getRows().getChildren().stream()
                        .filter(entry -> ((Checkbox)entry.getChildren().iterator().next()).isChecked())
                        .forEach(entry -> applyToRow(data, childrenIndex, entry));
    }


    protected void applyToRow(Object data, int childrenIndex, Component row)
    {
        int index = 0;
        for(Component myComponent : row.getChildren())
        {
            if(index == childrenIndex)
            {
                applyToCheckboxRow(data, myComponent);
                applyToComboboxRow(data, myComponent);
                if(myComponent instanceof Intbox)
                {
                    ((Intbox)myComponent).setValue((Integer)data);
                }
                else if(!(myComponent instanceof Combobox) && myComponent instanceof Textbox)
                {
                    ((Textbox)myComponent).setValue((String)data);
                }
            }
            index++;
        }
    }


    protected void applyToComboboxRow(Object data, Component component)
    {
        if(component instanceof Combobox)
        {
            if(data == null)
            {
                ((Combobox)component).setSelectedItem(null);
            }
            else
            {
                ((Combobox)component).setSelectedIndex(((Integer)data).intValue());
            }
        }
    }


    protected void applyToCheckboxRow(Object data, Component component)
    {
        if(component instanceof Checkbox)
        {
            if(data == null)
            {
                ((Checkbox)component).setChecked(Boolean.FALSE.booleanValue());
            }
            else
            {
                ((Checkbox)component).setChecked(((Boolean)data).booleanValue());
            }
        }
    }


    protected void autoSelect(Event event)
    {
        ((Checkbox)event.getTarget().getParent().getChildren().iterator().next()).setChecked(true);
    }


    protected void buildDeclineParam(ConsignmentProcessModel processModel, Collection<DeclineEntry> entriesToReallocate)
    {
        cleanDeclineParam(processModel);
        Collection<BusinessProcessParameterModel> contextParams = new ArrayList<>();
        contextParams.addAll(processModel.getContextParameters());
        DeclineEntries declinedEntries = new DeclineEntries();
        declinedEntries.setEntries(entriesToReallocate);
        BusinessProcessParameterModel declineParam = new BusinessProcessParameterModel();
        declineParam.setName("declineEntries");
        declineParam.setValue(declinedEntries);
        declineParam.setProcess((BusinessProcessModel)processModel);
        contextParams.add(declineParam);
        processModel.setContextParameters(contextParams);
        getModelService().save(processModel);
    }


    protected void cleanDeclineParam(ConsignmentProcessModel processModel)
    {
        Collection<BusinessProcessParameterModel> contextParams = new ArrayList<>();
        contextParams.addAll(processModel.getContextParameters());
        if(CollectionUtils.isNotEmpty(contextParams))
        {
            Optional<BusinessProcessParameterModel> declineEntriesParamOptional = contextParams.stream().filter(param -> param.getName().equals("declineEntries")).findFirst();
            if(declineEntriesParamOptional.isPresent())
            {
                BusinessProcessParameterModel declineEntriesParam = declineEntriesParamOptional.get();
                contextParams.remove(declineEntriesParam);
                getModelService().remove(declineEntriesParam);
                processModel.setContextParameters(contextParams);
                getModelService().save(processModel);
            }
        }
    }


    protected int getLocationIndex(WarehouseModel location)
    {
        int index = 0;
        for(WarehouseModel warehouseModel : this.locations)
        {
            if(location.getCode().equals(warehouseModel.getCode()))
            {
                break;
            }
            index++;
        }
        return index;
    }


    protected int getReasonIndex(DeclineReason declineReason)
    {
        int index = 0;
        String myReason = getEnumerationService().getEnumerationName((HybrisEnumValue)declineReason, getCockpitLocaleService().getCurrentLocale());
        for(String reason : this.declineReasons)
        {
            if(myReason.equals(reason))
            {
                break;
            }
            index++;
        }
        return index;
    }


    protected Optional<DeclineReason> getSelectedDeclineReason(Event event)
    {
        Optional<DeclineReason> result = Optional.empty();
        if(!((SelectEvent)event).getSelectedItems().isEmpty())
        {
            Object selectedValue = ((Comboitem)((SelectEvent)event).getSelectedItems().iterator().next()).getValue();
            result = matchingComboboxDeclineReason(selectedValue.toString());
        }
        return result;
    }


    protected Optional<DeclineReason> getCustomSelectedDeclineReason(Event event)
    {
        Optional<DeclineReason> reason = Optional.empty();
        if(event.getTarget() instanceof Combobox)
        {
            Object selectedValue = event.getData();
            reason = matchingComboboxDeclineReason(selectedValue.toString());
        }
        return reason;
    }


    protected WarehouseModel getSelectedLocation(Event event)
    {
        WarehouseModel result = null;
        if(!((SelectEvent)event).getSelectedItems().isEmpty())
        {
            result = (WarehouseModel)((Comboitem)((SelectEvent)event).getSelectedItems().iterator().next()).getValue();
        }
        return result;
    }


    protected void handleGlobalComment(Event event)
    {
        applyToGrid(((InputEvent)event).getValue(), 7);
        this.consignmentEntries.getRows().getChildren().stream()
                        .filter(entry -> ((Checkbox)entry.getChildren().iterator().next()).isChecked()).forEach(entry -> ((ConsignmentEntryToReallocateDto)((Row)entry).getValue()).setDeclineConsignmentEntryComment(((InputEvent)event).getValue()));
    }


    protected void handleGlobalLocation(Event event)
    {
        WarehouseModel selectedLocation = getSelectedLocation(event);
        if(selectedLocation != null)
        {
            applyToGrid(Integer.valueOf(getLocationIndex(selectedLocation)), 6);
            this.consignmentEntries.getRows().getChildren().stream()
                            .filter(entry -> ((Checkbox)entry.getChildren().iterator().next()).isChecked()).forEach(entry -> ((ConsignmentEntryToReallocateDto)((Row)entry).getValue()).setSelectedLocation(selectedLocation));
        }
    }


    protected void handleGlobalReason(Event event)
    {
        Optional<DeclineReason> declineReason = getSelectedDeclineReason(event);
        if(declineReason.isPresent())
        {
            applyToGrid(Integer.valueOf(getReasonIndex(declineReason.get())), 5);
            this.consignmentEntries.getRows().getChildren().stream()
                            .filter(entry -> ((Checkbox)entry.getChildren().iterator().next()).isChecked()).forEach(entry -> ((ConsignmentEntryToReallocateDto)((Row)entry).getValue()).setSelectedReason(declineReason.get()));
        }
    }


    protected void handleIndividualReason(Event event)
    {
        Optional<DeclineReason> declineReason = getCustomSelectedDeclineReason(event);
        if(declineReason.isPresent())
        {
            autoSelect(event);
            ((ConsignmentEntryToReallocateDto)((Row)event.getTarget().getParent()).getValue())
                            .setSelectedReason(declineReason.get());
        }
    }


    protected void handleIndividualLocation(Event event)
    {
        if(!((SelectEvent)event).getSelectedItems().isEmpty())
        {
            autoSelect(event);
            Object selectedValue = ((Comboitem)((SelectEvent)event).getSelectedItems().iterator().next()).getValue();
            if(selectedValue instanceof WarehouseModel)
            {
                ((ConsignmentEntryToReallocateDto)((Row)event.getTarget().getParent()).getValue())
                                .setSelectedLocation((WarehouseModel)selectedValue);
            }
        }
    }


    protected void handleRow(Row row)
    {
        ConsignmentEntryToReallocateDto myEntry = (ConsignmentEntryToReallocateDto)row.getValue();
        if(row.getChildren().iterator().next() instanceof Checkbox)
        {
            if(!((Checkbox)row.getChildren().iterator().next()).isChecked())
            {
                applyToRow(Integer.valueOf(0), 4, (Component)row);
                applyToRow(null, 5, (Component)row);
                applyToRow(null, 6, (Component)row);
                applyToRow(null, 7, (Component)row);
                myEntry.setQuantityToReallocate(Long.valueOf(0L));
                myEntry.setSelectedReason(null);
                myEntry.setSelectedLocation(null);
                myEntry.setDeclineConsignmentEntryComment(null);
            }
            else
            {
                applyToRow(Integer.valueOf(this.globalDeclineReasons.getSelectedIndex()), 5, (Component)row);
                applyToRow(Integer.valueOf(this.globalPossibleLocations.getSelectedIndex()), 6, (Component)row);
                applyToRow(this.globalDeclineComment.getValue(), 7, (Component)row);
                Optional<DeclineReason> reason = matchingComboboxDeclineReason(
                                (this.globalDeclineReasons.getSelectedItem() != null) ? this.globalDeclineReasons.getSelectedItem().getLabel() : null);
                myEntry.setSelectedReason(reason.isPresent() ? reason.get() : null);
                myEntry.setSelectedLocation((this.globalPossibleLocations.getSelectedItem() != null) ?
                                (WarehouseModel)this.globalPossibleLocations.getSelectedItem().getValue() :
                                null);
                myEntry.setDeclineConsignmentEntryComment(this.globalDeclineComment.getValue());
            }
        }
    }


    protected boolean isDeclineProcessDone(ConsignmentModel latestConsignmentModel, Collection<DeclineEntry> entriesToReallocate)
    {
        return entriesToReallocate.stream().allMatch(entry -> isDeclinedQuantityCorrect(latestConsignmentModel, entry));
    }


    protected boolean isDeclinedQuantityCorrect(ConsignmentModel latestConsignmentModel, DeclineEntry declineEntry)
    {
        Long expectedDeclinedQuantity = Long.valueOf(declineEntry
                        .getConsignmentEntry().getQuantityDeclined().longValue() + declineEntry.getQuantity().longValue());
        return latestConsignmentModel.getConsignmentEntries().stream().anyMatch(entry ->
                        (entry.getPk().equals(declineEntry.getConsignmentEntry().getPk()) && expectedDeclinedQuantity.equals(entry.getQuantityDeclined())));
    }


    protected Optional<DeclineReason> matchingComboboxDeclineReason(String declineReasonLabel)
    {
        return getEnumerationService().getEnumerationValues(DeclineReason.class).stream().filter(reason -> getEnumerationService().getEnumerationName((HybrisEnumValue)reason, getCockpitLocaleService().getCurrentLocale()).equals(declineReasonLabel))
                        .findFirst();
    }


    protected void selectAllEntries()
    {
        applyToGrid(Boolean.TRUE, 0);
        for(Component row : this.consignmentEntries.getRows().getChildren())
        {
            Component firstComponent = row.getChildren().iterator().next();
            if(firstComponent instanceof Checkbox)
            {
                ((Checkbox)firstComponent).setChecked(this.globalDeclineEntriesSelection.isChecked());
            }
            handleRow((Row)row);
            if(this.globalDeclineEntriesSelection.isChecked())
            {
                int reallocatableQuantity = Integer.parseInt(((Label)row.getChildren().get(3)).getValue());
                applyToRow(Integer.valueOf(reallocatableQuantity), 4, row);
            }
        }
        if(this.globalDeclineEntriesSelection.isChecked())
        {
            this.consignmentsEntriesToReallocate.stream()
                            .forEach(entry -> entry.setQuantityToReallocate(entry.getConsignmentEntry().getQuantityPending()));
        }
    }


    protected Component targetFieldToApplyValidation(String stringToValidate, int indexLabelToCheck, int indexTargetComponent)
    {
        for(Component component : this.consignmentEntries.getRows().getChildren())
        {
            Label label = component.getChildren().get(indexLabelToCheck);
            if(label.getValue().equals(stringToValidate))
            {
                return component.getChildren().get(indexTargetComponent);
            }
        }
        return null;
    }


    protected void validateConsignmentEntry(ConsignmentEntryToReallocateDto entry)
    {
        Preconditions.checkArgument((entry != null), "consignment entry to reallocate cannot be null.");
        String productCode = null;
        ConsignmentEntryModel consignmentEntry = entry.getConsignmentEntry();
        if(consignmentEntry != null && consignmentEntry.getOrderEntry() != null && consignmentEntry.getOrderEntry().getProduct() != null)
        {
            productCode = consignmentEntry.getOrderEntry().getProduct().getCode();
        }
        if(consignmentEntry != null && entry.getQuantityToReallocate().longValue() > consignmentEntry.getQuantityPending().longValue())
        {
            InputElement quantity = (InputElement)targetFieldToApplyValidation(productCode, 1, 4);
            throw new WrongValueException(quantity,
                            getLabel("warehousingbackoffice.reallocationconsignment.decline.validation.invalid.quantity"));
        }
        if(entry.getSelectedReason() != null && entry.getQuantityToReallocate().longValue() == 0L)
        {
            InputElement quantity = (InputElement)targetFieldToApplyValidation(productCode, 1, 4);
            throw new WrongValueException(quantity,
                            getLabel("warehousingbackoffice.reallocationconsignment.decline.validation.missing.quantity"));
        }
        if(entry.getSelectedReason() == null && entry.getQuantityToReallocate().longValue() > 0L)
        {
            Combobox reason = (Combobox)targetFieldToApplyValidation(productCode, 1, 5);
            throw new WrongValueException(reason,
                            getLabel("warehousingbackoffice.reallocationconsignment.decline.validation.missing.reason"));
        }
        if(entry.getSelectedLocation() != null)
        {
            Long availabilityAtWarehouse = getWarehouseStockService().getStockLevelForProductCodeAndWarehouse(productCode, entry
                            .getSelectedLocation());
            if(availabilityAtWarehouse != null && availabilityAtWarehouse.longValue() == 0L)
            {
                Combobox location = (Combobox)targetFieldToApplyValidation(productCode, 1, 6);
                throw new WrongValueException(location, getLabel("warehousingbackoffice.reallocationconsignment.decline.validation.invalid.stockLevel"));
            }
        }
    }


    protected void validateRequest()
    {
        for(Component row : getConsignmentEntries().getRows().getChildren())
        {
            Component firstComponent = row.getChildren().iterator().next();
            if(firstComponent instanceof Checkbox && ((Checkbox)firstComponent).isChecked())
            {
                InputElement returnQty = row.getChildren().get(4);
                if(returnQty.getRawValue().equals(Integer.valueOf(0)))
                {
                    throw new WrongValueException(returnQty,
                                    getLabel("warehousingbackoffice.reallocationconsignment.decline.validation.missing.quantity"));
                }
            }
        }
        ListModelList<ConsignmentEntryToReallocateDto> modelList = (ListModelList<ConsignmentEntryToReallocateDto>)getConsignmentEntries().getModel();
        if(modelList.stream().allMatch(entry -> (entry.getQuantityToReallocate().longValue() == 0L)))
        {
            throw new WrongValueException(this.globalDeclineEntriesSelection,
                            getLabel("warehousingbackoffice.reallocationconsignment.decline.validation.missing.selectedLine"));
        }
        modelList.forEach(this::validateConsignmentEntry);
    }


    protected ConsignmentModel getConsignment()
    {
        return this.consignment;
    }


    public void setConsignment(ConsignmentModel consignment)
    {
        this.consignment = consignment;
    }


    protected EnumerationService getEnumerationService()
    {
        return this.enumerationService;
    }


    protected Grid getConsignmentEntries()
    {
        return this.consignmentEntries;
    }


    protected WarehousingBusinessProcessService<ConsignmentModel> getConsignmentBusinessProcessService()
    {
        return this.consignmentBusinessProcessService;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    protected BackofficeLocaleService getCockpitLocaleService()
    {
        return this.cockpitLocaleService;
    }


    protected CockpitEventQueue getCockpitEventQueue()
    {
        return this.cockpitEventQueue;
    }


    protected DefaultWarehouseStockService getWarehouseStockService()
    {
        return this.warehouseStockService;
    }


    protected NotificationService getNotificationService()
    {
        return this.notificationService;
    }
}
