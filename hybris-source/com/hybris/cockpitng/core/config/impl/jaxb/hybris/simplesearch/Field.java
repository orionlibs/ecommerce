/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl.jaxb.hybris.simplesearch;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "field", propOrder = {"name"})
public class Field
{
    @XmlAttribute(required = true, name = "name")
    protected String name;
    @XmlAttribute(name = "merge-mode")
    protected String mergeMode;


    public String getName()
    {
        return name;
    }


    public void setName(final String name)
    {
        this.name = name;
    }


    /**
     * Gets the value of the mergeMode property.
     *
     * @return
     *         possible object is {@link String }
     *
     */
    public String getMergeMode()
    {
        return mergeMode;
    }


    /**
     * Sets the value of the mergeMode property.
     *
     * @param value
     *           allowed object is {@link String }
     *
     */
    public void setMergeMode(final String value)
    {
        this.mergeMode = value;
    }
}
