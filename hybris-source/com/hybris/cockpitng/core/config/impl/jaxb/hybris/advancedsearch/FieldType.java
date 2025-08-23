/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl.jaxb.hybris.advancedsearch;

import com.hybris.cockpitng.core.config.impl.jaxb.hybris.commonconfig.Positioned;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for FieldType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="FieldType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.hybris.com/cockpitng/config/common}positioned"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="editor-parameter" type="{http://www.hybris.com/cockpitng/config/advancedsearch}Parameter" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="operator" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="selected" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *       &lt;attribute name="editor" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="sortable" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" /&gt;
 *       &lt;attribute name="disabled" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *       &lt;attribute name="mandatory" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *       &lt;attribute name="merge-mode" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FieldType", namespace = "http://www.hybris.com/cockpitng/config/advancedsearch", propOrder = {
                "editorParameter"
})
public class FieldType
                extends Positioned
{
    @XmlElement(name = "editor-parameter", namespace = "http://www.hybris.com/cockpitng/config/advancedsearch")
    protected List<Parameter> editorParameter;
    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlAttribute(name = "operator")
    protected String operator;
    @XmlAttribute(name = "selected")
    protected Boolean selected;
    @XmlAttribute(name = "editor")
    protected String editor;
    @XmlAttribute(name = "sortable")
    protected Boolean sortable;
    @XmlAttribute(name = "disabled")
    protected Boolean disabled;
    @XmlAttribute(name = "mandatory")
    protected Boolean mandatory;
    @XmlAttribute(name = "merge-mode")
    protected String mergeMode;


    /**
     * Gets the value of the editorParameter property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the editorParameter property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEditorParameter().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Parameter }
     *
     *
     */
    public List<Parameter> getEditorParameter()
    {
        if(editorParameter == null)
        {
            editorParameter = new ArrayList<Parameter>();
        }
        return this.editorParameter;
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
     * Gets the value of the operator property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getOperator()
    {
        return operator;
    }


    /**
     * Sets the value of the operator property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setOperator(String value)
    {
        this.operator = value;
    }


    /**
     * Gets the value of the selected property.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public boolean isSelected()
    {
        if(selected == null)
        {
            return false;
        }
        else
        {
            return selected;
        }
    }


    /**
     * Sets the value of the selected property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setSelected(Boolean value)
    {
        this.selected = value;
    }


    /**
     * Gets the value of the editor property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getEditor()
    {
        return editor;
    }


    /**
     * Sets the value of the editor property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setEditor(String value)
    {
        this.editor = value;
    }


    /**
     * Gets the value of the sortable property.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
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
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setSortable(Boolean value)
    {
        this.sortable = value;
    }


    /**
     * Gets the value of the disabled property.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public boolean isDisabled()
    {
        if(disabled == null)
        {
            return false;
        }
        else
        {
            return disabled;
        }
    }


    /**
     * Sets the value of the disabled property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setDisabled(Boolean value)
    {
        this.disabled = value;
    }


    /**
     * Gets the value of the mandatory property.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public boolean isMandatory()
    {
        if(mandatory == null)
        {
            return false;
        }
        else
        {
            return mandatory;
        }
    }


    /**
     * Sets the value of the mandatory property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setMandatory(Boolean value)
    {
        this.mandatory = value;
    }


    /**
     * Gets the value of the mergeMode property.
     *
     * @return
     *     possible object is
     *     {@link String }
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
     *     allowed object is
     *     {@link String }
     *
     */
    public void setMergeMode(String value)
    {
        this.mergeMode = value;
    }
}
