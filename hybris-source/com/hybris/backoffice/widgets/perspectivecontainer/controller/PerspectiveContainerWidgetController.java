/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.perspectivecontainer.controller;

import com.hybris.backoffice.actionbar.ActionDefinition;
import com.hybris.backoffice.actionbar.DefaultActionDefinition;
import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.backoffice.navigation.impl.SimpleNode;
import com.hybris.backoffice.widgets.viewswitcher.ViewSwitcherWidgetController;
import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.components.Widgetchildren;
import com.hybris.cockpitng.core.Widget;
import com.hybris.cockpitng.core.events.CockpitEventQueue;
import com.hybris.cockpitng.core.events.impl.DefaultCockpitEvent;
import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.util.UITools;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;

public class PerspectiveContainerWidgetController extends ViewSwitcherWidgetController
{
    public static final String PERSPECTIVES_SLOT = "perspectives";
    public static final String SELECT_PERSPECTIVE_SOCKET = "selectPerspective";
    public static final String SELECT_PERSPECTIVE_BY_ID_SOCKET = "selectPerspectiveById";
    public static final String SELECTED_PERSPECTIVE_SOCKET = "perspectiveSelected";
    /**
     * @deprecated since 6.6 no longer used
     */
    @Deprecated(since = "6.6", forRemoval = true)
    public static final String POSSIBLE_PERSPECTIVES_OUT_SOCKET = "possiblePerspectives";
    public static final String PERSPECTIVES_OUT_SOCKET = "perspectives";
    private static final String PERSPECTIVE_DOMAIN_KEY = "perspectiveDomain";
    protected static final String SETTING_CONFIG_CONTEXT = "perspectiveContainerConfigCtx";
    protected static final String DEFAULT_CONFIG_CONTEXT = "perspective-container";
    @Wire
    private Widgetchildren perspectiveContainer;
    @WireVariable
    private transient CockpitEventQueue cockpitEventQueue;


    @Override
    protected void initialize()
    {
        super.initialize();
        final List<Widget> possibleWidgets = getPossibleWidgets();
        UITools.postponeExecution(getValue(MODEL_PARENT, Component.class), () -> this.sendOutput(PERSPECTIVES_OUT_SOCKET,
                        possibleWidgets.stream().map(this::mapToAction).collect(Collectors.toCollection(LinkedHashSet::new))));
    }


    @SocketEvent(socketId = SELECT_PERSPECTIVE_SOCKET)
    public void selectPerspective(final NavigationNode navigationNode)
    {
        selectViews(SELECT_PERSPECTIVE_SOCKET, navigationNode.getId());
    }


    @SocketEvent(socketId = SELECT_PERSPECTIVE_BY_ID_SOCKET)
    public void selectPerspectiveById(final String id)
    {
        selectViews(SELECT_PERSPECTIVE_BY_ID_SOCKET, id);
    }


    @Override
    protected String getViewsSlotId()
    {
        return PERSPECTIVES_SLOT;
    }


    @Override
    protected void notifyViewsSelected(final Collection<String> viewsId)
    {
        if(CollectionUtils.isNotEmpty(viewsId))
        {
            final String viewId = viewsId.stream().reduce((a, b) -> b).orElse(null);
            notifyViewsSelected(viewId);
        }
    }


    @Override
    protected void notifyViewsRequested(final Collection<String> viewsId)
    {
        // do nothing - this widget doesn't have the requestedViews socket
    }


    @Override
    protected void notifyViewsSwitched(final Collection<String> requested, final Collection<String> selected,
                    final Collection<String> deselected)
    {
        getCockpitEventQueue()
                        .publishEvent(new DefaultCockpitEvent("viewSwitched", Collections.unmodifiableCollection(selected), null));
    }


    protected void notifyViewsSelected(final String viewId)
    {
        getPossibleWidgets().stream().filter(widget -> StringUtils.equals(viewId, widget.getId())).findAny()
                        .ifPresent(node -> sendOutput(SELECTED_PERSPECTIVE_SOCKET, createNavigationNode(node)));
    }


    protected ActionDefinition mapToAction(final Widget widget)
    {
        Validate.notNull("Widget may not be null", widget);
        final DefaultActionDefinition node = new DefaultActionDefinition(widget.getId());
        String name = widget.getTitle();
        if(StringUtils.isBlank(name))
        {
            name = widget.getId();
        }
        node.setName(name);
        node.setNameLocKey(widget.getTitle());
        final String domain = widget.getWidgetSettings().getString(PERSPECTIVE_DOMAIN_KEY);
        node.setGroup(domain);
        return node;
    }


    @Override
    protected String getConfigurationContextCode()
    {
        return StringUtils.defaultIfBlank(getWidgetSettings().getString(SETTING_CONFIG_CONTEXT), DEFAULT_CONFIG_CONTEXT);
    }


    private static NavigationNode createNavigationNode(final Widget widget)
    {
        Validate.notNull("Widget may not be null", widget);
        final SimpleNode node = new SimpleNode(widget.getId());
        String name = widget.getTitle();
        if(StringUtils.isBlank(name))
        {
            name = widget.getId();
        }
        node.setName(name);
        node.setNameLocKey(widget.getTitle());
        return node;
    }


    public Widgetchildren getPerspectiveContainer()
    {
        return perspectiveContainer;
    }


    @Override
    public Widgetchildren getViewsContainer()
    {
        return getPerspectiveContainer();
    }


    public CockpitEventQueue getCockpitEventQueue()
    {
        return cockpitEventQueue;
    }
}
