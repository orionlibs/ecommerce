package de.hybris.platform.commercewebservicescommons.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

@ApiModel(value = "UserGroup", description = "Representation of an User Group")
public class UserGroupWsDTO extends PrincipalWsDTO
{
    @ApiModelProperty(name = "members", value = "List of members")
    private List<PrincipalWsDTO> members;
    @ApiModelProperty(name = "subGroups", value = "List of subgroups")
    private List<UserGroupWsDTO> subGroups;
    @ApiModelProperty(name = "membersCount", value = "Number of members")
    private Integer membersCount;


    public void setMembers(List<PrincipalWsDTO> members)
    {
        this.members = members;
    }


    public List<PrincipalWsDTO> getMembers()
    {
        return this.members;
    }


    public void setSubGroups(List<UserGroupWsDTO> subGroups)
    {
        this.subGroups = subGroups;
    }


    public List<UserGroupWsDTO> getSubGroups()
    {
        return this.subGroups;
    }


    public void setMembersCount(Integer membersCount)
    {
        this.membersCount = membersCount;
    }


    public Integer getMembersCount()
    {
        return this.membersCount;
    }
}
