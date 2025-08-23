package de.hybris.platform.audit.internal.config;

import de.hybris.platform.util.Config;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlTransient;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Type implements AuditReportItemNameResolvable
{
    @XmlID
    @XmlAttribute
    private String code;
    @XmlElement(name = "atomic-attributes")
    private AtomicAttributes atomicAttributes;
    @XmlElement(name = "reference-attributes")
    private ReferenceAttributes referenceAttributes;
    @XmlElement(name = "virtual-attributes")
    private VirtualAttributes virtualAttributes;
    @XmlElement(name = "relation-attributes")
    private RelationAttributes relationAttributes;
    @XmlAttribute
    private String displayName;
    @XmlAttribute
    private String displayKey;
    @XmlAttribute(name = "mode")
    private String combineMode;
    @XmlTransient
    private boolean valid = true;
    private static final Logger LOG = LoggerFactory.getLogger(Type.class);


    private Type()
    {
    }


    private Type(Builder builder)
    {
        this.code = builder.code;
        this.atomicAttributes = new AtomicAttributes(builder.atomicAttributes);
        this.referenceAttributes = new ReferenceAttributes(builder.referenceAttributes);
        this.virtualAttributes = new VirtualAttributes(builder.virtualAttributes);
        this.relationAttributes = new RelationAttributes(builder.relationAttributes);
        this.combineMode = builder.combineMode;
        this.displayKey = builder.displayKey;
        this.displayName = builder.displayName;
    }


    public static Builder builder()
    {
        return new Builder();
    }


    public List<AtomicAttribute> getAllAtomicAttributes()
    {
        return (this.atomicAttributes == null) ? Collections.<AtomicAttribute>emptyList() : new ArrayList<>(this.atomicAttributes.getAttributes());
    }


    public List<AtomicAttribute> getAtomicAttributes()
    {
        return (this.atomicAttributes == null) ? Collections.<AtomicAttribute>emptyList() :
                        new ArrayList(returnOnlyValidAttributes(this.atomicAttributes.getAttributes()));
    }


    public List<ReferenceAttribute> getAllReferenceAttributes()
    {
        return (this.referenceAttributes == null) ? Collections.<ReferenceAttribute>emptyList() : new ArrayList<>(this.referenceAttributes.getAttributes());
    }


    public List<ReferenceAttribute> getReferenceAttributes()
    {
        return (this.referenceAttributes == null) ? Collections.<ReferenceAttribute>emptyList() :
                        new ArrayList(returnOnlyValidAttributes(this.referenceAttributes.getAttributes()));
    }


    public List<VirtualAttribute> getAllVirtualAttributes()
    {
        return (this.virtualAttributes == null) ? Collections.<VirtualAttribute>emptyList() : new ArrayList<>(this.virtualAttributes.getAttributes());
    }


    public List<VirtualAttribute> getVirtualAttributes()
    {
        return (this.virtualAttributes == null) ? Collections.<VirtualAttribute>emptyList() :
                        new ArrayList(returnOnlyValidAttributes(this.virtualAttributes.getAttributes()));
    }


    private List<? extends AbstractAttribute> returnOnlyValidAttributes(List<? extends AbstractAttribute> attributes)
    {
        return (List<? extends AbstractAttribute>)attributes.stream().filter(AbstractAttribute::isValid).collect(Collectors.toList());
    }


    public List<RelationAttribute> getAllRelationAttributes()
    {
        return (this.relationAttributes == null) ? Collections.<RelationAttribute>emptyList() : new ArrayList<>(this.relationAttributes.getAttributes());
    }


    public List<RelationAttribute> getRelationAttributes()
    {
        return (this.relationAttributes == null) ? Collections.<RelationAttribute>emptyList() :
                        new ArrayList(returnOnlyValidAttributes(this.relationAttributes.getAttributes()));
    }


    public String getCode()
    {
        return this.code;
    }


    public String getCombineMode()
    {
        return this.combineMode;
    }


    @Deprecated(since = "1811", forRemoval = true)
    public String getTypeName()
    {
        String typeName;
        if(StringUtils.isNotEmpty(this.displayName))
        {
            typeName = this.displayName;
        }
        else if(StringUtils.isNotEmpty(this.displayKey))
        {
            typeName = getTypeNameFromProperties(this.displayKey);
            if(StringUtils.isEmpty(typeName))
            {
                LOG.warn("No value found for displayKey: {} in Configuration. Using code value ({}) as the type name instead.", this.displayKey, this.code);
                return this.code;
            }
        }
        else
        {
            typeName = this.code;
        }
        return typeName;
    }


    public String getDisplayKey()
    {
        return this.displayKey;
    }


    public String getDisplayName()
    {
        return this.displayName;
    }


    public String getDefaultName()
    {
        return this.code;
    }


    @XmlTransient
    public void setValid(boolean valid)
    {
        this.valid = valid;
    }


    public boolean isValid()
    {
        return this.valid;
    }


    String getTypeNameFromProperties(String displayKey)
    {
        return Config.getParameter(displayKey);
    }


    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(!(o instanceof Type))
        {
            return false;
        }
        Type type = (Type)o;
        return (Objects.equals(getCode(), type.getCode()) && Objects.equals(getAtomicAttributes(), type.getAtomicAttributes()) &&
                        Objects.equals(getReferenceAttributes(), type.getReferenceAttributes()) &&
                        Objects.equals(getVirtualAttributes(), type.getVirtualAttributes()));
    }


    public int hashCode()
    {
        return Objects.hash(new Object[] {getCode(), getAtomicAttributes(), getReferenceAttributes(), getVirtualAttributes()});
    }


    public String toString()
    {
        return (new ToStringBuilder(this)).append("code", this.code).toString();
    }
}
