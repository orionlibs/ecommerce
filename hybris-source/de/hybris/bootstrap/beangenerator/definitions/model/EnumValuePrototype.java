package de.hybris.bootstrap.beangenerator.definitions.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class EnumValuePrototype extends AttributePrototype
{
    public EnumValuePrototype(String extensionName, String name, String description)
    {
        super(extensionName, name, description);
    }


    public String toString()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
    }
}
