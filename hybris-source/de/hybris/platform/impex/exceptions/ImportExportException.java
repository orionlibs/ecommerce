package de.hybris.platform.impex.exceptions;

import de.hybris.platform.servicelayer.exceptions.BusinessException;

public abstract class ImportExportException extends BusinessException
{
    protected ImportExportException(String message, Throwable exeception)
    {
        super(message, exeception);
    }


    protected ImportExportException(Throwable cause)
    {
        super(cause);
    }


    protected ImportExportException(String message)
    {
        super(message);
    }
}
