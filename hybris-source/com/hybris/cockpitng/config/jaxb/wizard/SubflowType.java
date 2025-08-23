/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.config.jaxb.wizard;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for SubflowType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="SubflowType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.hybris.com/cockpitng/config/wizard-config}AbstractFlowType"&gt;
 *       &lt;attribute name="position" type="{http://www.w3.org/2001/XMLSchema}integer" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SubflowType", namespace = "http://www.hybris.com/cockpitng/config/wizard-config")
public class SubflowType
                extends AbstractFlowType
{
    @XmlAttribute(name = "position")
    protected BigInteger position;


    /**
     * Gets the value of the position property.
     *
     * @return
     *     possible object is
     *     {@link BigInteger }
     *
     */
    public BigInteger getPosition()
    {
        return position;
    }


    /**
     * Sets the value of the position property.
     *
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *
     */
    public void setPosition(BigInteger value)
    {
        this.position = value;
    }
}
