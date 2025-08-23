package de.hybris.platform.cockpit.services.config.jaxb.wizard;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "property", propOrder = {"parameter"})
public class Property
{
    @XmlElement
    protected List<Parameter> parameter;
    @XmlAttribute(required = true)
    protected String qualifier;
    @XmlAttribute(required = true)
    protected String editorCode;
    @XmlAttribute(required = true)
    protected Boolean visible = Boolean.TRUE;


    public boolean isVisible()
    {
        if(this.visible == null)
        {
            return false;
        }
        return this.visible.booleanValue();
    }


    public void setVisible(Boolean visible)
    {
        this.visible = visible;
    }


    public List<Parameter> getParameter()
    {
        if(this.parameter == null)
        {
            this.parameter = new ArrayList<>();
        }
        return this.parameter;
    }


    public String getQualifier()
    {
        return this.qualifier;
    }


    public void setQualifier(String value)
    {
        this.qualifier = value;
    }


    public String getEditorCode()
    {
        return this.editorCode;
    }


    public void setEditorCode(String value)
    {
        this.editorCode = value;
    }
}
