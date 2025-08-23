/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.networkchart;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hybris.backoffice.widgets.networkchart.context.NetworkChartContext;
import com.hybris.backoffice.widgets.networkchart.handler.DataManipulationHandler;
import com.hybris.backoffice.widgets.networkchart.handler.NetworkPopulator;
import com.hybris.backoffice.widgets.networkchart.handler.ViewEventHandler;
import com.hybris.cockpitng.annotations.GlobalCockpitEvent;
import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.components.visjs.network.NetworkChart;
import com.hybris.cockpitng.components.visjs.network.data.Edge;
import com.hybris.cockpitng.components.visjs.network.data.Network;
import com.hybris.cockpitng.components.visjs.network.data.NetworkSettings;
import com.hybris.cockpitng.components.visjs.network.data.Node;
import com.hybris.cockpitng.components.visjs.network.data.Options;
import com.hybris.cockpitng.components.visjs.network.data.Setting;
import com.hybris.cockpitng.components.visjs.network.event.AddEdgeEvent;
import com.hybris.cockpitng.components.visjs.network.event.AddNodeEvent;
import com.hybris.cockpitng.components.visjs.network.event.BlurEdgeEvent;
import com.hybris.cockpitng.components.visjs.network.event.BlurNodeEvent;
import com.hybris.cockpitng.components.visjs.network.event.ClickEdgeEvent;
import com.hybris.cockpitng.components.visjs.network.event.ClickNodeEvent;
import com.hybris.cockpitng.components.visjs.network.event.ClickOnAddNodeButtonEvent;
import com.hybris.cockpitng.components.visjs.network.event.DeselectEdgesEvent;
import com.hybris.cockpitng.components.visjs.network.event.DeselectNodesEvent;
import com.hybris.cockpitng.components.visjs.network.event.DoubleClickEdgeEvent;
import com.hybris.cockpitng.components.visjs.network.event.DoubleClickNodeEvent;
import com.hybris.cockpitng.components.visjs.network.event.DragEndEvent;
import com.hybris.cockpitng.components.visjs.network.event.EditEdgeEvent;
import com.hybris.cockpitng.components.visjs.network.event.EditNodeEvent;
import com.hybris.cockpitng.components.visjs.network.event.HoverEdgeEvent;
import com.hybris.cockpitng.components.visjs.network.event.HoverNodeEvent;
import com.hybris.cockpitng.components.visjs.network.event.RemoveEdgesEvent;
import com.hybris.cockpitng.components.visjs.network.event.RemoveNodesEvent;
import com.hybris.cockpitng.components.visjs.network.event.SelectEdgeEvent;
import com.hybris.cockpitng.components.visjs.network.event.SelectNodeEvent;
import com.hybris.cockpitng.components.visjs.network.event.ViewPositionChangeEvent;
import com.hybris.cockpitng.components.visjs.network.response.Action;
import com.hybris.cockpitng.components.visjs.network.response.NetworkUpdate;
import com.hybris.cockpitng.components.visjs.network.response.NetworkUpdates;
import com.hybris.cockpitng.core.events.CockpitEvent;
import com.hybris.cockpitng.core.theme.ThemeStyleService;
import com.hybris.cockpitng.core.util.impl.TypedSettingsMap;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectCRUDHandler;
import com.hybris.cockpitng.engine.impl.ListContainerCloseListener;
import com.hybris.cockpitng.engine.impl.ListContainerRenderer;
import com.hybris.cockpitng.util.BackofficeSpringUtil;
import com.hybris.cockpitng.util.CockpitComponentsUtils;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.cockpitng.util.UITools;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.spring.SpringUtil;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Window;

/**
 * Default controller for vis.js network chart's widget
 */
