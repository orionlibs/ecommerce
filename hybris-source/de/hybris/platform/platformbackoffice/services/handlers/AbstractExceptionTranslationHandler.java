package de.hybris.platform.platformbackoffice.services.handlers;

import com.hybris.cockpitng.service.ExceptionTranslationHandler;
import org.zkoss.util.resource.Labels;

public abstract class AbstractExceptionTranslationHandler implements ExceptionTranslationHandler
{
    public boolean canHandle(Throwable throwable)
    {
        if(getExceptionClass().isInstance(throwable))
        {
            return true;
        }
        return (throwable.getCause() != null && canHandle(throwable.getCause()));
    }


    protected String getLocalizedMessage(String key, Object... arguments)
    {
        return Labels.getLabel(key, arguments);
    }


    protected abstract Class<? extends Throwable> getExceptionClass();
}
