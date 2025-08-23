package de.hybris.bootstrap.typesystem.jaxb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "itemtypesType", propOrder = {"itemtype", "typegroup"})
public class ItemtypesType
{
    protected List<ItemtypeType> itemtype;
    protected List<TypeGroupType> typegroup;


    public List<ItemtypeType> getItemtype()
    {
        if(this.itemtype == null)
        {
            this.itemtype = new ArrayList<>();
        }
        return this.itemtype;
    }


    public List<TypeGroupType> getTypegroup()
    {
        if(this.typegroup == null)
        {
            this.typegroup = new ArrayList<>();
        }
        return this.typegroup;
    }
}
