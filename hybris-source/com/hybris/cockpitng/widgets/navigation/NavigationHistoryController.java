/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.navigation;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.hybris.cockpitng.annotations.GlobalCockpitEvent;
import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.common.configuration.MenupopupPosition;
import com.hybris.cockpitng.core.Widget;
import com.hybris.cockpitng.core.WidgetSocket;
import com.hybris.cockpitng.core.events.CockpitEvent;
import com.hybris.cockpitng.core.expression.ExpressionResolverFactory;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectCRUDHandler;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.util.WidgetControllers;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.A;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;

public class NavigationHistoryController extends DefaultWidgetController
{
    private static final Logger LOGGER = LoggerFactory.getLogger(NavigationHistoryController.class);
    public static final String INPUT_RESET = "resetHistory";
    public static final String INPUT_EMPTY_HISTORY_EVAL_TARGET = "emptyHistoryEvaluationTarget";
    public static final String SOCKET_INPUT_REMOVE_ITEM = "removeItem";
    public static final String SOCKET_INPUT_DO_BACK = "doBack";
    public static final String SOCKET_OUT_EMPTY_HISTORY_BACK_BUTTON_PRESSED = "emptyHistoryBackButtonPressed";
    public static final String SETTING_DISPLAY_EMPTY_HISTORY_ELEMENT_IN_POPUP = "displayEmptyHistoryElementInPopup";
    public static final String SETTING_HISTORY_LENGTH = "historyLength";
    public static final String SETTING_EMPTY_HISTORY_OUTPUT_VALUE_EVAL = "emptyHistoryOutputValueEval";
    public static final String SETTING_DEFAULT_OUTPUT = "defaultOutput";
    public static final String SETTING_OUTPUT_PATTERN = "%s_output";
    public static final String SETTING_PROXY = "proxyEnabled";
    public static final String MODEL_EMPTY_HISTORY_EVAL_TARGET = "modelEmptyHistoryEvaluationTarget";
    public static final String MODEL_CURRENT_ITEM = "currentItem";
    public static final String MODEL_HISTORY = "history";
    public static final int DEFAULT_MAX_HISTORY_LENGTH = 10;
    protected static final String SCLASS_NAVIGATION_HISTORY_BACK_DISABLED = "yw-navigationhistory-back-disabled";
    protected static final String SCLASS_NAVIGATION_STACK_DISABLED = "yw-navigationhistory-stack-disabled";
    protected static final String SCLASS_MENU_NO_ICON = " ";
    private static final String ATTRIBUTE_HISTORY_ELEMENT = "historyElement";
    @Wire
    private Menupopup historyStack;
    @Wire
    private Div currentItem;
    @Wire
    private A backButton;
    @Wire
    private A stackButton;
    @Wire
    private Label currentItemLabel;
    @WireVariable
    private transient LabelService labelService;
    @WireVariable
    private transient ExpressionResolverFactory expressionResolverFactory;
    private boolean proxy;
    private int maxLength;
    private transient HistoryElement currentItemObject;
    private transient List<HistoryElement> history;
    private boolean emptyHistoryElementInPopup;


    @Override
    public void initialize(final Component comp)
    {
        super.initialize(comp);
        maxLength = getWidgetSettings().getInt(SETTING_HISTORY_LENGTH);
        if(maxLength < 1)
        {
            maxLength = DEFAULT_MAX_HISTORY_LENGTH;
        }
        proxy = getWidgetSettings().getBoolean(SETTING_PROXY);
        emptyHistoryElementInPopup = getWidgetSettings().getBoolean(SETTING_DISPLAY_EMPTY_HISTORY_ELEMENT_IN_POPUP);
        restoreHistory();
        restoreCurrentItem();
        initializeVirtualSockets(comp);
        updateHistoryStackEnabled();
        stackButton.addEventListener(Events.ON_CLICK,
                        event -> historyStack.open(stackButton, MenupopupPosition.AFTER_START.getName()));
    }


    protected void restoreHistory()
    {
        history = getModel().getValue(MODEL_HISTORY, List.class);
        if(history == null)
        {
            history = new ArrayList<>(emptyHistoryElementInPopup ? maxLength + 1 : maxLength);
            getModel().setValue(MODEL_HISTORY, history);
        }
        else
        {
            //appends item at the beginning of the historyStack, hence we restore it in reverse order
            Lists.reverse(history).forEach(this::addHistoryMenuItem);
        }
    }


    protected void restoreCurrentItem()
    {
        currentItemObject = getModel().getValue(MODEL_CURRENT_ITEM, HistoryElement.class);
        currentItemLabel.setValue(getHistoryElementLabel(currentItemObject));
    }


