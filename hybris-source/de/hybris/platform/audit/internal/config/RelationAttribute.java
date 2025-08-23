package de.hybris.platform.audit.internal.config;

import javax.xml.bind.annotation.XmlAttribute;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

public class RelationAttribute extends AbstractTypedAttribute
{
    @XmlAttribute
    private String relation;


    public RelationAttribute()
    {
    }


    private RelationAttribute(Builder builder)
    {
        super(builder.displayKey, builder.displayName, builder.type, builder.many, builder.resolvesBy);
        this.relation = builder.relation;
    }


    public static Builder builder()
    {
        return new Builder();
    }


    public String getAttributeName()
    {
        String attributeName = super.getAttributeName();
        return StringUtils.isNotEmpty(attributeName) ? attributeName : this.relation;
    }


    public String getRelation()
    {
        return this.relation;
    }


    public String toString()
    {
        return (new ToStringBuilder(this))
                        .append("relation", this.relation)
                        .append("attributeName", getDefaultName())
                        .append("type", getType())
                        .append("resolvesBy", getResolvesBy())
                        .append("displayKey", getDisplayKey())
                        .append("displayName", getDisplayName())
                        .toString();
    }


    public String getDefaultName()
    {
        return this.relation;
    }
}
