package de.hybris.bootstrap.beangenerator.definitions.model;

import java.util.Collection;
import java.util.Collections;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class EnumPrototype extends AttributeContainer<EnumValuePrototype>
{
    private final String description;
    private final String deprecated;
    private final String deprecatedSince;


    public EnumPrototype(String extensionName, String className, String description, String deprecated, String deprecatedSince)
    {
        super(extensionName, className);
        this.description = description;
        this.deprecated = deprecated;
        this.deprecatedSince = deprecatedSince;
    }


    public String getDescription()
    {
        return this.description;
    }


    public String getDeprecated()
    {
        return this.deprecated;
    }


    public String getDeprecatedSince()
    {
        return this.deprecatedSince;
    }


    public Collection<EnumValuePrototype> getValues()
    {
        return Collections.unmodifiableCollection(this.attributes.values());
    }


    public void addAttribute(EnumValuePrototype attribute)
    {
        if(!this.attributes.containsKey(attribute.getName()))
        {
            this.attributes.put(attribute.getName(), attribute);
        }
    }


    public String toString()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
    }
}
