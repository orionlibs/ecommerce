package de.hybris.bootstrap.typesystem.jaxb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "indexType", propOrder = {"key"})
public class IndexType
{
    protected List<IndexKeyType> key;
    @XmlAttribute(required = true)
    protected String name;
    @XmlAttribute
    protected Boolean remove;
    @XmlAttribute
    protected Boolean replace;
    @XmlAttribute
    protected Boolean unique;


    public List<IndexKeyType> getKey()
    {
        if(this.key == null)
        {
            this.key = new ArrayList<>();
        }
        return this.key;
    }


    public String getName()
    {
        return this.name;
    }


    public void setName(String value)
    {
        this.name = value;
    }


    public boolean isRemove()
    {
        if(this.remove == null)
        {
            return false;
        }
        return this.remove.booleanValue();
    }


    public void setRemove(Boolean value)
    {
        this.remove = value;
    }


    public boolean isReplace()
    {
        if(this.replace == null)
        {
            return false;
        }
        return this.replace.booleanValue();
    }


    public void setReplace(Boolean value)
    {
        this.replace = value;
    }


    public Boolean isUnique()
    {
        return this.unique;
    }


    public void setUnique(Boolean value)
    {
        this.unique = value;
    }
}
