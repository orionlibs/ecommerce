/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl.jaxb.editorarea;

import com.hybris.cockpitng.core.config.annotations.Mergeable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for panel complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="panel"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.hybris.com/cockpitng/component/editorArea}abstractPanel"&gt;
 *       &lt;sequence maxOccurs="unbounded" minOccurs="0"&gt;
 *         &lt;choice maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;element name="attribute" type="{http://www.hybris.com/cockpitng/component/editorArea}attribute" maxOccurs="unbounded" minOccurs="0"/&gt;
 *           &lt;element name="custom" type="{http://www.hybris.com/cockpitng/component/editorArea}customElement" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;/choice&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "panel", namespace = "http://www.hybris.com/cockpitng/component/editorArea", propOrder = {
                "attributeOrCustom"
})
@XmlSeeAlso({
                CustomPanel.class
})
public class Panel
                extends AbstractPanel
{
    @XmlElements({
                    @XmlElement(name = "attribute", namespace = "http://www.hybris.com/cockpitng/component/editorArea", type = Attribute.class),
                    @XmlElement(name = "custom", namespace = "http://www.hybris.com/cockpitng/component/editorArea", type = CustomElement.class)
    })
    @Mergeable(key = "qualifier")
    protected List<AbstractPositioned> attributeOrCustom;


    /**
     * Gets the value of the attributeOrCustom property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the attributeOrCustom property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAttributeOrCustom().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Attribute }
     * {@link CustomElement }
     *
     *
     */
    public List<AbstractPositioned> getAttributeOrCustom()
    {
        if(attributeOrCustom == null)
        {
            attributeOrCustom = new ArrayList<AbstractPositioned>();
        }
        return this.attributeOrCustom;
    }
}