    protected void initializeVirtualSockets(final Component comp)
    {
        final Widget widget = getWidgetslot().getWidgetInstance().getWidget();
        final List<WidgetSocket> inputs = widget.getVirtualInputs();
        inputs.forEach(socket -> WidgetControllers.addWidgetSocketListeners(comp, socket.getId(),
                        e -> messageReceived(socket.getId(), e.getData())));
    }


    protected void messageReceived(final String socket, final Object item)
    {
        if(currentItemObject != null && ObjectUtils.notEqual(item, currentItemObject.getMessage()))
        {
            addHistoryMenuItem(currentItemObject);
            addToHistory(currentItemObject);
        }
        setCurrentObject(socket, item);
        if(proxy)
        {
            sendOutputMessage(socket, item);
        }
    }


    /**
     * Appends ui representation of given history element at the beginning of historyStack (LIFO)
     *
     * @param obj
     *           history element for which element on history stack will be created.
     */
    protected void addHistoryMenuItem(final HistoryElement obj)
    {
        final Menuitem menuItem = new Menuitem(getHistoryElementLabel(obj));
        menuItem.setIconSclass(SCLASS_MENU_NO_ICON);
        menuItem.addEventListener(Events.ON_CLICK, event -> itemClicked(lookupMenuPosition(menuItem)));
        menuItem.setAttribute(ATTRIBUTE_HISTORY_ELEMENT, obj);
        historyStack.insertBefore(menuItem, historyStack.getFirstChild());
        final List<Component> historyElements = historyStack.getChildren();
        if(historyElements != null)
        {
            removeOverflow(historyElements);
        }
        updateHistoryStackEnabled();
    }


    protected int lookupMenuPosition(final Menuitem menuitem)
    {
        if(CollectionUtils.isNotEmpty(historyStack.getChildren()))
        {
            int position = -1;
            for(final Component child : historyStack.getChildren())
            {
                position++;
                if(Objects.equal(child, menuitem))
                {
                    return position;
                }
            }
        }
        return -1;
    }


    protected void addToHistory(final HistoryElement obj)
    {
        history.add(0, obj);
        removeOverflow(history);
    }


    protected <T> void removeOverflow(final List<T> history)
    {
        final int numberOfAdditionalElements = isEmptyBackElementAvailable() ? 1 : 0;
        while(history.size() > maxLength + numberOfAdditionalElements)
        {
            history.remove(history.size() - 1 - numberOfAdditionalElements);
        }
    }


    protected boolean isEmptyBackElementAvailable()
    {
        return emptyHistoryElementInPopup && history.stream().anyMatch(EmptyHistoryElement.class::isInstance);
    }


    protected void updateHistoryStackEnabled()
    {
        final boolean emptyStack = historyStack.getChildren().isEmpty();
        UITools.modifySClass(backButton, SCLASS_NAVIGATION_HISTORY_BACK_DISABLED,
                        emptyStack && StringUtils.isEmpty(getEmptyHistoryOutputValueEval()));
        UITools.modifySClass(stackButton, SCLASS_NAVIGATION_STACK_DISABLED, emptyStack);
        stackButton.setDisabled(emptyStack);
    }


    @SocketEvent(socketId = INPUT_RESET)
    public void resetHistory(final Object object)
    {
        setCurrentObject(null, null);
        history.clear();
        historyStack.getChildren().clear();
        refreshEmptyHistoryElement();
        updateHistoryStackEnabled();
    }


    protected HistoryElement createHistoryElement(final String socket, final Object item)
    {
        return new HistoryElement(socket, item);
    }


    protected void setCurrentObject(final String socket, final Object item)
    {
        if(item != null)
        {
            currentItemObject = createHistoryElement(socket, item);
        }
        else
        {
            currentItemObject = null;
        }
        currentItemLabel.setValue(getHistoryElementLabel(currentItemObject));
        getModel().setValue(MODEL_CURRENT_ITEM, currentItemObject);
    }


    @SocketEvent(socketId = SOCKET_INPUT_DO_BACK)
    public void doBackOnSocket()
    {
        backButtonClicked();
    }


    @SocketEvent(socketId = SOCKET_INPUT_REMOVE_ITEM)
    public void removeItem(final Object inputObject)
    {
        if(inputObject instanceof Collection)
        {
            final Collection inputCollection = (Collection)inputObject;
            inputCollection.forEach(message -> {
                final List<HistoryElement> elementsToRemove = history.stream()
                                .filter(element -> Objects.equal(message, element.getMessage())).collect(Collectors.toList());
                elementsToRemove.forEach(this::removeElementsFromHistory);
                if(currentItemObject != null && message.equals(currentItemObject.message))
                {
                    removeCurrentObject();
                }
            });
        }
    }


    protected void removeCurrentObject()
    {
        if(hasAtLeastOneNotEmptyHistoryElement())
        {
            backButtonClicked();
        }
        else
        {
            setCurrentObject(null, null);
        }
    }


