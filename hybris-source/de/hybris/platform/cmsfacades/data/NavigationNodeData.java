package de.hybris.platform.cmsfacades.data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class NavigationNodeData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String uid;
    private String uuid;
    private String itemType;
    private String parentUid;
    private String name;
    private Map<String, String> title;
    private Boolean hasChildren;
    private Integer position;
    private List<NavigationEntryData> entries;
    private List<NavigationNodeData> children;
    private String localizedTitle;


    public void setUid(String uid)
    {
        this.uid = uid;
    }


    public String getUid()
    {
        return this.uid;
    }


    public void setUuid(String uuid)
    {
        this.uuid = uuid;
    }


    public String getUuid()
    {
        return this.uuid;
    }


    public void setItemType(String itemType)
    {
        this.itemType = itemType;
    }


    public String getItemType()
    {
        return this.itemType;
    }


    public void setParentUid(String parentUid)
    {
        this.parentUid = parentUid;
    }


    public String getParentUid()
    {
        return this.parentUid;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return this.name;
    }


    public void setTitle(Map<String, String> title)
    {
        this.title = title;
    }


    public Map<String, String> getTitle()
    {
        return this.title;
    }


    public void setHasChildren(Boolean hasChildren)
    {
        this.hasChildren = hasChildren;
    }


    public Boolean getHasChildren()
    {
        return this.hasChildren;
    }


    public void setPosition(Integer position)
    {
        this.position = position;
    }


    public Integer getPosition()
    {
        return this.position;
    }


    public void setEntries(List<NavigationEntryData> entries)
    {
        this.entries = entries;
    }


    public List<NavigationEntryData> getEntries()
    {
        return this.entries;
    }


    public void setChildren(List<NavigationNodeData> children)
    {
        this.children = children;
    }


    public List<NavigationNodeData> getChildren()
    {
        return this.children;
    }


    public void setLocalizedTitle(String localizedTitle)
    {
        this.localizedTitle = localizedTitle;
    }


    public String getLocalizedTitle()
    {
        return this.localizedTitle;
    }
}
