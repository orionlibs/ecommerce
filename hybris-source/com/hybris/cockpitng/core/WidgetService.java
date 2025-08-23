/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core;

import java.util.List;
import java.util.Set;

/**
 * Service for managing widget tree with socket connections.
 */
public interface WidgetService
{
    String COMPOSED_ROOT_SLOT_ID = "composedRootSlot";


    /**
     * Creates widget's instance
     *
     * @param parent of given widget
     * @param id of newly created widget
     * @param slotId in parent widget where newly created widget will be put
     * @param definitionId of widget
     * @return newly created widget
     */
    Widget createWidget(final Widget parent, final String id, final String slotId, final String definitionId);


    /**
     * Creates a widget that is composed of one or more widgets.
     *
     * @param parent of given widget
     * @param id of newly created widget
     * @param slotId in parent widget where newly created widget will be put
     * @param definitionId of widget
     * @param rootNode a template widget from which all the settings and properties are cloned
     * @return composed widget
     */
    Widget createComposedWidget(final Widget parent, final String id, final String slotId, final String definitionId,
                    Widget rootNode);


    /**
     * Removes widget
     *
     * @param widget to remove
     */
    void removeWidget(final Widget widget);


    /**
     * Moves widget to another one
     *
     * @param widget which should be moved
     * @param newParent new parent for the widget
     */
    void moveWidget(Widget widget, Widget newParent);


    /**
     * Gets all connections of widget's socket
     *
     * @param widget a widget's instance
     * @param outputId id of socket
     * @return list of all connections related to given socket id
     */
    List<WidgetConnection> getWidgetConnectionsForOutputWidgetAndSocketID(Widget widget, String outputId);


    /**
     * Creates a connection between widgets
     *
     * @param sourceWidget Widget from which the connection will be made
     * @param targetWidget Widget to which the connection will be made
     * @param inputId socket name of target widget
     * @param outputId socket name of source widget
     * @return connection between two widgets
     */
    WidgetConnection createWidgetConnection(Widget sourceWidget, Widget targetWidget, String inputId, String outputId);


    /**
     * Clones all the settings and properties from a template widget and creates
     * composed root widget which is assigned to composed widget
     *
     * @param composedWidget a composed widget
     * @param templateNode a template widget from which all the settings and properties are cloned
     */
    void loadComposedChildren(Widget composedWidget, Widget templateNode);


    /**
     * Returns set of descendants for given composed widget
     *
     * @param composedWidgetRoot composed widget
     * @return set of descendants for given composed widget
     */
    Set<Widget> getAllDescendants(Widget composedWidgetRoot);
}
