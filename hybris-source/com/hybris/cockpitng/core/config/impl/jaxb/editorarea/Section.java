/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl.jaxb.editorarea;

import com.hybris.cockpitng.core.config.annotations.Mergeable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for section complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="section"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.hybris.com/cockpitng/component/editorArea}abstractSection"&gt;
 *       &lt;sequence&gt;
 *         &lt;choice maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;element name="customPanel" type="{http://www.hybris.com/cockpitng/component/editorArea}customPanel"/&gt;
 *           &lt;element name="panel" type="{http://www.hybris.com/cockpitng/component/editorArea}panel"/&gt;
 *         &lt;/choice&gt;
 *         &lt;choice maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;element name="attribute" type="{http://www.hybris.com/cockpitng/component/editorArea}attribute" maxOccurs="unbounded" minOccurs="0"/&gt;
 *           &lt;element name="custom" type="{http://www.hybris.com/cockpitng/component/editorArea}customElement" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;/choice&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="columns" type="{http://www.w3.org/2001/XMLSchema}decimal" default="2" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "section", namespace = "http://www.hybris.com/cockpitng/component/editorArea", propOrder = {
                "customPanelOrPanel",
                "attributeOrCustom"
})
@XmlSeeAlso({
                EssentialSection.class,
                CustomSection.class
})
public class Section
                extends AbstractSection
{
    @XmlElements({
                    @XmlElement(name = "customPanel", namespace = "http://www.hybris.com/cockpitng/component/editorArea", type = CustomPanel.class),
                    @XmlElement(name = "panel", namespace = "http://www.hybris.com/cockpitng/component/editorArea")
    })
    @Mergeable(key = "name")
    protected List<Panel> customPanelOrPanel;
    @XmlElements({
                    @XmlElement(name = "attribute", namespace = "http://www.hybris.com/cockpitng/component/editorArea", type = Attribute.class),
                    @XmlElement(name = "custom", namespace = "http://www.hybris.com/cockpitng/component/editorArea", type = CustomElement.class)
    })
    @Mergeable(key = "qualifier")
    protected List<AbstractPositioned> attributeOrCustom;
    @XmlAttribute(name = "columns")
    protected BigDecimal columns;


    /**
     * Gets the value of the customPanelOrPanel property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the customPanelOrPanel property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCustomPanelOrPanel().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CustomPanel }
     * {@link Panel }
     *
     *
     */
    public List<Panel> getCustomPanelOrPanel()
    {
        if(customPanelOrPanel == null)
        {
            customPanelOrPanel = new ArrayList<Panel>();
        }
        return this.customPanelOrPanel;
    }


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
     */
    public List<AbstractPositioned> getAttributeOrCustom()
    {
        if(attributeOrCustom == null)
        {
            attributeOrCustom = new ArrayList<AbstractPositioned>();
        }
        return this.attributeOrCustom;
    }


    /**
     * Gets the value of the columns property.
     *
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *
     */
    public BigDecimal getColumns()
    {
        if(columns == null)
        {
            return new BigDecimal("2");
        }
        else
        {
            return columns;
        }
    }


    /**
     * Sets the value of the columns property.
     *
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *
     */
    public void setColumns(final BigDecimal value)
    {
        this.columns = value;
    }
}
