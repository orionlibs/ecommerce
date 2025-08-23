package de.hybris.bootstrap.typesystem.jaxb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "enumtypesType", propOrder = {"enumtype"})
public class EnumtypesType
{
    protected List<EnumtypeType> enumtype;


    public List<EnumtypeType> getEnumtype()
    {
        if(this.enumtype == null)
        {
            this.enumtype = new ArrayList<>();
        }
        return this.enumtype;
    }
}
