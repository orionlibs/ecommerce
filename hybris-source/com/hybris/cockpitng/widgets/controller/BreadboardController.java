/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.controller;

import com.hybris.cockpitng.admin.CockpitAdminService;
import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.components.Widgetslot;
import com.hybris.cockpitng.core.AbstractCockpitComponentDefinition;
import com.hybris.cockpitng.core.CockpitComponentDefinitionService;
import com.hybris.cockpitng.core.Widget;
import com.hybris.cockpitng.core.WidgetDefinition;
import com.hybris.cockpitng.core.WidgetService;
import com.hybris.cockpitng.core.WidgetSocket;
import com.hybris.cockpitng.core.WidgetSocket.Multiplicity;
import com.hybris.cockpitng.core.ui.WidgetInstance;
import com.hybris.cockpitng.core.ui.WidgetInstanceService;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.cockpitng.util.ExtendedWidgetResourceLoader;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Style;
import org.zkoss.zul.Window;

public class BreadboardController extends DefaultWidgetController
{
    public static final String SELECT_WIDGET_DEFINITION_INPUT_ID = "selectWidgetDefinition";
    protected static final String WIDGET_SELECTOR_ID = "widgetSelector";
    protected static final String CLEAR_BUTTON = "clearButton";
    protected static final String BUTTON_WIDGET_SETTINGS = "buttonWidgetSettings";
    protected static final String SOCKET_IN_COLLECTION_INPUT_LOGGER = "collectionInputLogger";
    protected static final String SOCKET_IN_SINGLE_INPUT_LOGGER = "singleInputLogger";
    protected static final String SOCKET_IN_COLLECTION_OUTPUT_LOGGER = "collectionOutputLogger";
    protected static final String SOCKET_IN_SINGLE_OUTPUT_LOGGER = "singleOutputLogger";
    private static final Logger LOG = LoggerFactory.getLogger(BreadboardController.class);
    private static final String SELECTED_DEFINITION = "selectedDefinition";
    private static final int MAX_CONSOLE_LOG_SIZE = 200;
    private static final long serialVersionUID = 5285271910475735733L;
    @Resource
    private transient CockpitComponentDefinitionService componentDefinitionService;
    @Resource
    private transient WidgetInstanceService widgetInstanceService;
    @Resource
    private transient WidgetService widgetService;
    @Resource
    private transient CockpitAdminService cockpitAdminService;
    @Wire
    private Listbox widgetSelector;
    @Wire
    private Label widgetCode;
    @Wire
    private Label widgetName;
    @Wire
    private Label widgetDescription;
    @Wire
    private Label widgetController;
    @Wire
    private Label widgetCategoryTag;
    @Wire
    private Label widgetViewURI;
    @Wire
    private Label widgetDefaultTitle;
    @Wire
    private Component bottom;
    @Wire
    private Label outputLabel;
    @Wire
    private Div divWidgetSettings;
    @Wire
    private Div additionalStyleContainer;
    @Wire
    private Widgetslot testSlot;


    @Override
    public void initialize(final Component comp)
    {
        super.initialize(comp);
        final List<WidgetDefinition> widgetDefinitions = getWidgetDefinitions(true);
        final SimpleListModel<WidgetDefinition> simpleListModel = new SimpleListModel<>(widgetDefinitions);
        simpleListModel.sort((def1, def2) -> {
            if(def1 == null && def2 == null)
            {
                return 0;
            }
            if(def1 == null && def2 != null)
            {
                return -1;
            }
            if(def2 == null && def1 != null)
            {
                return 1;
            }
            return StringUtils.defaultIfBlank(def1.getName(), StringUtils.EMPTY)
                            .compareTo(StringUtils.defaultIfBlank(def2.getName(), StringUtils.EMPTY));
        }, true);
        widgetSelector.setModel(simpleListModel);
        widgetSelector.setItemRenderer((final Listitem item, final WidgetDefinition data, final int index) -> {
            item.setValue(data);
            item.setLabel(StringUtils.isBlank(data.getName()) ? data.getCode() : data.getName());
            item.setTooltiptext(data.getCode());
        });
        final WidgetDefinition definition = getValue(SELECTED_DEFINITION, WidgetDefinition.class);
        if(definition != null)
        {
            updateDefinitionInfoArea(definition);
            updateCSSSlot(definition);
            for(int index = 0; index < simpleListModel.getSize(); index++)
            {
                final WidgetDefinition widgetDefinition = simpleListModel.getElementAt(index);
                if(widgetDefinition.equals(definition))
                {
                    widgetSelector.setSelectedIndex(index);
                    break;
                }
            }
        }
        Component lastCmp = null;
        for(final String consoleLine : getConsoleLog())
        {
            lastCmp = createConsoleEntry(consoleLine);
        }
        Clients.scrollIntoView(lastCmp);
        testSlot.setAttribute("_widgetEventLoggerBefore",
                        (EventListener<Event>)event -> addToConsole(((ViewEvent)event.getData()).eventName(),
                                        "viewEvent[" + ((ViewEvent)event.getData()).componentID() + "]"));
    }


