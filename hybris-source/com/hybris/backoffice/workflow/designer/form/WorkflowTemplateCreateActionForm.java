/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.form;

import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.workflow.enums.WorkflowActionType;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Object representing the user declaration of Action given in ConfigurableFlowWizard
 */
public class WorkflowTemplateCreateActionForm extends AbstractWorkflowTemplateCreateForm
{
    private PrincipalModel principalAssigned;
    private WorkflowActionType actionType;
    private WorkflowTemplateModel workflow;
    private Map<Locale, String> description = new HashMap<>();


    public PrincipalModel getPrincipalAssigned()
    {
        return principalAssigned;
    }


    public void setPrincipalAssigned(final PrincipalModel principalAssigned)
    {
        this.principalAssigned = principalAssigned;
    }


    public WorkflowActionType getActionType()
    {
        return actionType == null ? WorkflowActionType.NORMAL : actionType;
    }


    public void setActionType(final WorkflowActionType actionType)
    {
        this.actionType = actionType;
    }


    public WorkflowTemplateModel getWorkflow()
    {
        return workflow;
    }


    public void setWorkflow(final WorkflowTemplateModel workflow)
    {
        this.workflow = workflow;
    }


    public Map<Locale, String> getDescription()
    {
        return description;
    }


    public void setDescription(final Map<Locale, String> description)
    {
        this.description = description;
    }
}
