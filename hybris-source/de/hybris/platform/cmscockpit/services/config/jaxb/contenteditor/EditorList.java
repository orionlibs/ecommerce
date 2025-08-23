package de.hybris.platform.cmscockpit.services.config.jaxb.contenteditor;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "editor-list", propOrder = {"property"})
public class EditorList
{
    @XmlElement(required = true)
    protected List<Property> property;


    public List<Property> getProperty()
    {
        if(this.property == null)
        {
            this.property = new ArrayList<>();
        }
        return this.property;
    }
}