public class NetworkChartController extends DefaultWidgetController
{
    private static final Logger LOG = LoggerFactory.getLogger(NetworkChartController.class);
    private static final String WIDGET_WINDOW_CLASS = "z-window";
    public static final String SOCKET_IN_INIT_DATA = "initData";
    public static final String SOCKET_IN_UPDATE_OBJECT = "updateObject";
    public static final String SETTING_CHART_OPTIONS = "chartOptions";
    public static final String SETTING_CHART_OPTIONS_PROVIDER = "chartOptionsProvider";
    public static final String SETTING_NETWORK_POPULATOR = "networkPopulator";
    public static final String SETTING_VIEW_EVENT_HANDLER = "viewEventHandler";
    public static final String SETTING_DATA_MANIPULATION_HANDLER = "dataManipulationHandler";
    public static final String SETTING_SHOW_CONTROLS_BUTTONS = "showControlsButtons";
    public static final String SETTING_CUSTOM_ADD_NODE_BUTTON = "customAddNodeButton";
    public static final String MODEL_INIT_DATA = "initData";
    public static final String MODEL_NETWORK_NODES = "networkNodes";
    public static final String MODEL_NETWORK_EDGES = "networkEdges";
    public static final String MODEL_CANVAS_CENTER = "canvasCenter";
    public static final String SCLASS_EMPTY_CHART = "yw-visjs-empty-chart";
    protected final transient ObjectMapper mapper = new ObjectMapper();
    @Wire
    private NetworkChart networkChart;
    @Wire
    private Button saveButton;
    @Wire
    private Button refreshButton;
    @Wire
    private Button cancelButton;
    @Wire
    private Div controlsButtonContainer;
    @WireVariable
    private transient NetworkChartValidationPopupFactory networkChartValidationPopupFactory;
    @WireVariable
    private transient NetworkChartOptionsResolver networkChartOptionsResolver;
    @WireVariable
    private transient ThemeStyleService themeStyleService;


    @Override
    public void initialize(final Component comp)
    {
        super.initialize(comp);
        initModel();
        networkChart.setOptions(networkChartOptionsResolver.resolveOptions(this));
        restoreNetworkFromModel();
        injectSettings();
        initializeWorkflowDesignerButtons();
        initializeHandlers();
    }


    protected void injectSettings()
    {
        final List<Setting> settings = new ArrayList<>();
        final Serializable customSetting = Optional.ofNullable(getWidgetSettings().get(SETTING_CUSTOM_ADD_NODE_BUTTON))
                        .filter(Serializable.class::isInstance).map(Serializable.class::cast).orElse(null);
        settings.add(new Setting(SETTING_CUSTOM_ADD_NODE_BUTTON, customSetting));
        networkChart.setSettings(new NetworkSettings(settings));
    }


    protected void initializeWorkflowDesignerButtons()
    {
        final boolean controlsButtonsVisibility = getWidgetSettings().getBoolean(SETTING_SHOW_CONTROLS_BUTTONS);
        controlsButtonContainer.setVisible(controlsButtonsVisibility);
    }


    protected void initializeHandlers()
    {
        final NetworkChartContext context = new NetworkChartContext(getWidgetInstanceManager());
        final ViewEventHandler viewEventHandler = getViewEventHandler();
        final DataManipulationHandler dataManipulationHandler = getDataManipulationHandler();
        if(viewEventHandler != null)
        {
            initializeViewEventListeners(viewEventHandler, context);
        }
        if(dataManipulationHandler != null)
        {
            initializeDataManipulationListeners(dataManipulationHandler, context);
        }
    }


    protected void initModel()
    {
        final Set nodes = getValue(MODEL_NETWORK_NODES, Set.class);
        if(nodes == null)
        {
            setValue(MODEL_NETWORK_NODES, new HashSet<>());
        }
        final Set edges = getValue(MODEL_NETWORK_EDGES, Set.class);
        if(edges == null)
        {
            setValue(MODEL_NETWORK_EDGES, new HashSet<>());
        }
    }


    protected void resetModel()
    {
        setValue(MODEL_NETWORK_NODES, new HashSet<>());
        setValue(MODEL_NETWORK_EDGES, new HashSet<>());
    }


