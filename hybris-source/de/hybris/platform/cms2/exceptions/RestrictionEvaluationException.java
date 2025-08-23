package de.hybris.platform.cms2.exceptions;

import de.hybris.platform.servicelayer.exceptions.BusinessException;

public class RestrictionEvaluationException extends BusinessException
{
    private static final long serialVersionUID = -1451559610063495933L;


    public RestrictionEvaluationException(String message, Throwable cause)
    {
        super(message, cause);
    }


    public RestrictionEvaluationException(String message)
    {
        super(message);
    }


    public RestrictionEvaluationException(Throwable cause)
    {
        super(cause);
    }
}
