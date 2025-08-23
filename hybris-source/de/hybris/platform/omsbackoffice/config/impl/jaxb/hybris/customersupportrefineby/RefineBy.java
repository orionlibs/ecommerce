package de.hybris.platform.omsbackoffice.config.impl.jaxb.hybris.customersupportrefineby;

import com.hybris.cockpitng.core.config.annotations.Mergeable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"fieldList"})
@XmlRootElement(name = "refine-by")
public class RefineBy
{
    @Mergeable(key = {"name"})
    @XmlElement(name = "field-list", required = true)
    protected FieldList fieldList;


    public FieldList getFieldList()
    {
        return this.fieldList;
    }


    public void setFieldList(FieldList value)
    {
        this.fieldList = value;
    }
}
