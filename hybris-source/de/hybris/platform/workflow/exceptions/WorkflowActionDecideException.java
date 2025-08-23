package de.hybris.platform.workflow.exceptions;

public class WorkflowActionDecideException extends RuntimeException
{
    public WorkflowActionDecideException(Throwable nested)
    {
        super(nested.getMessage());
    }


    public WorkflowActionDecideException(String message)
    {
        super(message);
    }
}
