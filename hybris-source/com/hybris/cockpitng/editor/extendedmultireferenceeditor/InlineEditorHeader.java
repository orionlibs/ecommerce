/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.extendedmultireferenceeditor;

import com.hybris.cockpitng.editor.extendedmultireferenceeditor.validate.AbstractInlineEditorValidatableContainer;
import com.hybris.cockpitng.editor.extendedmultireferenceeditor.validate.RowInlineEditorValidatableContainer;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Listhead;

public class InlineEditorHeader extends Listhead
{
    private HtmlBasedComponent globalSaveButton;
    private transient AbstractInlineEditorValidatableContainer globalValidatableContainer;
    private transient RowInlineEditorValidatableContainer rowValidatableContainer;
    private int menuColumnIndex = 0;


    public HtmlBasedComponent getGlobalSaveButton()
    {
        return globalSaveButton;
    }


    public void setGlobalSaveButton(final HtmlBasedComponent globalSaveButton)
    {
        this.globalSaveButton = globalSaveButton;
    }


    public RowInlineEditorValidatableContainer getRowValidatableContainer()
    {
        return rowValidatableContainer;
    }


    public void setGlobalValidatableContainer(final AbstractInlineEditorValidatableContainer globalValidatableContainer)
    {
        this.globalValidatableContainer = globalValidatableContainer;
    }


    public AbstractInlineEditorValidatableContainer getGlobalValidatableContainer()
    {
        return globalValidatableContainer;
    }


    public void setRowValidatableContainer(final RowInlineEditorValidatableContainer rowValidatableContainer)
    {
        this.rowValidatableContainer = rowValidatableContainer;
    }


    public int getMenuColumnIndex()
    {
        return menuColumnIndex;
    }


    public void setMenuColumnIndex(final int menuColumnIndex)
    {
        this.menuColumnIndex = menuColumnIndex;
    }


    @Override
    public void invalidate()
    {
        super.invalidate();
        getGlobalValidatableContainer().getValidationResultPopup().invalidate();
        getRowValidatableContainer().getValidationResultPopup().invalidate();
    }
}
