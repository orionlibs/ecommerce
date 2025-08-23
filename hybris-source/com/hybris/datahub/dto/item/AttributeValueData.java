package com.hybris.datahub.dto.item;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Set;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AttributeValueData
{
    private String isoCode;
    @XmlElement(name = "value")
    @JsonProperty("value")
    private Collection<?> value;


    public AttributeValueData()
    {
        this(new LinkedList());
    }


    public AttributeValueData(Collection<?> value)
    {
        this(null, value);
    }


    public AttributeValueData(String isoCode, Collection<?> value)
    {
        this.isoCode = (isoCode != null && isoCode.isEmpty()) ? null : isoCode;
        this.value = value;
    }


    public AttributeValueData(String isoCode, Object... values)
    {
        this(isoCode, Arrays.asList(values));
    }


    public String getIsoCode()
    {
        return this.isoCode;
    }


    public void setIsoCode(String isoCode)
    {
        this.isoCode = (isoCode != null && isoCode.isEmpty()) ? null : isoCode;
    }


    public Collection<?> getValue()
    {
        return this.value;
    }


    public void setValue(Collection<?> value)
    {
        this.value = new LinkedList();
        if(value != null)
        {
            this.value = value;
        }
    }


    public boolean isSame(AttributeValueData other)
    {
        if(other != null && Objects.equals(other.isoCode, this.isoCode) && this.value.size() == other.value.size())
        {
            Set<Object> valueSet = new HashSet(this.value);
            Set<Object> otherSet = new HashSet(other.value);
            return valueSet.equals(otherSet);
        }
        return false;
    }


    public String toString()
    {
        return ((this.isoCode == null) ? "" : (this.isoCode + ":")) + ((this.isoCode == null) ? "" : (this.isoCode + ":"));
    }


    public boolean equals(Object other)
    {
        return (other instanceof AttributeValueData && isSame((AttributeValueData)other));
    }


    public int hashCode()
    {
        String isoHash = (this.isoCode != null) ? String.valueOf(this.isoCode.hashCode()) : "";
        String hash = isoHash + "\n" + isoHash;
        return hash.hashCode();
    }
}
