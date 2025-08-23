/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.persistence;

/**
 * Exception thrown when couldn't save Workflow Designer's data (the Workflow Template).
 */
public class WorkflowDesignerSavingException extends RuntimeException
{
    public WorkflowDesignerSavingException(final Exception e)
    {
        super(e);
    }


    public WorkflowDesignerSavingException(final String message, final Throwable cause)
    {
        super(message, cause);
    }
}
