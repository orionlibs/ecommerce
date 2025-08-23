/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.extendedmultireferenceeditor.renderer;

import com.hybris.cockpitng.components.validation.ValidationRenderer;
import com.hybris.cockpitng.editor.extendedmultireferenceeditor.state.RowState;
import com.hybris.cockpitng.util.UITools;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;

public class DefaultRowStateCell extends ValidatableListcell
{
    protected static final String ROW_STATUS_CELL = "ye-row-status";


    public DefaultRowStateCell(final RowState rowState, final ValidationRenderer validationRenderer)
    {
        super(rowState, StringUtils.EMPTY, validationRenderer);
        UITools.modifySClass(this, ROW_STATUS_CELL, true);
        UITools.modifySClass(this, ROW_STATUS_CELL + "-invalid", rowState.hasError());
        UITools.modifySClass(this, ROW_STATUS_CELL + "-edited", rowState.isRowModified());
    }


    @Override
    protected void clearSeverityClass()
    {
        super.clearSeverityClass();
        if(getParent() != null)
        {
            getValidationRenderer().cleanAllValidationCss((HtmlBasedComponent)getParent());
        }
    }


    @Override
    protected void applySeverityClass(final String sClass)
    {
        super.applySeverityClass(sClass);
        if(getParent() != null)
        {
            UITools.modifySClass((HtmlBasedComponent)getParent(), sClass, true);
        }
    }


    @Override
    public void applyValidationCss()
    {
        super.applyValidationCss();
        while(getFirstChild() != null)
        {
            removeChild(getFirstChild());
        }
        final Component messageBtn = getValidationRenderer().createValidationMessageBtn(getCurrentValidationResult());
        if(messageBtn != null)
        {
            appendChild(messageBtn);
        }
    }
}
