/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.engine;

import com.hybris.cockpitng.components.Widgetslot;
import com.hybris.cockpitng.core.Widget;
import org.zkoss.zk.ui.Component;

/**
 * The purpose of this interface is to render connect button in Application Orchestrator
 */
public interface ConnectButtonRenderer
{
    void renderConnectButton(final Component parent, final Widget currentWidget, final Widgetslot widgetslot);
}
