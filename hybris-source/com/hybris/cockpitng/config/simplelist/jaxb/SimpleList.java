/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.config.simplelist.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"name", "description", "image"})
@XmlRootElement(name = "simple-list")
public class SimpleList
{
    @XmlElement(name = "name")
    protected Name name;
    @XmlElement(name = "description")
    protected Description description;
    @XmlElement(name = "image")
    protected Image image;


    public void setName(final Name name)
    {
        this.name = name;
    }


    public Name getName()
    {
        return name;
    }


    public Description getDescription()
    {
        return description;
    }


    public void setDescription(final Description description)
    {
        this.description = description;
    }


    public Image getImage()
    {
        return image;
    }


    public void setImage(final Image image)
    {
        this.image = image;
    }
}
