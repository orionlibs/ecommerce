/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.renderer.actionexecutors;

import com.google.common.collect.Lists;
import com.hybris.backoffice.widgets.notificationarea.NotificationService;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.backoffice.workflow.WorkflowConstants;
import com.hybris.backoffice.workflow.WorkflowEventPublisher;
import com.hybris.backoffice.workflow.WorkflowFacade;
import com.hybris.backoffice.workflow.constants.WorkflowNotificationEventTypes;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacade;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectNotFoundException;
import de.hybris.platform.workflow.model.WorkflowItemAttachmentModel;
import de.hybris.platform.workflow.model.WorkflowModel;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class WorkflowAttachmentDeleteActionExecutor implements Consumer<WorkflowItemAttachmentModel>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(WorkflowAttachmentDeleteActionExecutor.class);
    private ObjectFacade objectFacade;
    private WorkflowEventPublisher workflowEventPublisher;
    private NotificationService notificationService;
    private WorkflowFacade workflowFacade;


    @Override
    public void accept(final WorkflowItemAttachmentModel workflowAttachment)
    {
        try
        {
            final WorkflowModel workflow = workflowAttachment.getWorkflow();
            getWorkflowFacade().removeItems(workflow, Lists.newArrayList(workflowAttachment));
            notifyUser(workflowAttachment, WorkflowNotificationEventTypes.NOTIFICATION_EVENT_WORKFLOW_ATTACHMENT_DELETED,
                            NotificationEvent.Level.SUCCESS);
            getWorkflowEventPublisher().publishWorkflowAttachmentDeletedEvent(workflowAttachment);
            getWorkflowEventPublisher().publishWorkflowUpdatedEvent(getObjectFacade().reload(workflow));
        }
        catch(final ObjectNotFoundException e)
        {
            LOGGER.error(e.getMessage(), e);
            notifyUser(workflowAttachment, WorkflowNotificationEventTypes.NOTIFICATION_EVENT_WORKFLOW_ATTACHMENT_DELETED,
                            NotificationEvent.Level.FAILURE);
        }
    }


    /**
     * @deprecated since 6.7, use the
     *             {@link #NotificationService.notifyUser(String, String, NotificationEvent.Level, Object...) } instead.
     */
    @Deprecated(since = "6.7", forRemoval = true)
    protected void notifyUser(final WorkflowItemAttachmentModel workflowAttachment, final String eventType,
                    final NotificationEvent.Level level)
    {
        getNotificationService().notifyUser(WorkflowConstants.HANDLER_NOTIFICATION_SOURCE, eventType, level,
                        workflowAttachment.getItem());
    }


    public ObjectFacade getObjectFacade()
    {
        return objectFacade;
    }


    @Required
    public void setObjectFacade(final ObjectFacade objectFacade)
    {
        this.objectFacade = objectFacade;
    }


    public WorkflowEventPublisher getWorkflowEventPublisher()
    {
        return workflowEventPublisher;
    }


    @Required
    public void setWorkflowEventPublisher(final WorkflowEventPublisher workflowEventPublisher)
    {
        this.workflowEventPublisher = workflowEventPublisher;
    }


    protected NotificationService getNotificationService()
    {
        return notificationService;
    }


    @Required
    public void setNotificationService(final NotificationService notificationService)
    {
        this.notificationService = notificationService;
    }


    public WorkflowFacade getWorkflowFacade()
    {
        return workflowFacade;
    }


    @Required
    public void setWorkflowFacade(final WorkflowFacade workflowFacade)
    {
        this.workflowFacade = workflowFacade;
    }
}
