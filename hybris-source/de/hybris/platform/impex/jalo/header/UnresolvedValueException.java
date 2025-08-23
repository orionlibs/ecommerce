package de.hybris.platform.impex.jalo.header;

import de.hybris.platform.impex.jalo.ImpExException;

public class UnresolvedValueException extends ImpExException
{
    public UnresolvedValueException(String msg)
    {
        super(msg);
    }


    public UnresolvedValueException(String qualifier, String cellValue)
    {
        this(null, qualifier, cellValue);
    }


    public UnresolvedValueException(Throwable e, String qualifier, String cellValue)
    {
        super(e, "cannot resolve value '" + cellValue + "' for attribute '" + qualifier + "'" + (
                        (e != null) ? (" because: " + e.getMessage()) : ""), 0);
    }
}
