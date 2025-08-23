/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.branding.customthemes.themes;

import com.hybris.backoffice.cockpitng.BackofficeValidationHandler;
import com.hybris.cockpitng.components.validation.AbstractValidatableContainer;
import com.hybris.cockpitng.components.validation.ValidationFocusTransferHandler;
import com.hybris.cockpitng.core.model.StandardModelKeys;
import com.hybris.cockpitng.core.model.impl.ObserverProxy;
import com.hybris.cockpitng.core.util.ObjectValuePath;
import com.hybris.cockpitng.validation.model.ValidationResult;
import org.apache.commons.lang.StringUtils;
import org.zkoss.zk.ui.Component;

public class ThemeValidatable extends AbstractValidatableContainer
{
    public static final String VALIDATION_RESULT_MODEL_PATH = "validationResult";
    private final ThemesController controller;
    private final Component parent;


    public ThemeValidatable(final ThemesController controller, final Component parent)
    {
        super(controller.getModel(), StandardModelKeys.VALIDATION_RESULT_KEY);
        this.controller = controller;
        this.parent = parent;
    }


    @Override
    protected ValidationFocusTransferHandler createFocusTransferHandler()
    {
        return (ValidationFocusTransferHandler)new BackofficeValidationHandler();
    }


    @Override
    public Component getContainer()
    {
        return parent;
    }


    @Override
    public Object getCurrentObject(final String path)
    {
        if(ObjectValuePath.parse(path).startsWith(ThemesController.MODEL_CURRENT_OBJECT))
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
        final var contextObjectPath = ObjectValuePath.parse(StandardModelKeys.CONTEXT_OBJECT);
        final var ovp = ObjectValuePath.parse(path);
        if(contextObjectPath.equals(ovp))
        {
            return StringUtils.EMPTY;
        }
        else if(ovp.startsWith(contextObjectPath))
        {
            return ovp.getRelative(contextObjectPath).buildPath();
        }
        else
        {
            return path;
        }
    }


    @Override
    public boolean isRootPath(final String path)
    {
        return StringUtils.isEmpty(path) || StringUtils.equals(ThemesController.MODEL_CURRENT_OBJECT, path);
    }


    @Override
    public boolean reactOnValidationChange(final String path)
    {
        return VALIDATION_RESULT_MODEL_PATH.equals(path);
    }


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
}

