package de.hybris.platform.ruleengine.concurrency;

public class GuardStatus
{
    private Type type;
    private String message;


    private GuardStatus(Type type)
    {
        this.type = type;
    }


    public static GuardStatus of(Type type)
    {
        return new GuardStatus(type);
    }


    public Type getType()
    {
        return this.type;
    }


    public void setType(Type type)
    {
        this.type = type;
    }


    public String getMessage()
    {
        return this.message;
    }


    public void setMessage(String message)
    {
        this.message = message;
    }
}