    protected boolean hasAtLeastOneNotEmptyHistoryElement()
    {
        return history.stream().anyMatch(historyElement -> !(historyElement instanceof EmptyHistoryElement));
    }


    protected void removeElementsFromHistory(final HistoryElement toRemove)
    {
        history.remove(toRemove);
        final Predicate<Component> isMenuItemWithHistoryElement = menuItem -> menuItem instanceof Menuitem
                        && toRemove.equals(menuItem.getAttribute(ATTRIBUTE_HISTORY_ELEMENT));
        historyStack.getChildren().removeIf(isMenuItemWithHistoryElement);
        removeDuplicatesInNeighborhoodFromHistory();
    }


    protected void removeDuplicatesInNeighborhoodFromHistory()
    {
        final List<Integer> duplicatedIndexes = findDuplicatesInNeighborhoodInHistory();
        Collections.reverse(duplicatedIndexes);
        for(final int index : duplicatedIndexes)
        {
            history.remove(index);
            historyStack.getChildren().remove(index);
        }
        if(!history.isEmpty() && history.get(0).equals(currentItemObject))
        {
            history.remove(0);
            historyStack.getChildren().remove(0);
        }
    }


    protected List<Integer> findDuplicatesInNeighborhoodInHistory()
    {
        final List<Integer> duplicates = new ArrayList<>();
        if(history.size() > 1)
        {
            for(int i = 0; i < history.size() - 1; i++)
            {
                if(history.get(i).equals(history.get(i + 1)))
                {
                    duplicates.add(i);
                }
            }
        }
        return duplicates;
    }


    @ViewEvent(componentID = "backButton", eventName = Events.ON_CLICK)
    public void backButtonClicked()
    {
        if(history.isEmpty())
        {
            onEmptyHistoryBackButtonPressed();
        }
        else
        {
            itemClicked(0);
        }
    }


    @SocketEvent(socketId = INPUT_EMPTY_HISTORY_EVAL_TARGET)
    public void setEmptyHistoryEvalTarget(final Object object)
    {
        getModel().setValue(MODEL_EMPTY_HISTORY_EVAL_TARGET, object);
        refreshEmptyHistoryElement();
    }


    protected void onEmptyHistoryBackButtonPressed()
    {
        final Object evaluatedValue = getBackButtonEvaluatedValue();
        if(evaluatedValue != null)
        {
            sendOutput(SOCKET_OUT_EMPTY_HISTORY_BACK_BUTTON_PRESSED, evaluatedValue);
        }
    }


    protected void refreshEmptyHistoryElement()
    {
        if(!emptyHistoryElementInPopup)
        {
            return;
        }
        removeExistingEmptyHistoryElements();
        final Object backButtonEvaluatedValue = getBackButtonEvaluatedValue();
        if(backButtonEvaluatedValue != null)
        {
            final EmptyHistoryElement historyElement = new EmptyHistoryElement(backButtonEvaluatedValue);
            final Menuitem menuItem = new Menuitem(getHistoryElementLabel(historyElement));
            menuItem.setIconSclass(SCLASS_MENU_NO_ICON);
            menuItem.addEventListener(Events.ON_CLICK, event -> itemClicked(lookupMenuPosition(menuItem)));
            historyStack.appendChild(menuItem);
            history.add(historyElement);
            historyStack.invalidate();
        }
        updateHistoryStackEnabled();
    }


    protected void removeExistingEmptyHistoryElements()
    {
        if(!emptyHistoryElementInPopup)
        {
            return;
        }
        int index = history.size();
        final ListIterator<HistoryElement> iterator = history.listIterator(index);
        while(iterator.hasPrevious())
        {
            index--;
            final HistoryElement historyElement = iterator.previous();
            if(historyElement instanceof EmptyHistoryElement && historyStack.getChildren().size() > index)
            {
                final Component emptyElementMenuItem = historyStack.getChildren().get(index);
                if(emptyElementMenuItem != null)
                {
                    historyStack.removeChild(emptyElementMenuItem);
                }
                iterator.remove();
            }
        }
    }


    protected Object getBackButtonEvaluatedValue()
    {
        final String emptyHistoryEval = getEmptyHistoryOutputValueEval();
        if(StringUtils.isNotBlank(emptyHistoryEval))
        {
            final Object evaluationObject = getModel().getValue(MODEL_EMPTY_HISTORY_EVAL_TARGET, Object.class);
            final Object spELRoot = ObjectUtils.defaultIfNull(evaluationObject, getModel());
            return expressionResolverFactory.createResolver().getValue(spELRoot, emptyHistoryEval);
        }
        return null;
    }


    protected String getEmptyHistoryOutputValueEval()
    {
        return getWidgetSettings().getString(SETTING_EMPTY_HISTORY_OUTPUT_VALUE_EVAL);
    }


