package de.hybris.platform.commercefacades.order;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.core.enums.GroupType;
import java.io.Serializable;
import java.util.List;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "groupNumber")
public class EntryGroupData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private Integer groupNumber;
    private Integer priority;
    private String label;
    private GroupType groupType;
    private List<EntryGroupData> children;
    private String externalReferenceId;
    private Boolean erroneous;
    private List<OrderEntryData> orderEntries;
    private EntryGroupData rootGroup;
    private EntryGroupData parent;


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


    public void setChildren(List<EntryGroupData> children)
    {
        this.children = children;
    }


    public List<EntryGroupData> getChildren()
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


    public void setOrderEntries(List<OrderEntryData> orderEntries)
    {
        this.orderEntries = orderEntries;
    }


    public List<OrderEntryData> getOrderEntries()
    {
        return this.orderEntries;
    }


    public void setRootGroup(EntryGroupData rootGroup)
    {
        this.rootGroup = rootGroup;
    }


    public EntryGroupData getRootGroup()
    {
        return this.rootGroup;
    }


    public void setParent(EntryGroupData parent)
    {
        this.parent = parent;
    }


    public EntryGroupData getParent()
    {
        return this.parent;
    }
}
