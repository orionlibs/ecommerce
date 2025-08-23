package de.hybris.platform.persistence.type.update.misc;

public class UpdateModelException extends Exception
{
    private String source = null;
    private String target = null;


    public UpdateModelException(Throwable throwable)
    {
        super(throwable);
    }


    public UpdateModelException(Throwable throwable, String sourceType, String targetType)
    {
        super(throwable);
        this.source = sourceType;
        this.target = targetType;
    }


    public String getSourceType()
    {
        return this.source;
    }


    public String getTargetType()
    {
        return this.target;
    }
}
