/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.config.simplelist.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "list-slot", propOrder = {"field"})
public abstract class ListSlot
{
    @XmlAttribute(name = "field", required = true)
    protected String field;


    public void setField(final String field)
    {
        this.field = field;
    }


    public String getField()
    {
        return field;
    }
}
