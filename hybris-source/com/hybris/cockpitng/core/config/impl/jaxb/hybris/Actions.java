/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl.jaxb.hybris;

import com.hybris.cockpitng.core.config.annotations.Mergeable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for actions complex type.
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="actions"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;choice maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;element name="three-dots-group" type="{http://www.hybris.com/cockpit/config/hybris}action-group-three-dots"/&gt;
 *           &lt;element name="extended-group" type="{http://www.hybris.com/cockpit/config/hybris}action-group-extended"/&gt;
 *           &lt;element name="split-group" type="{http://www.hybris.com/cockpit/config/hybris}action-group-split"/&gt;
 *           &lt;element name="group" type="{http://www.hybris.com/cockpit/config/hybris}action-group"/&gt;
 *         &lt;/choice&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "actions", propOrder =
                {"group"})
@XmlRootElement(name = "actions")
public class Actions
{
    @Mergeable(key = "qualifier")
    @XmlElements(
                    {@XmlElement(name = "split-group", type = ActionGroupSplit.class),
                                    @XmlElement(name = "extended-group", type = ActionGroupExtended.class),
                                    @XmlElement(name = "three-dots-group", type = ActionGroupThreeDots.class), @XmlElement(name = "group")})
    protected List<ActionGroup> group;


    public List<ActionGroup> getGroup()
    {
        if(group == null)
        {
            group = new ArrayList<>();
        }
        return this.group;
    }
}
