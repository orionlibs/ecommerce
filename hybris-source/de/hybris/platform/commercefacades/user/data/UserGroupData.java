package de.hybris.platform.commercefacades.user.data;

import java.util.List;

public class UserGroupData extends PrincipalData
{
    private List<? extends PrincipalData> members;
    private List<? extends UserGroupData> subGroups;
    private Integer membersCount;


    public void setMembers(List<? extends PrincipalData> members)
    {
        this.members = members;
    }


    public List<? extends PrincipalData> getMembers()
    {
        return this.members;
    }


    public void setSubGroups(List<? extends UserGroupData> subGroups)
    {
        this.subGroups = subGroups;
    }


    public List<? extends UserGroupData> getSubGroups()
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
