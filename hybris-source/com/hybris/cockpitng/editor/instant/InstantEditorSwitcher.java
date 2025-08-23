/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.instant;

import com.hybris.cockpitng.util.UITools;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Div;

public class InstantEditorSwitcher
{
    public void switchToLabel(final HtmlBasedComponent label, final Div editorContainer)
    {
        UITools.modifySClass(editorContainer, InstantEditorRenderer.YE_INSTANT_EDITOR_COMPONENT_VISIBLE, false);
        UITools.modifySClass(editorContainer, InstantEditorRenderer.YE_INSTANT_EDITOR_COMPONENT_HIDDEN, true);
        UITools.modifySClass(label, InstantEditorRenderer.YE_INSTANT_EDITOR_COMPONENT_VISIBLE, true);
        UITools.modifySClass(label, InstantEditorRenderer.YE_INSTANT_EDITOR_COMPONENT_HIDDEN, false);
    }


    public void switchToEditor(final HtmlBasedComponent label, final Div editorContainer)
    {
        UITools.modifySClass(editorContainer, InstantEditorRenderer.YE_INSTANT_EDITOR_COMPONENT_VISIBLE, true);
        UITools.modifySClass(editorContainer, InstantEditorRenderer.YE_INSTANT_EDITOR_COMPONENT_HIDDEN, false);
        UITools.modifySClass(label, InstantEditorRenderer.YE_INSTANT_EDITOR_COMPONENT_VISIBLE, false);
        UITools.modifySClass(label, InstantEditorRenderer.YE_INSTANT_EDITOR_COMPONENT_HIDDEN, true);
    }
}
