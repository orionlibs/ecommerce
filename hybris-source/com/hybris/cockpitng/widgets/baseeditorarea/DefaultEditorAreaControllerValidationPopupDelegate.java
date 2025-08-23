/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.baseeditorarea;

import com.hybris.cockpitng.components.validation.ValidationFocusTransferHandler;
import com.hybris.cockpitng.core.Executable;
import com.hybris.cockpitng.core.util.ObjectValuePath;
import com.hybris.cockpitng.validation.ValidationContext;
import com.hybris.cockpitng.validation.impl.DefaultValidationContext;
import com.hybris.cockpitng.validation.model.ValidationInfo;
import com.hybris.cockpitng.validation.model.ValidationResult;
import com.hybris.cockpitng.validation.model.ValidationSeverity;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Window;

public class DefaultEditorAreaControllerValidationPopupDelegate
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultEditorAreaControllerValidationPopupDelegate.class);
    private Window validationResultsWindow;
    private DefaultEditorAreaValidateable validatableContainer;
    private final DefaultEditorAreaController controller;
    private final DefaultEditorAreaControllerModelOperationsDelegate modelOperationsDelegate;


    public DefaultEditorAreaControllerValidationPopupDelegate(final DefaultEditorAreaController controller)
    {
        this.controller = controller;
        modelOperationsDelegate = controller.getModelOperationsDelegate();
    }


    public DefaultEditorAreaValidateable getValidatableContainer()
    {
        return validatableContainer;
    }


    public Window getValidationResultsWindow()
    {
        return validationResultsWindow;
    }


    public void prepareValidationResultsPopup(final Component parent, final Executable saveExecution)
    {
        if(validationResultsWindow == null)
        {
            validatableContainer = new DefaultEditorAreaValidateable(controller, parent);
            validationResultsWindow = controller.getValidationRenderer().createValidationViolationsPopup(validatableContainer,
                            event -> confirmationListener(saveExecution));
            if(controller.getSaveButton() != null)
            {
                controller.getSaveButton().getParent().appendChild(validationResultsWindow);
            }
        }
    }


    public ValidationResult doValidate(final Object objectToValidate, final boolean preventBroadcast)
    {
        return doValidate(objectToValidate, preventBroadcast, true);
    }


    public ValidationResult doValidate(final Object objectToValidate, final boolean preventBroadcast, final boolean applyConfirmed)
    {
        final ValidationResult validationResult = modelOperationsDelegate.getCurrentValidationResult();
        validationResultsWindow.setVisible(false);
        try
        {
            validatableContainer.setPreventBroadcastValidationChange(preventBroadcast);
            if(validationResult != null)
            {
                final EditorAreaLogicHandler logicHandler = controller.getLogicHandler();
                if(logicHandler != null)
                {
                    final List<ValidationInfo> validation = logicHandler.performValidation(controller.getWidgetInstanceManager(),
                                    objectToValidate, createValidationContext(applyConfirmed));
                    validationResult.setValidationInfo(DefaultEditorAreaController.MODEL_CURRENT_OBJECT, validation);
                }
            }
        }
        finally
        {
            validatableContainer.setPreventBroadcastValidationChange(false);
        }
        return validationResult;
    }


    public void focusAttribute(final String qualifier)
    {
        if(qualifier != null && validatableContainer != null)
        {
            final ObjectValuePath valuePath = ObjectValuePath.parse(qualifier);
            if(!valuePath.startsWith(DefaultEditorAreaController.MODEL_CURRENT_OBJECT))
            {
                valuePath.prepend(DefaultEditorAreaController.MODEL_CURRENT_OBJECT);
            }
            final int result = validatableContainer.getFocusTransfer().focusValidationPath(validatableContainer.getContainer(),
                            valuePath.buildPath());
            if(result == ValidationFocusTransferHandler.TRANSFER_ERROR_UNKNOWN_PATH)
            {
                LOG.warn("{}:{}", Labels.getLabel("validation.popup.message.unknownPath"), qualifier);
            }
            else if(result == ValidationFocusTransferHandler.TRANSFER_ERROR_OTHER)
            {
                LOG.warn("{}:{}", Labels.getLabel("validation.popup.message.unknownError"), qualifier);
            }
        }
    }


    private void confirmationListener(final Executable saveExecution)
    {
        final ValidationResult validationResult = modelOperationsDelegate.getCurrentValidationResult();
        final ValidationSeverity validationSeverity = validationResult.getHighestNotConfirmedSeverity();
        if(ValidationSeverity.WARN.equals(validationSeverity))
        {
            validationResult.getNotConfirmed(ValidationSeverity.WARN).collect().forEach(info -> info.setConfirmed(true));
        }
        if(validationSeverity.isLowerThan(ValidationSeverity.ERROR))
        {
            saveExecution.execute();
        }
    }


    private ValidationContext createValidationContext(final boolean applyConfirmed)
    {
        final DefaultValidationContext validationContext = new DefaultValidationContext();
        final ValidationResult validationResult = modelOperationsDelegate.getCurrentValidationResult()
                        .get(DefaultEditorAreaController.MODEL_CURRENT_OBJECT).wrap();
        if(validationResult != null && applyConfirmed)
        {
            validationContext.setConfirmed(validationResult.getConfirmed().wrap());
        }
        return validationContext;
    }
}
