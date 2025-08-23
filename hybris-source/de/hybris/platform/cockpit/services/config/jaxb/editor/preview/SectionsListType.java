package de.hybris.platform.cockpit.services.config.jaxb.editor.preview;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SectionsListType", propOrder = {"section"})
public class SectionsListType
{
    @XmlElement(required = true)
    protected List<Object> section;


    public List<Object> getSection()
    {
        if(this.section == null)
        {
            this.section = new ArrayList();
        }
        return this.section;
    }
}
