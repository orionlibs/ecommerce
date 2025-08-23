package de.hybris.platform.b2bwebservicescommons.dto.company;

import de.hybris.platform.commercewebservicescommons.dto.user.UserGroupWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

@ApiModel("OrgUnitUserGroup")
public class OrgUnitUserGroupWsDTO extends UserGroupWsDTO
{
    @ApiModelProperty(name = "orgUnit", value = "Organizational Unit of the user group")
    private B2BUnitWsDTO orgUnit;
    @ApiModelProperty(name = "permissions", value = "Order approval permission of the user group")
    private List<B2BPermissionWsDTO> permissions;
    @ApiModelProperty(name = "roles", value = "List of Roles")
    private List<String> roles;
    @ApiModelProperty(name = "selected", value = "Boolean flag of whether a user group is selected or not", example = "true")
    private Boolean selected;


    public void setOrgUnit(B2BUnitWsDTO orgUnit)
    {
        this.orgUnit = orgUnit;
    }


    public B2BUnitWsDTO getOrgUnit()
    {
        return this.orgUnit;
    }


    public void setPermissions(List<B2BPermissionWsDTO> permissions)
    {
        this.permissions = permissions;
    }


    public List<B2BPermissionWsDTO> getPermissions()
    {
        return this.permissions;
    }


    public void setRoles(List<String> roles)
    {
        this.roles = roles;
    }


    public List<String> getRoles()
    {
        return this.roles;
    }


    public void setSelected(Boolean selected)
    {
        this.selected = selected;
    }


    public Boolean getSelected()
    {
        return this.selected;
    }
}
