package de.hybris.platform.audit.internal.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;

public class VirtualAttributes
{
    @XmlElement(name = "virtual-attribute")
    protected List<VirtualAttribute> attributes;


    public VirtualAttributes()
    {
    }


    public VirtualAttributes(List<VirtualAttribute> attributes)
    {
        this.attributes = attributes;
    }


    public List<VirtualAttribute> getAttributes()
    {
        return (this.attributes == null) ? Collections.<VirtualAttribute>emptyList() : new ArrayList<>(this.attributes);
    }
}
