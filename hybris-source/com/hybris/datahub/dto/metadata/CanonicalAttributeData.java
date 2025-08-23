package com.hybris.datahub.dto.metadata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "CanonicalAttribute")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CanonicalAttributeData extends AttributeData
{
    private boolean isLocalizable;
    private boolean isCollection;
    private boolean isPrimaryKey;
    private String type;
    private Set<CanonicalTransformationData> transformations;


    public CanonicalAttributeData(String canonicalType, String canonicalAttr, boolean isSecured, boolean isCollection, boolean isLocalizable)
    {
        setName(canonicalAttr);
        setItemType(canonicalType);
        setIsSecured(isSecured);
        this.isCollection = isCollection;
        this.isLocalizable = isLocalizable;
    }


    public CanonicalAttributeData()
    {
        this(null, null, false, false, false);
    }


    public boolean getIsLocalizable()
    {
        return this.isLocalizable;
    }


    public void setIsLocalizable(boolean isLocalizable)
    {
        this.isLocalizable = isLocalizable;
    }


    public String getType()
    {
        return this.type;
    }


    public void setType(String type)
    {
        this.type = type;
    }


    public boolean getIsCollection()
    {
        return this.isCollection;
    }


    public void setIsCollection(boolean isCollection)
    {
        this.isCollection = isCollection;
    }


    public boolean getIsPrimaryKey()
    {
        return this.isPrimaryKey;
    }


    public void setIsPrimaryKey(boolean isPrimaryKey)
    {
        this.isPrimaryKey = isPrimaryKey;
    }


    public Set<CanonicalTransformationData> getTransformations()
    {
        if(this.transformations == null)
        {
            this.transformations = new HashSet<>();
        }
        return this.transformations;
    }


    public void setTransformations(Set<CanonicalTransformationData> transformations)
    {
        this.transformations = transformations;
    }


    public CanonicalAttributeData withTransformations(CanonicalTransformationData... data)
    {
        getTransformations().addAll(Arrays.asList(data));
        return this;
    }


    public CanonicalAttributeData withTransformation(CanonicalTransformationData data)
    {
        getTransformations().add(data);
        return this;
    }


    public CanonicalAttributeData forItemType(String type)
    {
        setItemType(type);
        return this;
    }


    public CanonicalAttributeData withName(String name)
    {
        setName(name);
        return this;
    }


    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(!(o instanceof CanonicalAttributeData))
        {
            return false;
        }
        if(!super.equals(o))
        {
            return false;
        }
        CanonicalAttributeData that = (CanonicalAttributeData)o;
        if(this.isCollection != that.isCollection)
        {
            return false;
        }
        if(this.isLocalizable != that.isLocalizable)
        {
            return false;
        }
        if(this.isPrimaryKey != that.isPrimaryKey)
        {
            return false;
        }
        if((this.transformations != null) ? !this.transformations.equals(that.transformations) : (that.transformations != null))
        {
            return false;
        }
        if((this.type != null) ? !this.type.equals(that.type) : (that.type != null))
        {
            return false;
        }
        return true;
    }


    public int hashCode()
    {
        int result = super.hashCode();
        result = 31 * result + (this.isLocalizable ? 1 : 0);
        result = 31 * result + (this.isCollection ? 1 : 0);
        result = 31 * result + (this.isPrimaryKey ? 1 : 0);
        result = 31 * result + ((this.type != null) ? this.type.hashCode() : 0);
        result = 31 * result + ((this.transformations != null) ? this.transformations.hashCode() : 0);
        return result;
    }
}
