package de.hybris.platform.commercewebservices.core.queues.data;

import java.io.Serializable;

public class OrderStatusUpdateElementData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String code;
    private String status;
    private String baseSiteId;


    public void setCode(String code)
    {
        this.code = code;
    }


    public String getCode()
    {
        return this.code;
    }


    public void setStatus(String status)
    {
        this.status = status;
    }


    public String getStatus()
    {
        return this.status;
    }


    public void setBaseSiteId(String baseSiteId)
    {
        this.baseSiteId = baseSiteId;
    }


    public String getBaseSiteId()
    {
        return this.baseSiteId;
    }
}
