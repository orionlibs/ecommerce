package de.hybris.platform.cockpit.services.config.jaxb.editor;

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
    @XmlAttribute
    protected Boolean visible;
    @XmlAttribute
    protected String editor;
    @XmlAttribute
    protected String printoutas;
    @XmlAttribute
    protected String xmlDataProvider;


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


    public boolean isVisible()
    {
        if(this.visible == null)
        {
            return true;
        }
        return this.visible.booleanValue();
    }


    public void setVisible(Boolean value)
    {
        this.visible = value;
    }


    public String getEditor()
    {
        return this.editor;
    }


    public void setEditor(String value)
    {
        this.editor = value;
    }


    public String getPrintoutas()
    {
        return this.printoutas;
    }


    public void setPrintoutas(String value)
    {
        this.printoutas = value;
    }


    public String getXmlDataProvider()
    {
        if(this.xmlDataProvider == null)
        {
            return "xmlDataProvider";
        }
        return this.xmlDataProvider;
    }


    public void setXmlDataProvider(String value)
    {
        this.xmlDataProvider = value;
    }
}
