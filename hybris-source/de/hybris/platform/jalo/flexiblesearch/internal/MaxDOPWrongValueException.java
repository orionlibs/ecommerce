package de.hybris.platform.jalo.flexiblesearch.internal;

import de.hybris.platform.jalo.JaloSystemException;

public class MaxDOPWrongValueException extends JaloSystemException
{
    MaxDOPWrongValueException(String message)
    {
        super(message);
    }


    MaxDOPWrongValueException(Throwable cause, String message, int errorCode)
    {
        super(cause, message, errorCode);
    }
}
