/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.form;

import de.hybris.platform.workflow.model.WorkflowTemplateModel;

/**
 * Object representing the user declaration of Decision given in ConfigurableFlowWizard
 */
public class WorkflowTemplateCreateDecisionForm extends AbstractWorkflowTemplateCreateForm
{
    private WorkflowTemplateModel workflow;


    public WorkflowTemplateModel getWorkflow()
    {
        return workflow;
    }


    public void setWorkflow(final WorkflowTemplateModel workflow)
    {
        this.workflow = workflow;
    }
}
