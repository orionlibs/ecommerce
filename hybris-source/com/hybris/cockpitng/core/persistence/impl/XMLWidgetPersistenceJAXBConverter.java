/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.persistence.impl;

import com.google.common.collect.Lists;
import com.hybris.cockpitng.core.AbstractCockpitComponentDefinition;
import com.hybris.cockpitng.core.CNG;
import com.hybris.cockpitng.core.CockpitComponentDefinitionService;
import com.hybris.cockpitng.core.Widget;
import com.hybris.cockpitng.core.WidgetDefinition;
import com.hybris.cockpitng.core.WidgetInstanceSettings;
import com.hybris.cockpitng.core.WidgetService;
import com.hybris.cockpitng.core.WidgetSocket;
import com.hybris.cockpitng.core.persistence.impl.jaxb.AllSocketEvents;
import com.hybris.cockpitng.core.persistence.impl.jaxb.CloseSettings;
import com.hybris.cockpitng.core.persistence.impl.jaxb.CreateSettings;
import com.hybris.cockpitng.core.persistence.impl.jaxb.InstanceSettings;
import com.hybris.cockpitng.core.persistence.impl.jaxb.SelectSettings;
import com.hybris.cockpitng.core.persistence.impl.jaxb.SettingType;
import com.hybris.cockpitng.core.persistence.impl.jaxb.Socket;
import com.hybris.cockpitng.core.persistence.impl.jaxb.SocketEvent;
import com.hybris.cockpitng.core.persistence.impl.jaxb.SocketEventRoutingMode;
import com.hybris.cockpitng.core.persistence.impl.jaxb.SocketEvents;
import com.hybris.cockpitng.core.persistence.impl.jaxb.SocketMultiplicity;
import com.hybris.cockpitng.core.persistence.impl.jaxb.SocketVisibility;
import com.hybris.cockpitng.core.persistence.impl.jaxb.VirtualSockets;
import com.hybris.cockpitng.core.persistence.impl.jaxb.WidgetConnection;
import com.hybris.cockpitng.core.persistence.impl.jaxb.WidgetSetting;
import com.hybris.cockpitng.core.persistence.impl.jaxb.Widgets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class to convert from/to JAXB classes to/from corresponding POJOs used by the framework.
 */
class XMLWidgetPersistenceJAXBConverter
{
    private static final Logger LOG = LoggerFactory.getLogger(XMLWidgetPersistenceJAXBConverter.class);
    private final XMLWidgetPersistenceJAXBLookupHandler lookupHandler;


    public XMLWidgetPersistenceJAXBConverter(final XMLWidgetPersistenceJAXBLookupHandler lookupHandler)
    {
        this.lookupHandler = lookupHandler;
    }


    /**
     * @param widgets
     *           widgets to update / store
     * @param node
     *           node to be added to the widgets
     * @param definitionService
     *           component definition service
     * @return existing widget or newly created one if the widgets did not contain the required element
     */
    public com.hybris.cockpitng.core.persistence.impl.jaxb.Widget updateWidgetsFromModel(final Widgets widgets, final Widget node,
                    final CockpitComponentDefinitionService definitionService)
    {
        if(node == null || node.isPartOfGroup())
        {
            return null;
        }
        com.hybris.cockpitng.core.persistence.impl.jaxb.Widget widget = lookupHandler.findWidgetById(widgets, node.getId());
        if(widget == null)
        {
            widget = createWidgetFromModel(node, definitionService);
            if(node.getParent() == null)
            {
                widgets.getWidget().add(widget);
            }
        }
        updateWidget(widget, node, definitionService);
        final List<com.hybris.cockpitng.core.persistence.impl.jaxb.Widget> widgetChildren = new ArrayList<>();
        for(final Widget childNode : node.getChildren())
        {
            if(node.equals(childNode))
            {
                continue;
            }
            final com.hybris.cockpitng.core.persistence.impl.jaxb.Widget widgetChild = updateWidgetsFromModel(widgets, childNode,
                            definitionService);
            if(widgetChild != null)
            {
                widgetChildren.add(widgetChild);
            }
        }
        widget.getWidget().clear();
        widget.getWidget().addAll(widgetChildren);
        return widget;
    }


