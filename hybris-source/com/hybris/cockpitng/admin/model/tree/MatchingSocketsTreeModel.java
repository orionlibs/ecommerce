/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.admin.model.tree;

import com.hybris.cockpitng.admin.strategy.socket.WidgetSocketMatchStrategy;
import com.hybris.cockpitng.core.CockpitComponentDefinitionService;
import com.hybris.cockpitng.core.Widget;
import com.hybris.cockpitng.core.WidgetConnection;
import com.hybris.cockpitng.core.WidgetDefinition;
import com.hybris.cockpitng.core.WidgetService;
import com.hybris.cockpitng.core.WidgetSocket;
import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.core.util.impl.WidgetSocketUtils;
import com.hybris.cockpitng.core.util.impl.WidgetTreeUtils;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zul.AbstractTreeModel;

public class MatchingSocketsTreeModel extends AbstractTreeModel<MatchingSocketsTreeModelInternalNode>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(MatchingSocketsTreeModel.class);
    private static final long serialVersionUID = -6777198356078871788L;
    private final Widget selectedWidget;
    private final WidgetSocket sourceSocket;
    private final transient List<WidgetSocketMatchStrategy> socketMatchStrategies;
    private final transient CockpitComponentDefinitionService widgetDefinitionService;
    private final transient WidgetService widgetService;
    private boolean initialized;


    public MatchingSocketsTreeModel(final Widget selectedWidget, final WidgetSocket sourceSocket,
                    final List<WidgetSocketMatchStrategy> socketMatchStrategies,
                    final CockpitComponentDefinitionService widgetDefinitionService, final WidgetService widgetService)
    {
        super(new MatchingSocketsTreeModelInternalNode(null, sourceSocket, null));
        Validate.notNull("Selected widget must not be null", selectedWidget);
        Validate.notNull("Source socket must not be null", sourceSocket);
        Validate.notNull("Socket match strategies must not be null", socketMatchStrategies);
        Validate.notNull("Widget definition service must not be null", widgetDefinitionService);
        Validate.notNull("Widget service must not be null", widgetService);
        this.selectedWidget = selectedWidget;
        this.sourceSocket = sourceSocket;
        this.socketMatchStrategies = socketMatchStrategies;
        this.widgetDefinitionService = widgetDefinitionService;
        this.widgetService = widgetService;
    }


    private void initializeModel()
    {
        final Set<Widget> allWidgets = WidgetTreeUtils.getAllChildWidgetsRecursively(WidgetTreeUtils.getRootWidget(selectedWidget));
        allWidgets.remove(selectedWidget);
        for(final Widget widget : allWidgets)
        {
            final WidgetDefinition selectedWidgetDefinition = getWidgetDefinition(selectedWidget);
            final WidgetDefinition widgetDefinition = getWidgetDefinition(widget);
            if(widgetDefinition != null)
            {
                if(selectedWidgetDefinition.isStubWidget() && widgetDefinition.isStubWidget())
                {
                    continue;
                }
                final List<WidgetSocket> sockets = sourceSocket.isInput() ? WidgetSocketUtils.getAllOutputs(widget, widgetDefinition)
                                : WidgetSocketUtils.getAllInputs(widget, widgetDefinition);
                for(final WidgetSocket socket : sockets)
                {
                    for(final WidgetSocketMatchStrategy strategy : socketMatchStrategies)
                    {
                        if(strategy.matches(sourceSocket, socket) && !connectionExists(sourceSocket, socket, widgetService))
                        {
                            MatchingSocketsTreeModelInternalNode node = getRoot().getOrInsertElement(strategy);
                            node = node.getOrInsertElement(widget);
                            node.getChildren().add(new MatchingSocketsTreeModelInternalNode(node, socket,
                                            Collections.<MatchingSocketsTreeModelInternalNode>emptyList()));
                        }
                    }
                }
            }
            else
            {
                LOGGER.error("Unknown definition for widget: {} ({})", widget.getId(), widget.getWidgetDefinitionId());
            }
        }
        initialized = true;
    }


    private static boolean connectionExists(final WidgetSocket selectedSocket, final WidgetSocket matchingSocket,
                    final WidgetService widgetService)
    {
        final Widget matchingWidget = matchingSocket.getDeclaringWidget();
        final List<WidgetConnection> connections = selectedSocket.isInput()
                        ? widgetService.getWidgetConnectionsForOutputWidgetAndSocketID(matchingWidget, matchingSocket.getId())
                        : widgetService.getWidgetConnectionsForOutputWidgetAndSocketID(selectedSocket.getDeclaringWidget(),
                                        selectedSocket.getId());
        for(final WidgetConnection con : connections)
        {
            if(selectedSocket.isInput())
            {
                if(con.getTarget().equals(selectedSocket.getDeclaringWidget()) && con.getInputId().equals(selectedSocket.getId()))
                {
                    return true;
                }
            }
            else
            {
                if(con.getTarget().equals(matchingWidget) && con.getInputId().equals(matchingSocket.getId()))
                {
                    return true;
                }
            }
        }
        return false;
    }


    protected WidgetDefinition getWidgetDefinition(final Widget widget)
    {
        Validate.notNull("Cannot load widget definition for null widget", widget);
        return widgetDefinitionService.getComponentDefinitionForCode(widget.getWidgetDefinitionId(), WidgetDefinition.class);
    }


    @Override
    public boolean isLeaf(final MatchingSocketsTreeModelInternalNode node)
    {
        Validate.notNull("Node may not be null", node);
        return node.getChildren().isEmpty();
    }


    @Override
    public MatchingSocketsTreeModelInternalNode getChild(final MatchingSocketsTreeModelInternalNode parent, final int index)
    {
        Validate.notNull("Parent may not be null", parent);
        return parent.getChildren().get(index);
    }


    @Override
    public int getChildCount(final MatchingSocketsTreeModelInternalNode parent)
    {
        Validate.notNull("Parent may not be null", parent);
        if(!initialized)
        {
            initializeModel();
        }
        return parent.getChildren() == null ? 0 : parent.getChildren().size();
    }
}
