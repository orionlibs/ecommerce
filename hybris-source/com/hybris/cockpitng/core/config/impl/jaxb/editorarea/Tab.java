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
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for tab complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="tab"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.hybris.com/cockpitng/component/editorArea}abstractTab"&gt;
 *       &lt;choice maxOccurs="unbounded" minOccurs="0"&gt;
 *         &lt;element name="customSection" type="{http://www.hybris.com/cockpitng/component/editorArea}customSection"/&gt;
 *         &lt;element name="section" type="{http://www.hybris.com/cockpitng/component/editorArea}section"/&gt;
 *       &lt;/choice&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tab", namespace = "http://www.hybris.com/cockpitng/component/editorArea", propOrder = {
                "customSectionOrSection"
})
public class Tab
                extends AbstractTab
{
    @XmlElements({
                    @XmlElement(name = "customSection", namespace = "http://www.hybris.com/cockpitng/component/editorArea", type = CustomSection.class),
                    @XmlElement(name = "section", namespace = "http://www.hybris.com/cockpitng/component/editorArea", type = Section.class)
    })
    @Mergeable(key = "name")
    protected List<AbstractSection> customSectionOrSection;


    /**
     * Gets the value of the customSectionOrSection property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the customSectionOrSection property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCustomSectionOrSection().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CustomSection }
     * {@link Section }
     *
     *
     */
    public List<AbstractSection> getCustomSectionOrSection()
    {
        if(customSectionOrSection == null)
        {
            customSectionOrSection = new ArrayList<AbstractSection>();
        }
        return this.customSectionOrSection;
    }
}
