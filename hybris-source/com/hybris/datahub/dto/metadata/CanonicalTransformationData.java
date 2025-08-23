package com.hybris.datahub.dto.metadata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Objects;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "CanonicalTransformation")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CanonicalTransformationData extends AttributeData
{
    private String rawItemType;
    private String expression;
    private boolean override;
    private boolean disabled;
    private boolean spel;


    public CanonicalTransformationData(String rawItemType, String rawAttribute, String itemType, String name, boolean isSecured, boolean override, boolean disabled)
    {
        setItemType(itemType);
        setName(name);
        setIsSecured(isSecured);
        this.rawItemType = rawItemType;
        this.expression = rawAttribute;
        this.override = override;
        this.disabled = disabled;
    }


    public CanonicalTransformationData(String rawItemType, String rawAttribute, String itemType, String name, boolean isSecured)
    {
        this(rawItemType, rawAttribute, itemType, name, isSecured, false, false);
    }


    public CanonicalTransformationData()
    {
        this(null, null, null, null, false, false, false);
    }


    public static CanonicalTransformationData transformation()
    {
        return new CanonicalTransformationData();
    }


    public String getRawItemType()
    {
        return this.rawItemType;
    }


    public void setRawItemType(String rawItemType)
    {
        this.rawItemType = rawItemType;
    }


    public CanonicalTransformationData fromType(String rawType)
    {
        setRawItemType(rawType);
        return this;
    }


    public String getExpression()
    {
        return this.expression;
    }


    public void setExpression(String expr)
    {
        this.expression = expr;
    }


    public CanonicalTransformationData withExpression(String attribute)
    {
        setExpression(attribute);
        return this;
    }


    public boolean isOverride()
    {
        return this.override;
    }


    public void setOverride(boolean override)
    {
        this.override = override;
    }


    public CanonicalTransformationData override()
    {
        setOverride(true);
        return this;
    }


    public boolean isDisabled()
    {
        return this.disabled;
    }


    public void setDisabled(boolean disabled)
    {
        this.disabled = disabled;
    }


    public CanonicalTransformationData disabled()
    {
        setDisabled(true);
        return this;
    }


    public CanonicalTransformationData forType(String type)
    {
        setItemType(type);
        return this;
    }


    public CanonicalTransformationData forAttribute(String name)
    {
        setName(name);
        return this;
    }


    public boolean isSpel()
    {
        return this.spel;
    }


    public void setSpel(boolean value)
    {
        this.spel = value;
    }


    public CanonicalTransformationData spel()
    {
        setSpel(true);
        return this;
    }


    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o instanceof CanonicalTransformationData)
        {
            CanonicalTransformationData that = (CanonicalTransformationData)o;
            return (super.equals(that) && Objects.equals(this.rawItemType, that.rawItemType));
        }
        return false;
    }


    public int hashCode()
    {
        return 31 * super.hashCode() + ((this.rawItemType != null) ? this.rawItemType.hashCode() : 0);
    }


    public String toString()
    {
        return "CanonicalTransformationData{rawItemType='" + this.rawItemType + "', expression='" + this.expression + "', override='" + this.override + "', disabled='" + this.disabled + "', spel='" + this.spel + "'}";
    }
}
