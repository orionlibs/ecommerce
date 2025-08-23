package de.hybris.platform.warehousingbackoffice.config.impl.jaxb.hybris.warehousingrefineby;

import com.hybris.cockpitng.core.config.annotations.Mergeable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SearchValue", propOrder = {"groupMemberValue"})
public class SearchValue
{
    @Mergeable(key = {"value"})
    @XmlElement(name = "group-member-value", required = true)
    protected List<GroupMemberValue> groupMemberValue;
    @XmlAttribute(name = "uniqueValue")
    protected String uniqueValue;
    @XmlAttribute(name = "label", required = true)
    protected String label;
    @XmlAttribute(name = "merge-mode")
    protected MergeMode mergeMode;


    public List<GroupMemberValue> getGroupMemberValue()
    {
        if(this.groupMemberValue == null)
        {
            this.groupMemberValue = new ArrayList<>();
        }
        return this.groupMemberValue;
    }


    public String getUniqueValue()
    {
        return this.uniqueValue;
    }


    public void setUniqueValue(String value)
    {
        this.uniqueValue = value;
    }


    public String getLabel()
    {
        return this.label;
    }


    public void setLabel(String value)
    {
        this.label = value;
    }


    public MergeMode getMergeMode()
    {
        return this.mergeMode;
    }


    public void setMergeMode(MergeMode value)
    {
        this.mergeMode = value;
    }
}
