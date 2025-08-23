package de.hybris.platform.audit.internal.config;

import javax.xml.bind.annotation.XmlAttribute;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

public class VirtualAttribute extends AbstractTypedAttribute
{
    @XmlAttribute
    private String expression;


    public VirtualAttribute()
    {
    }


    public VirtualAttribute(Builder builder)
    {
        super(builder.displayKey, builder.displayName, builder.type, builder.many, builder.resolvesBy);
        this.expression = builder.expression;
    }


    public String getAttributeName()
    {
        String attributeName = super.getAttributeName();
        return StringUtils.isNotEmpty(attributeName) ? attributeName : this.expression;
    }


    public String getExpression()
    {
        return this.expression;
    }


    public String toString()
    {
        return (new ToStringBuilder(this))
                        .append("expression", this.expression)
                        .append("attributeName", getDefaultName())
                        .append("type", getType())
                        .append("many", getMany())
                        .append("resolvesBy", getResolvesBy())
                        .append("handlesMany", handlesMany())
                        .append("displayKey", getDisplayKey())
                        .append("displayName", getDisplayName())
                        .toString();
    }


    public String getDefaultName()
    {
        return this.expression;
    }


    public static Builder builder()
    {
        return new Builder();
    }
}