    public com.hybris.cockpitng.core.persistence.impl.jaxb.Widget createWidgetFromModel(final Widget node,
                    final CockpitComponentDefinitionService definitionService)
    {
        final com.hybris.cockpitng.core.persistence.impl.jaxb.Widget widget = new com.hybris.cockpitng.core.persistence.impl.jaxb.Widget();
        updateWidget(widget, node, definitionService);
        return widget;
    }


    private void updateWidget(final com.hybris.cockpitng.core.persistence.impl.jaxb.Widget widget, final Widget node,
                    final CockpitComponentDefinitionService definitionService)
    {
        widget.setId(node.getId());
        widget.setSlotId(node.getSlotId());
        widget.setWidgetDefinitionId(node.getWidgetDefinitionId());
        widget.setTemplate(node.isTemplate() ? Boolean.TRUE : Boolean.FALSE);
        widget.setTitle(node.getTitleNoFallback());
        String access = null;
        if(CollectionUtils.isNotEmpty(node.getAccessRestrictions()))
        {
            access = StringUtils.join(node.getAccessRestrictions(), ',');
        }
        widget.setAccess(access);
        widget.getSetting().clear();
        final Set<String> keySet = new HashSet<>(node.getWidgetSettings().keySet());
        final AbstractCockpitComponentDefinition componentDefinition = definitionService
                        .getComponentDefinitionForCode(node.getWidgetDefinitionId());
        Map<String, Object> defaultSettings = null;
        if(componentDefinition != null && componentDefinition.getDefaultSettings() != null)
        {
            defaultSettings = componentDefinition.getDefaultSettings().getAll();
        }
        boolean isComposedWidget = false;
        if(componentDefinition instanceof WidgetDefinition)
        {
            isComposedWidget = ((WidgetDefinition)componentDefinition).getComposedWidgetRoot() != null;
        }
        for(final String settingKey : keySet)
        {
            if(isComposedWidget)
            {
                final NestedSetting nestedSetting = new NestedSetting(settingKey);
                if(nestedSetting.isNestedSetting())
                {
                    final String widgetId = nestedSetting.getWidgetId();
                    final WidgetDefinition widgetDefinition = findWidgetDefinition(
                                    ((WidgetDefinition)componentDefinition).getComposedWidgetRoot(), widgetId, definitionService);
                    if(widgetDefinition != null && widgetDefinition.getDefaultSettings() != null)
                    {
                        final Object defaultSettingValue = widgetDefinition.getDefaultSettings().getAll()
                                        .get(nestedSetting.getOnlySettingName());
                        final Object currentValue = node.getWidgetSettings().get(settingKey);
                        if(Objects.equals(defaultSettingValue, currentValue))
                        {
                            continue;
                        }
                    }
                }
            }
            final Object object = node.getWidgetSettings().get(settingKey);
            if(defaultSettings != null && defaultSettings.containsKey(settingKey)
                            && Objects.equals(defaultSettings.get(settingKey), object))
            {
                continue;
            }
            final WidgetSetting widgetSetting = new WidgetSetting();
            widgetSetting.setKey(settingKey);
            widgetSetting.setValue(String.valueOf(object));
            widgetSetting.setType(settingTypeByObject(object));
            widget.getSetting().add(widgetSetting);
        }
        updateWidgetInstanceSettings(widget, node);
        final VirtualSockets virtualSockets = new VirtualSockets();
        widget.setVirtualSockets(virtualSockets);
        for(final WidgetSocket socket : node.getVirtualInputs())
        {
            final Socket soc = new Socket();
            soc.setId(socket.getId());
            soc.setType(socket.getDataType());
            soc.setMultiplicity(socketMultiplicityFromModel(socket.getDataMultiplicity()));
            soc.setVisibility(socketVisibilityFromModel(socket.getVisibility()));
            virtualSockets.getInput().add(soc);
        }
        for(final WidgetSocket socket : node.getVirtualOutputs())
        {
            final Socket soc = new Socket();
            soc.setId(socket.getId());
            soc.setType(socket.getDataType());
            soc.setMultiplicity(socketMultiplicityFromModel(socket.getDataMultiplicity()));
            soc.setVisibility(socketVisibilityFromModel(socket.getVisibility()));
            virtualSockets.getOutput().add(soc);
        }
    }


