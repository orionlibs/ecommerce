package de.hybris.platform.commerceservices.organization.services;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import java.io.Serializable;
import java.util.Set;

public class OrgUnitMemberParameter<T> implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String uid;
    private Set<T> members;
    private Class<T> type;
    private PageableData pageableData;


    public void setUid(String uid)
    {
        this.uid = uid;
    }


    public String getUid()
    {
        return this.uid;
    }


    public void setMembers(Set<T> members)
    {
        this.members = members;
    }


    public Set<T> getMembers()
    {
        return this.members;
    }


    public void setType(Class<T> type)
    {
        this.type = type;
    }


    public Class<T> getType()
    {
        return this.type;
    }


    public void setPageableData(PageableData pageableData)
    {
        this.pageableData = pageableData;
    }


    public PageableData getPageableData()
    {
        return this.pageableData;
    }
}
