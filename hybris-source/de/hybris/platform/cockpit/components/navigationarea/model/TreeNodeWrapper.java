package de.hybris.platform.cockpit.components.navigationarea.model;

import de.hybris.platform.cockpit.model.meta.TypedObject;

public class TreeNodeWrapper
{
    private final TreeNodeWrapper parent;
    private final TypedObject item;
    private Boolean leaf;


    public TreeNodeWrapper()
    {
        this.parent = null;
        this.item = null;
        this.leaf = Boolean.FALSE;
    }


    public TreeNodeWrapper(TypedObject item, TreeNodeWrapper parent)
    {
        this.item = item;
        this.parent = parent;
    }


    public TypedObject getItem()
    {
        return this.item;
    }


    public boolean hasLeafInformation()
    {
        return (this.leaf != null);
    }


    public void setLeaf(boolean leaf)
    {
        this.leaf = Boolean.valueOf(leaf);
    }


    public TreeNodeWrapper getParent()
    {
        return this.parent;
    }


    public boolean isLeaf()
    {
        if(this.leaf == null)
        {
            return false;
        }
        return this.leaf.booleanValue();
    }
}
