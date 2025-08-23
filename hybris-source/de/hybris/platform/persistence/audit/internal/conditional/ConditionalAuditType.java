package de.hybris.platform.persistence.audit.internal.conditional;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class ConditionalAuditType
{
    @XmlAttribute
    private String code;
    @XmlAttribute
    private boolean conditional = true;
    @XmlAttribute
    private boolean subtypes = false;
    @XmlAttribute
    private String attribute;
    @XmlElement(name = "type")
    private final List<ConditionalAuditType> children;
    ConditionalAuditType parent;


    private ConditionalAuditType()
    {
        this.children = new ArrayList<>();
    }


    public ConditionalAuditType(String code, boolean conditional, boolean subtypes, String attribute, List<ConditionalAuditType> children, ConditionalAuditType parent)
    {
        this.code = code;
        this.conditional = conditional;
        this.subtypes = subtypes;
        this.attribute = attribute;
        this.children = children;
        this.parent = parent;
    }


    public String getCode()
    {
        return this.code;
    }


    public boolean isConditional()
    {
        return this.conditional;
    }


    public boolean isSubtypes()
    {
        return this.subtypes;
    }


    public String getAttribute()
    {
        return this.attribute;
    }


    public List<ConditionalAuditType> getChildren()
    {
        return this.children;
    }


    public ConditionalAuditType getParent()
    {
        return this.parent;
    }
}
