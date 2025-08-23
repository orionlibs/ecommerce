package de.hybris.platform.cmscockpit.services.config.jaxb.contenteditor;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "property", propOrder = {"parameter"})
public class Property
{
    protected List<Parameter> parameter;
    @XmlAttribute(required = true)
    protected String qualifier;
    @XmlAttribute(required = true)
    protected String editorCode;
    @XmlAttribute
    protected Boolean visible = Boolean.TRUE;


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


    public void setVisible(Boolean visible)
    {
        this.visible = visible;
    }


    public Boolean isVisible()
    {
        return this.visible;
    }
}
