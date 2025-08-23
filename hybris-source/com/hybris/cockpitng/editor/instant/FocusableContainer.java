/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.instant;

import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.core.Focusable;
import com.hybris.cockpitng.editor.util.FocusUtils;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

public class FocusableContainer extends Div implements Focusable
{
    private final HtmlBasedComponent label;
    private final Editor editor;
    private final Div editorContainer;
    private final transient InstantEditorSwitcher instantEditorSwitcher;


    public FocusableContainer(final HtmlBasedComponent label, final Editor editor, final Div editorContainer)
    {
        this.label = label;
        this.editor = editor;
        this.editorContainer = editorContainer;
        this.instantEditorSwitcher = new InstantEditorSwitcher();
        FocusableContainer.this.setId(Editor.DEFAULT_FOCUS_COMPONENT_ID);
    }


    @Override
    public void focus()
    {
        super.focus();
        switchToEditor();
        focusEditor();
    }


    @Override
    public void focus(final String path)
    {
        super.focus();
        switchToEditor();
        focusComponent(path);
    }


    protected void switchToEditor()
    {
        instantEditorSwitcher.switchToEditor(getLabelContainer(), getEditorContainer());
    }


    protected void focusEditor()
    {
        getEditor().focus();
    }


    protected void focusComponent(final String path)
    {
        FocusUtils.focusComponent(getEditor(), path);
    }


    /**
     * @deprecated since 6.6
     * @see #getLabelContainer()
     */
    @Deprecated(since = "6.6", forRemoval = true)
    protected Label getLabel()
    {
        return (Label)label;
    }


    protected HtmlBasedComponent getLabelContainer()
    {
        return label;
    }


    public Editor getEditor()
    {
        return editor;
    }


    public Div getEditorContainer()
    {
        return editorContainer;
    }
}