    private WidgetDefinition findWidgetDefinition(final Widget rootWidget, final String childId,
                    final CockpitComponentDefinitionService definitionService)
    {
        final Widget widget = lookupHandler.findWidgetInWidgetTree(rootWidget, childId);
        if(widget != null)
        {
            return definitionService.getComponentDefinitionForCode(widget.getWidgetDefinitionId(), WidgetDefinition.class);
        }
        return null;
    }


    private void updateWidgetInstanceSettings(final InstanceSettings wConfig, final WidgetInstanceSettings nodeConfig)
    {
        // socket event routing
        wConfig.setSocketEventRoutingMode(nodeConfig.getSocketEventRoutingMode() == null ? null
                        : socketEventRoutingModeFromModel(nodeConfig.getSocketEventRoutingMode()));
        // create
        CreateSettings create = wConfig.getCreate();
        if(create == null)
        {
            create = new CreateSettings();
            wConfig.setCreate(create);
        }
        updateCreateSettings(create, nodeConfig);
        // close
        CloseSettings close = wConfig.getClose();
        if(close == null)
        {
            close = new CloseSettings();
            wConfig.setClose(close);
        }
        updateCloseSettings(close, nodeConfig);
        // select
        SelectSettings select = wConfig.getSelect();
        if(select == null)
        {
            select = new SelectSettings();
            wConfig.setSelect(select);
        }
        updateSelectSettings(select, nodeConfig);
    }


    private void updateCreateSettings(final CreateSettings create, final WidgetInstanceSettings nodeConfig)
    {
        create.setOnInit(Boolean.valueOf(nodeConfig.isCreateOnInit()));
        create.setReuseExisting(Boolean.valueOf(nodeConfig.isReuseExisting()));
        if(nodeConfig.isCreateOnAllIncomingEvents())
        {
            create.setAllIncomingEvents(new AllSocketEvents());
        }
        else
        {
            create.setAllIncomingEvents(null);
        }
        create.setIncomingEvents(createSocketEvents(nodeConfig.getCreateOnIncomingEvents()));
    }


    private void updateCloseSettings(final CloseSettings close, final WidgetInstanceSettings nodeConfig)
    {
        // incoming
        if(nodeConfig.isCloseOnAllIncomingEvents())
        {
            close.setAllIncomingEvents(new AllSocketEvents());
        }
        else
        {
            close.setAllIncomingEvents(null);
        }
        close.setIncomingEvents(createSocketEvents(nodeConfig.getCloseOnIncomingEvents()));
        // outgoing
        if(nodeConfig.isCloseOnAllOutgoingEvents())
        {
            close.setAllOutgoingEvents(new AllSocketEvents());
        }
        else
        {
            close.setAllOutgoingEvents(null);
        }
        close.setOutgoingEvents(createSocketEvents(nodeConfig.getCloseOnOutgoingEvents()));
    }


    private void updateSelectSettings(final SelectSettings select, final WidgetInstanceSettings nodeConfig)
    {
        select.setOnInit(Boolean.valueOf(nodeConfig.isSelectOnInit()));
        if(nodeConfig.isSelectOnAllIncomingEvents())
        {
            select.setAllIncomingEvents(new AllSocketEvents());
        }
        else
        {
            select.setAllIncomingEvents(null);
        }
        select.setIncomingEvents(createSocketEvents(nodeConfig.getSelectOnIncomingEvents()));
    }


    private void updateWidgetInstanceSettings(final com.hybris.cockpitng.core.persistence.impl.jaxb.Widget widget,
                    final Widget node)
    {
        if(!node.isTemplate())
        {
            widget.setInstanceSettings(null);
        }
        else
        {
            final WidgetInstanceSettings nodeConfig = node.getWidgetInstanceSettings();
            InstanceSettings wConfig = widget.getInstanceSettings();
            if(wConfig == null)
            {
                wConfig = new InstanceSettings();
            }
            updateWidgetInstanceSettings(wConfig, nodeConfig);
            widget.setInstanceSettings(wConfig);
        }
    }


