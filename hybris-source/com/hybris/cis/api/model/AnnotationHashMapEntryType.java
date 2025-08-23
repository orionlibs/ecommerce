package com.hybris.cis.api.model;

import com.hybris.cis.api.validation.XSSSafe;
import java.util.Map;
import javax.xml.bind.annotation.XmlAttribute;

public class AnnotationHashMapEntryType
{
    @XmlAttribute(name = "key")
    @XSSSafe
    private String key;
    @XmlAttribute(name = "value")
    @XSSSafe
    private String value;


    public AnnotationHashMapEntryType()
    {
    }


    public AnnotationHashMapEntryType(Map.Entry<String, String> entry)
    {
        this.key = entry.getKey();
        this.value = entry.getValue();
    }


    public String getKey()
    {
        return this.key;
    }


    public String getValue()
    {
        return this.value;
    }
}
