package de.hybris.platform.cmsfacades.data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class PageContentSlotData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String pageId;
    private String slotId;
    private String slotUuid;
    private String position;
    private boolean slotShared;
    private SlotStatus slotStatus;
    private String name;
    private String catalogVersion;
    private List<AbstractCMSComponentData> components;
    private Map<String, Object> otherProperties;


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


    public void setSlotUuid(String slotUuid)
    {
        this.slotUuid = slotUuid;
    }


    public String getSlotUuid()
    {
        return this.slotUuid;
    }


    public void setPosition(String position)
    {
        this.position = position;
    }


    public String getPosition()
    {
        return this.position;
    }


    public void setSlotShared(boolean slotShared)
    {
        this.slotShared = slotShared;
    }


    public boolean isSlotShared()
    {
        return this.slotShared;
    }


    public void setSlotStatus(SlotStatus slotStatus)
    {
        this.slotStatus = slotStatus;
    }


    public SlotStatus getSlotStatus()
    {
        return this.slotStatus;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return this.name;
    }


    public void setCatalogVersion(String catalogVersion)
    {
        this.catalogVersion = catalogVersion;
    }


    public String getCatalogVersion()
    {
        return this.catalogVersion;
    }


    public void setComponents(List<AbstractCMSComponentData> components)
    {
        this.components = components;
    }


    public List<AbstractCMSComponentData> getComponents()
    {
        return this.components;
    }


    public void setOtherProperties(Map<String, Object> otherProperties)
    {
        this.otherProperties = otherProperties;
    }


    public Map<String, Object> getOtherProperties()
    {
        return this.otherProperties;
    }
}
