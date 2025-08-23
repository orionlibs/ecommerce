/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.actions;

import org.zkoss.zk.ui.Component;

/**
 * Renderer of cockpit actions.
 *
 * @param <I> input type of the action
 * @param <O> output type of the action
 */
public interface CockpitActionRenderer<I, O>
{
    void render(Component parent, CockpitAction<I, O> action, ActionContext<I> context, boolean updateMode,
                    ActionListener<O> listener);
}
