package de.hybris.platform.processengine.adminapi;

import java.io.Serializable;

public class TriggerEventRequest implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String event;
    private String choice;


    public void setEvent(String event)
    {
        this.event = event;
    }


    public String getEvent()
    {
        return this.event;
    }


    public void setChoice(String choice)
    {
        this.choice = choice;
    }


    public String getChoice()
    {
        return this.choice;
    }
}
