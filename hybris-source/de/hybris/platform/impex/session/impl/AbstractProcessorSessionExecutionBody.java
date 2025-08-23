package de.hybris.platform.impex.session.impl;

import de.hybris.platform.impex.session.ProcessorSessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;

public abstract class AbstractProcessorSessionExecutionBody<T, X extends Exception> extends SessionExecutionBody implements ProcessorSessionExecutionBody
{
    private X exception;


    protected abstract SessionService getSessionService();


    public final T execute()
    {
        try
        {
            prepareSession();
            return doProcess();
        }
        catch(Exception e)
        {
            this.exception = (X)e;
            return null;
        }
    }


    protected void prepareSession()
    {
        getSessionService().setAttribute("language", null);
        getSessionService().setAttribute("disableRestrictions", Boolean.TRUE);
        getSessionService().setAttribute("disableRestrictionGroupInheritance", Boolean.TRUE);
        getSessionService().setAttribute("use.fast.algorithms", Boolean.TRUE);
        getSessionService().setAttribute("import.mode", Boolean.TRUE);
        getSessionService().setAttribute("disable.attribute.check", Boolean.TRUE);
    }


    public final X getException()
    {
        return this.exception;
    }


    protected abstract T doProcess() throws X;
}
