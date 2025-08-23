package de.hybris.platform.cockpit.services.config.jaxb.dashboard;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"containerlayout", "widgetpositions"})
@XmlRootElement(name = "widget-dashboard")
public class WidgetDashboard
{
    @XmlElement(required = true)
    protected Containerlayout containerlayout;
    @XmlElement(required = true)
    protected Widgetpositions widgetpositions;


    public Containerlayout getContainerlayout()
    {
        return this.containerlayout;
    }


    public void setContainerlayout(Containerlayout value)
    {
        this.containerlayout = value;
    }


    public Widgetpositions getWidgetpositions()
    {
        return this.widgetpositions;
    }


    public void setWidgetpositions(Widgetpositions value)
    {
        this.widgetpositions = value;
    }
}
