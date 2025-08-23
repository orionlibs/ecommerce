package de.hybris.platform.audit.internal.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;

public class AtomicAttributes
{
    @XmlElement(name = "atomic-attribute")
    protected List<AtomicAttribute> attributes;


    public AtomicAttributes()
    {
    }


    public AtomicAttributes(List<AtomicAttribute> attributes)
    {
        this.attributes = attributes;
    }


    public List<AtomicAttribute> getAttributes()
    {
        return (this.attributes == null) ? Collections.<AtomicAttribute>emptyList() : new ArrayList<>(this.attributes);
    }
}
