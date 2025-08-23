package de.hybris.platform.servicelayer.event.events;

import java.io.Serializable;

public class InvalidateModelConverterRegistryEvent extends AbstractEvent
{
    private String composedTypeCode;
    private Class composedClass;
    private boolean refresh;


    public InvalidateModelConverterRegistryEvent()
    {
    }


    public InvalidateModelConverterRegistryEvent(Serializable source)
    {
        super(source);
    }


    public void setComposedTypeCode(String composedTypeCode)
    {
        this.composedTypeCode = composedTypeCode;
    }


    public String getComposedTypeCode()
    {
        return this.composedTypeCode;
    }


    public void setComposedClass(Class composedClass)
    {
        this.composedClass = composedClass;
    }


    public Class getComposedClass()
    {
        return this.composedClass;
    }


    public void setRefresh(boolean refresh)
    {
        this.refresh = refresh;
    }


    public boolean isRefresh()
    {
        return this.refresh;
    }
}