    protected void restoreNetworkFromModel()
    {
        final NetworkPopulator networkPopulator = getNetworkPopulator();
        final Object initData = getValue(MODEL_INIT_DATA, Object.class);
        final NetworkChartContext context = new NetworkChartContext(getWidgetInstanceManager());
        context.setInitData(initData);
        if(networkPopulator != null && getValue(MODEL_NETWORK_NODES, Set.class).isEmpty()
                        && getValue(MODEL_NETWORK_EDGES, Set.class).isEmpty())
        {
            final Network initNetwork = networkPopulator.populate(context);
            setValue(MODEL_NETWORK_NODES, new HashSet<>(initNetwork.getNodes()));
            setValue(MODEL_NETWORK_EDGES, new HashSet<>(initNetwork.getEdges()));
            networkChart.setNetwork(new Network(initNetwork.getNodes(), initNetwork.getEdges()));
        }
        else
        {
            networkChart.setNetwork(new Network(getValue(MODEL_NETWORK_NODES, Set.class), getValue(MODEL_NETWORK_EDGES, Set.class)));
        }
        updateNetworkChartScss();
    }


    protected void updateNetworkChartScss()
    {
        final boolean isChartEmpty = CollectionUtils.isEmpty(getValue(MODEL_NETWORK_NODES, Set.class));
        UITools.modifySClass(networkChart, SCLASS_EMPTY_CHART, isChartEmpty);
    }


    protected List joinCollections(final Collection... collections)
    {
        final Stream<Object> stream = Stream.of(collections).flatMap(Collection::stream);
        return stream.collect(Collectors.toList());
    }


    @SocketEvent(socketId = SOCKET_IN_INIT_DATA)
    public void initData(final Object initData)
    {
        setValue(MODEL_INIT_DATA, initData);
        networkChart.setOptions(networkChartOptionsResolver.resolveOptions(this));
        final NetworkPopulator networkPopulator = getNetworkPopulator();
        if(networkPopulator != null)
        {
            final NetworkChartContext context = new NetworkChartContext(getWidgetInstanceManager());
            context.setInitData(initData);
            final Network initNetwork = networkPopulator.populate(context);
            if(initNetwork != null)
            {
                setValue(MODEL_NETWORK_NODES, new HashSet<>(initNetwork.getNodes()));
                setValue(MODEL_NETWORK_EDGES, new HashSet<>(initNetwork.getEdges()));
                networkChart.setNetwork(initNetwork);
            }
        }
        updateNetworkChartScss();
    }


    @SocketEvent(socketId = SOCKET_IN_UPDATE_OBJECT)
    public void updateObject(final Object objectToUpdate)
    {
        if(objectToUpdate != null)
        {
            final NetworkPopulator networkPopulator = getNetworkPopulator();
            if(networkPopulator != null)
            {
                final NetworkUpdates networkUpdates = networkPopulator.update(objectToUpdate,
                                new NetworkChartContext(getWidgetInstanceManager()));
                if(networkUpdates != null)
                {
                    storeNetworkUpdateInModel(networkUpdates);
                    networkChart.updateNetwork(networkUpdates);
                }
            }
        }
        updateNetworkChartScss();
    }


    @GlobalCockpitEvent(eventName = ObjectCRUDHandler.OBJECTS_UPDATED_EVENT, scope = CockpitEvent.SESSION)
    public void handleObjectsUpdatedEvent(final CockpitEvent event)
    {
        handleEvent(event, this::initData);
    }


    @GlobalCockpitEvent(eventName = ObjectCRUDHandler.OBJECTS_DELETED_EVENT, scope = CockpitEvent.SESSION)
    public void handleObjectsDeletedEvent(final CockpitEvent event)
    {
        handleEvent(event, foundObject -> initData(null));
        updateNetworkChartScss();
    }


    protected void handleEvent(final CockpitEvent event, final Consumer consumer)
    {
        final Object initObject = getValue(MODEL_INIT_DATA, Object.class);
        if(initObject != null)
        {
            final Collection<Object> updatedObjects = event.getDataAsCollection();
            updatedObjects.stream().filter(initObject::equals).findFirst().ifPresent(updatedObject -> {
                resetModel();
                consumer.accept(updatedObject);
            });
        }
    }