    public void addToConsole(final Object data, final String prompt)
    {
        final List<String> consoleLog = getConsoleLog();
        if(consoleLog.size() > MAX_CONSOLE_LOG_SIZE)
        {
            consoleLog.remove(0);
            bottom.removeChild(bottom.getFirstChild());
        }
        final String newText = "$" + prompt + ":> " + data;
        consoleLog.add(newText);
        Clients.scrollIntoView(createConsoleEntry(newText));
    }


    public Component createConsoleEntry(final String entry)
    {
        final Label label = new Label(entry);
        bottom.appendChild(label);
        return label;
    }


    protected List<String> getConsoleLog()
    {
        Object attribute = getModel().getValue("consoleLog", Object.class);
        if(!(attribute instanceof List<?>))
        {
            attribute = new LinkedList<String>();
            getModel().setValue("consoleLog", attribute);
        }
        return (List<String>)attribute;
    }


    private String calcOutPrompt(final Event event)
    {
        return calcOutPrompt(event, null);
    }


    private String calcOutPrompt(final Event event, final String prefix)
    {
        String ret = prefix == null ? "output" : prefix;
        if(event instanceof com.hybris.cockpitng.events.SocketEvent)
        {
            final String sourceSocketID = ((com.hybris.cockpitng.events.SocketEvent)event).getSourceSocketID();
            ret += "[" + sourceSocketID + "]";
        }
        return ret;
    }


    private String calcInPrompt(final Event event)
    {
        String ret = "input";
        if(event instanceof com.hybris.cockpitng.events.SocketEvent)
        {
            final String sourceWidgetID = ((com.hybris.cockpitng.events.SocketEvent)event).getSourceWidgetID();
            try
            {
                final String[] split = sourceWidgetID.split("__");
                if(split.length > 0)
                {
                    ret += "[" + split[0] + "]";
                }
            }
            catch(final Exception e)
            {
                LOG.error("Cannot parse input prompt: ", e);
            }
        }
        return ret;
    }


    @SocketEvent(socketId = SOCKET_IN_COLLECTION_INPUT_LOGGER)
    public void logCollectionInput(final Event event)
    {
        addToConsole(event == null ? null : event.getData(), calcInPrompt(event));
    }


    @SocketEvent(socketId = SOCKET_IN_SINGLE_INPUT_LOGGER)
    public void logSingleInput(final Event event)
    {
        addToConsole(event != null ? event.getData() : null, calcInPrompt(event));
    }


    @SocketEvent(socketId = SOCKET_IN_COLLECTION_OUTPUT_LOGGER)
    public void logCollectionOutput(final Event event)
    {
        logData(event);
    }


    @SocketEvent(socketId = SOCKET_IN_SINGLE_OUTPUT_LOGGER)
    public void logSingleOutput(final Event event)
    {
        logData(event);
    }


    protected void logData(final Event event)
    {
        final Object val = event == null ? null : event.getData();
        addToConsole(val, calcOutPrompt(event));
        outputLabel.setValue(calcOutPrompt(event, "") + ": " + val);
    }


