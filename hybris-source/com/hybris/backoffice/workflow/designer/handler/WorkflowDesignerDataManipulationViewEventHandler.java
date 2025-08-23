/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.handler;

import com.hybris.backoffice.widgets.networkchart.NetworkChartController;
import com.hybris.backoffice.widgets.networkchart.context.NetworkChartContext;
import com.hybris.backoffice.widgets.networkchart.handler.DataManipulationHandler;
import com.hybris.backoffice.widgets.networkchart.handler.ViewEventHandler;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent.Level;
import com.hybris.backoffice.workflow.designer.WorkflowDesignerModelKey;
import com.hybris.backoffice.workflow.designer.WorkflowDesignerNetworkPopulator;
import com.hybris.backoffice.workflow.designer.handler.connection.WorkflowDesignerConnectionHandler;
import com.hybris.backoffice.workflow.designer.handler.connection.WorkflowDesignerGroup;
import com.hybris.backoffice.workflow.designer.handler.retriever.WorkflowModelRetriever;
import com.hybris.cockpitng.components.visjs.network.data.Edge;
import com.hybris.cockpitng.components.visjs.network.data.Edges;
import com.hybris.cockpitng.components.visjs.network.data.Node;
import com.hybris.cockpitng.components.visjs.network.data.Nodes;
import com.hybris.cockpitng.components.visjs.network.event.ClickOnAddNodeButtonEvent;
import com.hybris.cockpitng.components.visjs.network.response.Action;
import com.hybris.cockpitng.components.visjs.network.response.NetworkUpdate;
import com.hybris.cockpitng.components.visjs.network.response.NetworkUpdates;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.util.notifications.NotificationService;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.au.AuRequest;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;

/**
 * Default implementation of workflow {@link DataManipulationHandler} and {@link ViewEventHandler}
 */
public class WorkflowDesignerDataManipulationViewEventHandler implements DataManipulationHandler, ViewEventHandler
{
    public static final String VIRTUAL_SOCKET_OUT_CLOSE = "close";
    public static final String NOTIFICATION_AREA_SOURCE = "workflowDesigner";
    private static final String EVENT_TYPE_AND_NODE_EDITION = "andNodeEdition";
    private static final String LABEL_ACTION_BUTTON = "workflowdesigner.custombutton.action";
    private static final String LABEL_DECISION_BUTTON = "workflowdesigner.custombutton.decision";
    private static final String LABEL_AND_BUTTON = "workflowdesigner.custombutton.and";
    private static final String SCSS_ADD_NODE_POPUP = "yw-workflow-designer-add-node-popup";
    protected static final String SCSS_ACTION_BUTTON = "yw-workflow-designer-add-node-popup-action";
    protected static final String SCSS_DECISION_BUTTON = "yw-workflow-designer-add-node-popup-decision";
    protected static final String SCSS_AND_BUTTON = "yw-workflow-designer-add-node-popup-and";
    public static final String SOCKET_OUT_UPDATE_ACTION = "updateAction";
    public static final String SOCKET_OUT_UPDATE_DECISION = "updateDecision";
    private NotificationService notificationService;
    private WorkflowModelRetriever<ItemModel> workflowModelRetriever;
    private WorkflowDesignerOnAddNodeActionsListener onAddNodeActionsListener;
    private WorkflowDesignerSaveHandler workflowDesignerSaveHandler;
    private WorkflowDesignerRemoveHandler workflowDesignerRemoveHandler;
    private WorkflowDesignerConnectionHandler workflowDesignerConnectionHandler;


    @Override
    public NetworkUpdates onDragEnd(final Node node, final NetworkChartContext context)
    {
        return new NetworkUpdates(List.of(new NetworkUpdate(Action.UPDATE, node)));
    }


    @Override
    public NetworkUpdates onDoubleClick(final Node node, final NetworkChartContext context)
    {
        return handleNodeEdition(node, context);
    }


    @Override
    public NetworkUpdates onEdit(final Node node, final NetworkChartContext context)
    {
        return handleNodeEdition(node, context);
    }


    private NetworkUpdates handleNodeEdition(final Node node, final NetworkChartContext context)
    {
        if(WorkflowDesignerGroup.AND.getValue().equals(node.getGroup()))
        {
            notificationService.notifyUser(NOTIFICATION_AREA_SOURCE, EVENT_TYPE_AND_NODE_EDITION, Level.WARNING);
        }
        else
        {
            notifyAboutNodeEdition(node, context);
        }
        return NetworkUpdates.EMPTY;
    }


    protected void notifyAboutNodeEdition(final Node node, final NetworkChartContext context)
    {
        workflowModelRetriever.retrieve(node, context).ifPresent(model -> {
            final String output = model instanceof WorkflowActionTemplateModel ? SOCKET_OUT_UPDATE_ACTION
                            : SOCKET_OUT_UPDATE_DECISION;
            context.getWim().sendOutput(output,
                            Map.ofEntries(Pair.of(WorkflowDesignerModelKey.KEY_PARENT_OBJECT, model),
                                            Pair.of(WorkflowDesignerModelKey.KEY_NODE, node),
                                            Pair.of(WorkflowDesignerModelKey.KEY_NODES, extractNodes(context)),
                                            Pair.of(WorkflowDesignerModelKey.KEY_UPDATE_ID, !model.getItemModelContext().isNew())));
        });
    }


