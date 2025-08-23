/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.user.impl;

import java.io.Serializable;
import java.util.List;

public class AuthorityGroup implements Serializable
{
    private static final long serialVersionUID = -3443042035579018159L;
    private String code;
    private String name;
    private String description;
    private String thumbnailURL;
    private List<String> authorities;


    /**
     * @return the code
     */
    public String getCode()
    {
        return code;
    }


    /**
     * @param code the code to set
     */
    public void setCode(final String code)
    {
        this.code = code;
    }


    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }


    /**
     * @param name the name to set
     */
    public void setName(final String name)
    {
        this.name = name;
    }


    /**
     * @return the description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * @param description the description to set
     */
    public void setDescription(final String description)
    {
        this.description = description;
    }


    /**
     * @return the thumbnailURL
     */
    public String getThumbnailURL()
    {
        return thumbnailURL;
    }


    /**
     * @param thumbnailURL the thumbnailURL to set
     */
    public void setThumbnailURL(final String thumbnailURL)
    {
        this.thumbnailURL = thumbnailURL;
    }


    /**
     * @return the authorities
     */
    public List<String> getAuthorities()
    {
        return authorities;
    }


    /**
     * @param authorities the authorities to set
     */
    public void setAuthorities(final List<String> authorities)
    {
        this.authorities = authorities;
    }
}
