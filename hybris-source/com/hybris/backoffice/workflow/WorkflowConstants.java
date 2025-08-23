/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow;

public class WorkflowConstants
{
    public static final String HANDLER_NOTIFICATION_SOURCE = "collaboration-workflow";
    public static final String EVENT_LINK_DEFAULT_DESTINATION = "workflowEA";
    public static final String EVENT_LINK_WORKFLOW_DETAILS_DESTINATION = "workflowDetails";
    public static final String EVENT_REFERENCE_OBJECT_WORKFLOW_KEY = "workflow";
    public static final String EVENT_TYPE_WORKFLOW_STARTED = "WorkflowStarted";
    public static final String EVENT_TYPE_WORKFLOW_CREATED = "WorkflowCreated";
    public static final String EVENT_TYPE_WORKFLOW_DELETED = "WorkflowDeleted";
    public static final String EVENT_TYPE_WORKFLOW_TERMINATED = "WorkflowTerminated";
    public static final String EVENT_TYPE_WORKFLOW_CREATED_AND_STARTED = "WorkflowCreatedAndStarted";
    public static final String EVENT_TYPE_WORKFLOW_ACTION_PERMISSIONS = "WorkflowActionPermissions";
    public static final String EVENT_TYPE_WORKFLOW_ACTIONS_EMPTY = "WorkflowActionsEmpty";
    public static final String EVENT_TYPE_WORKFLOW_WITHOUT_ATTACHMENTS = "WorkflowWithoutAttachments";
    public static final String WIZARD_WORKFLOW_CTX_ATTACHMENTS = "attachments";
    public static final String WIZARD_WORKFLOW_CTX_ATTACHMENT_TYPE = "attachmentType";
    public static final String WIZARD_WORKFLOW_CTX_DESTINATION = "destination";


    private WorkflowConstants()
    {
        throw new AssertionError("Creating instances of this class is prohibited");
    }
}
