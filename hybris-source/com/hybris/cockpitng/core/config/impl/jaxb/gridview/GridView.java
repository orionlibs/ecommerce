/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl.jaxb.gridview;

import com.hybris.cockpitng.core.config.annotations.Mergeable;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.commonconfig.ImagePreview;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *     &lt;extension base="{http://www.hybris.com/cockpitng/config/common}image-preview"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="additionalRenderer" type="{http://www.hybris.com/cockpitng/component/gridView}Renderer" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
                "additionalRenderer"
})
@XmlRootElement(name = "grid-view", namespace = "http://www.hybris.com/cockpitng/component/gridView")
public class GridView
                extends ImagePreview
{
    @XmlElement(namespace = "http://www.hybris.com/cockpitng/component/gridView")
    @Mergeable(key = "springBean")
    protected List<Renderer> additionalRenderer;


    /**
     * Gets the value of the additionalRenderer property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the additionalRenderer property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAdditionalRenderer().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Renderer }
     *
     *
     */
    public List<Renderer> getAdditionalRenderer()
    {
        if(additionalRenderer == null)
        {
            additionalRenderer = new ArrayList<Renderer>();
        }
        return this.additionalRenderer;
    }
}
