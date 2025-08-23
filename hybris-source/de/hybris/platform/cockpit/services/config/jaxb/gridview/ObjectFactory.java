package de.hybris.platform.cockpit.services.config.jaxb.gridview;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class ObjectFactory
{
    public ActionSlotConfiguration createActionSlotConfiguration()
    {
        return new ActionSlotConfiguration();
    }


    public GridView createGridView()
    {
        return new GridView();
    }


    public Property createProperty()
    {
        return new Property();
    }


    public ImageSlotConfiguration createImageSlotConfiguration()
    {
        return new ImageSlotConfiguration();
    }


    public PropertySlotConfiguration createPropertySlotConfiguration()
    {
        return new PropertySlotConfiguration();
    }


    public Parameter createParameter()
    {
        return new Parameter();
    }
}
