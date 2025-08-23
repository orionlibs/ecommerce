package com.hybris.datahub.dto.count;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "itemStatusCounts")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemStatusCountData
{
    private RawItemStatusCountData rawItemStatusCounts = new RawItemStatusCountData();
    private CanonicalItemStatusCountData canonicalItemStatusCounts = new CanonicalItemStatusCountData();
    private CanonicalPublicationStatusCountData canonicalPublicationStatusCounts = new CanonicalPublicationStatusCountData();


    public RawItemStatusCountData getRawItemStatusCounts()
    {
        return this.rawItemStatusCounts;
    }


    public void setRawItemStatusCounts(RawItemStatusCountData cnt)
    {
        this.rawItemStatusCounts = (cnt != null) ? cnt : new RawItemStatusCountData();
    }


    public CanonicalItemStatusCountData getCanonicalItemStatusCounts()
    {
        return this.canonicalItemStatusCounts;
    }


    public void setCanonicalItemStatusCounts(CanonicalItemStatusCountData counts)
    {
        this.canonicalItemStatusCounts = (counts != null) ? counts : new CanonicalItemStatusCountData();
    }


    public CanonicalPublicationStatusCountData getCanonicalPublicationStatusCounts()
    {
        return this.canonicalPublicationStatusCounts;
    }


    public void setCanonicalPublicationStatusCounts(CanonicalPublicationStatusCountData counts)
    {
        this.canonicalPublicationStatusCounts = (counts != null) ? counts : new CanonicalPublicationStatusCountData();
    }


    public String toString()
    {
        return "ItemStatusCountData{\n\t" + this.rawItemStatusCounts + ",\n\t" + this.canonicalItemStatusCounts + ",\n\t" + this.canonicalPublicationStatusCounts + "\n}";
    }
}
