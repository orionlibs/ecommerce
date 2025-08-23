/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.renderers.header;

/**
 * <p>
 * This interface should be implemented by Controllers of widgets that want to handle its state. Anytime
 * when widget is switch from visible to hidden, or other way around, the method {@link #handleVisibilityState(WidgetVisibilityState)} should be invoked.
 * </p>
 */
public interface WidgetVisibilityStateAware
{
    /**
     * Handles state visibility widget state.
     *
     * @param state name of widget state. f.e.: WidgetVisibilityState.VISIBLE
     */
    void handleVisibilityState(WidgetVisibilityState state);
}
