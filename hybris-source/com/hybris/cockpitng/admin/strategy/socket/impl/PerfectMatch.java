/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.admin.strategy.socket.impl;

import com.hybris.cockpitng.core.WidgetSocket;
import org.apache.commons.lang3.StringUtils;

public class PerfectMatch extends TypeMatch
{
    @Override
    public String getStrategyCode()
    {
        return "perfect match";
    }


    @Override
    public boolean matches(final WidgetSocket socketA, final WidgetSocket socketB)
    {
        return super.matches(socketA, socketB) && StringUtils.equalsIgnoreCase(socketA.getId(), socketB.getId());
    }
}
