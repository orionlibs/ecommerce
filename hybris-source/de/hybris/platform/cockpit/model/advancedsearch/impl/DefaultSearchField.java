package de.hybris.platform.cockpit.model.advancedsearch.impl;

import de.hybris.platform.cockpit.model.advancedsearch.SearchField;
import de.hybris.platform.cockpit.model.advancedsearch.SearchFieldGroup;

public class DefaultSearchField implements SearchField
{
    private String name = null;
    private String label = null;
    private boolean visible = false;
    private boolean sortDisabled = true;
    private final boolean sortable;
    private SearchFieldGroup group = null;


    public DefaultSearchField(String name, boolean sortable)
    {
        this.name = name;
        this.sortable = sortable;
    }


    public String getName()
    {
        return this.name;
    }


    public void setLabel(String label)
    {
        this.label = label;
    }


    public String getLabel()
    {
        return this.label;
    }


    public boolean isSortable()
    {
        return (this.sortable && !this.sortDisabled);
    }


    public boolean isVisible()
    {
        return this.visible;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public void setVisible(boolean visible)
    {
        this.visible = visible;
    }


    public void setSortDisabled(boolean sortDisabled)
    {
        this.sortDisabled = sortDisabled;
    }


    public SearchFieldGroup getGroup()
    {
        return this.group;
    }


    public void setGroup(SearchFieldGroup group)
    {
        this.group = group;
    }


    public int hashCode()
    {
        return 1 + ((this.name == null) ? 0 : this.name.hashCode());
    }


    public boolean equals(Object obj)
    {
        if(this == obj)
        {
            return true;
        }
        if(obj == null)
        {
            return false;
        }
        if(!(obj instanceof SearchField))
        {
            return false;
        }
        SearchField other = (SearchField)obj;
        if(this.name == null)
        {
            if(other.getName() != null)
            {
                return false;
            }
        }
        else if(!this.name.equals(other.getName()))
        {
            return false;
        }
        return true;
    }
}
