package de.hybris.platform.audit.internal.config;

import de.hybris.bootstrap.util.LocaleHelper;
import javax.xml.bind.annotation.XmlAttribute;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

public class ReferenceAttribute extends AbstractTypedAttribute
{
    @XmlAttribute
    private String qualifier;


    public ReferenceAttribute()
    {
    }


    private ReferenceAttribute(Builder builder)
    {
        super(builder.displayKey, builder.displayName, builder.type, builder.many, builder.resolvesBy);
        this.qualifier = builder.qualifier;
    }


    public String getDefaultName()
    {
        return this.qualifier;
    }


    public static Builder builder()
    {
        return new Builder();
    }


    public String getAttributeName()
    {
        String attributeName = super.getAttributeName();
        return StringUtils.isNotEmpty(attributeName) ? attributeName : this.qualifier;
    }


    public String getQualifier()
    {
        return this.qualifier.toLowerCase(LocaleHelper.getPersistenceLocale());
    }


    public String toString()
    {
        return (new ToStringBuilder(this))
                        .append("qualifier", this.qualifier)
                        .append("attributeName", getDefaultName())
                        .append("type", getType())
                        .append("many", getMany())
                        .append("resolvesBy", getResolvesBy())
                        .append("handlesMany", handlesMany())
                        .append("displayKey", getDisplayKey())
                        .append("displayName", getDisplayName())
                        .toString();
    }
}
