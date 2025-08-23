package de.hybris.bootstrap.typesystem.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "modifiersType")
public class ModifiersType
{
    @XmlAttribute
    protected Boolean read;
    @XmlAttribute
    protected Boolean write;
    @XmlAttribute
    protected Boolean search;
    @XmlAttribute
    protected Boolean optional;
    @XmlAttribute(name = "private")
    protected Boolean _private;
    @XmlAttribute
    protected Boolean initial;
    @XmlAttribute
    protected Boolean removable;
    @XmlAttribute
    protected Boolean partof;
    @XmlAttribute
    protected Boolean unique;
    @XmlAttribute
    protected Boolean dontOptimize;
    @XmlAttribute
    protected Boolean encrypted;


    public Boolean isRead()
    {
        return this.read;
    }


    public void setRead(Boolean value)
    {
        this.read = value;
    }


    public Boolean isWrite()
    {
        return this.write;
    }


    public void setWrite(Boolean value)
    {
        this.write = value;
    }


    public Boolean isSearch()
    {
        return this.search;
    }


    public void setSearch(Boolean value)
    {
        this.search = value;
    }


    public Boolean isOptional()
    {
        return this.optional;
    }


    public void setOptional(Boolean value)
    {
        this.optional = value;
    }


    public Boolean isPrivate()
    {
        return this._private;
    }


    public void setPrivate(Boolean value)
    {
        this._private = value;
    }


    public Boolean isInitial()
    {
        return this.initial;
    }


    public void setInitial(Boolean value)
    {
        this.initial = value;
    }


    public Boolean isRemovable()
    {
        return this.removable;
    }


    public void setRemovable(Boolean value)
    {
        this.removable = value;
    }


    public Boolean isPartof()
    {
        return this.partof;
    }


    public void setPartof(Boolean value)
    {
        this.partof = value;
    }


    public Boolean isUnique()
    {
        return this.unique;
    }


    public void setUnique(Boolean value)
    {
        this.unique = value;
    }


    public Boolean isDontOptimize()
    {
        return this.dontOptimize;
    }


    public void setDontOptimize(Boolean value)
    {
        this.dontOptimize = value;
    }


    public Boolean isEncrypted()
    {
        return this.encrypted;
    }


    public void setEncrypted(Boolean value)
    {
        this.encrypted = value;
    }
}
