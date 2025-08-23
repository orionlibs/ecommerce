package de.hybris.platform.cockpit.services.config.jaxb.dashboard;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "widgetpositions", propOrder = {"position"})
public class Widgetpositions
{
    protected List<Position> position;


    public List<Position> getPosition()
    {
        if(this.position == null)
        {
            this.position = new ArrayList<>();
        }
        return this.position;
    }
}
