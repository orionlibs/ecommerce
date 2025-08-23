/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.handler.create;

import com.hybris.backoffice.workflow.designer.dto.ActionDto;
import com.hybris.backoffice.workflow.designer.form.WorkflowTemplateCreateActionForm;
import com.hybris.cockpitng.components.visjs.network.data.Node;
import com.hybris.cockpitng.core.model.WidgetModel;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;
import java.util.Optional;
import java.util.function.Function;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

/**
 * Handler of Action creation in ConfigurableFlowWizard
 */
public class WorkflowDesignerCreateActionHandler
                extends AbstractWorkflowDesignerCreateHandler<WorkflowTemplateCreateActionForm, WorkflowActionTemplateModel, ActionDto>
{
    public static final String MODEL_OBJECT = "newObject";
    public static final String SOCKET_OUT_ACTION = "workflowDesignerCreateAction";
    public static final String NON_UNIQUE_ACTION_CODE_MESSAGE_KEY = "nonUniqueActionCode";
    private ModelService modelService;


    @Override
    protected WorkflowTemplateCreateActionForm retrieveFormFromModel(final WidgetInstanceManager wim)
    {
        return wim.getModel().getValue(MODEL_OBJECT, WorkflowTemplateCreateActionForm.class);
    }


    @Override
    protected WorkflowActionTemplateModel retrieveOrCreateModelInstance(
                    final WorkflowTemplateCreateActionForm workflowTemplateCreateActionForm, final Node node, final WidgetModel widgetModel)
    {
        final WorkflowActionTemplateModel actionModel = Optional.ofNullable(node)
                        .map(n -> Optional.ofNullable(widgetModel.getValue("ctx.parentObject", WorkflowActionTemplateModel.class))
                                        .filter(action -> getNodeTypeService().isSameAction(action, node)))
                        .flatMap(Function.identity()).orElseGet(() -> modelService.create(WorkflowActionTemplateModel._TYPECODE));
        actionModel.setCode(workflowTemplateCreateActionForm.getCode());
        actionModel.setActionType(workflowTemplateCreateActionForm.getActionType());
        actionModel.setPrincipalAssigned(workflowTemplateCreateActionForm.getPrincipalAssigned());
        Optional.ofNullable(workflowTemplateCreateActionForm.getName())
                        .ifPresent(map -> map.forEach((locale, name) -> actionModel.setName(name, locale)));
        Optional.ofNullable(workflowTemplateCreateActionForm.getDescription())
                        .ifPresent(map -> map.forEach((locale, name) -> actionModel.setDescription(name, locale)));
        return actionModel;
    }


    @Override
    protected ActionDto retrieveDtoObject()
    {
        return new ActionDto();
    }


    @Override
    protected String getSocketOutput()
    {
        return SOCKET_OUT_ACTION;
    }


    @Override
    protected boolean isCodeUnique(final WorkflowTemplateCreateActionForm form, final WidgetModel widgetModel)
    {
        return CollectionUtils.emptyIfNull(extractNodes(widgetModel)).stream().filter(node -> getNodeTypeService().isAction(node))
                        .noneMatch(node -> getNodeTypeService().hasCode(node, form.getCode()));
    }


    @Override
    protected String getNonUniqueCodeMessageKey()
    {
        return NON_UNIQUE_ACTION_CODE_MESSAGE_KEY;
    }


    @Required
    public void setModelService(final ModelService modelService)
    {
        this.modelService = modelService;
    }
}

