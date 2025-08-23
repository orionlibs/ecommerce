package de.hybris.platform.audit.internal.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;

public class RelationAttributes
{
    @XmlElement(name = "relation-attribute")
    protected List<RelationAttribute> attributes;


    public RelationAttributes()
    {
    }


    public RelationAttributes(List<RelationAttribute> attributes)
    {
        this.attributes = attributes;
    }


    public List<RelationAttribute> getAttributes()
    {
        return (this.attributes == null) ? Collections.<RelationAttribute>emptyList() : new ArrayList<>(this.attributes);
    }
}
