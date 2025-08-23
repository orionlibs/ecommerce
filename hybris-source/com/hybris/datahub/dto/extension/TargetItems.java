package com.hybris.datahub.dto.extension;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"item"})
public class TargetItems
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


    public String toString()
    {
        return "TargetItems{item=" + this.item + "}";
    }
}
