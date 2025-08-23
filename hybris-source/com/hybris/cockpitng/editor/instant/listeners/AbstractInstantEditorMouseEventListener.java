/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.instant.listeners;

import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.editor.instant.InstantEditorSwitcher;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.MouseEvent;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

/**
 * Abstract MouseEvent EventListener for Instant Editor.
 */
public abstract class AbstractInstantEditorMouseEventListener implements EventListener<MouseEvent>
{
    /**
     * Key under which current editor's value is stored in {@link Editor#getParameters()} when displaying underlying editor.
     * If user cancels the edit it is used to rollback editor's value.
     */
    public static final String KEY_OLD_VALUE = "OLD_VALUE";
    private final HtmlBasedComponent labelContainer;
    private final Div editorContainer;
    private final Editor editor;
    private final InstantEditorSwitcher instantEditorSwitcher;


    /**
     * @param labelContainer
     *           label component containing string representation of the data
     * @param editorContainer
     *           container component for the underlying editor
     * @param editor
     *           instant editor's underlying editor
     */
    public AbstractInstantEditorMouseEventListener(final HtmlBasedComponent labelContainer, final Div editorContainer, final Editor editor)
    {
        this.labelContainer = labelContainer;
        this.editorContainer = editorContainer;
        this.editor = editor;
        this.instantEditorSwitcher = new InstantEditorSwitcher();
    }


    protected void switchToLabel()
    {
        instantEditorSwitcher.switchToLabel(getLabelContainer(), getEditorContainer());
    }


    protected void switchToEditor()
    {
        instantEditorSwitcher.switchToEditor(getLabelContainer(), getEditorContainer());
    }


    /**
     * @deprecated since 6.7
     * @see #getLabelContainer()
     */
    @Deprecated(since = "6.7", forRemoval = true)
    protected Label getLabel()
    {
        return (Label)labelContainer;
    }


    protected HtmlBasedComponent getLabelContainer()
    {
        return labelContainer;
    }


    protected Div getEditorContainer()
    {
        return editorContainer;
    }


    protected Editor getEditor()
    {
        return editor;
    }
}
