package de.hybris.bootstrap.typesystem.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"atomictypes", "collectiontypes", "enumtypes", "maptypes", "relations", "itemtypes"})
@XmlRootElement(name = "items")
public class Items
{
    protected AtomictypesType atomictypes;
    protected CollectiontypesType collectiontypes;
    protected EnumtypesType enumtypes;
    protected MaptypesType maptypes;
    protected RelationsType relations;
    protected ItemtypesType itemtypes;


    public AtomictypesType getAtomictypes()
    {
        return this.atomictypes;
    }


    public void setAtomictypes(AtomictypesType value)
    {
        this.atomictypes = value;
    }


    public CollectiontypesType getCollectiontypes()
    {
        return this.collectiontypes;
    }


    public void setCollectiontypes(CollectiontypesType value)
    {
        this.collectiontypes = value;
    }


    public EnumtypesType getEnumtypes()
    {
        return this.enumtypes;
    }


    public void setEnumtypes(EnumtypesType value)
    {
        this.enumtypes = value;
    }


    public MaptypesType getMaptypes()
    {
        return this.maptypes;
    }


    public void setMaptypes(MaptypesType value)
    {
        this.maptypes = value;
    }


    public RelationsType getRelations()
    {
        return this.relations;
    }


    public void setRelations(RelationsType value)
    {
        this.relations = value;
    }


    public ItemtypesType getItemtypes()
    {
        return this.itemtypes;
    }


    public void setItemtypes(ItemtypesType value)
    {
        this.itemtypes = value;
    }
}
