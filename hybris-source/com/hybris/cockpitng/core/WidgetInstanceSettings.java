/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;
import org.apache.commons.lang3.SerializationUtils;

/**
 * Rule configuration for forwarding of an incoming socket event to view instance(s) of a template widget.
 */
public class WidgetInstanceSettings implements Serializable, Cloneable
{
    private static final long serialVersionUID = 6725125897973911406L;


    public enum SocketEventRoutingMode
    {
        SELECTED, LAST_USED, FIRST, LAST
    }


    private boolean createOnInit;
    private boolean reuseExisting = true;
    private boolean responsive = false;
    private boolean createOnAllIncomingEvents = true;
    private final Set<String> createOnIncomingEvents = new LinkedHashSet<>();
    private boolean closeOnAllIncomingEvents;
    private final Set<String> closeOnIncomingEvents = new LinkedHashSet<>();
    private boolean closeOnAllOutgoingEvents;
    private final Set<String> closeOnOutgoingEvents = new LinkedHashSet<>();
    private boolean selectOnInit;
    private boolean selectOnAllIncomingEvents = true;
    private final Set<String> selectOnIncomingEvents = new LinkedHashSet<>();
    private SocketEventRoutingMode socketEventRoutingMode = SocketEventRoutingMode.LAST_USED;


    public boolean isCreateOnInit()
    {
        return createOnInit;
    }


    public void setCreateOnInit(final boolean createOnInit)
    {
        this.createOnInit = createOnInit;
    }


    public void setReuseExisting(final boolean reuseExisting)
    {
        this.reuseExisting = reuseExisting;
    }


    public boolean isReuseExisting()
    {
        return reuseExisting;
    }


    public boolean isCreateOnAllIncomingEvents()
    {
        return createOnAllIncomingEvents;
    }


    public void setCreateOnAllIncomingEvents(final boolean createOnAllIncomingEvents)
    {
        this.createOnAllIncomingEvents = createOnAllIncomingEvents;
    }


    public boolean isCloseOnAllIncomingEvents()
    {
        return closeOnAllIncomingEvents;
    }


    public void setCloseOnAllIncomingEvents(final boolean closeOnAllIncomingEvents)
    {
        this.closeOnAllIncomingEvents = closeOnAllIncomingEvents;
    }


    public boolean isCloseOnAllOutgoingEvents()
    {
        return closeOnAllOutgoingEvents;
    }


    public void setCloseOnAllOutgoingEvents(final boolean closeOnAllOutgoingEvents)
    {
        this.closeOnAllOutgoingEvents = closeOnAllOutgoingEvents;
    }


    public boolean isSelectOnInit()
    {
        return selectOnInit;
    }


    public void setSelectOnInit(final boolean selectOnInit)
    {
        this.selectOnInit = selectOnInit;
    }


    public boolean isSelectOnAllIncomingEvents()
    {
        return selectOnAllIncomingEvents;
    }


    public void setSelectOnAllIncomingEvents(final boolean selectOnAllIncomingEvents)
    {
        this.selectOnAllIncomingEvents = selectOnAllIncomingEvents;
    }


    public Set<String> getCreateOnIncomingEvents()
    {
        return createOnIncomingEvents;
    }


    public Set<String> getCloseOnIncomingEvents()
    {
        return closeOnIncomingEvents;
    }


    public Set<String> getCloseOnOutgoingEvents()
    {
        return closeOnOutgoingEvents;
    }


    public Set<String> getSelectOnIncomingEvents()
    {
        return selectOnIncomingEvents;
    }


    public SocketEventRoutingMode getSocketEventRoutingMode()
    {
        return socketEventRoutingMode;
    }


    public void setSocketEventRoutingMode(final SocketEventRoutingMode instanceResolveMode)
    {
        this.socketEventRoutingMode = instanceResolveMode;
    }


    /**
     * @deprecated since 6.7 use {@link SerializationUtils#clone()} instead.
     */
    @Deprecated(since = "6.7", forRemoval = true)
    @Override
    public WidgetInstanceSettings clone()
    {
        return SerializationUtils.clone(this);
    }


    public boolean isResponsive()
    {
        return responsive;
    }


    public void setResponsive(final boolean responsive)
    {
        this.responsive = responsive;
    }
}
