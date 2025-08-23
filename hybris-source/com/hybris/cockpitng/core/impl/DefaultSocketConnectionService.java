/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.impl;

import com.hybris.cockpitng.core.SocketConnectionService;
import com.hybris.cockpitng.core.Widget;
import com.hybris.cockpitng.core.WidgetSocket;
import com.hybris.cockpitng.core.WidgetSocket.SocketVisibility;
import com.hybris.cockpitng.core.util.CockpitTypeUtils;
import com.hybris.cockpitng.core.util.Validate;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

/**
 * Default implementation of {@link SocketConnectionService}. Provides methods dealing with widget sockets and
 * connections.
 */
public class DefaultSocketConnectionService implements SocketConnectionService
{
    public static final String JAVA_LANG_OBJECT = Object.class.getName();
    public static final String EXTENDS = " extends ";
    private static final Logger LOG = LoggerFactory.getLogger(DefaultSocketConnectionService.class);
    private CockpitTypeUtils cockpitTypeUtils;


    @Override
    public boolean canReceiveFrom(final WidgetSocket inputSocket, final Widget targetWidget, final WidgetSocket outputSocket,
                    final Widget srcWidget)
    {
        if(!checkVisibilityRestrictions(inputSocket, targetWidget, outputSocket, srcWidget))
        {
            return false;
        }
        String inputSocketType = inputSocket.getDataType();
        final String resolvedInputGenericType = resolveGenericTypeInternal(inputSocket, targetWidget, true);
        if(resolvedInputGenericType != null)
        {
            inputSocketType = resolvedInputGenericType;
        }
        if(inputSocketType == null)
        {
            return false;
        }
        if(canAcceptAnyType(inputSocket))
        {
            return true;
        }
        final String resolvedOutputGenericType = resolveGenericType(outputSocket, srcWidget);
        final String outputSocketType;
        if(hasGenericType(outputSocket) && resolvedOutputGenericType == null)
        {
            outputSocketType = JAVA_LANG_OBJECT;
        }
        else if(resolvedOutputGenericType.charAt(0) == '#') // here we have an unset typevariable
        {
            return true;
        }
        else
        {
            outputSocketType = resolvedOutputGenericType;
        }
        final boolean typeMatch = inputSocketType.equals(outputSocketType)
                        || cockpitTypeUtils.isAssignableFrom(inputSocketType, outputSocketType);
        return typeMatch && isCollectionTypeAssignable(inputSocket.getDataMultiplicity(), outputSocket.getDataMultiplicity());
    }


    protected boolean canAcceptAnyType(final WidgetSocket inputSocket)
    {
        return JAVA_LANG_OBJECT.equalsIgnoreCase(inputSocket.getDataType()) && inputSocket.getDataMultiplicity() == null;
    }


    @Override
    public boolean checkVisibilityRestrictions(final WidgetSocket inputSocket, final Widget targetWidget,
                    final WidgetSocket outputSocket, final Widget srcWidget)
    {
        final SocketVisibility inputVisibility = inputSocket == null ? null : inputSocket.getVisibility();
        final SocketVisibility outputVisibility = outputSocket.getVisibility();
        if(inputVisibility == null && outputVisibility == null)
        {
            return true;
        }
        if(SocketVisibility.HIDDEN.equals(inputVisibility))
        {
            return false;
        }
        if((SocketVisibility.EXTERNAL.equals(outputVisibility) && isChildOf(srcWidget, targetWidget))
                        || (SocketVisibility.INTERNAL.equals(outputVisibility) && !isChildOf(srcWidget, targetWidget)))
        {
            return false;
        }
        return (!SocketVisibility.EXTERNAL.equals(inputVisibility) || !isChildOf(targetWidget, srcWidget))
                        && (!SocketVisibility.INTERNAL.equals(inputVisibility) || isChildOf(targetWidget, srcWidget));
    }


    private boolean isChildOf(final Widget parent, final Widget potentialChild)
    {
        Widget current = potentialChild;
        do
        {
            if(parent.equals(current))
            {
                return true;
            }
            current = current.getParent();
        }
        while(current != null);
        return false;
    }


