package de.hybris.platform.warehousingbackoffice.widgets.stocks;

import com.hybris.backoffice.i18n.BackofficeLocaleService;
import com.hybris.backoffice.widgets.notificationarea.NotificationService;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;
import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.warehousing.enums.StockLevelAdjustmentReason;
import de.hybris.platform.warehousingbackoffice.dtos.StockAdjustmentDto;
import de.hybris.platform.warehousingfacades.stocklevel.data.StockLevelAdjustmentData;
import de.hybris.platform.warehousingfacades.stocklevel.impl.DefaultWarehousingStockLevelFacade;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.impl.InputElement;

public class StockToAdjustController extends DefaultWidgetController
{
    private static final long serialVersionUID = 1L;
    protected static final String IN_SOCKET = "stockLevelInput";
    protected static final String OUT_CONFIRM = "confirmOutput";
    protected static final Object COMPLETED = "completed";
    protected static final int COLUMN_INDEX_REASON = 0;
    protected static final int COLUMN_INDEX_QTY = 1;
    private transient Set<StockAdjustmentDto> stockAdjustmentsToCreate = new HashSet<>();
    private StockLevelModel stockLevel;
    private final List<String> stockAdjustmentReasons = new ArrayList<>();
    @Wire
    private Textbox productCode;
    @Wire
    private Textbox warehouseName;
    @Wire
    private Textbox bin;
    @Wire
    private Grid stockAdjustments;
    @WireVariable
    private transient EnumerationService enumerationService;
    @WireVariable
    private transient ModelService modelService;
    @WireVariable
    private transient BackofficeLocaleService cockpitLocaleService;
    @WireVariable
    private transient DefaultWarehousingStockLevelFacade warehousingStockLevelFacade;
    @WireVariable
    private transient NotificationService notificationService;


    @SocketEvent(socketId = "stockLevelInput")
    public void initStockLevelAdjustmentForm(StockLevelModel inputObject)
    {
        this.stockAdjustmentReasons.clear();
        setStockLevel(inputObject);
        getWidgetInstanceManager().setTitle(
                        getWidgetInstanceManager().getLabel("warehousingbackoffice.stockadjustment.title") + " " + getWidgetInstanceManager().getLabel("warehousingbackoffice.stockadjustment.title"));
        this.productCode.setValue(getStockLevel().getProductCode());
        this.warehouseName.setValue(getStockLevel().getWarehouse().getName());
        this.bin.setValue(getStockLevel().getBin());
        getEnumerationService().getEnumerationValues(StockLevelAdjustmentReason.class)
                        .forEach(reason -> this.stockAdjustmentReasons.add(getEnumerationService().getEnumerationName((HybrisEnumValue)reason, getLocale())));
        this.stockAdjustmentsToCreate.add(new StockAdjustmentDto(this.stockAdjustmentReasons));
        refreshGrid();
    }


    @ViewEvent(componentID = "addadjustment", eventName = "onClick")
    public void addAdjustment()
    {
        if(this.stockAdjustmentsToCreate.size() < (StockLevelAdjustmentReason.values()).length)
        {
            this.stockAdjustmentsToCreate.add(new StockAdjustmentDto(this.stockAdjustmentReasons));
            refreshGrid();
        }
    }


    @ViewEvent(componentID = "reset", eventName = "onClick")
    public void reset()
    {
        this.stockAdjustmentsToCreate.clear();
        this.stockAdjustmentsToCreate.add(new StockAdjustmentDto(this.stockAdjustmentReasons));
        refreshGrid();
    }


    protected void refreshGrid()
    {
        getStockAdjustments().setModel((ListModel)new ListModelList(this.stockAdjustmentsToCreate));
        getStockAdjustments().renderAll();
        addListeners();
        getStockAdjustments().getRows().getChildren().stream().filter(myRow -> myRow instanceof Row)
                        .forEach(myRow -> manageFieldsVisibility((Row)myRow, (StockAdjustmentDto)((Row)myRow).getValue()));
    }


    protected void addListeners()
    {
        List<Component> rows = getStockAdjustments().getRows().getChildren();
        for(Component row : rows)
        {
            for(Component myComponent : row.getChildren())
            {
                if(myComponent instanceof org.zkoss.zul.Vbox)
                {
                    addListComponentListeners(myComponent);
                    continue;
                }
                if(myComponent instanceof org.zkoss.zul.Hbox)
                {
                    addButtonListeners(myComponent);
                }
            }
        }
    }


