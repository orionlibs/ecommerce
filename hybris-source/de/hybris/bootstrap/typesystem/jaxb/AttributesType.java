package de.hybris.bootstrap.typesystem.jaxb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "attributesType", propOrder = {"attribute"})
public class AttributesType
{
    protected List<AttributeType> attribute;


    public List<AttributeType> getAttribute()
    {
        if(this.attribute == null)
        {
            this.attribute = new ArrayList<>();
        }
        return this.attribute;
    }
}
