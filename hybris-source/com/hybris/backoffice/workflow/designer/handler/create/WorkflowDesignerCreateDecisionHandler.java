/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.handler.create;

import com.hybris.backoffice.workflow.designer.dto.DecisionDto;
import com.hybris.backoffice.workflow.designer.form.WorkflowTemplateCreateDecisionForm;
import com.hybris.cockpitng.components.visjs.network.data.Node;
import com.hybris.cockpitng.core.model.WidgetModel;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.workflow.model.WorkflowDecisionTemplateModel;
import java.util.Optional;
import java.util.function.Function;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

/**
 * Handler of Decision creation in ConfigurableFlowWizard
 */
public class WorkflowDesignerCreateDecisionHandler extends
                AbstractWorkflowDesignerCreateHandler<WorkflowTemplateCreateDecisionForm, WorkflowDecisionTemplateModel, DecisionDto>
{
    public static final String MODEL_OBJECT = "newObject";
    public static final String SOCKET_OUT_DECISION = "workflowDesignerCreateDecision";
    public static final String NON_UNIQUE_DECISION_CODE_MESSAGE_KEY = "nonUniqueDecisionCode";
    private ModelService modelService;


    @Override
    protected WorkflowTemplateCreateDecisionForm retrieveFormFromModel(final WidgetInstanceManager wim)
    {
        return wim.getModel().getValue(MODEL_OBJECT, WorkflowTemplateCreateDecisionForm.class);
    }


    @Override
    protected WorkflowDecisionTemplateModel retrieveOrCreateModelInstance(
                    final WorkflowTemplateCreateDecisionForm workflowTemplateCreateDecisionForm, final Node node,
                    final WidgetModel widgetModel)
    {
        final WorkflowDecisionTemplateModel decisionModel = Optional.ofNullable(node)
                        .map(n -> Optional.ofNullable(widgetModel.getValue("ctx.parentObject", WorkflowDecisionTemplateModel.class))
                                        .filter(decision -> getNodeTypeService().isSameDecision(decision, node)))
                        .flatMap(Function.identity()).orElseGet(() -> modelService.create(WorkflowDecisionTemplateModel._TYPECODE));
        decisionModel.setCode(workflowTemplateCreateDecisionForm.getCode());
        Optional.ofNullable(workflowTemplateCreateDecisionForm.getName())
                        .ifPresent(map -> map.forEach((locale, name) -> decisionModel.setName(name, locale)));
        return decisionModel;
    }


    @Override
    protected DecisionDto retrieveDtoObject()
    {
        return new DecisionDto();
    }


    @Override
    protected String getSocketOutput()
    {
        return SOCKET_OUT_DECISION;
    }


    @Override
    protected boolean isCodeUnique(final WorkflowTemplateCreateDecisionForm form, final WidgetModel widgetModel)
    {
        return CollectionUtils.emptyIfNull(extractNodes(widgetModel)).stream().filter(node -> getNodeTypeService().isDecision(node))
                        .noneMatch(node -> getNodeTypeService().hasCode(node, form.getCode()));
    }


    @Override
    protected String getNonUniqueCodeMessageKey()
    {
        return NON_UNIQUE_DECISION_CODE_MESSAGE_KEY;
    }


    @Required
    public void setModelService(final ModelService modelService)
    {
        this.modelService = modelService;
    }
}

