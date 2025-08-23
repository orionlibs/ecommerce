package de.hybris.platform.persistence.type.update.misc;

public class UpdateValueException extends UpdateModelException
{
    private Object value = null;


    public UpdateValueException(Throwable throwable, Object value)
    {
        super(throwable);
        this.value = value;
    }


    public Object getValue()
    {
        return this.value;
    }
}
