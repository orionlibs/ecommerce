package de.hybris.bootstrap.beangenerator.definitions.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "import")
public class Import
{
    @XmlAttribute(name = "static", required = true)
    protected boolean asStatic;
    @XmlAttribute(required = true)
    protected String type;


    public String getType()
    {
        return this.type;
    }


    public void setType(String value)
    {
        this.type = value;
    }


    public boolean isAsStatic()
    {
        return this.asStatic;
    }


    public void setAsStatic(boolean asStatic)
    {
        this.asStatic = asStatic;
    }
}
