package de.hybris.platform.cmsfacades.data;

import java.io.Serializable;

public class PageContentSlotComponentData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String pageId;
    private String componentId;
    private String componentUuid;
    private String slotId;
    private Integer position;


    public void setPageId(String pageId)
    {
        this.pageId = pageId;
    }


    public String getPageId()
    {
        return this.pageId;
    }


    public void setComponentId(String componentId)
    {
        this.componentId = componentId;
    }


    public String getComponentId()
    {
        return this.componentId;
    }


    public void setComponentUuid(String componentUuid)
    {
        this.componentUuid = componentUuid;
    }


    public String getComponentUuid()
    {
        return this.componentUuid;
    }


    public void setSlotId(String slotId)
    {
        this.slotId = slotId;
    }


    public String getSlotId()
    {
        return this.slotId;
    }


    public void setPosition(Integer position)
    {
        this.position = position;
    }


    public Integer getPosition()
    {
        return this.position;
    }
}
