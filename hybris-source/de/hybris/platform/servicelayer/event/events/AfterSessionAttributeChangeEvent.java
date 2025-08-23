package de.hybris.platform.servicelayer.event.events;

import java.io.Serializable;

public class AfterSessionAttributeChangeEvent extends AbstractEvent
{
    private String attributeName;
    private Object value;


    public AfterSessionAttributeChangeEvent()
    {
    }


    public AfterSessionAttributeChangeEvent(Serializable source)
    {
        super(source);
    }


    public void setAttributeName(String attributeName)
    {
        this.attributeName = attributeName;
    }


    public String getAttributeName()
    {
        return this.attributeName;
    }


    public void setValue(Object value)
    {
        this.value = value;
    }


    public Object getValue()
    {
        return this.value;
    }
}
