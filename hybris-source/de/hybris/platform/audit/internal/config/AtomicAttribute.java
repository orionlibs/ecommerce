package de.hybris.platform.audit.internal.config;

import de.hybris.bootstrap.util.LocaleHelper;
import javax.xml.bind.annotation.XmlAttribute;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

public class AtomicAttribute extends AbstractAttribute
{
    @XmlAttribute
    private String qualifier;


    public AtomicAttribute()
    {
    }


    private AtomicAttribute(Builder builder)
    {
        super(builder.displayKey, builder.displayName);
        this.qualifier = builder.qualifier;
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


    public String getDefaultName()
    {
        return this.qualifier;
    }


    public String toString()
    {
        return (new ToStringBuilder(this))
                        .append("qualifier", this.qualifier)
                        .append("attributeName", getDefaultName())
                        .append("displayKey", getDisplayKey())
                        .append("displayName", getDisplayName())
                        .toString();
    }
}
