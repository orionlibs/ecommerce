package de.hybris.platform.cmscockpit.services.config.jaxb.contenteditor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"template", "customEditors"})
@XmlRootElement(name = "content-editor")
public class ContentEditor
{
    @XmlElement(required = true)
    protected String template;
    @XmlElement(name = "custom-editors")
    protected EditorList customEditors;
    @XmlAttribute
    protected Boolean hideReadOnly;
    @XmlAttribute
    protected Boolean hideEmpty;
    @XmlAttribute
    protected Boolean groupCollections;


    public String getTemplate()
    {
        return this.template;
    }


    public void setTemplate(String value)
    {
        this.template = value;
    }


    public EditorList getCustomEditors()
    {
        return this.customEditors;
    }


    public void setCustomEditors(EditorList value)
    {
        this.customEditors = value;
    }


    public boolean isHideReadOnly()
    {
        if(this.hideReadOnly == null)
        {
            return true;
        }
        return this.hideReadOnly.booleanValue();
    }


    public void setHideReadOnly(Boolean value)
    {
        this.hideReadOnly = value;
    }


    public boolean isHideEmpty()
    {
        if(this.hideEmpty == null)
        {
            return true;
        }
        return this.hideEmpty.booleanValue();
    }


    public void setHideEmpty(Boolean value)
    {
        this.hideEmpty = value;
    }


    public boolean isGroupCollections()
    {
        if(this.groupCollections == null)
        {
            return false;
        }
        return this.groupCollections.booleanValue();
    }


    public void setGroupCollections(Boolean value)
    {
        this.groupCollections = value;
    }
}
