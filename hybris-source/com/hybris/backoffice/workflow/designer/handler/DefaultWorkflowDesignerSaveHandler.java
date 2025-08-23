/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.handler;

import com.hybris.backoffice.widgets.networkchart.context.NetworkChartContext;
import com.hybris.backoffice.workflow.designer.persistence.WorkflowDesignerPersistenceService;
import com.hybris.backoffice.workflow.designer.persistence.WorkflowDesignerSavingException;
import com.hybris.backoffice.workflow.designer.services.WorkflowModelFinder;
import com.hybris.cockpitng.components.visjs.network.response.NetworkUpdates;
import com.hybris.cockpitng.util.notifications.NotificationService;
import com.hybris.cockpitng.util.notifications.event.NotificationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

/**
 * Default implementation of save - delegates to @{@link WorkflowDesignerPersistenceService}
 */
public class DefaultWorkflowDesignerSaveHandler implements WorkflowDesignerSaveHandler
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultWorkflowDesignerSaveHandler.class);
    public static final String NOTIFICATION_AREA_SOURCE = "workflowDesigner";
    protected static final String EVENT_TYPE_CANNOT_SAVE = "cannotSave";
    private static final String EVENT_TYPE_SAVE_SUCCESS = "saveSuccess";
    public static final String EXISTING_DECISIONS = "existingDecisions";
    private NotificationService notificationService;
    private WorkflowDesignerPersistenceService workflowTemplatePersistenceService;
    private WorkflowModelFinder workflowModelFinder;


    @Override
    public NetworkUpdates save(final NetworkChartContext context)
    {
        try
        {
            context.getWim().getModel().setValue(EXISTING_DECISIONS,
                            workflowModelFinder.findWorkflowDecisionsFromWorkflowTemplateModel(context));
            workflowTemplatePersistenceService.persist(context);
            notificationService.notifyUser(NOTIFICATION_AREA_SOURCE, EVENT_TYPE_SAVE_SUCCESS, NotificationEvent.Level.SUCCESS);
        }
        catch(final WorkflowDesignerSavingException e)
        {
            final String workflowCode = workflowModelFinder.findWorkflowTemplate(context).getCode();
            LOG.error(String.format("Cannot save workflow with code %s", workflowCode), e);
            notificationService.notifyUser(NOTIFICATION_AREA_SOURCE, EVENT_TYPE_CANNOT_SAVE, NotificationEvent.Level.FAILURE);
        }
        finally
        {
            context.getWim().getModel().remove(EXISTING_DECISIONS);
        }
        return NetworkUpdates.EMPTY;
    }


    @Required
    public void setWorkflowTemplatePersistenceService(final WorkflowDesignerPersistenceService workflowTemplatePersistenceService)
    {
        this.workflowTemplatePersistenceService = workflowTemplatePersistenceService;
    }


    @Required
    public void setNotificationService(final NotificationService notificationService)
    {
        this.notificationService = notificationService;
    }


    @Required
    public void setWorkflowModelFinder(final WorkflowModelFinder workflowModelFinder)
    {
        this.workflowModelFinder = workflowModelFinder;
    }
}
