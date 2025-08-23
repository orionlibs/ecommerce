package de.hybris.platform.cmscockpit.services.config.jaxb.contentelement;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"contentElement"})
@XmlRootElement(name = "content-elements")
public class ContentElements
{
    @XmlElement(name = "content-element")
    protected List<ContentElement> contentElement;


    public List<ContentElement> getContentElement()
    {
        if(this.contentElement == null)
        {
            this.contentElement = new ArrayList<>();
        }
        return this.contentElement;
    }
}
