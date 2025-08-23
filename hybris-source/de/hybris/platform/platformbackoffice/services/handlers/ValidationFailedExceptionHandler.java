package de.hybris.platform.platformbackoffice.services.handlers;

import de.hybris.platform.validation.exceptions.HybrisConstraintViolation;
import de.hybris.platform.validation.exceptions.ValidationViolationException;
import java.util.Set;

public class ValidationFailedExceptionHandler extends AbstractExceptionTranslationHandler
{
    protected Class<? extends Throwable> getExceptionClass()
    {
        return (Class)ValidationViolationException.class;
    }


    public String toString(Throwable throwable)
    {
        if(throwable instanceof ValidationViolationException)
        {
            return getLocalizedMessage((ValidationViolationException)throwable);
        }
        return (throwable.getCause() == null) ? null : toString(throwable.getCause());
    }


    private String getLocalizedMessage(ValidationViolationException e)
    {
        Set<HybrisConstraintViolation> hybrisConstraintViolationSet = e.getHybrisConstraintViolations();
        StringBuilder localizedMessage = new StringBuilder();
        for(HybrisConstraintViolation hybrisConstraintViolation : hybrisConstraintViolationSet)
        {
            localizedMessage.append(hybrisConstraintViolation.getLocalizedMessage()).append(" ");
        }
        return localizedMessage.toString();
    }
}
