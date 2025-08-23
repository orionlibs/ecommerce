/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.handler.create;

import com.hybris.backoffice.workflow.designer.WorkflowDesignerModelKey;
import com.hybris.backoffice.workflow.designer.dto.ElementDto;
import com.hybris.backoffice.workflow.designer.dto.Operation;
import com.hybris.backoffice.workflow.designer.form.AbstractWorkflowTemplateCreateForm;
import com.hybris.backoffice.workflow.designer.handler.WorkflowDesignerDataManipulationViewEventHandler;
import com.hybris.backoffice.workflow.designer.services.NodeTypeService;
import com.hybris.cockpitng.components.visjs.network.data.Node;
import com.hybris.cockpitng.config.jaxb.wizard.CustomType;
import com.hybris.cockpitng.core.model.WidgetModel;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.util.notifications.NotificationService;
import com.hybris.cockpitng.util.notifications.event.NotificationEvent;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandler;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandlerAdapter;
import de.hybris.platform.core.model.ItemModel;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Required;

/**
 * A common handler for {@link de.hybris.platform.workflow.model.WorkflowActionTemplateModel} and
 * {@link de.hybris.platform.workflow.model.WorkflowDecisionTemplateModel} create handlers
 *
 * @param <FORM>
 *           a POJO which holds wizard specific data
 * @param <MODEL>
 *           a subtype of {@link ItemModel}
 */
public abstract class AbstractWorkflowDesignerCreateHandler<FORM extends AbstractWorkflowTemplateCreateForm, MODEL extends ItemModel, DTO extends ElementDto<MODEL>>
                implements FlowActionHandler
{
    private NotificationService notificationService;
    private NodeTypeService nodeTypeService;


    @Override
    public void perform(final CustomType customType, final FlowActionHandlerAdapter adapter, final Map<String, String> map)
    {
        final WidgetInstanceManager wim = adapter.getWidgetInstanceManager();
        final FORM form = retrieveFormFromModel(wim);
        final Node node = wim.getModel().getValue("ctx." + WorkflowDesignerModelKey.KEY_NODE, Node.class);
        final MODEL model = retrieveOrCreateModelInstance(form, node, wim.getModel());
        final ElementDto<MODEL> dto = retrieveDtoObject();
        dto.setOperation(node != null ? Operation.EDIT : Operation.CREATE);
        dto.setModel(model);
        dto.setNode(node);
        if(isCurrentNodeBeingEdited(form, node) || isCodeUnique(form, wim.getModel()))
        {
            wim.sendOutput(getSocketOutput(), dto);
            adapter.done();
        }
        else
        {
            getNotificationService().notifyUser(WorkflowDesignerDataManipulationViewEventHandler.NOTIFICATION_AREA_SOURCE,
                            getNonUniqueCodeMessageKey(), NotificationEvent.Level.FAILURE);
        }
    }


    protected abstract String getNonUniqueCodeMessageKey();


    protected abstract FORM retrieveFormFromModel(final WidgetInstanceManager wim);


    protected abstract MODEL retrieveOrCreateModelInstance(final FORM form, final Node node, final WidgetModel widgetModel);


    protected abstract DTO retrieveDtoObject();


    protected abstract String getSocketOutput();


    protected boolean isCurrentNodeBeingEdited(final FORM form, final Node node)
    {
        return Optional.ofNullable(node).map(nonNullNode -> nodeTypeService.hasCode(nonNullNode, form.getCode())).orElse(false);
    }


    protected abstract boolean isCodeUnique(final FORM form, final WidgetModel widgetModel);


    protected static Set<Node> extractNodes(final WidgetModel widgetModel)
    {
        return widgetModel.getValue("ctx." + WorkflowDesignerModelKey.KEY_NODES, Set.class);
    }


    @Required
    public void setNotificationService(final NotificationService notificationService)
    {
        this.notificationService = notificationService;
    }


    public NotificationService getNotificationService()
    {
        return notificationService;
    }


    @Required
    public void setNodeTypeService(final NodeTypeService nodeTypeService)
    {
        this.nodeTypeService = nodeTypeService;
    }


    public NodeTypeService getNodeTypeService()
    {
        return nodeTypeService;
    }
}
