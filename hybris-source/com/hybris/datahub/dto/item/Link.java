package com.hybris.datahub.dto.item;

import java.net.URI;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {"rel", "href"})
public class Link
{
    private URI href;
    private Type rel;


    public Link()
    {
    }


    public Link(Type rel, URI href)
    {
        this.rel = rel;
        this.href = href;
    }


    @XmlAttribute
    public Type getRel()
    {
        return this.rel;
    }


    public void setRel(Type rel)
    {
        this.rel = rel;
    }


    @XmlAttribute
    public URI getHref()
    {
        return this.href;
    }


    public void setHref(URI href)
    {
        this.href = href;
    }
}
