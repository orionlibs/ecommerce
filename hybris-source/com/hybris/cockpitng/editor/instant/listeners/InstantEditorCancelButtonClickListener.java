/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.instant.listeners;

import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.core.Executable;
import com.hybris.cockpitng.editor.instant.labelprovider.InstantEditorLabelProvider;
import com.hybris.cockpitng.editor.instant.labelprovider.function.LabelUpdateFunction;
import com.hybris.cockpitng.editors.EditorListener;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.MouseEvent;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

/**
 * Cancel button click listener for Instant Editor.
 */
public class InstantEditorCancelButtonClickListener extends InstantEditorConfirmButtonClickListener
{
    /**
     * @param labelContainer
     *           label component containing string representation of the data
     * @param editorContainer
     *           container component for the underlying editor
     * @param editor
     *           instant editor's underlying editor
     * @param listener
     *           listener for events related to editor
     * @param labelUpdate
     *           executable that updates label with new value
     */
    public InstantEditorCancelButtonClickListener(final HtmlBasedComponent labelContainer, final Div editorContainer, final Editor editor,
                    final EditorListener<Object> listener, final Executable labelUpdate)
    {
        super(labelContainer, editorContainer, editor, listener, labelUpdate);
    }


    /**
     * @deprecated since 6.6
     * @see #InstantEditorCancelButtonClickListener(HtmlBasedComponent, Div, Editor, EditorListener, Executable)
     */
    @Deprecated(since = "6.6", forRemoval = true)
    public InstantEditorCancelButtonClickListener(final Label label, final Div editorContainer, final Editor editor,
                    final InstantEditorLabelProvider labelProvider, final LabelUpdateFunction labelUpdater)
    {
        super(label, editorContainer, editor, null, labelProvider, labelUpdater);
    }


    @Override
    public void onEvent(final MouseEvent event) throws Exception
    {
        rollbackEditorValue();
        super.onEvent(event);
    }


    protected void rollbackEditorValue()
    {
        if(getEditor().getParameters().containsKey(KEY_OLD_VALUE))
        {
            getEditor().setValue(getEditor().getParameters().get(KEY_OLD_VALUE));
        }
    }
}