    protected void initializeViewEventListeners(final ViewEventHandler viewEventHandler, final NetworkChartContext context)
    {
        networkChart.addEventListener(ClickNodeEvent.NAME, (final ClickNodeEvent event) -> {
            final NetworkUpdates chartUpdates = viewEventHandler.onClick(event.getNode(), context);
            storeNetworkUpdateInModel(chartUpdates);
            networkChart.updateNetwork(chartUpdates);
        });
        networkChart.addEventListener(DoubleClickNodeEvent.NAME, (final DoubleClickNodeEvent event) -> {
            final NetworkUpdates chartUpdates = viewEventHandler.onDoubleClick(event.getNode(), context);
            storeNetworkUpdateInModel(chartUpdates);
            networkChart.updateNetwork(chartUpdates);
        });
        networkChart.addEventListener(SelectNodeEvent.NAME, (final SelectNodeEvent event) -> {
            final NetworkUpdates chartUpdates = viewEventHandler.onSelect(event.getNode(), context);
            storeNetworkUpdateInModel(chartUpdates);
            networkChart.updateNetwork(chartUpdates);
        });
        networkChart.addEventListener(DeselectNodesEvent.NAME, (final DeselectNodesEvent event) -> {
            final NetworkUpdates chartUpdates = viewEventHandler.onDeselect(event.getNodes(), context);
            storeNetworkUpdateInModel(chartUpdates);
            networkChart.updateNetwork(chartUpdates);
        });
        networkChart.addEventListener(ClickEdgeEvent.NAME, (final ClickEdgeEvent event) -> {
            final NetworkUpdates chartUpdates = viewEventHandler.onClick(event.getEdge(), context);
            storeNetworkUpdateInModel(chartUpdates);
            networkChart.updateNetwork(chartUpdates);
        });
        networkChart.addEventListener(DoubleClickEdgeEvent.NAME, (final DoubleClickEdgeEvent event) -> {
            final NetworkUpdates chartUpdates = viewEventHandler.onDoubleClick(event.getEdge(), context);
            storeNetworkUpdateInModel(chartUpdates);
            networkChart.updateNetwork(chartUpdates);
        });
        networkChart.addEventListener(SelectEdgeEvent.NAME, (final SelectEdgeEvent event) -> {
            final NetworkUpdates chartUpdates = viewEventHandler.onSelect(event.getEdge(), context);
            storeNetworkUpdateInModel(chartUpdates);
            networkChart.updateNetwork(chartUpdates);
        });
        networkChart.addEventListener(HoverEdgeEvent.NAME, (final HoverEdgeEvent event) -> {
            final NetworkUpdates chartUpdates = viewEventHandler.onHover(event.getEdge(), context);
            storeNetworkUpdateInModel(chartUpdates);
            networkChart.updateNetwork(chartUpdates);
        });
        networkChart.addEventListener(HoverNodeEvent.NAME, (final HoverNodeEvent event) -> {
            final NetworkUpdates chartUpdates = viewEventHandler.onHover(event.getNode(), context);
            storeNetworkUpdateInModel(chartUpdates);
            networkChart.updateNetwork(chartUpdates);
        });
        networkChart.addEventListener(BlurNodeEvent.NAME, (final BlurNodeEvent event) -> {
            final NetworkUpdates chartUpdates = viewEventHandler.onBlur(event.getNode(), context);
            storeNetworkUpdateInModel(chartUpdates);
            networkChart.updateNetwork(chartUpdates);
        });
        networkChart.addEventListener(BlurEdgeEvent.NAME, (final BlurEdgeEvent event) -> {
            final NetworkUpdates chartUpdates = viewEventHandler.onBlur(event.getEdge(), context);
            storeNetworkUpdateInModel(chartUpdates);
            networkChart.updateNetwork(chartUpdates);
        });
        networkChart.addEventListener(DeselectEdgesEvent.NAME, (final DeselectEdgesEvent event) -> {
            final NetworkUpdates chartUpdates = viewEventHandler.onDeselect(event.getEdges(), context);
            storeNetworkUpdateInModel(chartUpdates);
            networkChart.updateNetwork(chartUpdates);
        });
        networkChart.addEventListener(DragEndEvent.NAME, (final DragEndEvent event) -> {
            final NetworkUpdates chartUpdates = viewEventHandler.onDragEnd(event.getNode(), context);
            storeNetworkUpdateInModel(chartUpdates);
            networkChart.updateNetwork(chartUpdates);
        });
    }


