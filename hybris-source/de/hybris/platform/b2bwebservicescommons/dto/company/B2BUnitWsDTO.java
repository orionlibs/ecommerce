package de.hybris.platform.b2bwebservicescommons.dto.company;

import de.hybris.platform.commercewebservicescommons.dto.user.AddressWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.user.UserWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "B2BUnit", description = "Representation of an organizational unit")
public class B2BUnitWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "active", value = "Boolean flag of whether Organizational Unit is active", example = "true")
    private Boolean active;
    @ApiModelProperty(name = "uid", value = "Identifier of the organizational unit", required = true, example = "Pronto")
    private String uid;
    @ApiModelProperty(name = "name", value = "Name of the organizational unit", example = "Pronto")
    private String name;
    @ApiModelProperty(name = "parentOrgUnit", value = "Parent unit of the organizational unit")
    private B2BUnitWsDTO parentOrgUnit;
    @ApiModelProperty(name = "approvalProcess", value = "Approval Process of the organizational unit node")
    private B2BApprovalProcessWsDTO approvalProcess;
    @ApiModelProperty(name = "addresses", value = "Addresses of the organizational unit node")
    private List<AddressWsDTO> addresses;
    @ApiModelProperty(name = "approvers", value = "Approvers of the organizational unit node")
    private List<UserWsDTO> approvers;
    @ApiModelProperty(name = "managers", value = "Managers of the organizational unit node")
    private List<UserWsDTO> managers;
    @ApiModelProperty(name = "administrators", value = "Administrators of the organizational unit node")
    private List<UserWsDTO> administrators;
    @ApiModelProperty(name = "customers", value = "Customers of the organizational unit node")
    private List<UserWsDTO> customers;
    @ApiModelProperty(name = "costCenters", value = "The cost centers of the organizational unit node")
    private List<B2BCostCenterShallowWsDTO> costCenters;
    @ApiModelProperty(name = "defaultUnit")
    private boolean defaultUnit;


    public void setActive(Boolean active)
    {
        this.active = active;
    }


    public Boolean getActive()
    {
        return this.active;
    }


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


    public void setParentOrgUnit(B2BUnitWsDTO parentOrgUnit)
    {
        this.parentOrgUnit = parentOrgUnit;
    }


    public B2BUnitWsDTO getParentOrgUnit()
    {
        return this.parentOrgUnit;
    }


    public void setApprovalProcess(B2BApprovalProcessWsDTO approvalProcess)
    {
        this.approvalProcess = approvalProcess;
    }


    public B2BApprovalProcessWsDTO getApprovalProcess()
    {
        return this.approvalProcess;
    }


    public void setAddresses(List<AddressWsDTO> addresses)
    {
        this.addresses = addresses;
    }


    public List<AddressWsDTO> getAddresses()
    {
        return this.addresses;
    }


    public void setApprovers(List<UserWsDTO> approvers)
    {
        this.approvers = approvers;
    }


    public List<UserWsDTO> getApprovers()
    {
        return this.approvers;
    }


    public void setManagers(List<UserWsDTO> managers)
    {
        this.managers = managers;
    }


    public List<UserWsDTO> getManagers()
    {
        return this.managers;
    }


    public void setAdministrators(List<UserWsDTO> administrators)
    {
        this.administrators = administrators;
    }


    public List<UserWsDTO> getAdministrators()
    {
        return this.administrators;
    }


    public void setCustomers(List<UserWsDTO> customers)
    {
        this.customers = customers;
    }


    public List<UserWsDTO> getCustomers()
    {
        return this.customers;
    }


    public void setCostCenters(List<B2BCostCenterShallowWsDTO> costCenters)
    {
        this.costCenters = costCenters;
    }


    public List<B2BCostCenterShallowWsDTO> getCostCenters()
    {
        return this.costCenters;
    }


    public void setDefaultUnit(boolean defaultUnit)
    {
        this.defaultUnit = defaultUnit;
    }


    public boolean isDefaultUnit()
    {
        return this.defaultUnit;
    }
}
