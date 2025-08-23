/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editors;

import org.zkoss.zk.ui.Component;

/**
 * Renderer of a cockpit editor. Specified in the cockpit editor definition.
 */
public interface CockpitEditorRenderer<T>
{
    String INITIAL_EDIT_STRING = "initialEditString";


    /**
     * Creates a new editor component and attaches it to the specified {@link Component} <code>component</code>.
     *
     * @param parent parent component the editor should be attached to
     * @param context the context for the editor creation
     * @param listener the listener to notify the rest of the world about value changes and other events
     */
    void render(Component parent, EditorContext<T> context, EditorListener<T> listener);
}
