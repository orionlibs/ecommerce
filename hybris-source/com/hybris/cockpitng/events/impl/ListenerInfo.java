/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.events.impl;

public class ListenerInfo
{
    private final String listenerUid;
    private final ScopeContext scopeContext;
    private final String scope;


    public ListenerInfo(final String listenerUid, final String scope, final ScopeContext scopeContext)
    {
        super();
        this.listenerUid = listenerUid;
        this.scopeContext = scopeContext;
        this.scope = scope;
    }


    public String getListenerUid()
    {
        return listenerUid;
    }


    public String getScope()
    {
        return scope;
    }


    public ScopeContext getScopeContext()
    {
        return scopeContext;
    }
}
