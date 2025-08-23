/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.validation;

import com.hybris.cockpitng.components.validation.AbstractValidatableContainer;
import com.hybris.cockpitng.components.validation.ValidationFocusTransferHandler;
import com.hybris.cockpitng.core.model.WidgetModel;
import com.hybris.cockpitng.validation.impl.DefaultValidationInfo;
import com.hybris.cockpitng.validation.model.ValidationResult;
import com.hybris.cockpitng.validation.model.ValidationSeverity;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;

public class WorkflowDesignerValidatableContainer extends AbstractValidatableContainer
{
    private static final ValidationFocusTransferHandler EMPTY_VALIDATION_FOCUS_TRANSFER_HANDLER = (parent, property) -> 0;
    public static final String VALIDATION_RESULT_MODEL_PATH = "validationResult";
    private final WidgetModel model;
    private final Component component;


    WorkflowDesignerValidatableContainer(final WidgetModel model, final String rootPath, final Component component)
    {
        super(model, rootPath);
        this.model = model;
        this.component = component;
    }


    @Override
    protected ValidationFocusTransferHandler createFocusTransferHandler()
    {
        return EMPTY_VALIDATION_FOCUS_TRANSFER_HANDLER;
    }


    @Override
    public Component getContainer()
    {
        return component;
    }


    @Override
    public Object getCurrentObject(final String path)
    {
        return StringUtils.EMPTY;
    }


    @Override
    public String getCurrentObjectPath(final String path)
    {
        return StringUtils.EMPTY;
    }


    @Override
    public boolean isRootPath(final String path)
    {
        return VALIDATION_RESULT_MODEL_PATH.equals(path);
    }


    @Override
    public boolean reactOnValidationChange(final String path)
    {
        return VALIDATION_RESULT_MODEL_PATH.equals(path);
    }


    @Override
    public ValidationResult getCurrentValidationResult(final String path)
    {
        final ValidationResult validationResult = new ValidationResult();
        for(final Violation violation : model.getValue(VALIDATION_RESULT_MODEL_PATH, WorkflowDesignerValidationResult.class)
                        .getViolations())
        {
            final DefaultValidationInfo info = new DefaultValidationInfo();
            info.setValidationMessage(getValidationMessage(violation));
            info.setValidationSeverity(getValidationSeverity(violation));
            validationResult.addValidationInfo(info);
        }
        return validationResult;
    }


    protected String getValidationMessage(final Violation violation)
    {
        return Labels.getLabel(violation.getMessageKey(), violation.getMessageParameters().toArray());
    }


    private ValidationSeverity getValidationSeverity(final Violation violation)
    {
        switch(violation.getLevel())
        {
            case INFO:
                return ValidationSeverity.INFO;
            case WARN:
                return ValidationSeverity.WARN;
            case ERROR:
                return ValidationSeverity.ERROR;
        }
        return ValidationSeverity.NONE;
    }
}
