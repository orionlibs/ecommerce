package de.hybris.platform.audit.internal.config;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlIDREF;
import org.apache.commons.lang.builder.ToStringBuilder;

public abstract class AbstractTypedAttribute extends AbstractAttribute
{
    @XmlAttribute(name = "type")
    @XmlIDREF
    private Type type;
    @XmlAttribute
    private Boolean many;
    @XmlElement(name = "resolves-by")
    private ResolvesBy resolvesBy;


    public AbstractTypedAttribute()
    {
    }


    public AbstractTypedAttribute(String displayKey, String displayName, Type type, Boolean many, ResolvesBy resolvesBy)
    {
        super(displayKey, displayName);
        this.type = type;
        this.many = many;
        this.resolvesBy = resolvesBy;
    }


    public Type getType()
    {
        return this.type;
    }


    public Boolean getMany()
    {
        return this.many;
    }


    public ResolvesBy getResolvesBy()
    {
        return this.resolvesBy;
    }


    public boolean handlesMany()
    {
        return (this.many != null && this.many.booleanValue());
    }


    void setExtraResolvesBy(ResolvesBy resolvesBy)
    {
        this.resolvesBy = resolvesBy;
    }


    public String toString()
    {
        return (new ToStringBuilder(this))
                        .append("type", this.type)
                        .append("many", this.many)
                        .append("resolvesBy", this.resolvesBy)
                        .append("attributeName", getDefaultName())
                        .append("handlesMany", handlesMany())
                        .append("displayKey", getDisplayKey())
                        .append("displayName", getDisplayName())
                        .append("valid", isValid())
                        .toString();
    }
}
