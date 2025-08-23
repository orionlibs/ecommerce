package de.hybris.platform.b2bcommercefacades.company.data;

import java.io.Serializable;
import java.util.List;

public class B2BUnitNodeData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String id;
    private String name;
    private String parent;
    private boolean active;
    private List<B2BUnitNodeData> children;


    public void setId(String id)
    {
        this.id = id;
    }


    public String getId()
    {
        return this.id;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return this.name;
    }


    public void setParent(String parent)
    {
        this.parent = parent;
    }


    public String getParent()
    {
        return this.parent;
    }


    public void setActive(boolean active)
    {
        this.active = active;
    }


    public boolean isActive()
    {
        return this.active;
    }


    public void setChildren(List<B2BUnitNodeData> children)
    {
        this.children = children;
    }


    public List<B2BUnitNodeData> getChildren()
    {
        return this.children;
    }
}
