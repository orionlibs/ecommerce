package de.hybris.platform.jdbcwrapper;

import de.hybris.platform.jalo.JaloSystemException;

public class ConnectionLostException extends JaloSystemException
{
    public ConnectionLostException(String message, int vendorCode)
    {
        this(null, message, vendorCode);
    }


    public ConnectionLostException(Throwable throwable, String message, int vendorCode)
    {
        super(throwable, message, vendorCode);
    }
}
