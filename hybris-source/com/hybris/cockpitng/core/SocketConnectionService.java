/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core;

import com.hybris.cockpitng.core.util.Validate;
import org.apache.commons.lang3.StringUtils;

/**
 * Provides methods dealing with widget sockets and connections.
 */
public interface SocketConnectionService
{
    String SOCKET_DATA_TYPE_PREFIX = "socketDataType_$";


    /**
     * Checks if outputSocket from srcWidget can be connected with inputSocket from targetWidget according to data type.
     *
     * @param inputSocket
     *           - {@link WidgetSocket} represents input socket
     * @param outputSocket
     *           - {@link WidgetSocket} represents output socket
     * @param srcWidget
     *           - {@link Widget} represents source widget
     * @param targetWidget
     *           - {@link Widget} represents target widget
     * @return whether outputSocket from srcWidget can be connected with inputSocket from targetWidget according to data
     *         type
     */
    boolean canReceiveFrom(WidgetSocket inputSocket, Widget targetWidget, WidgetSocket outputSocket, Widget srcWidget);


    /**
     * Checks if outputSocket from srcWidget can be connected with inputSocket from targetWidget according to socket
     * visibility.
     *
     * @param inputSocket
     *           - {@link WidgetSocket} represents input socket
     * @param outputSocket
     *           - {@link WidgetSocket} represents output socket
     * @param srcWidget
     *           - {@link Widget} represents source widget
     * @param targetWidget
     *           - {@link Widget} represents target widget
     * @return whether outputSocket from srcWidget can be connected with inputSocket from targetWidget according to socket
     */
    boolean checkVisibilityRestrictions(WidgetSocket inputSocket, Widget targetWidget, WidgetSocket outputSocket,
                    Widget srcWidget);


    /**
     * Checks if the generic type for the specified socket and widget can be resolved to concrete type.
     *
     * @param socket
     *           - {@link WidgetSocket} represents socket
     * @param widget
     *           - {@link Widget} represents widget
     * @return <code>true</code> if a widget socket does not have generic type or the type can be resolved
     */
    default boolean canResolveGenericType(final WidgetSocket socket, final Widget widget)
    {
        return !hasGenericType(socket) || !resolveGenericType(socket, widget).startsWith("#");
    }


    /**
     * Resolves the generic type for the specified socket and widget if any.
     *
     * @param socket
     *           - {@link WidgetSocket} represents socket
     * @param widget
     *           - {@link Widget} represents widget
     * @return the generic type for the specified socket and widget if any.
     */
    String resolveGenericType(WidgetSocket socket, Widget widget);


    /**
     * Checks if target is compatible with source basing on WidgetSocket.Multiplicity
     *
     * @param source
     *           multiplicity to check
     * @param target
     *           multiplicity to check
     * @return whether target is compatible with source basing on WidgetSocket.Multiplicity
     */
    default boolean isCollectionTypeAssignable(final WidgetSocket.Multiplicity source, final WidgetSocket.Multiplicity target)
    {
        return (source == null && target == null) || WidgetSocket.Multiplicity.COLLECTION.equals(source)
                        || (source != null && source.equals(target));
    }


    /**
     * Checks if socket data type definition has generic type
     *
     * @param socket
     *           - {@link WidgetSocket} represents socket
     * @return whether socket data type definition has generic type
     */
    default boolean hasGenericType(final WidgetSocket socket)
    {
        Validate.notNull("Socket must not be null", socket);
        final char charAt0 = socket.getDataType().trim().charAt(0);
        return StringUtils.isNotEmpty(socket.getDataType()) && (charAt0 == '<' || charAt0 == '[');
    }
}
