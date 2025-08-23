package de.hybris.platform.servicelayer.event.events;

import java.io.Serializable;

public class AfterItemCreationEvent extends AbstractPersistenceEvent
{
    private String typeCode;


    public AfterItemCreationEvent()
    {
    }


    public AfterItemCreationEvent(Serializable source)
    {
        super(source);
    }


    public void setTypeCode(String typeCode)
    {
        this.typeCode = typeCode;
    }


    public String getTypeCode()
    {
        return this.typeCode;
    }
}
