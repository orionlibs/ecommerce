/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl.jaxb.hybris;

import com.hybris.cockpitng.core.config.annotations.Mergeable;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.commonconfig.Positioned;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for action-group complex type.
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="action-group"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="label" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="action" type="{http://www.hybris.com/cockpit/config/hybris}action" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="qualifier" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="show-group-header" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *       &lt;attribute name="show-separator" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "action-group", propOrder =
                {"label", "actions"})
@XmlSeeAlso(
                {ActionGroupExtended.class, ActionGroupSplit.class, ActionGroupThreeDots.class})
public class ActionGroup extends Positioned
{
    @XmlElement
    protected String label;
    @Mergeable(key =
                    {"actionId", "id"})
    @XmlElement(name = "action")
    protected List<Action> actions;
    @XmlAttribute(name = "qualifier")
    protected String qualifier;
    @XmlAttribute(name = "show-group-header")
    protected Boolean showGroupHeader;
    @XmlAttribute(name = "show-separator")
    protected Boolean showSeparator;
    @XmlAttribute(name = "merge-mode")
    protected String mergeMode;


    /**
     * Gets the value of the label property.
     *
     * @return possible object is {@link String }
     */
    public String getLabel()
    {
        return label;
    }


    /**
     * Sets the value of the label property.
     *
     * @param value
     *           allowed object is {@link String }
     */
    public void setLabel(final String value)
    {
        this.label = value;
    }


    public List<Action> getActions()
    {
        if(actions == null)
        {
            actions = new ArrayList<>();
        }
        return this.actions;
    }


    public String getQualifier()
    {
        return qualifier;
    }


    public void setQualifier(final String value)
    {
        this.qualifier = value;
    }


    /**
     * Gets the value of the showGroupHeader property.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isShowGroupHeader()
    {
        return showGroupHeader;
    }


    /**
     * Sets the value of the showGroupHeader property.
     *
     * @param value
     *           allowed object is {@link Boolean }
     */
    public void setShowGroupHeader(final Boolean value)
    {
        this.showGroupHeader = value;
    }


    /**
     * Gets the value of the showSeparator property.
     *
     * @return possible object is {@link Boolean }
     */
    public boolean isShowSeparator()
    {
        if(showSeparator == null)
        {
            return true;
        }
        else
        {
            return showSeparator;
        }
    }


    /**
     * Sets the value of the showSeparator property.
     *
     * @param value
     *           allowed object is {@link Boolean }
     */
    public void setShowSeparator(final Boolean value)
    {
        this.showSeparator = value;
    }


    /**
     * Gets the value of the mergeMode property.
     *
     * @return possible object is {@link String }
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
     */
    public void setMergeMode(String value)
    {
        this.mergeMode = value;
    }
}