    @Override
    public boolean canResolveGenericType(final WidgetSocket socket, final Widget widget)
    {
        return !hasGenericType(socket) || !resolveGenericType(socket, widget).startsWith("#");
    }


    @Override
    public String resolveGenericType(final WidgetSocket socket, final Widget widget)
    {
        return resolveGenericTypeInternal(socket, widget, false);
    }


    String resolveGenericTypeInternal(final WidgetSocket socket, final Widget widget, final boolean generifyToObject)
    {
        if(StringUtils.isEmpty(socket.getDataType()) || widget == null)
        {
            return null;
        }
        final String socketType = socket.getDataType().trim();
        char genericsOpeningBrace = '<';
        char genericsClosingBrace = '>';
        if(socketType.charAt(0) == '[')
        {
            genericsOpeningBrace = '[';
            genericsClosingBrace = ']';
        }
        else if(socketType.charAt(0) != genericsOpeningBrace)
        {
            if(isSpecializedGenericClass(socketType))
            {
                LOG.warn("Generic types will be erased. Used generalised generic class: {}", socketType);
                return eraseGenericClass(socketType);
            }
            return socketType;
        }
        final String typeVariable;
        final String fallbackType;
        if(socketType.contains(EXTENDS))
        {
            final String[] split = socketType.split(EXTENDS);
            if(split.length == 2)
            {
                typeVariable = split[0].replace(genericsOpeningBrace, ' ').replace(genericsClosingBrace, ' ').trim();
                fallbackType = split[1].replace(genericsOpeningBrace, ' ').replace(genericsClosingBrace, ' ').trim();
            }
            else
            {
                LOG.error("Cannot parse type variable '{}', please check syntax.", socketType);
                return null;
            }
        }
        else
        {
            typeVariable = socketType.replace(genericsOpeningBrace, ' ').replace(genericsClosingBrace, ' ').trim();
            fallbackType = null;
        }
        if(StringUtils.containsWhitespace(typeVariable))
        {
            LOG.error("Cannot parse type variable '{}', no whitespace are allowed.", socketType);
            return null;
        }
        if(StringUtils.containsWhitespace(fallbackType))
        {
            LOG.error("Cannot parse type '{}', no whitespace are allowed.", fallbackType);
            return null;
        }
        final String key = getGenericDataTypeHolderName(typeVariable);
        if(StringUtils.isNotBlank(widget.getWidgetSettings().getString(key)))
        {
            return widget.getWidgetSettings().getString(key);
        }
        else if(fallbackType != null)
        {
            return fallbackType;
        }
        else if(socket.isInput() && generifyToObject)
        {
            return Object.class.getName();
        }
        else if(typeVariable != null)
        {
            return "#" + typeVariable;
        }
        return null;
    }


    protected String eraseGenericClass(final String socketType)
    {
        int index = socketType.indexOf('<');
        if(index == -1)
        {
            index = socketType.indexOf('[');
            if(index == -1)
            {
                throw new IllegalArgumentException("Passed type does not contain generic type definition: " + socketType);
            }
        }
        return socketType.substring(0, index);
    }


    protected boolean isSpecializedGenericClass(final String socketType)
    {
        return StringUtils.containsAny(socketType, '<', '[');
    }


    @Override
    public boolean isCollectionTypeAssignable(final WidgetSocket.Multiplicity source, final WidgetSocket.Multiplicity target)
    {
        return Objects.equals(source, target) || WidgetSocket.Multiplicity.COLLECTION.equals(source);
    }


    @Override
    public boolean hasGenericType(final WidgetSocket socket)
    {
        Validate.notNull("Socket must not be null", socket);
        final char charAt0 = socket.getDataType().trim().charAt(0);
        return StringUtils.isNotEmpty(socket.getDataType()) && (charAt0 == '<' || charAt0 == '[');
    }


    protected String getGenericDataTypeHolderName(final String typeVariable)
    {
        return SOCKET_DATA_TYPE_PREFIX + typeVariable;
    }


    @Required
    public void setCockpitTypeUtils(final CockpitTypeUtils cockpitTypeUtils)
    {
        this.cockpitTypeUtils = cockpitTypeUtils;
    }
}
