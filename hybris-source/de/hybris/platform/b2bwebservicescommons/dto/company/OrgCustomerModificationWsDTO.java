package de.hybris.platform.b2bwebservicescommons.dto.company;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "OrgCustomerModification", description = "Representation of data used for user modification operations. Consists of fields used to modify customer")
public class OrgCustomerModificationWsDTO extends OrgCustomerCreationWsDTO
{
    @ApiModelProperty(name = "active", value = "Boolean flag of whether the user is active/enabled or not", example = "true")
    private Boolean active;
    @ApiModelProperty(name = "password", value = "Password of the user")
    private String password;


    public void setActive(Boolean active)
    {
        this.active = active;
    }


    public Boolean getActive()
    {
        return this.active;
    }


    public void setPassword(String password)
    {
        this.password = password;
    }


    public String getPassword()
    {
        return this.password;
    }
}
