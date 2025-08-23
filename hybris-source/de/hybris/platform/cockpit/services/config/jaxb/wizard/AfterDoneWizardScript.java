package de.hybris.platform.cockpit.services.config.jaxb.wizard;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "after-done-wizard-script", propOrder = {"content"})
public class AfterDoneWizardScript
{
    @XmlElement(name = "content")
    protected String content;
    @XmlAttribute
    protected String language;


    public String getContent()
    {
        return this.content;
    }


    public String getLanguage()
    {
        return this.language;
    }
}
