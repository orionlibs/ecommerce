/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl.jaxb.dynamicforms;

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
 * <p>
 * Java class for anonymous complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="attribute" type="{http://www.hybris.com/cockpitng/component/dynamicForms}dynamicAttribute" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="section" type="{http://www.hybris.com/cockpitng/component/dynamicForms}dynamicSection" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="tab" type="{http://www.hybris.com/cockpitng/component/dynamicForms}dynamicTab" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="visitor" type="{http://www.hybris.com/cockpitng/component/dynamicForms}dynamicVisitor" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="modelProperty" type="{http://www.w3.org/2001/XMLSchema}string" default="currentObject" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"attribute", "section", "tab", "visitor"})
@XmlRootElement(name = "dynamicForms", namespace = "http://www.hybris.com/cockpitng/component/dynamicForms")
public class DynamicForms
{
    @XmlElement(namespace = "http://www.hybris.com/cockpitng/component/dynamicForms")
    @Mergeable(key = "id")
    protected List<DynamicAttribute> attribute;
    @XmlElement(namespace = "http://www.hybris.com/cockpitng/component/dynamicForms")
    @Mergeable(key = "id")
    protected List<DynamicSection> section;
    @XmlElement(namespace = "http://www.hybris.com/cockpitng/component/dynamicForms")
    @Mergeable(key = "id")
    protected List<DynamicTab> tab;
    @XmlElement(namespace = "http://www.hybris.com/cockpitng/component/dynamicForms")
    @Mergeable(key = "id")
    protected List<DynamicVisitor> visitor;
    @XmlAttribute(name = "modelProperty")
    protected String modelProperty;


    /**
     * Gets the value of the attribute property.
     *
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
     * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
     * the attribute property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     *
     * <pre>
     * getAttribute().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list {@link DynamicAttribute }
     *
     *
     */
    public List<DynamicAttribute> getAttribute()
    {
        if(attribute == null)
        {
            attribute = new ArrayList<DynamicAttribute>();
        }
        return this.attribute;
    }


    /**
     * Gets the value of the section property.
     *
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
     * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
     * the section property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     *
     * <pre>
     * getSection().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list {@link DynamicSection }
     *
     *
     */
    public List<DynamicSection> getSection()
    {
        if(section == null)
        {
            section = new ArrayList<DynamicSection>();
        }
        return this.section;
    }


    /**
     * Gets the value of the tab property.
     *
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
     * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
     * the tab property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     *
     * <pre>
     * getTab().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list {@link DynamicTab }
     *
     *
     */
    public List<DynamicTab> getTab()
    {
        if(tab == null)
        {
            tab = new ArrayList<DynamicTab>();
        }
        return this.tab;
    }


    /**
     * Gets the value of the visitor property.
     *
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
     * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
     * the visitor property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     *
     * <pre>
     * getVisitor().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list {@link DynamicVisitor }
     *
     *
     */
    public List<DynamicVisitor> getVisitor()
    {
        if(visitor == null)
        {
            visitor = new ArrayList<DynamicVisitor>();
        }
        return this.visitor;
    }


    /**
     * Gets the value of the modelProperty property.
     *
     * @return
     *         possible object is {@link String }
     *
     */
    public String getModelProperty()
    {
        if(modelProperty == null)
        {
            return "currentObject";
        }
        else
        {
            return modelProperty;
        }
    }


    /**
     * Sets the value of the modelProperty property.
     *
     * @param value
     *           allowed object is {@link String }
     *
     */
    public void setModelProperty(final String value)
    {
        this.modelProperty = value;
    }
}
