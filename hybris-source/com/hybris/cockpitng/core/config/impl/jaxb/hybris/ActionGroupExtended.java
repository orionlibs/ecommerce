/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl.jaxb.hybris;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for action-group-extended complex type.
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="action-group-extended"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.hybris.com/cockpit/config/hybris}action-group"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="extended-action" type="{http://www.hybris.com/cockpit/config/hybris}action"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "action-group-extended", propOrder =
                {"extendedAction"})
public class ActionGroupExtended extends ActionGroup
{
    @XmlElement(name = "extended-action", required = true)
    protected Action extendedAction;


    /**
     * Gets the value of the extendedAction property.
     *
     * @return possible object is {@link Action }
     */
    public Action getExtendedAction()
    {
        return extendedAction;
    }


    /**
     * Sets the value of the extendedAction property.
     *
     * @param value
     *           allowed object is {@link Action }
     */
    public void setExtendedAction(final Action value)
    {
        this.extendedAction = value;
    }
}