    protected void initializeDataManipulationListeners(final DataManipulationHandler dataManipulationHandler,
                    final NetworkChartContext context)
    {
        networkChart.addEventListener(AddNodeEvent.NAME, (final AddNodeEvent event) -> {
            final NetworkUpdates chartUpdates = dataManipulationHandler.onAdd(event.getNode(), context);
            storeNetworkUpdateInModel(chartUpdates);
            networkChart.updateNetwork(chartUpdates);
        });
        networkChart.addEventListener(EditNodeEvent.NAME, (final EditNodeEvent event) -> {
            final NetworkUpdates chartUpdates = dataManipulationHandler.onEdit(event.getNode(), context);
            storeNetworkUpdateInModel(chartUpdates);
            networkChart.updateNetwork(chartUpdates);
        });
        networkChart.addEventListener(RemoveNodesEvent.NAME, (final RemoveNodesEvent event) -> {
            final NetworkUpdates chartUpdates = dataManipulationHandler.onRemove(event.getNodes(), context);
            storeNetworkUpdateInModel(chartUpdates);
            networkChart.updateNetwork(chartUpdates);
            updateNetworkChartScss();
        });
        networkChart.addEventListener(AddEdgeEvent.NAME, (final AddEdgeEvent event) -> {
            final NetworkUpdates chartUpdates = dataManipulationHandler.onAdd(event.getEdge(), context);
            storeNetworkUpdateInModel(chartUpdates);
            networkChart.updateNetwork(chartUpdates);
        });
        networkChart.addEventListener(EditEdgeEvent.NAME, (final EditEdgeEvent event) -> {
            final NetworkUpdates chartUpdates = dataManipulationHandler.onEdit(event.getEdge(), context);
            storeNetworkUpdateInModel(chartUpdates);
            networkChart.updateNetwork(chartUpdates);
        });
        networkChart.addEventListener(RemoveEdgesEvent.NAME, (final RemoveEdgesEvent event) -> {
            final NetworkUpdates chartUpdates = dataManipulationHandler.onRemove(event.getEdges(), context);
            storeNetworkUpdateInModel(chartUpdates);
            networkChart.updateNetwork(chartUpdates);
        });
        networkChart.addEventListener(ClickOnAddNodeButtonEvent.NAME, (final ClickOnAddNodeButtonEvent event) -> {
            final NetworkUpdates chartUpdates = dataManipulationHandler.onAddNodeButtonClick(event, context);
            storeNetworkUpdateInModel(chartUpdates);
            networkChart.updateNetwork(chartUpdates);
        });
        networkChart.addEventListener(ViewPositionChangeEvent.NAME,
                        (final ViewPositionChangeEvent event) -> setValue(MODEL_CANVAS_CENTER, event.getViewPosition()));
        initializeControlButtonsListeners(dataManipulationHandler, context);
    }


    protected void initializeControlButtonsListeners(final DataManipulationHandler dataManipulationHandler,
                    final NetworkChartContext context)
    {
        cancelButton.addEventListener(Events.ON_CLICK, event -> {
            final Optional<ListContainerCloseListener> evaluatedCloseListener = evaluateOnCloseListener(getWidgetSettings());
            if(evaluatedCloseListener.isEmpty())
            {
                dataManipulationHandler.onCancel(context);
                return;
            }
            findTemplateWindow().ifPresent(window -> {
                final Event onCancelWindowEvent = new Event(Events.ON_CANCEL, window);
                evaluatedCloseListener.get().onClose(onCancelWindowEvent, getWidgetslot().getWidgetInstance());
                if(onCancelWindowEvent.isPropagatable())
                {
                    dataManipulationHandler.onCancel(context);
                    window.onClose();
                }
            });
        });
        refreshButton.addEventListener(Events.ON_CLICK, event ->
                        handleRefresh(dataManipulationHandler, context));
        saveButton.addEventListener(Events.ON_CLICK, event -> {
            final Runnable saveRunnable = () -> {
                dataManipulationHandler.onSave(context);
                handleRefresh(dataManipulationHandler, context);
            };
            final Optional<Window> validationPopup = networkChartValidationPopupFactory.createValidationPopup(context, saveButton,
                            saveRunnable);
            if(validationPopup.isEmpty())
            {
                saveRunnable.run();
            }
        });
    }


