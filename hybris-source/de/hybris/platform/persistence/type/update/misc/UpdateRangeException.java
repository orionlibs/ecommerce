package de.hybris.platform.persistence.type.update.misc;

public class UpdateRangeException extends UpdateValueException
{
    private int range = 0;


    public UpdateRangeException(Throwable throwable, Object value)
    {
        super(throwable, value);
    }


    public UpdateRangeException(Throwable throwable, Object value, int range)
    {
        this(throwable, value);
        this.range = range;
    }


    public int getRange()
    {
        return this.range;
    }
}
