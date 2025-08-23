package de.hybris.platform.processengine.definition;

import java.net.URL;

public class InvalidProcessDefinitionException extends RuntimeException
{
    private URL url;


    InvalidProcessDefinitionException(String msg)
    {
        this(msg, null);
    }


    InvalidProcessDefinitionException(String msg, Throwable cause)
    {
        this(null, msg, cause);
    }


    InvalidProcessDefinitionException(URL url, Throwable cause)
    {
        this(url, "Process definition '" + url + "' is invalid.", cause);
    }


    InvalidProcessDefinitionException(URL url, String msg, Throwable cause)
    {
        super(msg, cause);
        this.url = url;
    }


    public URL getURL()
    {
        return this.url;
    }


    protected void setURL(URL url)
    {
        this.url = url;
    }
}
