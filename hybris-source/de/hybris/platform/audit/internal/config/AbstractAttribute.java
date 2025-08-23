package de.hybris.platform.audit.internal.config;

import de.hybris.platform.util.Config;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractAttribute implements AuditReportItemNameResolvable
{
    @XmlAttribute
    protected String displayKey;
    @XmlAttribute
    protected String displayName;
    @XmlTransient
    private boolean valid = true;
    private static final Logger LOG = LoggerFactory.getLogger(AbstractAttribute.class);


    public AbstractAttribute()
    {
    }


    public AbstractAttribute(String displayKey, String displayName)
    {
        this.displayKey = displayKey;
        this.displayName = displayName;
    }


    public String getDisplayKey()
    {
        return this.displayKey;
    }


    public String getDisplayName()
    {
        return this.displayName;
    }


    public boolean isValid()
    {
        return this.valid;
    }


    @XmlTransient
    public void setValid(boolean valid)
    {
        this.valid = valid;
    }


    public String toString()
    {
        return (new ToStringBuilder(this))
                        .append("displayKey", this.displayKey)
                        .append("displayName", this.displayName)
                        .append("attributeName", getDefaultName())
                        .append("valid", isValid())
                        .toString();
    }


    @Deprecated(since = "1811", forRemoval = true)
    public String getAttributeName()
    {
        String attributeName;
        if(StringUtils.isNotEmpty(this.displayName))
        {
            attributeName = this.displayName;
        }
        else if(StringUtils.isNotEmpty(this.displayKey))
        {
            attributeName = getAttributeNameFromProperties(this.displayKey);
            if(StringUtils.isEmpty(attributeName))
            {
                LOG.warn("No value found for displayKey: {} in Configuration. Will fallback to different value as attribute name", this.displayKey);
            }
        }
        else
        {
            attributeName = "";
        }
        return attributeName;
    }


    String getAttributeNameFromProperties(String displayKey)
    {
        return Config.getParameter(displayKey);
    }
}
