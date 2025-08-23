/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.configurableflow.validation;

import com.hybris.cockpitng.components.validation.AbstractValidatableContainer;
import com.hybris.cockpitng.components.validation.ValidationFocusTransferHandler;
import com.hybris.cockpitng.core.model.StandardModelKeys;
import com.hybris.cockpitng.core.model.impl.ObserverProxy;
import com.hybris.cockpitng.core.util.ObjectValuePath;
import com.hybris.cockpitng.validation.model.ValidationResult;
import com.hybris.cockpitng.widgets.configurableflow.ConfigurableFlowController;
import java.util.Comparator;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.zk.ui.Component;

public class ConfigurableFlowValidatable extends AbstractValidatableContainer
{
    private final ConfigurableFlowController controller;
    private final Component parent;


    public ConfigurableFlowValidatable(final ConfigurableFlowController controller, final Component parent)
    {
        super(controller.getModel(), StandardModelKeys.VALIDATION_RESULT_KEY);
        this.controller = controller;
        this.parent = parent;
    }


    @Override
    public Component getContainer()
    {
        return parent;
    }


    @Override
    public Object getCurrentObject(final String fullPathQualifier)
    {
        Object ret = null;
        final String currentPrefix = getCurrentPrefix(fullPathQualifier);
        if(StringUtils.isNotBlank(currentPrefix))
        {
            ret = controller.getCurrentObject(currentPrefix);
        }
        return ret;
    }


    @Override
    public boolean isRootPath(final String path)
    {
        return StringUtils.isEmpty(path) || getRootPath().buildPath().equals(path) || controller.getModelContexts().contains(path);
    }


    public String getCurrentPrefix(final String fullPathQualifier)
    {
        final ObjectValuePath path = ObjectValuePath.parse(fullPathQualifier);
        final Optional<ObjectValuePath> longestParent = controller.getModelContexts().stream().map(ObjectValuePath::parse)
                        .filter(path::startsWith).max(Comparator.comparing(ObjectValuePath::size));
        return longestParent.isPresent() ? longestParent.get().buildPath() : StringUtils.EMPTY;
    }


    @Override
    public String getCurrentObjectPath(final String path)
    {
        final ObjectValuePath parentPath = ObjectValuePath.parse(getCurrentPrefix(path));
        final ObjectValuePath fullPath = ObjectValuePath.parse(path);
        return fullPath.getRelative(parentPath).buildPath();
    }


    @Override
    protected ValidationFocusTransferHandler createFocusTransferHandler()
    {
        return new ConfigurableFlowValidationHandler(controller);
    }


    @Override
    public boolean reactOnValidationChange(final String path)
    {
        final boolean validationPopupVisible = controller.getValidationViolationsPopup().isVisible();
        return !preventBroadcastValidationChange && (validationPopupVisible || isRootPath(path));
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
