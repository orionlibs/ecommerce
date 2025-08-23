package de.hybris.platform.cockpit.model.undo.impl;

public class CannotUndoException extends Exception
{
    public CannotUndoException(String message)
    {
        super(message);
    }


    public CannotUndoException(String message, Throwable throwable)
    {
        super(message, throwable);
    }


    public CannotUndoException(Throwable throwable)
    {
        super(throwable);
    }
}
