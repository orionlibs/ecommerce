/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.persistence.packaging;

import com.hybris.cockpitng.core.Widget;
import com.hybris.cockpitng.core.WidgetSocket;

/**
 *
 */
public class SocketWrapper
{
    private final WidgetSocket socket;
    private final boolean input;
    private String id;
    private final Widget widget;


    public SocketWrapper(final Widget widget, final WidgetSocket socket, final boolean input)
    {
        this.socket = socket;
        this.input = input;
        this.widget = widget;
    }


    public boolean isInput()
    {
        return input;
    }


    public Widget getWidget()
    {
        return widget;
    }


    public WidgetSocket getSocket()
    {
        return this.socket;
    }


    public void setId(final String id)
    {
        this.id = id;
    }


    public String getId()
    {
        return id == null ? socket.getId() + (isInput() ? "Input" : "Output") : id;
    }


    @Override
    public boolean equals(final Object obj)
    {
        if(this == obj)
        {
            return true;
        }
        if(obj == null)
        {
            return false;
        }
        if(obj.getClass() == this.getClass())
        {
            final SocketWrapper other = (SocketWrapper)obj;
            return this == other || (checkEquality(this.socket, other.socket) && checkEquality(this.id, other.id)
                            && checkEquality(this.widget, other.widget) && this.input == other.input);
        }
        return false;
    }


    private boolean checkEquality(final Object object1, final Object object2)
    {
        return object1 == object2 || (object1 != null && object1.equals(object2));
    }


    @Override
    public int hashCode()
    {
        return (socket == null ? 0 : socket.hashCode()) + (id == null ? 0 : id.hashCode())
                        + (widget == null ? 0 : widget.hashCode()) + (input ? 1 : 0);
    }
}
