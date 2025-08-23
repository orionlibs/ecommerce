package de.hybris.bootstrap.config.tenants;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"tenant"})
@XmlRootElement(name = "tenants")
public class Tenants
{
    protected List<Tenant> tenant;


    public Tenants()
    {
    }


    Tenants(List<Tenant> tenantList)
    {
        this.tenant = tenantList;
    }


    public List<Tenant> getTenants()
    {
        if(this.tenant == null)
        {
            this.tenant = new ArrayList<>();
        }
        return this.tenant;
    }
}
