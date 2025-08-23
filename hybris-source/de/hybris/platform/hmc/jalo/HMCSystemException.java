package de.hybris.platform.hmc.jalo;

import de.hybris.platform.jalo.JaloSystemException;

public class HMCSystemException extends JaloSystemException
{
    public HMCSystemException(Exception nested)
    {
        super(nested, nested.getLocalizedMessage(), 0);
    }


    public HMCSystemException(Exception nested, String message)
    {
        super(nested, message, 0);
    }
}
