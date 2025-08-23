/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl.jaxb.treecollection;

import com.hybris.cockpitng.core.config.annotations.Mergeable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
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
 *         &lt;element name="node" type="{http://www.hybris.com/cockpitng/component/treeCollection}tree-node" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
                "node"
})
@XmlRootElement(name = "tree-collection-nodes", namespace = "http://www.hybris.com/cockpitng/component/treeCollection")
public class TreeCollectionNodes
{
    @Mergeable(key = "attribute")
    @XmlElement(namespace = "http://www.hybris.com/cockpitng/component/treeCollection")
    protected List<TreeNode> node;
    @XmlAttribute(name = "skip-only-attribute")
    protected Boolean skipOnlyAttribute;


    /**
     * Gets the value of the node property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the node property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNode().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TreeNode }
     *
     *
     */
    public List<TreeNode> getNode()
    {
        if(node == null)
        {
            node = new ArrayList<TreeNode>();
        }
        return this.node;
    }


    /**
     * Gets the value of the skipOnlyAttribute property.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public boolean isSkipOnlyAttribute()
    {
        if(skipOnlyAttribute == null)
        {
            return false;
        }
        else
        {
            return skipOnlyAttribute;
        }
    }


    /**
     * Sets the value of the skipOnlyAttribute property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setSkipOnlyAttribute(Boolean value)
    {
        this.skipOnlyAttribute = value;
    }
}
