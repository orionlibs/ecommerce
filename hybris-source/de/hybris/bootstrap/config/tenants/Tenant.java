package de.hybris.bootstrap.config.tenants;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tenant", propOrder = {"param"})
public class Tenant
{
    protected List<Param> param;
    @XmlAttribute(required = true)
    protected String id;


    public Tenant()
    {
    }


    Tenant(List<Param> params)
    {
        this.param = params;
    }


    public List<Param> getParams()
    {
        if(this.param == null)
        {
            this.param = new ArrayList<>();
        }
        return this.param;
    }


    public String getId()
    {
        return this.id;
    }


    public void setId(String value)
    {
        this.id = value;
    }
}
