package de.hybris.platform.cockpit.services.config.jaxb.listview;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "cellRenderer")
public class CellRenderer extends ColumnConfiguration
{
    @XmlAttribute(name = "class")
    protected String clazz;
    @XmlAttribute(name = "spring-bean")
    protected String springBean;


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
}
