package de.hybris.platform.cockpit.services.config.jaxb.gridview;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"imageslot", "labelslot", "descriptionslot", "shortinfoslot", "actionslot", "specialactionslot", "additionalPropertySlot"})
@XmlRootElement(name = "grid-view")
public class GridView
{
    @XmlElement(required = true)
    protected ImageSlotConfiguration imageslot;
    @XmlElement(required = true)
    protected PropertySlotConfiguration labelslot;
    @XmlElement(required = true)
    protected PropertySlotConfiguration descriptionslot;
    @XmlElement(required = true)
    protected PropertySlotConfiguration shortinfoslot;
    @XmlElement(required = true)
    protected ActionSlotConfiguration actionslot;
    protected ActionSlotConfiguration specialactionslot;
    protected List<PropertySlotConfiguration> additionalPropertySlot;


    public ImageSlotConfiguration getImageslot()
    {
        return this.imageslot;
    }


    public void setImageslot(ImageSlotConfiguration value)
    {
        this.imageslot = value;
    }


    public PropertySlotConfiguration getLabelslot()
    {
        return this.labelslot;
    }


    public void setLabelslot(PropertySlotConfiguration value)
    {
        this.labelslot = value;
    }


    public PropertySlotConfiguration getDescriptionslot()
    {
        return this.descriptionslot;
    }


    public void setDescriptionslot(PropertySlotConfiguration value)
    {
        this.descriptionslot = value;
    }


    public PropertySlotConfiguration getShortinfoslot()
    {
        return this.shortinfoslot;
    }


    public void setShortinfoslot(PropertySlotConfiguration value)
    {
        this.shortinfoslot = value;
    }


    public ActionSlotConfiguration getActionslot()
    {
        return this.actionslot;
    }


    public void setActionslot(ActionSlotConfiguration value)
    {
        this.actionslot = value;
    }


    public ActionSlotConfiguration getSpecialactionslot()
    {
        return this.specialactionslot;
    }


    public void setSpecialactionslot(ActionSlotConfiguration value)
    {
        this.specialactionslot = value;
    }


    public List<PropertySlotConfiguration> getAdditionalPropertySlot()
    {
        if(this.additionalPropertySlot == null)
        {
            this.additionalPropertySlot = new ArrayList<>();
        }
        return this.additionalPropertySlot;
    }
}
