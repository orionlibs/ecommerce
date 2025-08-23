/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.controller;

import com.hybris.cockpitng.util.DefaultWidgetController;
import org.zkoss.zk.ui.Component;

public class SplitterLayoutController extends DefaultWidgetController
{
    private static final long serialVersionUID = 1L;


    @Override
    public void initialize(final Component comp)
    {
        initWidgetSetting("orient", "v_split");
    }
}
