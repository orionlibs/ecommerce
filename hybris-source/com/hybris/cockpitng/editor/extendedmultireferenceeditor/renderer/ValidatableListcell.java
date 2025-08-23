/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.extendedmultireferenceeditor.renderer;

import com.hybris.cockpitng.components.validation.ValidationRenderer;
import com.hybris.cockpitng.core.Cleanable;
import com.hybris.cockpitng.core.Initializable;
import com.hybris.cockpitng.core.model.ValueObserver;
import com.hybris.cockpitng.editor.extendedmultireferenceeditor.state.RowState;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.validation.model.ValidationResult;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.zul.Listcell;

public class ValidatableListcell extends Listcell implements Cleanable, Initializable
{
    private final transient RowState rowState;
    private final String path;
    private final transient ValidationRenderer validationRenderer;
    private transient ValueObserver cellValidationObserver;


    public ValidatableListcell(final RowState rowState, final String path, final ValidationRenderer validationRenderer)
    {
        this.rowState = rowState;
        this.path = path;
        this.validationRenderer = validationRenderer;
        ValidatableListcell.this.applyValidationCss();
    }


    public RowState getRowState()
    {
        return rowState;
    }


    @Override
    public void initialize()
    {
        cellValidationObserver = this::applyValidationCss;
        if(StringUtils.isEmpty(path))
        {
            getRowState().getValidationResult().addObserver(cellValidationObserver);
        }
        else
        {
            getRowState().getValidationResult().addObserver(path, cellValidationObserver);
        }
    }


    @Override
    public void cleanup()
    {
        getRowState().getValidationResult().removeObserver(cellValidationObserver);
    }


    public ValidationRenderer getValidationRenderer()
    {
        return validationRenderer;
    }


    protected ValidationResult getCurrentValidationResult()
    {
        if(StringUtils.isEmpty(path))
        {
            return rowState.getValidationResult();
        }
        else
        {
            return rowState.getValidationResult().find(path).wrap();
        }
    }


    protected void clearSeverityClass()
    {
        getValidationRenderer().cleanAllValidationCss(this);
    }


    protected void applySeverityClass(final String sClass)
    {
        UITools.modifySClass(this, sClass, true);
    }


    public void applyValidationCss()
    {
        clearSeverityClass();
        final String severityStyleClass = getValidationRenderer()
                        .getSeverityStyleClass(getCurrentValidationResult().getHighestSeverity());
        if(!StringUtils.isEmpty(severityStyleClass))
        {
            applySeverityClass(severityStyleClass);
        }
    }
}
