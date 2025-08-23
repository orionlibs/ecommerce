package de.hybris.platform.ruleengineservices.rao;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

public class UserRAO implements Serializable
{
    private String id;
    private String pk;
    private Set<AbstractOrderRAO> orders;
    private Set<UserGroupRAO> groups;


    public void setId(String id)
    {
        this.id = id;
    }


    public String getId()
    {
        return this.id;
    }


    public void setPk(String pk)
    {
        this.pk = pk;
    }


    public String getPk()
    {
        return this.pk;
    }


    public void setOrders(Set<AbstractOrderRAO> orders)
    {
        this.orders = orders;
    }


    public Set<AbstractOrderRAO> getOrders()
    {
        return this.orders;
    }


    public void setGroups(Set<UserGroupRAO> groups)
    {
        this.groups = groups;
    }


    public Set<UserGroupRAO> getGroups()
    {
        return this.groups;
    }


    public boolean equals(Object o)
    {
        if(o == null)
        {
            return false;
        }
        if(o == this)
        {
            return true;
        }
        if(getClass() != o.getClass())
        {
            return false;
        }
        UserRAO other = (UserRAO)o;
        return (Objects.equals(getId(), other.getId()) &&
                        Objects.equals(getPk(), other.getPk()));
    }


    public int hashCode()
    {
        int result = 1;
        Object attribute = this.id;
        result = 31 * result + ((attribute == null) ? 0 : attribute.hashCode());
        attribute = this.pk;
        result = 31 * result + ((attribute == null) ? 0 : attribute.hashCode());
        return result;
    }
}
