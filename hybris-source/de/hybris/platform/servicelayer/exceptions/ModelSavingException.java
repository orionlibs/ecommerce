package de.hybris.platform.servicelayer.exceptions;

public class ModelSavingException extends SystemException
{
    private ModelSavingException next;


    public ModelSavingException(String message, Throwable nested)
    {
        super(message, nested);
    }


    public ModelSavingException(String message)
    {
        super(message);
    }


    public ModelSavingException getNextException()
    {
        return this.next;
    }


    public void setNextException(ModelSavingException exception)
    {
        ModelSavingException current = this;
        while(current.next != null)
        {
            current = current.next;
        }
        current.next = exception;
    }
}
