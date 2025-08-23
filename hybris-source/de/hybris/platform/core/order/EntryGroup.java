package de.hybris.platform.core.order;

import de.hybris.platform.core.enums.GroupType;
import java.io.Serializable;
import java.util.List;

public class EntryGroup implements Serializable
{
    private static final long serialVersionUID = 1L;
    private Integer groupNumber;
    private Integer priority;
    private String label;
    private GroupType groupType;
    private List<EntryGroup> children;
    private String externalReferenceId;
    private Boolean erroneous;


    public void setGroupNumber(Integer groupNumber)
    {
        this.groupNumber = groupNumber;
    }


    public Integer getGroupNumber()
    {
        return this.groupNumber;
    }


    public void setPriority(Integer priority)
    {
        this.priority = priority;
    }


    public Integer getPriority()
    {
        return this.priority;
    }


    public void setLabel(String label)
    {
        this.label = label;
    }


    public String getLabel()
    {
        return this.label;
    }


    public void setGroupType(GroupType groupType)
    {
        this.groupType = groupType;
    }


    public GroupType getGroupType()
    {
        return this.groupType;
    }


    public void setChildren(List<EntryGroup> children)
    {
        this.children = children;
    }


    public List<EntryGroup> getChildren()
    {
        return this.children;
    }


    public void setExternalReferenceId(String externalReferenceId)
    {
        this.externalReferenceId = externalReferenceId;
    }


    public String getExternalReferenceId()
    {
        return this.externalReferenceId;
    }


    public void setErroneous(Boolean erroneous)
    {
        this.erroneous = erroneous;
    }


    public Boolean getErroneous()
    {
        return this.erroneous;
    }
}