    public Widget createWidgetTreeFromJAXB(final com.hybris.cockpitng.core.persistence.impl.jaxb.Widget jaxbWidget,
                    final Widget parent, final Map<com.hybris.cockpitng.core.persistence.impl.jaxb.Widget, Widget> map,
                    final WidgetService ws)
    {
        final Widget widget = ws.createWidget(parent, jaxbWidget.getId(), jaxbWidget.getSlotId(),
                        jaxbWidget.getWidgetDefinitionId());
        widget.setTemplate(jaxbWidget.isTemplate());
        widget.setTitle(jaxbWidget.getTitle());
        if(jaxbWidget.getAccess() != null)
        {
            widget.setAccessRestrictions(extractAccessRestrictions(jaxbWidget.getAccess()));
        }
        if(jaxbWidget.getInstanceSettings() != null)
        {
            widget.setWidgetInstanceSettings(widgetInstanceSettingsFromJAXB(jaxbWidget.getInstanceSettings()));
        }
        if(CollectionUtils.isNotEmpty(jaxbWidget.getSetting()))
        {
            rewriteWidgetSettingsFromJAXB(jaxbWidget, widget);
        }
        final VirtualSockets virtualSockets = jaxbWidget.getVirtualSockets();
        if(virtualSockets != null)
        {
            rewriteVirtualSocketsFromJAXB(widget, virtualSockets);
        }
        map.put(jaxbWidget, widget);
        for(final com.hybris.cockpitng.core.persistence.impl.jaxb.Widget child : jaxbWidget.getWidget())
        {
            createWidgetTreeFromJAXB(child, widget, map, ws);
        }
        return widget;
    }


    /**
     * @param access
     *           comma-separated list of access restrictions
     * @return list of split and trimmed access restrictions
     */
    public List<String> extractAccessRestrictions(final String access)
    {
        final List<String> accessRestrictions = new ArrayList<>();
        final String[] split = StringUtils.split(access, ',');
        for(final String string : split)
        {
            accessRestrictions.add(string.trim());
        }
        return accessRestrictions;
    }


    public SocketVisibility socketVisibilityFromModel(final WidgetSocket.SocketVisibility visibility)
    {
        if(visibility == null)
        {
            return SocketVisibility.DEFAULT;
        }
        switch(visibility)
        {
            case EXTERNAL:
                return SocketVisibility.EXTERNAL;
            case INTERNAL:
                return SocketVisibility.INTERNAL;
            case HIDDEN:
                return SocketVisibility.INVISIBLE;
            default:
                return SocketVisibility.EXTERNAL;
        }
    }


    public SocketMultiplicity socketMultiplicityFromModel(final WidgetSocket.Multiplicity multiplicity)
    {
        if(multiplicity == null)
        {
            return null;
        }
        switch(multiplicity)
        {
            case COLLECTION:
                return SocketMultiplicity.COLLECTION;
            case LIST:
                return SocketMultiplicity.LIST;
            case SET:
                return SocketMultiplicity.SET;
            default:
                throw new IllegalStateException("Unknown multiplicity: " + multiplicity);
        }
    }


    public WidgetInstanceSettings.SocketEventRoutingMode socketEventRoutingModeFromJAXB(final SocketEventRoutingMode wMode)
    {
        if(wMode == null)
        {
            return WidgetInstanceSettings.SocketEventRoutingMode.LAST_USED;
        }
        switch(wMode)
        {
            case SELECTED:
                return WidgetInstanceSettings.SocketEventRoutingMode.SELECTED;
            case FIRST:
                return WidgetInstanceSettings.SocketEventRoutingMode.FIRST;
            case LAST:
                return WidgetInstanceSettings.SocketEventRoutingMode.LAST;
            case LAST_USED:
                return WidgetInstanceSettings.SocketEventRoutingMode.LAST_USED;
            default:
                return WidgetInstanceSettings.SocketEventRoutingMode.LAST_USED;
        }
    }


    public WidgetSocket.Multiplicity socketMultiplicityFromJAXB(final SocketMultiplicity multiplicity)
    {
        if(multiplicity == null)
        {
            return null;
        }
        switch(multiplicity)
        {
            case COLLECTION:
                return WidgetSocket.Multiplicity.COLLECTION;
            case LIST:
                return WidgetSocket.Multiplicity.LIST;
            case SET:
                return WidgetSocket.Multiplicity.SET;
            default:
                return WidgetSocket.Multiplicity.COLLECTION;
        }
    }


