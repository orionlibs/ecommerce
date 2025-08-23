/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.handler.connection;

import com.hybris.backoffice.widgets.networkchart.context.NetworkChartContext;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent.Level;
import com.hybris.backoffice.workflow.designer.handler.connection.validator.ValidationContext;
import com.hybris.backoffice.workflow.designer.handler.connection.validator.Violation;
import com.hybris.backoffice.workflow.designer.handler.connection.validator.WorkflowConnectionValidationResult;
import com.hybris.backoffice.workflow.designer.handler.connection.validator.WorkflowConnectionValidator;
import com.hybris.cockpitng.components.visjs.network.data.Edge;
import com.hybris.cockpitng.components.visjs.network.response.Action;
import com.hybris.cockpitng.components.visjs.network.response.NetworkUpdate;
import com.hybris.cockpitng.components.visjs.network.response.NetworkUpdates;
import com.hybris.cockpitng.util.notifications.NotificationService;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

/**
 * Default implementation of adding edge in workflow designer
 */
public class DefaultWorkflowDesignerConnectionHandler implements WorkflowDesignerConnectionHandler
{
    protected static final String NOTIFICATION_AREA_SOURCE = "workflowDesigner";
    private static final Logger LOG = LoggerFactory.getLogger(DefaultWorkflowDesignerConnectionHandler.class);
    private WorkflowConnectionValidator workflowConnectionValidator;
    private NotificationService notificationService;


    @Override
    public NetworkUpdates addEdge(final NetworkChartContext context, final Edge edge)
    {
        final WorkflowConnectionValidationResult validationResult = getWorkflowConnectionValidator()
                        .validate(ValidationContext.ofContextAndEdge(context, edge));
        if(validationResult.isFailed())
        {
            LOG.debug("Prevented edge creation because of violations: {}", validationResult.getViolations());
            notifyAboutViolationsDetected(validationResult.getViolations());
            return NetworkUpdates.EMPTY;
        }
        final Edge newlyCreatedEdge = new Edge.Builder(edge, edge.getFromNode(), edge.getToNode()).build();
        return new NetworkUpdates(new NetworkUpdate(Action.ADD, newlyCreatedEdge));
    }


    protected void notifyAboutViolationsDetected(final Collection<Violation> violations)
    {
        for(final Violation violation : violations)
        {
            final String eventCode = violation.getCode();
            final Object[] params = violation.getParams().toArray(Object[]::new);
            notifyAboutFailure(eventCode, params);
        }
    }


    protected void notifyAboutFailure(final String eventType, final Object... objects)
    {
        getNotificationService().notifyUser(NOTIFICATION_AREA_SOURCE, eventType, Level.WARNING, objects);
    }


    public WorkflowConnectionValidator getWorkflowConnectionValidator()
    {
        return workflowConnectionValidator;
    }


    @Required
    public void setWorkflowConnectionValidator(final WorkflowConnectionValidator workflowConnectionValidator)
    {
        this.workflowConnectionValidator = workflowConnectionValidator;
    }


    public NotificationService getNotificationService()
    {
        return notificationService;
    }


    @Required
    public void setNotificationService(final NotificationService notificationService)
    {
        this.notificationService = notificationService;
    }
}
