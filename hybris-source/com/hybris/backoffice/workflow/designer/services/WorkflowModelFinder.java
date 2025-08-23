/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.services;

import static com.hybris.backoffice.widgets.networkchart.NetworkChartController.MODEL_INIT_DATA;

import com.hybris.backoffice.widgets.networkchart.context.NetworkChartContext;
import com.hybris.backoffice.workflow.designer.WorkflowDesignerNetworkPopulator;
import com.hybris.cockpitng.components.visjs.network.data.Node;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowDecisionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.SetUtils;
import org.springframework.beans.factory.annotation.Required;

/**
 * Utility methods for finding models from Workflow Designer nodes.
 */
public class WorkflowModelFinder
{
    private NodeTypeService nodeTypeService;


    /**
     * Finds corresponding {@link WorkflowActionTemplateModel} for given Node.
     *
     * @param context
     *           contains Network Chart widget model, where the Workflow Designer data is stored
     * @param node
     *           node for which a corresponding model should be found
     * @return found action model
     */
    public Optional<WorkflowActionTemplateModel> findWorkflowAction(final NetworkChartContext context, final Node node)
    {
        return findAllWorkflowActions(context).stream().filter(action -> getNodeTypeService().isSameAction(action, node))
                        .findFirst();
    }


    /**
     * Finds corresponding {@link WorkflowActionTemplateModel} for given Node in WorkflowTemplate model only
     *
     * @param context
     *           contains Network Chart widget model, where the Workflow Designer data is stored
     * @param node
     *           node for which a corresponding model should be found
     * @return found action model
     */
    public Optional<WorkflowActionTemplateModel> findWorkflowActionInWorkflowTemplateModel(final NetworkChartContext context,
                    final Node node)
    {
        return findWorkflowActionsFromWorkflowTemplateModel(context).stream()
                        .filter(action -> getNodeTypeService().isSameAction(action, node)).findFirst();
    }


    /**
     * Finds corresponding {@link WorkflowDecisionTemplateModel} for given Node.
     *
     * @param context
     *           contains Network Chart widget model, where the Workflow Designer data is stored
     * @param node
     *           for which corresponding model should be found
     * @return found decision model
     */
    public Optional<WorkflowDecisionTemplateModel> findWorkflowDecision(final NetworkChartContext context, final Node node)
    {
        return findAllWorkflowDecisions(context).stream().filter(decision -> getNodeTypeService().isSameDecision(decision, node))
                        .findFirst();
    }


    /**
     * Finds corresponding {@link WorkflowDecisionTemplateModel} for given Node in WorkflowTemplate model only.
     *
     * @param context
     *           contains Network Chart widget model, where the Workflow Designer data is stored
     * @param node
     *           for which corresponding model should be found
     * @return found decision model
     */
    public Optional<WorkflowDecisionTemplateModel> findWorkflowDecisionInWorkflowTemplateModel(final NetworkChartContext context,
                    final Node node)
    {
        return findWorkflowDecisionsFromWorkflowTemplateModel(context).stream()
                        .filter(decision -> getNodeTypeService().isSameDecision(decision, node)).findFirst();
    }


    /**
     * Returns new (unsaved) {@link WorkflowActionTemplateModel}s and {@link WorkflowDecisionTemplateModel}s.
     *
     * @param context
     *           contains Network Chart widget model, where the Workflow Designer data is stored
     * @return list of new (unsaved) {@link WorkflowActionTemplateModel}s and {@link WorkflowDecisionTemplateModel}s
     */
    public List<ItemModel> findNewModels(final NetworkChartContext context)
    {
        final List<ItemModel> models = context.getWim().getModel()
                        .getValue(WorkflowDesignerNetworkPopulator.MODEL_NEW_WORKFLOW_ITEMS_KEY, List.class);
        return ListUtils.emptyIfNull(models);
    }


    /**
     * Gets new (unsaved) {@link WorkflowActionTemplateModel}s from the widget model
     *
     * @param context
     *           contains Network Chart widget model, where the Workflow Designer data is stored
     * @return new (unsaved) {@link WorkflowActionTemplateModel}s from the widget model
     */
    public Set<WorkflowActionTemplateModel> findNewWorkflowActions(final NetworkChartContext context)
    {
        return findNewModels(context).stream().filter(WorkflowActionTemplateModel.class::isInstance)
                        .map(WorkflowActionTemplateModel.class::cast).collect(Collectors.toSet());
    }


    /**
     * Gets new (unsaved) {@link WorkflowActionTemplateModel} corresponding to given {@link Node} from the widget model
     *
     * @param context
     *           contains Network Chart widget model, where the Workflow Designer data is stored
     * @param node
     *           that represents the desired {@link WorkflowActionTemplateModel}
     * @return corresponding {@link WorkflowActionTemplateModel} to given node
     */
    public Optional<WorkflowActionTemplateModel> findNewWorkflowAction(final NetworkChartContext context, final Node node)
    {
        return findNewWorkflowActions(context).stream().filter(action -> getNodeTypeService().isSameAction(action, node))
                        .findFirst();
    }


