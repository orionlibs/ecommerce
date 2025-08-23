package de.hybris.platform.audit.internal.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;

public class ReferenceAttributes
{
    @XmlElement(name = "reference-attribute")
    protected List<ReferenceAttribute> attributes;


    public ReferenceAttributes()
    {
    }


    public ReferenceAttributes(List<ReferenceAttribute> attributes)
    {
        this.attributes = attributes;
    }


    public List<ReferenceAttribute> getAttributes()
    {
        return (this.attributes == null) ? Collections.<ReferenceAttribute>emptyList() : new ArrayList<>(this.attributes);
    }
}
