/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl.jaxb.listview;

import com.hybris.cockpitng.core.config.impl.jaxb.hybris.commonconfig.Positioned;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for list-column complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="list-column"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.hybris.com/cockpitng/config/common}positioned"&gt;
 *       &lt;attribute name="qualifier" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="auto-extract" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *       &lt;attribute name="label" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="sortable" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" /&gt;
 *       &lt;attribute name="class" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="width" type="{http://www.w3.org/2001/XMLSchema}string" default="" /&gt;
 *       &lt;attribute name="hflex" type="{http://www.w3.org/2001/XMLSchema}string" default="" /&gt;
 *       &lt;attribute name="spring-bean" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="merge-mode" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="maxChar" type="{http://www.w3.org/2001/XMLSchema}int" /&gt;
 *       &lt;attribute name="link" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *       &lt;attribute name="link-value" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "list-column", namespace = "http://www.hybris.com/cockpitng/component/listView")
public class ListColumn extends Positioned
{
    @XmlAttribute(name = "qualifier")
    protected String qualifier;
    @XmlAttribute(name = "auto-extract")
    protected Boolean autoExtract;
    @XmlAttribute(name = "label")
    protected String label;
    @XmlAttribute(name = "type")
    protected String type;
    @XmlAttribute(name = "sortable")
    protected Boolean sortable;
    @XmlAttribute(name = "class")
    protected String clazz;
    @XmlAttribute(name = "width")
    protected String width;
    @XmlAttribute(name = "hflex")
    protected String hflex;
    @XmlAttribute(name = "spring-bean")
    protected String springBean;
    @XmlAttribute(name = "merge-mode")
    protected String mergeMode;
    @XmlAttribute(name = "maxChar")
    protected Integer maxChar;
    @XmlAttribute(name = "link")
    protected Boolean link;
    @XmlAttribute(name = "link-value")
    protected String linkValue;


    /**
     * Gets the value of the qualifier property.
     *
     * @return
     *            possible object is
     *         {@link String }
     *
     */
    public String getQualifier()
    {
        return qualifier;
    }


    /**
     * Sets the value of the qualifier property.
     *
     * @param value
     *           allowed object is
     *           {@link String }
     *
     */
    public void setQualifier(String value)
    {
        this.qualifier = value;
    }


    /**
     * Gets the value of the autoExtract property.
     *
     * @return
     *            possible object is
     *         {@link Boolean }
     *
     */
    public boolean isAutoExtract()
    {
        if(autoExtract == null)
        {
            return false;
        }
        else
        {
            return autoExtract;
        }
    }


    /**
     * Sets the value of the autoExtract property.
     *
     * @param value
     *           allowed object is
     *           {@link Boolean }
     *
     */
    public void setAutoExtract(Boolean value)
    {
        this.autoExtract = value;
    }


    /**
     * Gets the value of the label property.
     *
     * @return
     *            possible object is
     *         {@link String }
     *
     */
    public String getLabel()
    {
        return label;
    }


    /**
     * Sets the value of the label property.
     *
     * @param value
     *           allowed object is
     *           {@link String }
     *
     */
    public void setLabel(String value)
    {
        this.label = value;
    }


    /**
     * Gets the value of the type property.
     *
     * @return
     *            possible object is
     *         {@link String }
     *
     */
    public String getType()
    {
        return type;
    }


    /**
     * Sets the value of the type property.
     *
     * @param value
     *           allowed object is
     *           {@link String }
     *
     */
    public void setType(String value)
    {
        this.type = value;
    }


    /**
     * Gets the value of the sortable property.
     *
     * @return
     *            possible object is
     *         {@link Boolean }
     *
     */
    public boolean isSortable()
    {
        if(sortable == null)
        {
            return true;
        }
        else
        {
            return sortable;
        }
    }


    /**
     * Sets the value of the sortable property.
     *
     * @param value
     *           allowed object is
     *           {@link Boolean }
     *
     */
    public void setSortable(Boolean value)
    {
        this.sortable = value;
    }


    /**
     * Gets the value of the clazz property.
     *
     * @return
     *            possible object is
     *         {@link String }
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
     *           allowed object is
     *           {@link String }
     *
     */
    public void setClazz(String value)
    {
        this.clazz = value;
    }


    /**
     * Gets the value of the width property.
     *
     * @return
     *            possible object is
     *         {@link String }
     *
     */
    public String getWidth()
    {
        if(width == null)
        {
            return "";
        }
        else
        {
            return width;
        }
    }


    /**
     * Sets the value of the width property.
     *
     * @param value
     *           allowed object is
     *           {@link String }
     *
     */
    public void setWidth(String value)
    {
        this.width = value;
    }


    /**
     * Gets the value of the hflex property.
     *
     * @return
     *            possible object is
     *         {@link String }
     *
     */
    public String getHflex()
    {
        if(hflex == null)
        {
            return "";
        }
        else
        {
            return hflex;
        }
    }


    /**
     * Sets the value of the hflex property.
     *
     * @param value
     *           allowed object is
     *           {@link String }
     *
     */
    public void setHflex(String value)
    {
        this.hflex = value;
    }


    /**
     * Gets the value of the springBean property.
     *
     * @return
     *            possible object is
     *         {@link String }
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
     *           allowed object is
     *           {@link String }
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
     *            possible object is
     *         {@link String }
     *
     */
    public String getMergeMode()
    {
        return mergeMode;
    }


    /**
     * Sets the value of the mergeMode property.
     *
     * @param value
     *           allowed object is
     *           {@link String }
     *
     */
    public void setMergeMode(String value)
    {
        this.mergeMode = value;
    }


    /**
     * Gets the value of the maxChar property.
     *
     * @return
     *            possible object is
     *         {@link Integer }
     *
     */
    public Integer getMaxChar()
    {
        return maxChar;
    }


    /**
     * Sets the value of the maxChar property.
     *
     * @param value
     *           allowed object is
     *           {@link Integer }
     *
     */
    public void setMaxChar(Integer value)
    {
        this.maxChar = value;
    }


    /**
     * Gets the value of the link property.
     *
     * @return
     *            possible object is
     *         {@link Boolean }
     *
     */
    public Boolean isLink()
    {
        return link;
    }


    /**
     * Sets the value of the link property.
     *
     * @param value
     *           allowed object is
     *           {@link Boolean }
     *
     */
    public void setLink(Boolean value)
    {
        this.link = value;
    }


    /**
     * Gets the value of the linkValue property.
     *
     * @return
     *            possible object is
     *         {@link String }
     *
     */
    public String getLinkValue()
    {
        return linkValue;
    }


    /**
     * Sets the value of the linkValue property.
     *
     * @param value
     *           allowed object is
     *           {@link String }
     *
     */
    public void setLinkValue(String value)
    {
        this.linkValue = value;
    }
}
