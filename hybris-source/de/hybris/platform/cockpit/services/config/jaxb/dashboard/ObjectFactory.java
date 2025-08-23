package de.hybris.platform.cockpit.services.config.jaxb.dashboard;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class ObjectFactory
{
    public Position createPosition()
    {
        return new Position();
    }


    public Widgetpositions createWidgetpositions()
    {
        return new Widgetpositions();
    }


    public WidgetDashboard createWidgetDashboard()
    {
        return new WidgetDashboard();
    }


    public Containerlayout createContainerlayout()
    {
        return new Containerlayout();
    }
}
