/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.engine.impl;

import com.hybris.cockpitng.admin.CockpitAdminService;
import com.hybris.cockpitng.core.Widget;
import com.hybris.cockpitng.core.ui.WidgetInstance;
import com.hybris.cockpitng.core.ui.impl.DefaultWidgetInstance;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;

/**
 * Widget instance facade for the admin mode.
 */
public class AdminWidgetInstanceFacade extends DefaultWidgetInstanceFacade
{
    private CockpitAdminService cockpitAdminService;


    @Override
    public List<WidgetInstance> getWidgetInstances(final Widget widget, final WidgetInstance parentWidgetInstance)
    {
        if(getCockpitAdminService().isAdminMode())
        {
            List<WidgetInstance> ret = new ArrayList<>();
            if(widget.isTemplate())
            {
                // temporary admin mode template widget instance
                final WidgetInstance widgetInstance = new DefaultWidgetInstance(widget, new HashMap<String, Object>(), 0);
                ret.add(widgetInstance);
            }
            else
            {
                ret = super.getWidgetInstances(widget, parentWidgetInstance);
            }
            return ret;
        }
        else
        {
            return super.getWidgetInstances(widget, parentWidgetInstance);
        }
    }


    public CockpitAdminService getCockpitAdminService()
    {
        return cockpitAdminService;
    }


    @Required
    public void setCockpitAdminService(final CockpitAdminService cockpitAdminService)
    {
        this.cockpitAdminService = cockpitAdminService;
    }
}
