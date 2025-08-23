package com.hybris.datahub.dto.extension;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"itemList"})
public class CanonicalItems
{
    @XmlElement(name = "item")
    private List<Item> itemList;


    public boolean isEmpty()
    {
        return getItemList().isEmpty();
    }


    public List<Item> getItemList()
    {
        if(this.itemList == null)
        {
            this.itemList = new ArrayList<>();
        }
        return this.itemList;
    }


    public Item getItem(String type)
    {
        Item returnItem = null;
        for(Item item : this.itemList)
        {
            if(item.getType().equals(type))
            {
                returnItem = item;
                break;
            }
        }
        return returnItem;
    }


    public CanonicalItems withItem(Item item)
    {
        getItemList().add(item);
        return this;
    }


    public String toString()
    {
        return "CanonicalItems{itemList=" + this.itemList + "}";
    }
}
