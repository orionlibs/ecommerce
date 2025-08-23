package de.hybris.bootstrap.typesystem.jaxb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "relationType", propOrder = {"content"})
public class RelationType
{
    @XmlElementRefs({@XmlElementRef(name = "deployment", type = JAXBElement.class), @XmlElementRef(name = "description", type = JAXBElement.class), @XmlElementRef(name = "targetElement", type = JAXBElement.class), @XmlElementRef(name = "sourceElement", type = JAXBElement.class)})
    @XmlMixed
    protected List<Serializable> content;
    @XmlAttribute(required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String code;
    @XmlAttribute(required = true)
    protected boolean localized;
    @XmlAttribute
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String deployment;
    @XmlAttribute
    protected Boolean autocreate;
    @XmlAttribute
    protected Boolean generate;


    public List<Serializable> getContent()
    {
        if(this.content == null)
        {
            this.content = new ArrayList<>();
        }
        return this.content;
    }


    public String getCode()
    {
        return this.code;
    }


    public void setCode(String value)
    {
        this.code = value;
    }


    public boolean isLocalized()
    {
        return this.localized;
    }


    public void setLocalized(boolean value)
    {
        this.localized = value;
    }


    public String getDeployment()
    {
        return this.deployment;
    }


    public void setDeployment(String value)
    {
        this.deployment = value;
    }


    public boolean isAutocreate()
    {
        if(this.autocreate == null)
        {
            return true;
        }
        return this.autocreate.booleanValue();
    }


    public void setAutocreate(Boolean value)
    {
        this.autocreate = value;
    }


    public boolean isGenerate()
    {
        if(this.generate == null)
        {
            return true;
        }
        return this.generate.booleanValue();
    }


    public void setGenerate(Boolean value)
    {
        this.generate = value;
    }
}
