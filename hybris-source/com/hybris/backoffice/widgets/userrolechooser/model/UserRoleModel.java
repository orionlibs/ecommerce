/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.userrolechooser.model;

public class UserRoleModel
{
    private String code;
    private String name;
    private String description;
    private String thumbnailURL;
    private boolean selected;


    public String getCode()
    {
        return code;
    }


    public void setCode(final String code)
    {
        this.code = code;
    }


    public String getName()
    {
        return name;
    }


    public void setName(final String name)
    {
        this.name = name;
    }


    public String getDescription()
    {
        return description;
    }


    public void setDescription(final String description)
    {
        this.description = description;
    }


    public String getThumbnailURL()
    {
        return thumbnailURL;
    }


    public void setThumbnailURL(final String thumbnailURL)
    {
        this.thumbnailURL = thumbnailURL;
    }


    public boolean isSelected()
    {
        return selected;
    }


    public void setSelected(final boolean selected)
    {
        this.selected = selected;
    }
}
