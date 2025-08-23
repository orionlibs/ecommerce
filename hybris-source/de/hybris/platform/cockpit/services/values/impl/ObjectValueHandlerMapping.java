package de.hybris.platform.cockpit.services.values.impl;

import de.hybris.platform.cockpit.services.values.ObjectValueHandler;

public class ObjectValueHandlerMapping
{
    private String typeCode;
    private ObjectValueHandler valueHandler;
    private int order;


    public void setTypeCode(String typeCode)
    {
        this.typeCode = typeCode;
    }


    public String getTypeCode()
    {
        return this.typeCode;
    }


    public void setValueHandler(ObjectValueHandler valueHandler)
    {
        this.valueHandler = valueHandler;
    }


    public ObjectValueHandler getValueHandler()
    {
        return this.valueHandler;
    }


    public void setOrder(int order)
    {
        this.order = order;
    }


    public int getOrder()
    {
        return this.order;
    }
}
