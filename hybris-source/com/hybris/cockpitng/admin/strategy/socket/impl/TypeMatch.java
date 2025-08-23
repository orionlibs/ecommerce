/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.admin.strategy.socket.impl;

import com.hybris.cockpitng.admin.CockpitAdminService;
import com.hybris.cockpitng.admin.strategy.socket.WidgetSocketMatchStrategy;
import com.hybris.cockpitng.core.WidgetSocket;
import org.springframework.beans.factory.annotation.Required;

public class TypeMatch implements WidgetSocketMatchStrategy
{
    private CockpitAdminService cockpitAdminService;


    @Override
    public String getStrategyCode()
    {
        return "type match";
    }


    @Override
    public boolean matches(final WidgetSocket socketA, final WidgetSocket socketB)
    {
        if(socketA.isInput() ^ socketB.isInput())
        {
            if(socketA.isInput())
            {
                return cockpitAdminService.canReceiveFrom(socketA, socketB);
            }
            else
            {
                return cockpitAdminService.canReceiveFrom(socketB, socketA);
            }
        }
        throw new IllegalStateException("Cannot match two " + (socketA.isInput() ? "input" : "output") + " sockets.");
    }


    @Required
    public void setCockpitAdminService(final CockpitAdminService cockpitAdminService)
    {
        this.cockpitAdminService = cockpitAdminService;
    }
}
