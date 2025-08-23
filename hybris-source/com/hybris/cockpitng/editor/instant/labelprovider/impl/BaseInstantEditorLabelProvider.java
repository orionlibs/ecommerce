/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.instant.labelprovider.impl;

import com.hybris.cockpitng.labels.LabelService;
import org.apache.commons.lang3.StringUtils;

/**
 * Base implementation of label provider for {@link com.hybris.cockpitng.editor.instant.InstantEditorRenderer} editor.
 * Delegates conversion to {@link LabelService}.
 * <p>
 * Technically able to handle all types of editors, but not all of them will give good results.
 * </p>
 */
public class BaseInstantEditorLabelProvider extends AbstractInstantEditorLabelProvider
{
    private LabelService labelService;


    @Override
    public boolean canHandle(final String editorType)
    {
        return true;
    }


    @Override
    public String getLabel(final String editorType, final Object value)
    {
        if(value != null)
        {
            return getLabelService().getObjectLabel(value);
        }
        return StringUtils.EMPTY;
    }


    protected LabelService getLabelService()
    {
        return labelService;
    }


    public void setLabelService(final LabelService labelService)
    {
        this.labelService = labelService;
    }
}
