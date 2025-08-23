package de.hybris.bootstrap.typesystem.jaxb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "collectiontypesType", propOrder = {"collectiontype"})
public class CollectiontypesType
{
    protected List<CollectiontypeType> collectiontype;


    public List<CollectiontypeType> getCollectiontype()
    {
        if(this.collectiontype == null)
        {
            this.collectiontype = new ArrayList<>();
        }
        return this.collectiontype;
    }
}
