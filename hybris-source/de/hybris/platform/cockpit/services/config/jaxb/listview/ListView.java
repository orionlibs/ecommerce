package de.hybris.platform.cockpit.services.config.jaxb.listview;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"group", "dynamicColumnProviders"})
@XmlRootElement(name = "list-view")
public class ListView
{
    @XmlElement(required = true)
    protected Group group;
    @XmlElement(required = false)
    protected DynamicColumnProviders dynamicColumnProviders;
    @XmlAttribute(name = "unassigned-group-name")
    protected String unassignedGroupName;
    @XmlAttribute(name = "allow-create-inline-items")
    protected boolean allowCreateInlineItems;
    @XmlAttribute(name = "header-popup-bean")
    protected String headerPopupBean;


    public Group getGroup()
    {
        return this.group;
    }


    public String getHeaderPopupBean()
    {
        return this.headerPopupBean;
    }


    public String getUnassignedGroupName()
    {
        return this.unassignedGroupName;
    }


    public boolean isAllowCreateInlineItems()
    {
        return this.allowCreateInlineItems;
    }


    public void setAllowCreateInlineItems(boolean allowCreateInlineItems)
    {
        this.allowCreateInlineItems = allowCreateInlineItems;
    }


    public void setGroup(Group value)
    {
        this.group = value;
    }


    public void setHeaderPopupBean(String headerPopupBean)
    {
        this.headerPopupBean = headerPopupBean;
    }


    public void setUnassignedGroupName(String value)
    {
        this.unassignedGroupName = value;
    }


    public DynamicColumnProviders getDynamicColumnProviders()
    {
        return this.dynamicColumnProviders;
    }


    public void setDynamicColumnProviders(DynamicColumnProviders dynamicColumnProviders)
    {
        this.dynamicColumnProviders = dynamicColumnProviders;
    }
}
