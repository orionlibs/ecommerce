/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl.jaxb.gridview;

import com.hybris.cockpitng.core.config.impl.jaxb.hybris.commonconfig.MergeMode;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.commonconfig.Positioned;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for Renderer complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="Renderer"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.hybris.com/cockpitng/config/common}positioned"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="parameter" type="{http://www.hybris.com/cockpitng/component/gridView}parameter" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="spring-bean" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="merge-mode" type="{http://www.hybris.com/cockpitng/config/common}MergeMode" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Renderer", namespace = "http://www.hybris.com/cockpitng/component/gridView", propOrder = {
                "parameter"
})
public class Renderer
                extends Positioned
{
    @XmlElement(namespace = "http://www.hybris.com/cockpitng/component/gridView")
    protected List<Parameter> parameter;
    @XmlAttribute(name = "spring-bean", required = true)
    protected String springBean;
    @XmlAttribute(name = "merge-mode")
    protected MergeMode mergeMode;


    /**
     * Gets the value of the parameter property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the parameter property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getParameter().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Parameter }
     *
     *
     */
    public List<Parameter> getParameter()
    {
        if(parameter == null)
        {
            parameter = new ArrayList<Parameter>();
        }
        return this.parameter;
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


    /**
     * Gets the value of the mergeMode property.
     *
     * @return
     *     possible object is
     *     {@link MergeMode }
     *
     */
    public MergeMode getMergeMode()
    {
        return mergeMode;
    }


    /**
     * Sets the value of the mergeMode property.
     *
     * @param value
     *     allowed object is
     *     {@link MergeMode }
     *
     */
    public void setMergeMode(MergeMode value)
    {
        this.mergeMode = value;
    }
}
