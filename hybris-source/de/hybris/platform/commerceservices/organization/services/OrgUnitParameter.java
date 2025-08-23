package de.hybris.platform.commerceservices.organization.services;

import de.hybris.platform.catalog.enums.LineOfBusiness;
import de.hybris.platform.commerceservices.model.OrgUnitModel;
import java.io.Serializable;

public class OrgUnitParameter implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String uid;
    private String name;
    private OrgUnitModel parentUnit;
    private Boolean active;
    private String description;
    private LineOfBusiness lineOfBusiness;
    private OrgUnitModel orgUnit;
    private Boolean buyer;
    private Boolean supplier;
    private Boolean manufacturer;
    private Boolean carrier;


    public void setUid(String uid)
    {
        this.uid = uid;
    }


    public String getUid()
    {
        return this.uid;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return this.name;
    }


    public void setParentUnit(OrgUnitModel parentUnit)
    {
        this.parentUnit = parentUnit;
    }


    public OrgUnitModel getParentUnit()
    {
        return this.parentUnit;
    }


    public void setActive(Boolean active)
    {
        this.active = active;
    }


    public Boolean getActive()
    {
        return this.active;
    }


    public void setDescription(String description)
    {
        this.description = description;
    }


    public String getDescription()
    {
        return this.description;
    }


    public void setLineOfBusiness(LineOfBusiness lineOfBusiness)
    {
        this.lineOfBusiness = lineOfBusiness;
    }


    public LineOfBusiness getLineOfBusiness()
    {
        return this.lineOfBusiness;
    }


    public void setOrgUnit(OrgUnitModel orgUnit)
    {
        this.orgUnit = orgUnit;
    }


    public OrgUnitModel getOrgUnit()
    {
        return this.orgUnit;
    }


    public void setBuyer(Boolean buyer)
    {
        this.buyer = buyer;
    }


    public Boolean getBuyer()
    {
        return this.buyer;
    }


    public void setSupplier(Boolean supplier)
    {
        this.supplier = supplier;
    }


    public Boolean getSupplier()
    {
        return this.supplier;
    }


    public void setManufacturer(Boolean manufacturer)
    {
        this.manufacturer = manufacturer;
    }


    public Boolean getManufacturer()
    {
        return this.manufacturer;
    }


    public void setCarrier(Boolean carrier)
    {
        this.carrier = carrier;
    }


    public Boolean getCarrier()
    {
        return this.carrier;
    }
}
