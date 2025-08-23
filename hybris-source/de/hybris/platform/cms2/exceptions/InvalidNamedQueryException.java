package de.hybris.platform.cms2.exceptions;

public class InvalidNamedQueryException extends RuntimeException
{
    private static final long serialVersionUID = -6615030091084809142L;
    private final String queryName;


    public InvalidNamedQueryException(String queryName)
    {
        super("Named Query is not present in the configuration.");
        this.queryName = queryName;
    }


    public String getQueryName()
    {
        return this.queryName;
    }
}
