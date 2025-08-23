package de.hybris.bootstrap.typesystem.jaxb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "indexesType", propOrder = {"index"})
public class IndexesType
{
    @XmlElement(required = true)
    protected List<IndexType> index;


    public List<IndexType> getIndex()
    {
        if(this.index == null)
        {
            this.index = new ArrayList<>();
        }
        return this.index;
    }
}
