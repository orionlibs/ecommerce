/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.components;

import org.zkoss.zk.ui.ext.AfterCompose;

/**
 * Interface for components that can contain cockpit widgets.
 */
public interface WidgetContainer extends AfterCompose
{
    /**
     * Redraws this component, i.e. clears its current children and renders content again.
     */
    void updateView();
}