    @SocketEvent(socketId = SELECT_WIDGET_DEFINITION_INPUT_ID)
    public void selectWidgetDefinition(final String definitionID)
    {
        try
        {
            final AbstractCockpitComponentDefinition componentDefinitionForCode = componentDefinitionService
                            .getComponentDefinitionForCode(definitionID);
            if(componentDefinitionForCode instanceof WidgetDefinition)
            {
                selectWidgetDefinitionInternal((WidgetDefinition)componentDefinitionForCode);
            }
        }
        catch(final IllegalArgumentException e)
        {
            final String message = String.format("No component definition with id '%s' found, ignoring.", definitionID);
            if(LOG.isDebugEnabled())
            {
                LOG.warn(message, e);
            }
            else
            {
                LOG.warn(message);
            }
        }
    }


    @ViewEvent(componentID = WIDGET_SELECTOR_ID, eventName = Events.ON_SELECT)
    public void selectWidgetDefinition()
    {
        final WidgetDefinition selectedDefinition = widgetSelector.getSelectedItem().getValue();
        selectWidgetDefinitionInternal(selectedDefinition);
    }


    public void selectWidgetDefinitionInternal(final WidgetDefinition selectedDefinition)
    {
        setValue(SELECTED_DEFINITION, selectedDefinition);
        updateWidgetDefinition(selectedDefinition);
        getWidgetslot().updateView();
        updateCSSSlot(selectedDefinition);
    }


    private void updateCSSSlot(final WidgetDefinition selectedDefinition)
    {
        additionalStyleContainer.getChildren().clear();
        additionalStyleContainer.appendChild(new Style(selectedDefinition.getLocationPath() + "/" + "default.css"));
        additionalStyleContainer.appendChild(new Style(selectedDefinition.getLocationPath() + "/"
                        + ExtendedWidgetResourceLoader.getIdWithoutPackage(selectedDefinition) + ".css"));
    }


    @ViewEvent(componentID = BUTTON_WIDGET_SETTINGS, eventName = Events.ON_CLICK)
    public void showInputSocket()
    {
        final WidgetInstance widgetInstance = testSlot.getWidgetInstance();
        if(widgetInstance != null)
        {
            final Window createSettingsWizard = cockpitAdminService.createSettingsWizard(divWidgetSettings, testSlot,
                            widgetInstance.getWidget(), null);
            createSettingsWizard.doHighlighted();
        }
    }


    @ViewEvent(componentID = CLEAR_BUTTON, eventName = Events.ON_CLICK)
    public void clearConsole()
    {
        getConsoleLog().clear();
        bottom.getChildren().clear();
    }


    protected void updateDefinitionInfoArea(final WidgetDefinition definition)
    {
        widgetName.setValue(definition.getName());
        widgetCode.setValue(definition.getCode());
        widgetDescription.setValue(definition.getDescription());
        widgetController.setValue(definition.getController());
        widgetViewURI.setValue(definition.getViewURI());
        widgetCategoryTag.setValue(definition.getCategoryTag());
        widgetDefaultTitle.setValue(definition.getDefaultTitle());
    }


