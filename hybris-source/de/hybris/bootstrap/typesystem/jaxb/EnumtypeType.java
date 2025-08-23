package de.hybris.bootstrap.typesystem.jaxb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "enumtypeType", propOrder = {"description", "model", "value"})
public class EnumtypeType
{
    protected String description;
    protected EnumModelType model;
    protected List<EnumValueType> value;
    @XmlAttribute(required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String code;
    @XmlAttribute
    protected Boolean autocreate;
    @XmlAttribute
    protected Boolean generate;
    @XmlAttribute
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String jaloclass;
    @XmlAttribute
    protected Boolean dynamic;


    public String getDescription()
    {
        return this.description;
    }


    public void setDescription(String value)
    {
        this.description = value;
    }


    public EnumModelType getModel()
    {
        return this.model;
    }


    public void setModel(EnumModelType value)
    {
        this.model = value;
    }


    public List<EnumValueType> getValue()
    {
        if(this.value == null)
        {
            this.value = new ArrayList<>();
        }
        return this.value;
    }


    public String getCode()
    {
        return this.code;
    }


    public void setCode(String value)
    {
        this.code = value;
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


    public String getJaloclass()
    {
        return this.jaloclass;
    }


    public void setJaloclass(String value)
    {
        this.jaloclass = value;
    }


    public Boolean isDynamic()
    {
        return this.dynamic;
    }


    public void setDynamic(Boolean value)
    {
        this.dynamic = value;
    }
}