    protected void itemClicked(final int position)
    {
        if(position >= 0 && position < history.size())
        {
            final HistoryElement historyElement = history.get(position);
            if(historyElement instanceof EmptyHistoryElement)
            {
                setCurrentObject(historyElement.getSocket(), null);
                history.clear();
                Components.removeAllChildren(historyStack);
                sendOutput(historyElement.getSocket(), historyElement.getMessage());
            }
            else
            {
                setCurrentObject(historyElement.getSocket(), historyElement.getMessage());
                reduceStack(position);
                sendOutputMessage(historyElement.getSocket(), historyElement.getMessage());
            }
        }
    }


    protected void sendOutputMessage(final String input, final Object message)
    {
        final String socket = getOutputSocket(input);
        if(socket != null)
        {
            sendOutput(socket, message);
        }
        else
        {
            LOGGER.error("Unable to determine output socket for: {}", input);
        }
    }


    protected String getOutputSocket(final String input)
    {
        final String setting = String.format(SETTING_OUTPUT_PATTERN, input);
        return (String)getWidgetSettings().getOrDefault(setting, getWidgetSettings().getString(SETTING_DEFAULT_OUTPUT));
    }


    protected void reduceStack(final int index)
    {
        history.subList(0, index + 1).clear();
        historyStack.getChildren().subList(0, index + 1).clear();
        updateHistoryStackEnabled();
    }


    @GlobalCockpitEvent(eventName = ObjectCRUDHandler.OBJECTS_UPDATED_EVENT, scope = CockpitEvent.SESSION)
    public void handleObjectUpdatedEvent(final CockpitEvent event)
    {
        for(final Object element : event.getDataAsCollection())
        {
            updateElement(element);
        }
    }


    protected void updateElement(final Object data)
    {
        if(currentItemObject != null && Objects.equal(currentItemObject.getMessage(), data))
        {
            setCurrentObject(currentItemObject.getSocket(), data);
        }
        for(int i = 0; i < history.size(); i++)
        {
            final HistoryElement historyElement = history.get(i);
            if(Objects.equal(historyElement.getMessage(), data))
            {
                final Menuitem menuitem = (Menuitem)historyStack.getChildren().get(i);
                final HistoryElement updatedHistoryElement = new HistoryElement(historyElement.getSocket(), data);
                menuitem.setLabel(getHistoryElementLabel(updatedHistoryElement));
                history.set(i, updatedHistoryElement);
            }
        }
    }


    protected String getHistoryElementLabel(final HistoryElement historyElement)
    {
        String label;
        if(historyElement == null)
        {
            label = StringUtils.EMPTY;
        }
        else if(historyElement.getMessage() instanceof String)
        {
            label = getGlobalLabel((String)historyElement.getMessage());
            if(StringUtils.isBlank(label))
            {
                label = (String)historyElement.getMessage();
            }
        }
        else
        {
            label = getObjectLabel(historyElement);
        }
        return label;
    }


    protected String getObjectLabel(final HistoryElement object)
    {
        return getLabelService().getObjectLabel(object.getMessage());
    }


    protected String getGlobalLabel(final String labelKey)
    {
        return Labels.getLabel(labelKey);
    }


    protected LabelService getLabelService()
    {
        return labelService;
    }


    public Menupopup getHistoryStack()
    {
        return historyStack;
    }


    public Div getCurrentItem()
    {
        return currentItem;
    }


    public Label getCurrentItemLabel()
    {
        return currentItemLabel;
    }


    public A getBackButton()
    {
        return backButton;
    }


    public A getStackButton()
    {
        return stackButton;
    }


    protected static class HistoryElement
    {
        private final String socket;
        private final Object message;


        public HistoryElement(final String socket, final Object message)
        {
            this.socket = socket;
            this.message = message;
        }


        public String getSocket()
        {
            return socket;
        }


        public Object getMessage()
        {
            return message;
        }


        @Override
        public boolean equals(final Object o)
        {
            if(this == o)
            {
                return true;
            }
            if(o == null)
            {
                return false;
            }
            if(o.getClass() != this.getClass())
            {
                return false;
            }
            final HistoryElement that = (HistoryElement)o;
            return (socket != null ? socket.equals(that.socket) : that.socket == null)
                            && (message != null ? message.equals(that.message) : that.message == null);
        }


        @Override
        public int hashCode()
        {
            int result = socket != null ? socket.hashCode() : 0;
            result = 31 * result + (message != null ? message.hashCode() : 0);
            return result;
        }
    }


    protected static class EmptyHistoryElement extends HistoryElement
    {
        public EmptyHistoryElement(final Object message)
        {
            super(SOCKET_OUT_EMPTY_HISTORY_BACK_BUTTON_PRESSED, message);
        }
    }
}