    private static Set<Node> extractNodes(final NetworkChartContext context)
    {
        return context.getWim().getModel().getValue(NetworkChartController.MODEL_NETWORK_NODES, Set.class);
    }


    @Override
    public NetworkUpdates onAddNodeButtonClick(final ClickOnAddNodeButtonEvent event, final NetworkChartContext context)
    {
        getCoordinates(event).ifPresent(coordinates -> {
            final Menupopup menupopup = new Menupopup();
            UITools.addSClass(menupopup, SCSS_ADD_NODE_POPUP);
            final Menuitem action = new Menuitem(Labels.getLabel(LABEL_ACTION_BUTTON));
            UITools.addSClass(action, SCSS_ACTION_BUTTON);
            menupopup.appendChild(action);
            action.addEventListener(Events.ON_CLICK, ev -> {
                onAddNodeActionsListener.onAddActionNodeButtonClick(ev, context);
                menupopup.close();
            });
            final Menuitem decision = new Menuitem(Labels.getLabel(LABEL_DECISION_BUTTON));
            UITools.addSClass(decision, SCSS_DECISION_BUTTON);
            menupopup.appendChild(decision);
            decision.addEventListener(Events.ON_CLICK, ev -> {
                onAddNodeActionsListener.onAddDecisionNodeButtonClick(ev, context);
                menupopup.close();
            });
            final Menuitem and = new Menuitem(Labels.getLabel(LABEL_AND_BUTTON));
            UITools.addSClass(and, SCSS_AND_BUTTON);
            menupopup.appendChild(and);
            and.addEventListener(Events.ON_CLICK, ev -> {
                onAddNodeActionsListener.onAddAndNodeButtonClick(ev, context);
                menupopup.close();
            });
            event.getTarget().appendChild(menupopup);
            menupopup.open(coordinates.getLeft(), coordinates.getRight());
        });
        return NetworkUpdates.EMPTY;
    }


    private Optional<Pair<String, String>> getCoordinates(final Event event)
    {
        if(event == null || !(event.getData() instanceof AuRequest))
        {
            return Optional.empty();
        }
        final String left = Objects.toString(((AuRequest)event.getData()).getData().get("left"));
        final String right = Objects.toString(((AuRequest)event.getData()).getData().get("bottom"));
        return Optional.of(Pair.of(left, right));
    }


    @Override
    public NetworkUpdates onAdd(final Edge edge, final NetworkChartContext context)
    {
        return workflowDesignerConnectionHandler.addEdge(context, edge);
    }


    @Override
    public NetworkUpdates onRemove(final Nodes nodes, final NetworkChartContext context)
    {
        return workflowDesignerRemoveHandler.remove(nodes, context);
    }


    @Override
    public NetworkUpdates onRemove(final Edges edges, final NetworkChartContext context)
    {
        return workflowDesignerRemoveHandler.remove(edges, context);
    }


    @Override
    public NetworkUpdates onCancel(final NetworkChartContext context)
    {
        context.getWim().sendOutput(VIRTUAL_SOCKET_OUT_CLOSE, null);
        return NetworkUpdates.EMPTY;
    }


    @Override
    public NetworkUpdates onRefresh(final NetworkChartContext context)
    {
        context.getWim().getModel().setValue(WorkflowDesignerNetworkPopulator.MODEL_NEW_WORKFLOW_ITEMS_KEY, new ArrayList<>());
        return NetworkUpdates.EMPTY;
    }


    @Override
    public NetworkUpdates onSave(final NetworkChartContext context)
    {
        return workflowDesignerSaveHandler.save(context);
    }


    @Required
    public void setWorkflowModelRetriever(final WorkflowModelRetriever<ItemModel> workflowModelRetriever)
    {
        this.workflowModelRetriever = workflowModelRetriever;
    }


    @Required
    public void setOnAddNodeActionsListener(final WorkflowDesignerOnAddNodeActionsListener onAddNodeActionsListener)
    {
        this.onAddNodeActionsListener = onAddNodeActionsListener;
    }


    @Required
    public void setNotificationService(final NotificationService notificationService)
    {
        this.notificationService = notificationService;
    }


    @Required
    public void setWorkflowDesignerSaveHandler(final WorkflowDesignerSaveHandler workflowDesignerSaveHandler)
    {
        this.workflowDesignerSaveHandler = workflowDesignerSaveHandler;
    }


    @Required
    public void setWorkflowDesignerRemoveHandler(final WorkflowDesignerRemoveHandler workflowDesignerRemoveHandler)
    {
        this.workflowDesignerRemoveHandler = workflowDesignerRemoveHandler;
    }


    @Required
    public void setWorkflowDesignerConnectionHandler(final WorkflowDesignerConnectionHandler workflowDesignerConnectionHandler)
    {
        this.workflowDesignerConnectionHandler = workflowDesignerConnectionHandler;
    }
}
