package de.hybris.platform.cockpit.services.config.jaxb.listview;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "custom", propOrder = {"label"})
public class Custom extends ColumnConfiguration
{
    protected List<Label> label;
    @XmlAttribute(name = "class")
    protected String clazz;
    @XmlAttribute(name = "spring-bean")
    protected String springBean;
    @XmlAttribute
    protected Boolean selectable;


    public List<Label> getLabel()
    {
        if(this.label == null)
        {
            this.label = new ArrayList<>();
        }
        return this.label;
    }


    public String getClazz()
    {
        return this.clazz;
    }


    public void setClazz(String value)
    {
        this.clazz = value;
    }


    public String getSpringBean()
    {
        return this.springBean;
    }


    public void setSpringBean(String value)
    {
        this.springBean = value;
    }


    public boolean isSelectable()
    {
        if(this.selectable == null)
        {
            return false;
        }
        return this.selectable.booleanValue();
    }


    public void setSelectable(Boolean value)
    {
        this.selectable = value;
    }
}
