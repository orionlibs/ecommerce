package com.hybris.datahub.dto.extension;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"item"})
public class RawItems
{
    private List<Item> item;


    public List<Item> getItemList()
    {
        if(this.item == null)
        {
            this.item = new ArrayList<>();
        }
        return this.item;
    }


    public boolean isEmpty()
    {
        return getItemList().isEmpty();
    }


    public RawItems withItem(Item item)
    {
        getItemList().add(item);
        return this;
    }


    public String toString()
    {
        return "RawItems{item=" + this.item + "}";
    }
}
