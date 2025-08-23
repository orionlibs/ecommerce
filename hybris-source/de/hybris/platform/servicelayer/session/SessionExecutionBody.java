package de.hybris.platform.servicelayer.session;

public abstract class SessionExecutionBody
{
    public Object execute()
    {
        executeWithoutResult();
        return null;
    }


    public void executeWithoutResult()
    {
    }
}