    /**
     * Tries to find parent window for this template widget
     *
     * @return window object if the widget is a template, {@link Optional#empty()} otherwise
     */
    protected Optional<Window> findTemplateWindow()
    {
        return CockpitComponentsUtils.findClosestComponent(getWidgetslot(), Window.class,
                        getWidgetSettings().getString(WIDGET_WINDOW_CLASS));
    }


    /**
     * Evaluates {@link ListContainerCloseListener} specified by {@link ListContainerRenderer#CLOSE_LISTENER_SETTING} on
     * widget settings
     *
     * @param widgetSettings
     *           widget settings containing close listener spring bean name
     * @return ListContainerCloseListener or empty if not found
     */
    protected Optional<ListContainerCloseListener> evaluateOnCloseListener(final TypedSettingsMap widgetSettings)
    {
        final String closeListenerBeanName = widgetSettings.getString(ListContainerRenderer.CLOSE_LISTENER_SETTING);
        if(StringUtils.isNoneBlank(closeListenerBeanName))
        {
            return Optional.ofNullable(BackofficeSpringUtil.getBean(closeListenerBeanName, ListContainerCloseListener.class));
        }
        return Optional.empty();
    }


    /**
     * Allows to refresh model and sends the event about refreshing
     *
     * @param dataManipulationHandler
     * @param context
     */
    protected void handleRefresh(final DataManipulationHandler dataManipulationHandler, final NetworkChartContext context)
    {
        dataManipulationHandler.onRefresh(context);
        resetModel();
        restoreNetworkFromModel();
    }


    /**
     * Persists network updates in model
     *
     * @param updatesToStore
     */
    protected void storeNetworkUpdateInModel(final NetworkUpdates updatesToStore)
    {
        final Set nodes = getValue(MODEL_NETWORK_NODES, Set.class);
        final Set edges = getValue(MODEL_NETWORK_EDGES, Set.class);
        for(final NetworkUpdate update : updatesToStore.getUpdates())
        {
            if(update.getAction() == Action.ADD || update.getAction() == Action.UPDATE)
            {
                if(update.getEntity() instanceof Node)
                {
                    nodes.remove(update.getEntity());
                    nodes.add(update.getEntity());
                    updateEdgesConnectedWithNode((Node)update.getEntity());
                }
                else
                {
                    edges.remove(update.getEntity());
                    edges.add(update.getEntity());
                }
            }
            else if(update.getAction() == Action.REMOVE)
            {
                if(update.getEntity() instanceof Node)
                {
                    nodes.remove(update.getEntity());
                    removeEdgesConnectedWithNode((Node)update.getEntity());
                }
                else
                {
                    edges.remove(update.getEntity());
                }
            }
        }
    }


    private void updateEdgesConnectedWithNode(final Node node)
    {
        final Set<Edge> edges = getValue(MODEL_NETWORK_EDGES, Set.class);
        if(edges != null)
        {
            final List<Edge> updatedEdges = new ArrayList<>();
            edges.forEach(edge -> {
                if(Objects.equals(edge.getFromNode(), node))
                {
                    updatedEdges.add(new Edge.Builder(edge, node, edge.getToNode()).build());
                }
                else if(Objects.equals(edge.getToNode(), node))
                {
                    updatedEdges.add(new Edge.Builder(edge, edge.getFromNode(), node).build());
                }
            });
            edges.removeAll(updatedEdges);
            edges.addAll(updatedEdges);
        }
    }


    private void removeEdgesConnectedWithNode(final Node node)
    {
        final Set<Edge> edges = getValue(MODEL_NETWORK_EDGES, Set.class);
        if(edges != null)
        {
            final List<Edge> connectedEdges = edges.stream()
                            .filter(edge -> edge.getFromNode().equals(node) || edge.getToNode().equals(node)).collect(Collectors.toList());
            edges.removeAll(connectedEdges);
        }
    }


