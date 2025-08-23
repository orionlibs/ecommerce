package de.hybris.platform.impex.session.impl;

import de.hybris.platform.core.Constants;
import de.hybris.platform.impex.session.ProcessorSessionExecutionBody;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;

public abstract class AbstractInsertProcessorSessionExecutionBody<T, X extends SystemException> extends SessionExecutionBody implements ProcessorSessionExecutionBody
{
    private X exception;


    public final X getException()
    {
        return this.exception;
    }


    public final T execute()
    {
        try
        {
            prepareSession();
            return doProcess();
        }
        catch(SystemException e)
        {
            this.exception = (X)e;
            return null;
        }
    }


    protected void prepareSession()
    {
        getSessionService().setAttribute("dont.change.existing.links", Boolean.TRUE);
        getSessionService().setAttribute(Constants.DISABLE_CYCLIC_CHECKS, Boolean.TRUE);
    }


    protected abstract SessionService getSessionService();


    protected abstract T doProcess() throws X;
}
