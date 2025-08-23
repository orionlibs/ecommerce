package com.hybris.datahub.dto.extension;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import org.springframework.core.io.Resource;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"dependencies", "canonicalItems", "rawItems", "targetSystems"})
@XmlRootElement(name = "extension")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Extension
{
    private Dependencies dependencies;
    @XmlElement(required = true)
    private CanonicalItems canonicalItems = new CanonicalItems();
    private RawItems rawItems = new RawItems();
    private TargetSystems targetSystems = new TargetSystems();
    @XmlAttribute(name = "name")
    private String name;
    @XmlAttribute(name = "version")
    private String version;
    @XmlAttribute(name = "originator")
    private String originator;
    @XmlTransient
    private Resource resource;


    public static Extension extension()
    {
        return new Extension();
    }


    public boolean containsDependencies()
    {
        return (this.dependencies != null && !this.dependencies.getDependencyList().isEmpty());
    }


    public Dependencies getDependencies()
    {
        return this.dependencies;
    }


    public void setDependencies(Dependencies value)
    {
        this.dependencies = value;
    }


    public boolean containsCanonicalItems()
    {
        return !this.canonicalItems.isEmpty();
    }


    public CanonicalItems getCanonicalItems()
    {
        return this.canonicalItems;
    }


    public void setCanonicalItems(CanonicalItems value)
    {
        if(value != null)
        {
            this.canonicalItems = value;
        }
    }


    public Extension withItemType(CanonicalItems.Item type)
    {
        this.canonicalItems.getItemList().add(type);
        return this;
    }


    public RawItems getRawItems()
    {
        return this.rawItems;
    }


    public void setRawItems(RawItems value)
    {
        if(value != null)
        {
            this.rawItems = value;
        }
    }


    public boolean containsRawItems()
    {
        return !this.rawItems.isEmpty();
    }


    public Extension withItemType(RawItems.Item type)
    {
        this.rawItems.getItemList().add(type);
        return this;
    }


    public boolean containsTargetSystems()
    {
        return !this.targetSystems.isEmpty();
    }


    public TargetSystems getTargetSystems()
    {
        return this.targetSystems;
    }


    public void setTargetSystems(TargetSystems value)
    {
        if(value != null)
        {
            this.targetSystems = value;
        }
    }


    public Extension withTargetSystem(TargetSystem system)
    {
        this.targetSystems.getTargetSystemList().add(system);
        return this;
    }


    public String getName()
    {
        return this.name;
    }


    public void setName(String value)
    {
        this.name = value;
    }


    public Extension named(String n)
    {
        setName(n);
        return this;
    }


    String getVersion()
    {
        return this.version;
    }


    public void setVersion(String value)
    {
        this.version = value;
    }


    String getOriginator()
    {
        return this.originator;
    }


    public void setOriginator(String value)
    {
        this.originator = value;
    }


    public boolean matchesExpected(Extension expected)
    {
        if(expected == null)
        {
            return false;
        }
        boolean matchesName = (expected.getName() == null || expected.getName().equals(this.name));
        boolean matchesVersion = (expected.getVersion() == null || expected.getVersion().equals(this.version));
        boolean matchesOriginator = (expected.getOriginator() == null || expected.getOriginator().equals(this.originator));
        return (matchesName && matchesVersion && matchesOriginator);
    }


    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o instanceof Extension)
        {
            Extension extension = (Extension)o;
            return (this.name != null) ? this.name.equals(extension.name) : ((extension.name == null));
        }
        return false;
    }


    public int hashCode()
    {
        int result = (this.name != null) ? this.name.hashCode() : 0;
        result = 31 * result + ((this.version != null) ? this.version.hashCode() : 0);
        return result;
    }


    public String toString()
    {
        return "Extension{dependencies=" + this.dependencies + ", canonicalItems=" + this.canonicalItems + ", rawItems=" + this.rawItems + ", targetSystems=" + this.targetSystems + ", name='" + this.name + "', version='" + this.version + "', originator='" + this.originator + "'}";
    }


    public Resource getResource()
    {
        return this.resource;
    }


    public void setResource(Resource resource)
    {
        this.resource = resource;
    }
}
