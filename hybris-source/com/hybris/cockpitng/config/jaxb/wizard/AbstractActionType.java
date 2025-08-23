/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.config.jaxb.wizard;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for AbstractActionType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="AbstractActionType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="if" type="{http://www.hybris.com/cockpitng/config/wizard-config}IfType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;choice minOccurs="0"&gt;
 *           &lt;sequence&gt;
 *             &lt;element name="save" type="{http://www.hybris.com/cockpitng/config/wizard-config}SaveType" maxOccurs="unbounded"/&gt;
 *           &lt;/sequence&gt;
 *           &lt;element name="save-all" type="{http://www.hybris.com/cockpitng/config/wizard-config}SaveAllType"/&gt;
 *         &lt;/choice&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="visible" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="default-target" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AbstractActionType", namespace = "http://www.hybris.com/cockpitng/config/wizard-config", propOrder = {
                "_if",
                "save",
                "saveAll"
})
@XmlSeeAlso({
                CustomType.class,
                BackType.class,
                NextType.class,
                DoneType.class,
                CancelType.class
})
public abstract class AbstractActionType
{
    @XmlElement(name = "if", namespace = "http://www.hybris.com/cockpitng/config/wizard-config")
    protected List<IfType> _if;
    @XmlElement(namespace = "http://www.hybris.com/cockpitng/config/wizard-config")
    protected List<SaveType> save;
    @XmlElement(name = "save-all", namespace = "http://www.hybris.com/cockpitng/config/wizard-config")
    protected SaveAllType saveAll;
    @XmlAttribute(name = "visible")
    protected String visible;
    @XmlAttribute(name = "default-target")
    protected String defaultTarget;


    /**
     * Gets the value of the if property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the if property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIf().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link IfType }
     *
     *
     */
    public List<IfType> getIf()
    {
        if(_if == null)
        {
            _if = new ArrayList<IfType>();
        }
        return this._if;
    }


    /**
     * Gets the value of the save property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the save property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSave().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SaveType }
     *
     *
     */
    public List<SaveType> getSave()
    {
        if(save == null)
        {
            save = new ArrayList<SaveType>();
        }
        return this.save;
    }


    /**
     * Gets the value of the saveAll property.
     *
     * @return
     *     possible object is
     *     {@link SaveAllType }
     *
     */
    public SaveAllType getSaveAll()
    {
        return saveAll;
    }


    /**
     * Sets the value of the saveAll property.
     *
     * @param value
     *     allowed object is
     *     {@link SaveAllType }
     *
     */
    public void setSaveAll(SaveAllType value)
    {
        this.saveAll = value;
    }


    /**
     * Gets the value of the visible property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getVisible()
    {
        return visible;
    }


    /**
     * Sets the value of the visible property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setVisible(String value)
    {
        this.visible = value;
    }


    /**
     * Gets the value of the defaultTarget property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getDefaultTarget()
    {
        return defaultTarget;
    }


    /**
     * Sets the value of the defaultTarget property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setDefaultTarget(String value)
    {
        this.defaultTarget = value;
    }
}