    public SocketEventRoutingMode socketEventRoutingModeFromModel(final WidgetInstanceSettings.SocketEventRoutingMode mode)
    {
        if(mode == null)
        {
            return SocketEventRoutingMode.LAST_USED;
        }
        switch(mode)
        {
            case SELECTED:
                return SocketEventRoutingMode.SELECTED;
            case FIRST:
                return SocketEventRoutingMode.FIRST;
            case LAST:
                return SocketEventRoutingMode.LAST;
            case LAST_USED:
                return SocketEventRoutingMode.LAST_USED;
            default:
                return SocketEventRoutingMode.LAST_USED;
        }
    }


    public SettingType settingTypeByObject(final Object object)
    {
        if(object instanceof Boolean)
        {
            return SettingType.BOOLEAN;
        }
        else if(object instanceof Integer)
        {
            return SettingType.INTEGER;
        }
        else if(object instanceof Double)
        {
            return SettingType.DOUBLE;
        }
        else
        {
            return SettingType.STRING;
        }
    }


    public WidgetConnection createConnection(final String sourceId, final String targetId, final String inputId,
                    final String outputId)
    {
        final WidgetConnection conn = new WidgetConnection();
        conn.setSourceWidgetId(sourceId);
        conn.setTargetWidgetId(targetId);
        conn.setInputId(inputId);
        conn.setOutputId(outputId);
        return conn;
    }


    public SocketEvents createSocketEvents(final Set<String> events)
    {
        if(events.isEmpty())
        {
            return null;
        }
        else
        {
            final SocketEvents socketEvents = new SocketEvents();
            final List<SocketEvent> socketEventList = socketEvents.getSocketEvent();
            for(final String event : events)
            {
                final SocketEvent socketEvent = new SocketEvent();
                socketEvent.setId(event);
                socketEventList.add(socketEvent);
            }
            return socketEvents;
        }
    }


    public WidgetSocket createVirtualSocketFromJAXB(final Widget widget, final Socket socket, final int mask)
    {
        final WidgetSocket widgetSocket = new WidgetSocket(widget, mask);
        widgetSocket.setId(socket.getId());
        widgetSocket.setDataType(socket.getType());
        widgetSocket.setDataMultiplicity(socketMultiplicityFromJAXB(socket.getMultiplicity()));
        widgetSocket.setVisibility(WidgetSocket.SocketVisibility.fromJAXB(socket.getVisibility()));
        return widgetSocket;
    }


    public void rewriteVirtualSocketsFromJAXB(final Widget widget, final VirtualSockets virtualSockets)
    {
        for(final Socket socket : virtualSockets.getInput())
        {
            widget.addVirtualInput(createVirtualSocketFromJAXB(widget, socket, CNG.SOCKET_IN | CNG.SOCKET_VIRTUAL));
        }
        for(final Socket socket : virtualSockets.getOutput())
        {
            widget.addVirtualOutput(createVirtualSocketFromJAXB(widget, socket, CNG.SOCKET_OUT | CNG.SOCKET_VIRTUAL));
        }
    }


    public com.hybris.cockpitng.core.WidgetConnection createWidgetConnection(final WidgetConnection conn, final Widget sourceNode,
                    final Widget targetNode)
    {
        final com.hybris.cockpitng.core.WidgetConnection widgetConn = new com.hybris.cockpitng.core.WidgetConnection(sourceNode,
                        targetNode, conn.getInputId(), conn.getOutputId());
        widgetConn.setName(conn.getName());
        return widgetConn;
    }


    public void rewriteWidgetSettingsFromJAXB(final com.hybris.cockpitng.core.persistence.impl.jaxb.Widget jaxbWidget,
                    final Widget widget)
    {
        for(final WidgetSetting setting : jaxbWidget.getSetting())
        {
            Object value = setting.getValue();
            try
            {
                if(SettingType.BOOLEAN.equals(setting.getType()))
                {
                    value = Boolean.valueOf(setting.getValue());
                }
                else if(SettingType.DOUBLE.equals(setting.getType()))
                {
                    value = Double.valueOf(setting.getValue());
                }
                else if(SettingType.INTEGER.equals(setting.getType()))
                {
                    value = Integer.valueOf(setting.getValue());
                }
                widget.getWidgetSettings().put(setting.getKey(), value);
            }
            catch(final NumberFormatException e)
            {
                LOG.error(
                                "Could not read widget setting '" + setting.getKey() + "' of type '" + setting.getType() + "', " + "reason: ",
                                e);
            }
        }
    }


