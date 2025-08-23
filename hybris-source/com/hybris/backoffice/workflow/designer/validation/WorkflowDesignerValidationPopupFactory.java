/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.validation;

import com.hybris.backoffice.widgets.networkchart.NetworkChartValidationPopupFactory;
import com.hybris.backoffice.widgets.networkchart.context.NetworkChartContext;
import com.hybris.cockpitng.widgets.configurableflow.ConfigurableFlowDefinitions;
import com.hybris.cockpitng.widgets.configurableflow.validation.ConfigurableFlowValidationRenderer;
import com.hybris.cockpitng.widgets.configurableflow.validation.ConfigurableFlowValidationResultsPopup;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Button;
import org.zkoss.zul.Window;

/**
 * Creates pop up presenting violations for Workflow Designer
 */
public class WorkflowDesignerValidationPopupFactory implements NetworkChartValidationPopupFactory
{
    private ConfigurableFlowValidationRenderer validationRenderer;
    private WorkflowDesignerValidator validator;


    @Override
    public Optional<Window> createValidationPopup(final NetworkChartContext context, final Button saveButton,
                    final Runnable saveOperation)
    {
        final Component buttonsContainer = saveButton.getParent();
        final WorkflowDesignerValidationResult workflowDesignerValidationResult = validator.validate(context);
        if(workflowDesignerValidationResult.hasViolations())
        {
            final WorkflowDesignerValidatableContainer validatableContainer = new WorkflowDesignerValidatableContainer(
                            context.getWim().getModel(), StringUtils.EMPTY, buttonsContainer);
            removePreviousPopUpIfExists(buttonsContainer);
            final ConfigurableFlowValidationResultsPopup validationViolationsPopup = createPopup(saveOperation, validatableContainer,
                            workflowDesignerValidationResult);
            updateValidationPopupAnchor(saveButton, validationViolationsPopup);
            validationViolationsPopup.setParent(buttonsContainer);
            setValidationResultToContext(context, workflowDesignerValidationResult);
            return Optional.of(validationViolationsPopup);
        }
        return Optional.empty();
    }


    private ConfigurableFlowValidationResultsPopup createPopup(final Runnable saveOperation,
                    final WorkflowDesignerValidatableContainer validatableContainer,
                    final WorkflowDesignerValidationResult workflowDesignerValidationResult)
    {
        return validationRenderer.createValidationViolationsPopup(validatableContainer, (actionId, validationResult) -> {
            if(!workflowDesignerValidationResult.hasErrors())
            {
                saveOperation.run();
            }
        });
    }


    private void setValidationResultToContext(final NetworkChartContext context,
                    final WorkflowDesignerValidationResult validationResult)
    {
        context.getWim().getModel().setValue(WorkflowDesignerValidatableContainer.VALIDATION_RESULT_MODEL_PATH, validationResult);
    }


    private void updateValidationPopupAnchor(final Button saveButton, final ConfigurableFlowValidationResultsPopup popup)
    {
        popup.updateValidationPopupAnchor(saveButton, ConfigurableFlowDefinitions.WIZARD_CURRENT_STEP_PERSIST);
    }


    private void removePreviousPopUpIfExists(final Component component)
    {
        getPreviousPopup(component).ifPresent(component::removeChild);
    }


    private Optional<ConfigurableFlowValidationResultsPopup> getPreviousPopup(final Component component)
    {
        return component.getChildren().stream().filter(ConfigurableFlowValidationResultsPopup.class::isInstance)
                        .map(ConfigurableFlowValidationResultsPopup.class::cast).findFirst();
    }


    @Required
    public void setValidationRenderer(final ConfigurableFlowValidationRenderer validationRenderer)
    {
        this.validationRenderer = validationRenderer;
    }


    @Required
    public void setValidator(final WorkflowDesignerValidator validator)
    {
        this.validator = validator;
    }
}
