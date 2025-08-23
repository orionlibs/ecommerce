/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl.jaxb.editorarea;

import com.hybris.cockpitng.core.config.annotations.Mergeable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
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
 *         &lt;element name="essentials" type="{http://www.hybris.com/cockpitng/component/editorArea}essentials" minOccurs="0"/&gt;
 *         &lt;choice maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;element name="customTab" type="{http://www.hybris.com/cockpitng/component/editorArea}customTab"/&gt;
 *           &lt;element name="tab" type="{http://www.hybris.com/cockpitng/component/editorArea}tab"/&gt;
 *         &lt;/choice&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="viewMode" use="optional" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="hideTabNameIfOnlyOneVisible" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" /&gt;
 *       &lt;attribute name="logic-handler" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
                "essentials",
                "customTabOrTab"
})
@XmlRootElement(name = "editorArea", namespace = "http://www.hybris.com/cockpitng/component/editorArea")
public class EditorArea
{
    @Mergeable(key = {})
    @XmlElement(namespace = "http://www.hybris.com/cockpitng/component/editorArea")
    protected Essentials essentials;
    @XmlElements({
                    @XmlElement(name = "customTab", namespace = "http://www.hybris.com/cockpitng/component/editorArea", type = CustomTab.class),
                    @XmlElement(name = "tab", namespace = "http://www.hybris.com/cockpitng/component/editorArea", type = Tab.class)
    })
    @Mergeable(key = "name")
    protected List<AbstractTab> customTabOrTab;
    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlAttribute(name = "viewMode")
    protected String viewMode;
    @XmlAttribute(name = "hideTabNameIfOnlyOneVisible")
    protected Boolean hideTabNameIfOnlyOneVisible;
    @XmlAttribute(name = "logic-handler")
    protected String logicHandler;


    /**
     * Gets the value of the essentials property.
     *
     * @return
     *     possible object is
     *     {@link Essentials }
     *
     */
    public Essentials getEssentials()
    {
        return essentials;
    }


    /**
     * Sets the value of the essentials property.
     *
     * @param value
     *     allowed object is
     *     {@link Essentials }
     *
     */
    public void setEssentials(Essentials value)
    {
        this.essentials = value;
    }


    /**
     * Gets the value of the customTabOrTab property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the customTabOrTab property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCustomTabOrTab().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CustomTab }
     * {@link Tab }
     *
     *
     */
    public List<AbstractTab> getCustomTabOrTab()
    {
        if(customTabOrTab == null)
        {
            customTabOrTab = new ArrayList<AbstractTab>();
        }
        return this.customTabOrTab;
    }


    /**
     * Gets the value of the name property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getName()
    {
        return name;
    }


    /**
     * Sets the value of the name property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setName(String value)
    {
        this.name = value;
    }


    /**
     * Gets the value of the name viewMode.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getViewMode()
    {
        return viewMode;
    }


    /**
     * Sets the value of the viewMode property.
     *
     * @param viewMode
     *     allowed object is
     *     {@link String }
     *
     */
    public void setViewMode(String viewMode)
    {
        this.viewMode = viewMode;
    }


    /**
     * Gets the value of the hideTabNameIfOnlyOneVisible property.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public boolean isHideTabNameIfOnlyOneVisible()
    {
        if(hideTabNameIfOnlyOneVisible == null)
        {
            return true;
        }
        else
        {
            return hideTabNameIfOnlyOneVisible;
        }
    }


    /**
     * Sets the value of the hideTabNameIfOnlyOneVisible property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setHideTabNameIfOnlyOneVisible(Boolean value)
    {
        this.hideTabNameIfOnlyOneVisible = value;
    }


    /**
     * Gets the value of the handler property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getLogicHandler()
    {
        return logicHandler;
    }


    /**
     * Sets the value of the handler property.
     *
     * @param logicHandler
     *     allowed object is
     *     {@link String }
     *
     */
    public void setLogicHandler(String logicHandler)
    {
        this.logicHandler = logicHandler;
    }
}
