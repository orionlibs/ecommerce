package de.hybris.platform.servicelayer.impex.impl;

import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.servicelayer.impex.ImpExValidationResult;

public class DefaultImpExValidationResult implements ImpExValidationResult
{
    private final boolean successful;
    private final ImpExException exception;


    public DefaultImpExValidationResult(boolean successful)
    {
        this.successful = successful;
        this.exception = null;
    }


    public DefaultImpExValidationResult(boolean successful, ImpExException exception)
    {
        this.successful = successful;
        this.exception = exception;
    }


    public boolean isSuccessful()
    {
        return this.successful;
    }


    public String getFailureCause()
    {
        if(this.exception == null)
        {
            return null;
        }
        return this.exception.getMessage();
    }
}
