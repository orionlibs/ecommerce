package de.hybris.platform.processengine.adminapi;

import java.io.Serializable;

public class TriggerEventResponse implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String message;


    public void setMessage(String message)
    {
        this.message = message;
    }


    public String getMessage()
    {
        return this.message;
    }
}