    public WidgetInstanceSettings widgetInstanceSettingsFromJAXB(final InstanceSettings wConfig)
    {
        final WidgetInstanceSettings config = new WidgetInstanceSettings();
        // event routing
        if(wConfig.getSocketEventRoutingMode() != null)
        {
            config.setSocketEventRoutingMode(socketEventRoutingModeFromJAXB(wConfig.getSocketEventRoutingMode()));
        }
        // create
        applyCreateConfig(wConfig, config);
        // close
        applyCloseConfig(wConfig, config);
        // select
        applySelectConfig(wConfig, config);
        return config;
    }


    private static void applyCreateConfig(final InstanceSettings wConfig, final WidgetInstanceSettings config)
    {
        final CreateSettings create = wConfig.getCreate();
        if(wConfig.getCreate() != null)
        {
            config.setCreateOnInit(create.isOnInit());
            config.setReuseExisting(create.isReuseExisting());
            if(create.getAllIncomingEvents() == null)
            {
                config.setCreateOnAllIncomingEvents(false);
                config.getCreateOnIncomingEvents().addAll(extractEventNames(create.getIncomingEvents()));
            }
            else
            {
                config.setCreateOnAllIncomingEvents(true);
            }
        }
    }


    private static void applyCloseConfig(final InstanceSettings wConfig, final WidgetInstanceSettings config)
    {
        final CloseSettings close = wConfig.getClose();
        if(wConfig.getClose() != null)
        {
            // incoming
            if(close.getAllIncomingEvents() == null)
            {
                config.setCloseOnAllIncomingEvents(false);
                config.getCloseOnIncomingEvents().addAll(extractEventNames(close.getIncomingEvents()));
            }
            else
            {
                config.setCloseOnAllIncomingEvents(true);
            }
            // outgoing
            if(close.getAllOutgoingEvents() == null)
            {
                config.setCloseOnAllOutgoingEvents(false);
                config.getCloseOnOutgoingEvents().addAll(extractEventNames(close.getOutgoingEvents()));
            }
            else
            {
                config.setCloseOnAllOutgoingEvents(true);
            }
        }
    }


    private static void applySelectConfig(final InstanceSettings wConfig, final WidgetInstanceSettings config)
    {
        final SelectSettings select = wConfig.getSelect();
        if(wConfig.getCreate() != null)
        {
            config.setSelectOnInit(select.isOnInit());
            if(select.getAllIncomingEvents() == null)
            {
                config.setSelectOnAllIncomingEvents(false);
                config.getSelectOnIncomingEvents().addAll(extractEventNames(select.getIncomingEvents()));
            }
            else
            {
                config.setSelectOnAllIncomingEvents(true);
            }
        }
    }


    private static List<String> extractEventNames(final SocketEvents socketEvents)
    {
        if(socketEvents != null)
        {
            final List<SocketEvent> socketEventList = socketEvents.getSocketEvent();
            if(!socketEventList.isEmpty())
            {
                final List<String> events = new ArrayList<>();
                for(final SocketEvent socketEvent : socketEventList)
                {
                    events.add(socketEvent.getId());
                }
                return events;
            }
        }
        return Collections.emptyList();
    }


    public void removeWidgetStubsFromJAXB(final Widgets widgets, final CockpitComponentDefinitionService definitionService)
    {
        final List<com.hybris.cockpitng.core.persistence.impl.jaxb.Widget> toRemove = Lists.newArrayList();
        final com.hybris.cockpitng.core.persistence.impl.jaxb.Widget widgetRoot = CollectionUtils.isNotEmpty(widgets.getWidget())
                        ? widgets.getWidget().iterator().next()
                        : null;
        if(widgetRoot != null)
        {
            for(final com.hybris.cockpitng.core.persistence.impl.jaxb.Widget widgetOrStub : widgetRoot.getWidget())
            {
                if(widgetOrStub != null && isStubWidget(widgetOrStub.getId(), definitionService))
                {
                    toRemove.add(widgetOrStub);
                }
            }
            for(final com.hybris.cockpitng.core.persistence.impl.jaxb.Widget stub : toRemove)
            {
                widgetRoot.getWidget().remove(stub);
            }
        }
    }


