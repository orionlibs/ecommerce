package de.hybris.platform.cockpit.comments.exceptions;

public class UnknownCommentActionExecutorException extends Exception
{
    public UnknownCommentActionExecutorException()
    {
    }


    public UnknownCommentActionExecutorException(String mode)
    {
        super("Comment Layer Action Executor for mode " + mode + " could not be found");
    }
}
