package de.hybris.platform.ruleengineservices.rao;

import java.util.Objects;

public class OrderEntryGroupRAO extends AbstractActionedRAO
{
    private OrderEntryGroupRAO rootEntryGroup;
    private Integer entryGroupId;
    private String externalReferenceId;
    private String groupType;


    public void setRootEntryGroup(OrderEntryGroupRAO rootEntryGroup)
    {
        this.rootEntryGroup = rootEntryGroup;
    }


    public OrderEntryGroupRAO getRootEntryGroup()
    {
        return this.rootEntryGroup;
    }


    public void setEntryGroupId(Integer entryGroupId)
    {
        this.entryGroupId = entryGroupId;
    }


    public Integer getEntryGroupId()
    {
        return this.entryGroupId;
    }


    public void setExternalReferenceId(String externalReferenceId)
    {
        this.externalReferenceId = externalReferenceId;
    }


    public String getExternalReferenceId()
    {
        return this.externalReferenceId;
    }


    public void setGroupType(String groupType)
    {
        this.groupType = groupType;
    }


    public String getGroupType()
    {
        return this.groupType;
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
        OrderEntryGroupRAO other = (OrderEntryGroupRAO)o;
        return (Objects.equals(getEntryGroupId(), other.getEntryGroupId()) &&
                        Objects.equals(getExternalReferenceId(), other.getExternalReferenceId()) &&
                        Objects.equals(getGroupType(), other.getGroupType()));
    }


    public int hashCode()
    {
        int result = 1;
        Object attribute = this.entryGroupId;
        result = 31 * result + ((attribute == null) ? 0 : attribute.hashCode());
        attribute = this.externalReferenceId;
        result = 31 * result + ((attribute == null) ? 0 : attribute.hashCode());
        attribute = this.groupType;
        result = 31 * result + ((attribute == null) ? 0 : attribute.hashCode());
        return result;
    }
}
