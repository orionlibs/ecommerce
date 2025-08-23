/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl.jaxb.hybris.simplesearch;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sort-field", propOrder = {"asc"})
public class SortField extends Field
{
    @XmlAttribute(name = "asc", required = false)
    private boolean asc;


    public boolean isAsc()
    {
        return asc;
    }


    public void setAsc(final boolean asc)
    {
        this.asc = asc;
    }
}
