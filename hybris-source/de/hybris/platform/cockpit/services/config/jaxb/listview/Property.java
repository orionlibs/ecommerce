package de.hybris.platform.cockpit.services.config.jaxb.listview;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "property", propOrder = {"language", "parameter"})
public class Property extends ColumnConfiguration
{
    protected List<Language> language;
    protected List<Parameter> parameter;
    @XmlAttribute(required = true)
    protected String qualifier;
    @XmlAttribute
    protected Boolean selectable;
    @XmlAttribute
    protected String editor;


    public List<Language> getLanguage()
    {
        if(this.language == null)
        {
            this.language = new ArrayList<>();
        }
        return this.language;
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


    public boolean isSelectable()
    {
        if(this.selectable == null)
        {
            return true;
        }
        return this.selectable.booleanValue();
    }


    public void setSelectable(Boolean value)
    {
        this.selectable = value;
    }


    public String getEditor()
    {
        return this.editor;
    }


    public void setEditor(String value)
    {
        this.editor = value;
    }
}
