/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.baseeditorarea;

import com.hybris.cockpitng.components.validation.AbstractValidatableContainer;
import com.hybris.cockpitng.components.validation.ValidationFocusTransferHandler;
import com.hybris.cockpitng.core.model.StandardModelKeys;
import com.hybris.cockpitng.core.model.impl.ObserverProxy;
import com.hybris.cockpitng.core.util.ObjectValuePath;
import com.hybris.cockpitng.validation.model.ValidationResult;
import com.hybris.cockpitng.widgets.editorarea.renderer.EditorAreaRendererUtils;
import org.apache.commons.lang.StringUtils;
import org.zkoss.zk.ui.Component;

/**
 * ValidateableContainer for {@link DefaultEditorAreaController}
 */
public class DefaultEditorAreaValidateable extends AbstractValidatableContainer
{
    private final DefaultEditorAreaController controller;
    private final Component parent;


    public DefaultEditorAreaValidateable(final DefaultEditorAreaController controller, final Component parent)
    {
        super(controller.getModel(), StandardModelKeys.VALIDATION_RESULT_KEY);
        this.controller = controller;
        this.parent = parent;
    }


    @Override
    public boolean isRootPath(final String path)
    {
        return StringUtils.isEmpty(path) || StringUtils.equals(DefaultEditorAreaController.MODEL_CURRENT_OBJECT, path);
    }


    @Override
    public Component getContainer()
    {
        return parent;
    }


    @Override
    public Object getCurrentObject(final String path)
    {
        if(ObjectValuePath.parse(path).startsWith(DefaultEditorAreaController.MODEL_CURRENT_OBJECT))
        {
            return controller.getCurrentObject();
        }
        else
        {
            return null;
        }
    }


    @Override
    public String getCurrentObjectPath(final String path)
    {
        return EditorAreaRendererUtils.getRelativeAttributePath(path);
    }


    @Override
    public ValidationResult getCurrentValidationResult(final String path)
    {
        ValidationResult result = controller.getCurrentValidationResult();
        if(!isRootPath(path))
        {
            result = result.find(path).wrap();
            result.addObserver(ObserverProxy.createWeakProxy(controller.getCurrentValidationResult(), ObjectValuePath.parse(path)));
        }
        return result;
    }


    @Override
    protected ValidationFocusTransferHandler createFocusTransferHandler()
    {
        return new DefaultEditorAreaValidationHandler(parent, controller, this);
    }


    @Override
    public boolean reactOnValidationChange(final String path)
    {
        final boolean validationPopupVisible = controller.getValidationResults().isVisible();
        return !preventBroadcastValidationChange && (validationPopupVisible || isRootPath(path));
    }
}