    public boolean isStubWidget(final String widgetId, final CockpitComponentDefinitionService definitionService)
    {
        return definitionService.getStubWidgetDefinitions().containsKey(widgetId);
    }


    public void storeWidgetConnectionsFromModel(final Widgets widgets, final Widget node)
    {
        if(node != null)
        {
            final com.hybris.cockpitng.core.persistence.impl.jaxb.Widget widget = lookupHandler.findWidgetById(widgets,
                            node.getId());
            if(widget != null)
            {
                storeWidgetConnectionsFromModel(widgets, node, widget, true);
                storeWidgetConnectionsFromModel(widgets, node, widget, false);
            }
            for(final Widget childNode : node.getChildren())
            {
                storeWidgetConnectionsFromModel(widgets, childNode);
            }
        }
    }


    private void storeWidgetConnectionsFromModel(final Widgets widgets, final Widget node,
                    final com.hybris.cockpitng.core.persistence.impl.jaxb.Widget widget, final boolean isInConnection)
    {
        final List<com.hybris.cockpitng.core.WidgetConnection> connections = (isInConnection ? node.getInConnections()
                        : node.getOutConnections());
        final Set<com.hybris.cockpitng.core.persistence.impl.jaxb.WidgetConnection> widgetConnections = new HashSet<>();
        for(final com.hybris.cockpitng.core.WidgetConnection conn : connections)
        {
            final Widget otherNode = (isInConnection ? conn.getSource() : conn.getTarget());
            if(widget != null && otherNode != null)
            {
                final String otherWidgetId = otherNode.getId();
                final String widgetId = widget.getId();
                com.hybris.cockpitng.core.persistence.impl.jaxb.WidgetConnection widgetConn = (isInConnection
                                ? lookupHandler.findWidgetConnection(widgets, otherWidgetId, widgetId, conn.getInputId(), conn.getOutputId())
                                : lookupHandler.findWidgetConnection(widgets, widgetId, otherWidgetId, conn.getInputId(), conn.getOutputId()));
                if(widgetConn == null)
                {
                    widgetConn = (isInConnection ? createConnection(otherWidgetId, widgetId, conn.getInputId(), conn.getOutputId())
                                    : createConnection(widgetId, otherWidgetId, conn.getInputId(), conn.getOutputId()));
                    widgets.getWidgetConnection().add(widgetConn);
                }
                widgetConnections.add(widgetConn);
            }
        }
        final List<com.hybris.cockpitng.core.persistence.impl.jaxb.WidgetConnection> all = lookupHandler
                        .findWidgetConnections(widgets, widget, isInConnection);
        for(final com.hybris.cockpitng.core.persistence.impl.jaxb.WidgetConnection conn : all)
        {
            if(!widgetConnections.contains(conn))
            {
                widgets.getWidgetConnection().remove(conn);
            }
        }
    }


    public void removeOrphanedConnectionsFromJAXB(final Widgets widgets, final CockpitComponentDefinitionService definitionService)
    {
        final Set<com.hybris.cockpitng.core.persistence.impl.jaxb.WidgetConnection> toRemove = new HashSet<>();
        for(final com.hybris.cockpitng.core.persistence.impl.jaxb.WidgetConnection conn : widgets.getWidgetConnection())
        {
            final com.hybris.cockpitng.core.persistence.impl.jaxb.Widget sourceWidget = lookupHandler.findWidgetById(widgets,
                            conn.getSourceWidgetId());
            if(sourceWidget == null && !isStubWidget(conn.getSourceWidgetId(), definitionService))
            {
                toRemove.add(conn);
                continue;
            }
            final com.hybris.cockpitng.core.persistence.impl.jaxb.Widget targetWidget = lookupHandler.findWidgetById(widgets,
                            conn.getTargetWidgetId());
            if(targetWidget == null && !isStubWidget(conn.getTargetWidgetId(), definitionService))
            {
                toRemove.add(conn);
            }
        }
        for(final com.hybris.cockpitng.core.persistence.impl.jaxb.WidgetConnection conn : toRemove)
        {
            widgets.getWidgetConnection().remove(conn);
        }
    }


    public void removeImportsFromJAXB(final Widgets widgets)
    {
        if(widgets.getImports() != null)
        {
            widgets.getImports().clear();
        }
    }
}
