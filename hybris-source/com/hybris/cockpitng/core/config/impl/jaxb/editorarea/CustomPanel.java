/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl.jaxb.editorarea;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for customPanel complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="customPanel"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.hybris.com/cockpitng/component/editorArea}panel"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="render-parameter" type="{http://www.hybris.com/cockpitng/component/editorArea}parameter" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="class" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="spring-bean" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "customPanel", namespace = "http://www.hybris.com/cockpitng/component/editorArea", propOrder = {
                "renderParameter"
})
public class CustomPanel
                extends Panel
{
    @XmlElement(name = "render-parameter", namespace = "http://www.hybris.com/cockpitng/component/editorArea")
    protected List<Parameter> renderParameter;
    @XmlAttribute(name = "class")
    protected String clazz;
    @XmlAttribute(name = "spring-bean")
    protected String springBean;


    /**
     * Gets the value of the renderParameter property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the renderParameter property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRenderParameter().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Parameter }
     *
     *
     */
    public List<Parameter> getRenderParameter()
    {
        if(renderParameter == null)
        {
            renderParameter = new ArrayList<Parameter>();
        }
        return this.renderParameter;
    }


    /**
     * Gets the value of the clazz property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getClazz()
    {
        return clazz;
    }


    /**
     * Sets the value of the clazz property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setClazz(String value)
    {
        this.clazz = value;
    }


    /**
     * Gets the value of the springBean property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getSpringBean()
    {
        return springBean;
    }


    /**
     * Sets the value of the springBean property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setSpringBean(String value)
    {
        this.springBean = value;
    }
}
