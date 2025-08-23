/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl.jaxb.hybris.simplesearch;

import com.hybris.cockpitng.core.config.annotations.Mergeable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for anonymous complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="field" type="{http://www.hybris.com/cockpitng/config/simplesearch}field" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="sort-field" type="{http://www.hybris.com/cockpitng/config/simplesearch}sort-field" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="includeSubtypes" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"field", "sortField"})
@XmlRootElement(name = "simple-search")
public class SimpleSearch
{
    @XmlElement(name = "sort-field")
    protected SortField sortField;
    @XmlElement(name = "field")
    @Mergeable(key = "name")
    protected List<Field> field;
    @XmlAttribute(name = "includeSubtypes")
    protected Boolean includeSubtypes;


    public List<Field> getField()
    {
        if(this.field == null)
        {
            this.field = new ArrayList<Field>();
        }
        return this.field;
    }


    public SortField getSortField()
    {
        return sortField;
    }


    public void setSortField(final SortField sortField)
    {
        this.sortField = sortField;
    }


    /**
     * Gets the value of the includeSubtypes property.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public boolean isIncludeSubtypes()
    {
        if(includeSubtypes == null)
        {
            return true;
        }
        else
        {
            return includeSubtypes;
        }
    }


    /**
     * Sets the value of the includeSubtypes property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setIncludeSubtypes(Boolean value)
    {
        this.includeSubtypes = value;
    }
}
