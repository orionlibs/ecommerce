package de.hybris.platform.cmsfacades.data;

import java.io.Serializable;
import java.util.List;

public class PageContentSlotContainerData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String pageId;
    private String slotId;
    private String containerId;
    private String containerType;
    private List<String> components;


    public void setPageId(String pageId)
    {
        this.pageId = pageId;
    }


    public String getPageId()
    {
        return this.pageId;
    }


    public void setSlotId(String slotId)
    {
        this.slotId = slotId;
    }


    public String getSlotId()
    {
        return this.slotId;
    }


    public void setContainerId(String containerId)
    {
        this.containerId = containerId;
    }


    public String getContainerId()
    {
        return this.containerId;
    }


    public void setContainerType(String containerType)
    {
        this.containerType = containerType;
    }


    public String getContainerType()
    {
        return this.containerType;
    }


    public void setComponents(List<String> components)
    {
        this.components = components;
    }


    public List<String> getComponents()
    {
        return this.components;
    }
}
