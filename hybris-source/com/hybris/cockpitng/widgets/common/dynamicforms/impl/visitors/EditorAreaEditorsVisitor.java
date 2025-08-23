/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.common.dynamicforms.impl.visitors;

import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.widgets.editorarea.renderer.impl.AbstractEditorAreaComponentRenderer;

/**
 * Subclass of {@link EditorsVisitor} dedicated for editors placed inside Editor Area
 */
public class EditorAreaEditorsVisitor extends EditorsVisitor
{
    @Override
    protected void disableEditor(final Editor editor, final boolean disabled)
    {
        super.disableEditor(editor, disabled);
        editor.setSclass(disabled ? AbstractEditorAreaComponentRenderer.SCLASS_READONLY_EDITOR
                        : AbstractEditorAreaComponentRenderer.SCLASS_EDITOR);
    }
}