    protected void addListComponentListeners(Component component)
    {
        for(Iterator<Component> iterator = component.getChildren().iterator(); iterator.hasNext(); )
        {
            Component myComponent = iterator.next();
            if(myComponent instanceof Combobox)
            {
                myComponent.addEventListener("onCustomChange", event -> Events.echoEvent("onLaterCustomChange", myComponent, event.getData()));
                myComponent.addEventListener("onLaterCustomChange", event -> {
                    Clients.clearWrongValue(myComponent);
                    myComponent.invalidate();
                    handleIndividualReason(event);
                });
                myComponent.addEventListener("onSelect", this::handleIndividualReason);
                continue;
            }
            if(myComponent instanceof Intbox)
            {
                myComponent.addEventListener("onChange", event -> ((StockAdjustmentDto)((Row)event.getTarget().getParent().getParent()).getValue()).setQuantity(Long.valueOf(Long.parseLong(((InputEvent)event).getValue()))));
                continue;
            }
            if(myComponent instanceof Textbox)
            {
                myComponent.addEventListener("onChanging", event -> ((StockAdjustmentDto)((Row)event.getTarget().getParent().getParent()).getValue()).setComment(((InputEvent)event).getValue()));
            }
        }
    }


    protected void handleIndividualReason(Event event)
    {
        Optional<StockLevelAdjustmentReason> reason = Optional.empty();
        if(event.getTarget() instanceof Combobox)
        {
            Object selectedValue = event.getData();
            if(selectedValue == null)
            {
                selectedValue = ((Combobox)event.getTarget()).getSelectedItem().getValue();
            }
            reason = matchingComboboxStockAdjustmentReason(selectedValue.toString());
        }
        if(reason.isPresent())
        {
            StockAdjustmentDto stockAdjustmentDto = (StockAdjustmentDto)((Row)event.getTarget().getParent().getParent()).getValue();
            stockAdjustmentDto.setSelectedReason(reason.get());
            stockAdjustmentDto.setLocalizedStringReason(getEnumerationService().getEnumerationName((HybrisEnumValue)reason.get(), getLocale()));
        }
    }


    protected Optional<StockLevelAdjustmentReason> matchingComboboxStockAdjustmentReason(String stockAdjustmentReasonLabel)
    {
        return getEnumerationService().getEnumerationValues(StockLevelAdjustmentReason.class).stream()
                        .filter(reason -> getEnumerationService().getEnumerationName((HybrisEnumValue)reason, getLocale()).equals(stockAdjustmentReasonLabel))
                        .findFirst();
    }


    protected void addButtonListeners(Component myComponent)
    {
        int buttonIndex = 0;
        for(Component button : myComponent.getChildren())
        {
            if(button instanceof org.zkoss.zul.Button)
            {
                switch(buttonIndex)
                {
                    case 0:
                        button.addEventListener("onClick", this::addStockAdjustment);
                        break;
                    case 1:
                        button.addEventListener("onClick", this::editStockAdjustment);
                        break;
                    case 2:
                        button.addEventListener("onClick", this::removeStockAdjustment);
                        break;
                }
                buttonIndex++;
            }
        }
    }


    protected void addStockAdjustment(Event event)
    {
        Row myRow = (Row)event.getTarget().getParent().getParent();
        StockAdjustmentDto stockAdjustmentDto = (StockAdjustmentDto)myRow.getValue();
        Combobox reason = ((Component)myRow.getChildren().get(0)).getChildren().get(0);
        if(stockAdjustmentDto.getQuantity().equals(Long.valueOf(0L)))
        {
            InputElement quantity = ((Component)myRow.getChildren().get(1)).getChildren().get(0);
            throw new WrongValueException(quantity, getLabel("warehousingbackoffice.stockadjustment.validation.missing.quantity"));
        }
        if(stockAdjustmentDto.getSelectedReason() == null)
        {
            throw new WrongValueException(reason, getLabel("warehousingbackoffice.stockadjustment.validation.missing.reason"));
        }
        if(this.stockAdjustmentsToCreate
                        .stream().filter(entry -> (entry.getSelectedReason() == stockAdjustmentDto.getSelectedReason()))
                        .count() > 1L)
        {
            throw new WrongValueException(reason, getLabel("warehousingbackoffice.stockadjustment.validation.duplicate.reason"));
        }
        stockAdjustmentDto.setUnderEdition(Boolean.valueOf(false));
        manageFieldsVisibility(myRow, stockAdjustmentDto);
    }


    protected void removeStockAdjustment(Event event)
    {
        StockAdjustmentDto stockAdjustmentDto = (StockAdjustmentDto)((Row)event.getTarget().getParent().getParent()).getValue();
        this.stockAdjustmentsToCreate.remove(stockAdjustmentDto);
        refreshGrid();
    }


    protected void editStockAdjustment(Event event)
    {
        Row myRow = (Row)event.getTarget().getParent().getParent();
        StockAdjustmentDto myStockAdjustmentDto = (StockAdjustmentDto)myRow.getValue();
        myStockAdjustmentDto.setUnderEdition(Boolean.valueOf(true));
        getStockAdjustments().renderAll();
        manageFieldsVisibility(myRow, myStockAdjustmentDto);
    }


