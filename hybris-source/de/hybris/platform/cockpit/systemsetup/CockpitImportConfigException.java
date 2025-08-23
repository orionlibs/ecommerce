package de.hybris.platform.cockpit.systemsetup;

public class CockpitImportConfigException extends RuntimeException
{
    public CockpitImportConfigException()
    {
    }


    public CockpitImportConfigException(String message)
    {
        super(message);
    }


    public CockpitImportConfigException(Throwable cause)
    {
        this(null, cause);
    }


    public CockpitImportConfigException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