    public void updateWidgetDefinition(final WidgetDefinition definition)
    {
        // clear all children
        final List<WidgetInstance> widgetInstances = widgetInstanceService.getWidgetInstances(getWidgetslot().getWidgetInstance());
        for(final WidgetInstance widgetInstance : widgetInstances)
        {
            widgetService.removeWidget(widgetInstance.getWidget());
            widgetInstanceService.removeWidgetInstance(widgetInstance);
        }
        final String widgetID = UUID.randomUUID().toString();
        final Widget widget = widgetService.createWidget(getWidgetslot().getWidgetInstance().getWidget(), widgetID, "testedWidget",
                        definition.getCode());
        widgetInstanceService.createWidgetInstance(widget, getWidgetslot().getWidgetInstance());
        setValue("selectedWidgetSlot", widgetID);
        // add input widgets for inputs
        final List<WidgetSocket> inputs = definition.getInputs();
        for(final WidgetSocket widgetSocket : inputs)
        {
            final String id = widgetSocket.getId();
            final Multiplicity dataMultiplicity = widgetSocket.getDataMultiplicity();
            final String inputDefinitionId = "com.hybris.cockpitng.inputtestwidget";
            final Widget inputWidget = widgetService.createWidget(getWidgetslot().getWidgetInstance().getWidget(),
                            id + "__" + UUID.randomUUID().toString(), "inputs", inputDefinitionId);
            inputWidget.setTitle(id);
            inputWidget.getWidgetSettings().put("inputType",
                            (widgetSocket.getDataMultiplicity() != null) ? (StringUtils.capitalize((widgetSocket.getDataMultiplicity()
                                            .getCode())) + "<" + widgetSocket.getDataType() + ">") : widgetSocket.getDataType());
            widgetInstanceService.createWidgetInstance(inputWidget, getWidgetslot().getWidgetInstance());
            String outputID = "singleoutput";
            if(dataMultiplicity != null)
            {
                outputID = (dataMultiplicity.getCode() + "output").toLowerCase(Locale.getDefault());
            }
            widgetService.createWidgetConnection(inputWidget, widget, id, outputID);
            final String loggerID = dataMultiplicity == null ? "singleInputLogger" : "collectionInputLogger";
            widgetService.createWidgetConnection(inputWidget, getWidgetslot().getWidgetInstance().getWidget(), loggerID, outputID);
        }
        // Output widget
        final List<WidgetSocket> outputs = definition.getOutputs();
        if(CollectionUtils.isNotEmpty(outputs))
        {
            final String outputDefinitionId = "com.hybris.cockpitng.outputtestwidget";
            final Widget outputWidget = widgetService.createWidget(getWidgetslot().getWidgetInstance().getWidget(),
                            UUID.randomUUID().toString(), "output", outputDefinitionId);
            widgetInstanceService.createWidgetInstance(outputWidget, getWidgetslot().getWidgetInstance());
            for(final WidgetSocket widgetSocket : outputs)
            {
                final String id = widgetSocket.getId();
                final Multiplicity dataMultiplicity = widgetSocket.getDataMultiplicity();
                final String inputID = dataMultiplicity == null ? "genericInput" : "collectionInput";
                widgetService.createWidgetConnection(widget, outputWidget, inputID, id);
                final String loggerID = dataMultiplicity == null ? "singleOutputLogger" : "collectionOutputLogger";
                widgetService.createWidgetConnection(widget, getWidgetslot().getWidgetInstance().getWidget(), loggerID, id);
            }
        }
        setValue("linkURI", "./?mode=breadboard&widget=" + definition.getCode());
    }


    private List<WidgetDefinition> getWidgetDefinitions(final boolean excludeStubs)
    {
        final List<WidgetDefinition> componentDefinitionsByClass = componentDefinitionService
                        .getComponentDefinitionsByClass(WidgetDefinition.class);
        return componentDefinitionsByClass.stream().filter(def -> !(excludeStubs && def.isStubWidget()))
                        .collect(Collectors.toList());
    }


    public CockpitComponentDefinitionService getComponentDefinitionService()
    {
        return componentDefinitionService;
    }


    public WidgetInstanceService getWidgetInstanceService()
    {
        return widgetInstanceService;
    }


    public WidgetService getWidgetService()
    {
        return widgetService;
    }


    public CockpitAdminService getCockpitAdminService()
    {
        return cockpitAdminService;
    }


    public Listbox getWidgetSelector()
    {
        return widgetSelector;
    }


    public Label getWidgetCode()
    {
        return widgetCode;
    }


    public Label getWidgetName()
    {
        return widgetName;
    }


    public Label getWidgetDescription()
    {
        return widgetDescription;
    }


    public Label getWidgetController()
    {
        return widgetController;
    }


    public Label getWidgetCategoryTag()
    {
        return widgetCategoryTag;
    }


    public Label getWidgetViewURI()
    {
        return widgetViewURI;
    }


    public Label getWidgetDefaultTitle()
    {
        return widgetDefaultTitle;
    }


    public Component getBottom()
    {
        return bottom;
    }


    public Label getOutputLabel()
    {
        return outputLabel;
    }


    public Div getDivWidgetSettings()
    {
        return divWidgetSettings;
    }


    public Div getAdditionalStyleContainer()
    {
        return additionalStyleContainer;
    }


    public Widgetslot getTestSlot()
    {
        return testSlot;
    }
}
