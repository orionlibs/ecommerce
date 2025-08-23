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
@XmlType(name = "itemtypeType", propOrder = {"content"})
public class ItemtypeType
{
    @XmlElementRefs({@XmlElementRef(name = "indexes", type = JAXBElement.class), @XmlElementRef(name = "description", type = JAXBElement.class), @XmlElementRef(name = "deployment", type = JAXBElement.class), @XmlElementRef(name = "attributes", type = JAXBElement.class),
                    @XmlElementRef(name = "custom-properties", type = JAXBElement.class), @XmlElementRef(name = "model", type = JAXBElement.class)})
    @XmlMixed
    protected List<Serializable> content;
    @XmlAttribute(required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String code;
    @XmlAttribute(name = "extends")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String _extends;
    @XmlAttribute
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String jaloclass;
    @XmlAttribute
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String deployment;
    @XmlAttribute
    protected Boolean singleton;
    @XmlAttribute
    protected Boolean jaloonly;
    @XmlAttribute
    protected Boolean autocreate;
    @XmlAttribute
    protected Boolean generate;
    @XmlAttribute(name = "abstract")
    protected Boolean _abstract;
    @XmlAttribute
    protected String metatype;


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


    public String getExtends()
    {
        return this._extends;
    }


    public void setExtends(String value)
    {
        this._extends = value;
    }


    public String getJaloclass()
    {
        return this.jaloclass;
    }


    public void setJaloclass(String value)
    {
        this.jaloclass = value;
    }


    public String getDeployment()
    {
        return this.deployment;
    }


    public void setDeployment(String value)
    {
        this.deployment = value;
    }


    public Boolean isSingleton()
    {
        return this.singleton;
    }


    public void setSingleton(Boolean value)
    {
        this.singleton = value;
    }


    public Boolean isJaloonly()
    {
        return this.jaloonly;
    }


    public void setJaloonly(Boolean value)
    {
        this.jaloonly = value;
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


    public Boolean isAbstract()
    {
        return this._abstract;
    }


    public void setAbstract(Boolean value)
    {
        this._abstract = value;
    }


    public String getMetatype()
    {
        return this.metatype;
    }


    public void setMetatype(String value)
    {
        this.metatype = value;
    }
}
