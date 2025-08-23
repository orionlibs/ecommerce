package de.hybris.bootstrap.typesystem.jaxb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "attributeModelType", propOrder = {"getter", "setter"})
public class AttributeModelType
{
    protected List<ModelMethodType> getter;
    protected List<ModelMethodType> setter;
    @XmlAttribute
    protected Boolean generate;


    public List<ModelMethodType> getGetter()
    {
        if(this.getter == null)
        {
            this.getter = new ArrayList<>();
        }
        return this.getter;
    }


    public List<ModelMethodType> getSetter()
    {
        if(this.setter == null)
        {
            this.setter = new ArrayList<>();
        }
        return this.setter;
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
