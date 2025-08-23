package de.hybris.platform.b2bwebservicescommons.dto.company;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "OrgCustomerCreation", description = "Representation of data used for user creation operations. Consists of fields used to create customer")
public class OrgCustomerCreationWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "email", value = "Email of the user", example = "akiro.nakamura@rustic-hw.com")
    private String email;
    @ApiModelProperty(name = "firstName", value = "First name of the user", example = "Akiro")
    private String firstName;
    @ApiModelProperty(name = "lastName", value = "Last name of the user", example = "Nakamura")
    private String lastName;
    @ApiModelProperty(name = "titleCode", value = "Code of the user's title", example = "mr")
    private String titleCode;
    @ApiModelProperty(name = "orgUnit", value = "The unit of the user", example = "Rustic")
    private B2BUnitWsDTO orgUnit;
    @ApiModelProperty(name = "roles", value = "List of organizational approvers")
    private List<String> roles;


    public void setEmail(String email)
    {
        this.email = email;
    }


    public String getEmail()
    {
        return this.email;
    }


    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }


    public String getFirstName()
    {
        return this.firstName;
    }


    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }


    public String getLastName()
    {
        return this.lastName;
    }


    public void setTitleCode(String titleCode)
    {
        this.titleCode = titleCode;
    }


    public String getTitleCode()
    {
        return this.titleCode;
    }


    public void setOrgUnit(B2BUnitWsDTO orgUnit)
    {
        this.orgUnit = orgUnit;
    }


    public B2BUnitWsDTO getOrgUnit()
    {
        return this.orgUnit;
    }


    public void setRoles(List<String> roles)
    {
        this.roles = roles;
    }


    public List<String> getRoles()
    {
        return this.roles;
    }
}
