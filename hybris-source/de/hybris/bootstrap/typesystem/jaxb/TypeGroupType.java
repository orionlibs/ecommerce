package de.hybris.bootstrap.typesystem.jaxb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "typeGroupType", propOrder = {"itemtype"})
public class TypeGroupType
{
    protected List<ItemtypeType> itemtype;
    @XmlAttribute
    protected String name;


    public List<ItemtypeType> getItemtype()
    {
        if(this.itemtype == null)
        {
            this.itemtype = new ArrayList<>();
        }
        return this.itemtype;
    }


    public String getName()
    {
        return this.name;
    }


    public void setName(String value)
    {
        this.name = value;
    }
}
