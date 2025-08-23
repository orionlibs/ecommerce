package de.hybris.bootstrap.typesystem.jaxb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "itemModelType", propOrder = {"constructor"})
public class ItemModelType
{
    protected List<ModelConstructorType> constructor;
    @XmlAttribute
    protected Boolean generate;


    public List<ModelConstructorType> getConstructor()
    {
        if(this.constructor == null)
        {
            this.constructor = new ArrayList<>();
        }
        return this.constructor;
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
