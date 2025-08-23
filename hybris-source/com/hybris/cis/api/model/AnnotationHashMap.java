package com.hybris.cis.api.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class AnnotationHashMap
{
    @XmlElement(name = "parameter")
    @Valid
    private List<AnnotationHashMapEntryType> entries = new ArrayList<>();


    public AnnotationHashMap()
    {
    }


    public AnnotationHashMap(Map<String, String> map)
    {
        for(Map.Entry<String, String> e : map.entrySet())
        {
            this.entries.add(new AnnotationHashMapEntryType(e));
        }
    }


    public Map<String, String> getMap()
    {
        HashMap<String, String> res = new HashMap<>();
        for(AnnotationHashMapEntryType entry : this.entries)
        {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }


    public List<AnnotationHashMapEntryType> getEntries()
    {
        return this.entries;
    }


    public void setEntries(List<AnnotationHashMapEntryType> entries)
    {
        this.entries = entries;
    }
}