    /**
     * @return {@link Options} of vis.js
     */
    public Options getChartOptions()
    {
        var opt = getWidgetSettings().getString(SETTING_CHART_OPTIONS);
        if(StringUtils.isBlank(opt))
        {
            return new Options.Builder().build();
        }
        try
        {
            final Map<String, String> currentThemeStyleMap = getThemeStyleService().getCurrentThemeStyleMap();
            if(!currentThemeStyleMap.isEmpty())
            {
                final var THEME_VALUE_REGEXP = "var\\(\\-\\-.*\\)";
                final var pattern = Pattern.compile(THEME_VALUE_REGEXP);
                final var matcher = pattern.matcher(opt);
                while(matcher.find())
                {
                    final String matchedValue = matcher.group();
                    final var keyValue = matchedValue.substring(4, matchedValue.length() - 1);
                    opt = currentThemeStyleMap.get(keyValue) != null ?
                                    opt.replace(matchedValue, currentThemeStyleMap.get(keyValue)) :
                                    opt;
                }
            }
            return mapper.readValue(opt, Options.class);
        }
        catch(final IOException e)
        {
            LOG.warn("Cannot read vis.js chart's options", e);
        }
        return new Options.Builder().build();
    }


    public OptionsProvider getChartOptionsProvider()
    {
        final String chartOptionsProviderBeanId = getWidgetSettings().getString(SETTING_CHART_OPTIONS_PROVIDER);
        return (OptionsProvider)SpringUtil.getBean(chartOptionsProviderBeanId, OptionsProvider.class);
    }


    /**
     * @return {@link NetworkPopulator} which is responsible for providing initial data to network chart.
     */
    protected NetworkPopulator getNetworkPopulator()
    {
        final String networkPopulatorBeanId = getWidgetSettings().getString(SETTING_NETWORK_POPULATOR);
        return (NetworkPopulator)SpringUtil.getBean(networkPopulatorBeanId, NetworkPopulator.class);
    }


    /**
     * @return {@link ViewEventHandler} which is responsible for handling network events: click, doubleClick, selection and
     *         deselection of node and edge.
     */
    protected ViewEventHandler getViewEventHandler()
    {
        final String viewEventHandlerBeanId = getWidgetSettings().getString(SETTING_VIEW_EVENT_HANDLER);
        return (ViewEventHandler)SpringUtil.getBean(viewEventHandlerBeanId, ViewEventHandler.class);
    }


    /**
     * @return {@link DataManipulationHandler} which is responsible for manipulating network structure.
     */
    protected DataManipulationHandler getDataManipulationHandler()
    {
        final String dataManipulationBeanId = getWidgetSettings().getString(SETTING_DATA_MANIPULATION_HANDLER);
        return (DataManipulationHandler)SpringUtil.getBean(dataManipulationBeanId, DataManipulationHandler.class);
    }


    public void setNetworkChart(final NetworkChart networkChart)
    {
        this.networkChart = networkChart;
    }


    public NetworkChart getNetworkChart()
    {
        return networkChart;
    }


    public Button getSaveButton()
    {
        return saveButton;
    }


    public void setSaveButton(final Button saveButton)
    {
        this.saveButton = saveButton;
    }


    public Button getRefreshButton()
    {
        return refreshButton;
    }


    public void setRefreshButton(final Button refreshButton)
    {
        this.refreshButton = refreshButton;
    }


    public Button getCancelButton()
    {
        return cancelButton;
    }


    public void setCancelButton(final Button cancelButton)
    {
        this.cancelButton = cancelButton;
    }


    public Div getControlsButtonContainer()
    {
        return controlsButtonContainer;
    }


    public void setControlsButtonContainer(final Div controlsButtonContainer)
    {
        this.controlsButtonContainer = controlsButtonContainer;
    }


    public ThemeStyleService getThemeStyleService()
    {
        return themeStyleService;
    }
}
