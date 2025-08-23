package de.hybris.platform.workflow.exceptions;

public class AutomatedWorkflowActionException extends RuntimeException
{
    public AutomatedWorkflowActionException(Throwable nested)
    {
        super(nested.getMessage());
    }


    public AutomatedWorkflowActionException(String message)
    {
        super(message);
    }
}
