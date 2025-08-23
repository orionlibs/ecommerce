package de.hybris.platform.commercewebservicescommons.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "MemberList", description = "Representation of a Member List")
public class MemberListWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "members", value = "List of member")
    private List<PrincipalWsDTO> members;


    public void setMembers(List<PrincipalWsDTO> members)
    {
        this.members = members;
    }


    public List<PrincipalWsDTO> getMembers()
    {
        return this.members;
    }
}