    /**
     * Gets new (unsaved) {@link WorkflowDecisionTemplateModel}s from the widget model
     *
     * @param context
     *           contains Network Chart widget model, where the Workflow Designer data is stored
     * @return new (unsaved) {@link WorkflowDecisionTemplateModel}s from the widget model
     */
    public Set<WorkflowDecisionTemplateModel> findNewWorkflowDecisions(final NetworkChartContext context)
    {
        return findNewModels(context).stream().filter(WorkflowDecisionTemplateModel.class::isInstance)
                        .map(WorkflowDecisionTemplateModel.class::cast).collect(Collectors.toSet());
    }


    /**
     * Gets new (unsaved) {@link WorkflowDecisionTemplateModel} corresponding to given {@link Node} from the widget model
     *
     * @param context
     *           contains Network Chart widget model, where the Workflow Designer data is stored
     * @param node
     *           that represents the desired {@link WorkflowDecisionTemplateModel}
     * @return corresponding {@link WorkflowDecisionTemplateModel} to given node
     */
    public Optional<WorkflowDecisionTemplateModel> findNewWorkflowDecision(final NetworkChartContext context, final Node node)
    {
        return findNewWorkflowDecisions(context).stream().filter(decision -> getNodeTypeService().isSameDecision(decision, node))
                        .findFirst();
    }


    /**
     * Gets existing (already saved) {@link WorkflowActionTemplateModel}s from the Workflow Template that is being edited.
     *
     * @param context
     *           contains Network Chart widget model, where the Workflow Designer data is stored
     * @return existing (already saved) {@link WorkflowActionTemplateModel}s from the Workflow Template that is being
     *         edited.
     */
    public Set<WorkflowActionTemplateModel> findWorkflowActionsFromWorkflowTemplateModel(final NetworkChartContext context)
    {
        return new HashSet<>(findWorkflowTemplate(context).getActions());
    }


    /**
     * Gets existing (already saved) {@link WorkflowDecisionTemplateModel}s from the Workflow Template that is being edited.
     *
     * @param context
     *           contains Network Chart widget model, where the Workflow Designer data is stored
     * @return existing (already saved) {@link WorkflowDecisionTemplateModel}s from the Workflow Template that is being
     *         edited.
     */
    public Set<WorkflowDecisionTemplateModel> findWorkflowDecisionsFromWorkflowTemplateModel(final NetworkChartContext context)
    {
        return findWorkflowActionsFromWorkflowTemplateModel(context).stream().map(this::findDecisionsOfAction)
                        .flatMap(Collection::stream).collect(Collectors.toSet());
    }


    /**
     * Finds all decisions that are incoming to this action or outgoing from this action.
     */
    public Set<WorkflowDecisionTemplateModel> findDecisionsOfAction(final WorkflowActionTemplateModel action)
    {
        final Collection<WorkflowDecisionTemplateModel> union = CollectionUtils.union(action.getDecisionTemplates(),
                        action.getIncomingTemplateDecisions());
        return new HashSet<>(union);
    }


    /**
     * Returns a list of both saved and unsaved {@link WorkflowActionTemplateModel}s.
     *
     * @param context
     *           contains Network Chart widget model, where the Workflow Designer data is stored
     * @return a list of both saved and unsaved {@link WorkflowActionTemplateModel}s.
     */
    public Set<WorkflowActionTemplateModel> findAllWorkflowActions(final NetworkChartContext context)
    {
        return SetUtils.union(findNewWorkflowActions(context), findWorkflowActionsFromWorkflowTemplateModel(context));
    }


    /**
     * Returns a list of both saved and unsaved {@link WorkflowDecisionTemplateModel}s.
     *
     * @param context
     *           contains Network Chart widget model, where the Workflow Designer data is stored
     * @return a list of both saved and unsaved {@link WorkflowDecisionTemplateModel}s.
     */
    public Set<WorkflowDecisionTemplateModel> findAllWorkflowDecisions(final NetworkChartContext context)
    {
        return SetUtils.union(findNewWorkflowDecisions(context), findWorkflowDecisionsFromWorkflowTemplateModel(context));
    }


    /**
     * Returns the workflow template that is currently edited in Workflow Designer
     *
     * @param context
     *           contains Network Chart widget model, where the Workflow Designer data is stored
     * @return the workflow template that is currently edited in Workflow Designer
     */
    public WorkflowTemplateModel findWorkflowTemplate(final NetworkChartContext context)
    {
        return context.getWim().getModel().getValue(MODEL_INIT_DATA, WorkflowTemplateModel.class);
    }


    public NodeTypeService getNodeTypeService()
    {
        return nodeTypeService;
    }


    @Required
    public void setNodeTypeService(final NodeTypeService nodeTypeService)
    {
        this.nodeTypeService = nodeTypeService;
    }
}
