/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.instant.listeners;

import com.hybris.cockpitng.components.Editor;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.MouseEvent;
import org.zkoss.zul.Div;

/**
 * Label click listener for Instant Editor.
 */
public class InstantEditorLabelClickListener extends AbstractInstantEditorMouseEventListener
{
    /**
     * @param label
     *           label container containing string representation of the data
     * @param editorContainer
     *           container component for the underlying editor
     * @param editor
     *           instant editor's underlying editor
     */
    public InstantEditorLabelClickListener(final HtmlBasedComponent label, final Div editorContainer, final Editor editor)
    {
        super(label, editorContainer, editor);
    }


    @Override
    public void onEvent(final MouseEvent event) throws Exception
    {
        getEditor().addParameter(KEY_OLD_VALUE, getEditor().getValue());
        switchToEditor();
    }


    @Override
    protected void switchToEditor()
    {
        super.switchToEditor();
        getEditor().reload();
        getEditor().focus();
    }
}
