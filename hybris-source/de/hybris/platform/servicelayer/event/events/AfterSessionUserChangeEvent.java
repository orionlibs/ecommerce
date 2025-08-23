package de.hybris.platform.servicelayer.event.events;

import java.io.Serializable;

public class AfterSessionUserChangeEvent extends AbstractEvent
{
    private String previousUserUID;


    public AfterSessionUserChangeEvent()
    {
    }


    public AfterSessionUserChangeEvent(Serializable source)
    {
        super(source);
    }


    public void setPreviousUserUID(String previousUserUID)
    {
        this.previousUserUID = previousUserUID;
    }


    public String getPreviousUserUID()
    {
        return this.previousUserUID;
    }
}
