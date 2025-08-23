package de.hybris.platform.workflow.exceptions;

public class WorkflowTerminatedException extends RuntimeException
{
    public WorkflowTerminatedException(Throwable nested)
    {
        super(nested.getMessage());
    }


    public WorkflowTerminatedException(String message)
    {
        super(message);
    }
}