    protected void manageFieldsVisibility(Row myRow, StockAdjustmentDto stockAdjustmentDto)
    {
        for(Component myComponent : myRow.getChildren())
        {
            myComponent.getChildren().stream().filter(comp -> comp instanceof Label).forEach(comp -> {
                comp.setVisible(!stockAdjustmentDto.getUnderEdition().booleanValue());
                comp.invalidate();
            });
            myComponent.getChildren().stream()
                            .filter(comp -> (comp instanceof Combobox || comp instanceof Intbox || comp instanceof Textbox)).forEach(comp -> {
                                comp.setVisible(stockAdjustmentDto.getUnderEdition().booleanValue());
                                Label myLabel = comp.getParent().getChildren().get(1);
                                if(comp instanceof Intbox)
                                {
                                    myLabel.setValue(((Intbox)comp).getValue().toString());
                                }
                                else if(comp instanceof Combobox && stockAdjustmentDto.getSelectedReason() != null)
                                {
                                    myLabel.setValue(stockAdjustmentDto.getLocalizedStringReason());
                                }
                                else
                                {
                                    myLabel.setValue(((Textbox)comp).getValue());
                                }
                                myLabel.invalidate();
                                comp.invalidate();
                            });
            manageButtonsVisibility(stockAdjustmentDto.getUnderEdition().booleanValue(), myComponent);
        }
    }


    protected void manageButtonsVisibility(boolean isEditable, Component myComponent)
    {
        int buttonIndex = 0;
        for(Component myButton : myComponent.getChildren())
        {
            if(myButton instanceof org.zkoss.zul.Button)
            {
                switch(buttonIndex)
                {
                    case 0:
                        myButton.setVisible(isEditable);
                        myButton.invalidate();
                        break;
                    case 1:
                        myButton.setVisible(!isEditable);
                        myButton.invalidate();
                        break;
                }
                buttonIndex++;
            }
        }
    }


    @ViewEvent(componentID = "confirm", eventName = "onClick")
    public void confirmStockAdjustmentCreation() throws InterruptedException
    {
        validateStockAdjustmentCreation();
        if(!this.stockAdjustmentsToCreate.isEmpty())
        {
            this.stockAdjustmentsToCreate.forEach(stockAdjustment -> getWarehousingStockLevelFacade().createStockLevelAdjustment(this.stockLevel, getStockLevelAdjustmentData(stockAdjustment)));
            getNotificationService().notifyUser("", "JustMessage", NotificationEvent.Level.SUCCESS, new Object[] {getLabel("warehousingbackoffice.stockadjustment.success.message")});
        }
        else
        {
            getNotificationService().notifyUser("", "JustMessage", NotificationEvent.Level.FAILURE, new Object[] {getLabel("warehousingbackoffice.stockadjustment.error.message")});
        }
        sendOutput("confirmOutput", COMPLETED);
    }


    protected void validateStockAdjustmentCreation()
    {
        if(getStockAdjustments().getRows().getChildren().stream().anyMatch(row -> row.getChildren().stream().anyMatch(())))
        {
            getStockAdjustments().getRows().getChildren()
                            .forEach(row -> row.getChildren().forEach(()));
        }
    }


    protected StockLevelAdjustmentData getStockLevelAdjustmentData(StockAdjustmentDto stockAdjustmentDto)
    {
        StockLevelAdjustmentData stockLevelAdjustmentData = new StockLevelAdjustmentData();
        stockLevelAdjustmentData.setReason(stockAdjustmentDto.getSelectedReason());
        stockLevelAdjustmentData.setQuantity(stockAdjustmentDto.getQuantity());
        stockLevelAdjustmentData.setComment(stockAdjustmentDto.getComment());
        return stockLevelAdjustmentData;
    }


    protected Locale getLocale()
    {
        return getCockpitLocaleService().getCurrentLocale();
    }


    protected StockLevelModel getStockLevel()
    {
        return this.stockLevel;
    }


    public void setStockLevel(StockLevelModel stockLevel)
    {
        this.stockLevel = stockLevel;
    }


    protected BackofficeLocaleService getCockpitLocaleService()
    {
        return this.cockpitLocaleService;
    }


    protected EnumerationService getEnumerationService()
    {
        return this.enumerationService;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    public Grid getStockAdjustments()
    {
        return this.stockAdjustments;
    }


    public DefaultWarehousingStockLevelFacade getWarehousingStockLevelFacade()
    {
        return this.warehousingStockLevelFacade;
    }


    protected NotificationService getNotificationService()
    {
        return this.notificationService;
    }
}
