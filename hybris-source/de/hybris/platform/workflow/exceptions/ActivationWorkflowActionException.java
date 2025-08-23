package de.hybris.platform.workflow.exceptions;

public class ActivationWorkflowActionException extends RuntimeException
{
    public ActivationWorkflowActionException(Throwable nested)
    {
        super(nested.getMessage());
    }


    public ActivationWorkflowActionException(String message)
    {
        super(message);
    }
}
