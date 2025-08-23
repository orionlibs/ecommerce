package de.hybris.platform.cockpit.model.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class PropertyGroupConfiguration
{
    private final List<PropertyGroup> groups = new ArrayList<>();


    public List<PropertyGroup> getGroups()
    {
        return this.groups.isEmpty() ? Collections.EMPTY_LIST : Collections.<PropertyGroup>unmodifiableList(this.groups);
    }


    public List<PropertyGroup> getVisibleGroups()
    {
        List<PropertyGroup> ret = Collections.EMPTY_LIST;
        if(!this.groups.isEmpty())
        {
            ret = new ArrayList<>(this.groups.size());
            for(PropertyGroup g : this.groups)
            {
                if(g.isVisible())
                {
                    ret.add(g);
                }
            }
        }
        return ret;
    }


    public List<PropertyGroup> getHiddenGroups()
    {
        List<PropertyGroup> ret = Collections.EMPTY_LIST;
        if(!this.groups.isEmpty())
        {
            ret = new ArrayList<>(this.groups.size());
            for(PropertyGroup g : this.groups)
            {
                if(!g.isVisible())
                {
                    ret.add(g);
                }
            }
        }
        return ret;
    }


    public void removeGroup(PropertyGroup group)
    {
        this.groups.remove(group);
    }


    public void addGroup(int pos, PropertyGroup group)
    {
        this.groups.add(pos, group);
    }


    public PropertyGroup createGroup(int pos, String qualifier)
    {
        PropertyGroup ret = new PropertyGroup(this, qualifier);
        if(pos > -1)
        {
            this.groups.add(pos, ret);
        }
        else
        {
            this.groups.add(ret);
        }
        return ret;
    }


    public void moveGroup(int toPos, PropertyGroup grp)
    {
        this.groups.remove(grp);
        this.groups.add(toPos, grp);
    }
}
