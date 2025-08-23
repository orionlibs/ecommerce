package de.hybris.platform.cockpit.services.config.jaxb.base;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "label", propOrder = {"property"})
public class Label
{
    protected List<Property> property;
    @XmlAttribute(name = "spring-bean")
    protected String springBean;
    @XmlAttribute(name = "class")
    protected String clazz;


    public List<Property> getProperty()
    {
        if(this.property == null)
        {
            this.property = new ArrayList<>();
        }
        return this.property;
    }


    public String getSpringBean()
    {
        return this.springBean;
    }


    public void setSpringBean(String value)
    {
        this.springBean = value;
    }


    public String getClazz()
    {
        return this.clazz;
    }


    public void setClazz(String value)
    {
        this.clazz = value;
    }
}
