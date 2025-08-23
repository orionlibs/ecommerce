/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.events.impl;

public class ScopeContext
{
    private final String desktopID;
    private final String sessionID;
    private final String userID;


    public ScopeContext(final String desktopID, final String sessionID, final String userID)
    {
        super();
        this.desktopID = desktopID;
        this.sessionID = sessionID;
        this.userID = userID;
    }


    public String getDesktopID()
    {
        return desktopID;
    }


    public String getSessionID()
    {
        return sessionID;
    }


    public String getUserID()
    {
        return userID;
    }
}
