/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.actionbar;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a action node with name, description and icon.
 */
public class DefaultActionDefinition implements ActionDefinition
{
    private final String id;
    private String name;
    private String nameLocKey;
    private String group;
    private String description;
    private String descriptionLocKey;
    private String iconUri;
    private String iconUriLocKey;


    @JsonCreator
    public DefaultActionDefinition(@JsonProperty("id") final String id)
    {
        this.id = id;
    }


    public String getName()
    {
        return name;
    }


    public void setName(final String name)
    {
        this.name = name;
    }


    public String getNameLocKey()
    {
        return nameLocKey;
    }


    public void setNameLocKey(final String nameLocKey)
    {
        this.nameLocKey = nameLocKey;
    }


    public String getDescription()
    {
        return description;
    }


    public void setDescription(final String description)
    {
        this.description = description;
    }


    public String getDescriptionLocKey()
    {
        return descriptionLocKey;
    }


    public void setDescriptionLocKey(final String descriptionLocKey)
    {
        this.descriptionLocKey = descriptionLocKey;
    }


    public String getIconUri()
    {
        return iconUri;
    }


    public void setIconUri(final String iconUri)
    {
        this.iconUri = iconUri;
    }


    public String getIconUriLocKey()
    {
        return iconUriLocKey;
    }


    public void setIconUriLocKey(final String iconUriLocKey)
    {
        this.iconUriLocKey = iconUriLocKey;
    }


    public String getId()
    {
        return id;
    }


    @Override
    public String getGroup()
    {
        return group;
    }


    public void setGroup(final String group)
    {
        this.group = group;
    }


    @Override
    public boolean equals(final Object obj)
    {
        if(this == obj)
        {
            return true;
        }
        if(obj == null)
        {
            return false;
        }
        if(this.getClass() != obj.getClass())
        {
            return false;
        }
        final DefaultActionDefinition other = (DefaultActionDefinition)obj;
        if(this.id == null)
        {
            if(other.id != null)
            {
                return false;
            }
        }
        else if(!this.id.equals(other.id))
        {
            return false;
        }
        return true;
    }


    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        return result;
    }


    @Override
    public String toString()
    {
        return this.getName();
    }
}
