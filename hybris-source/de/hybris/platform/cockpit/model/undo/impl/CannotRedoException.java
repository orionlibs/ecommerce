package de.hybris.platform.cockpit.model.undo.impl;

public class CannotRedoException extends Exception
{
    public CannotRedoException(String message)
    {
        super(message);
    }


    public CannotRedoException(String message, Throwable throwable)
    {
        super(message, throwable);
    }


    public CannotRedoException(Throwable throwable)
    {
        super(throwable);
    }
}
