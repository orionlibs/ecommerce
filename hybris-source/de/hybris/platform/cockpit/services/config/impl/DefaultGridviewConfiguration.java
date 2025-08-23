package de.hybris.platform.cockpit.services.config.impl;

import de.hybris.platform.cockpit.services.config.GridProperty;
import de.hybris.platform.cockpit.services.config.GridViewConfiguration;

public class DefaultGridviewConfiguration implements GridViewConfiguration
{
    private GridProperty labelProperty;
    private GridProperty imageURLProperty;
    private GridProperty descriptionProperty;
    private GridProperty shortInfoProperty;
    private String actionSpringBeanID;
    private String specialactionSpringBeanID;


    public GridProperty getDescriptionProperty()
    {
        return this.descriptionProperty;
    }


    public GridProperty getImageURLProperty()
    {
        return this.imageURLProperty;
    }


    public GridProperty getLabelProperty()
    {
        return this.labelProperty;
    }


    public GridProperty getShortInfoProperty()
    {
        return this.shortInfoProperty;
    }


    public void setShortInfoProperty(GridProperty shortInfoProperty)
    {
        this.shortInfoProperty = shortInfoProperty;
    }


    public void setDescriptionProperty(GridProperty descriptionProperty)
    {
        this.descriptionProperty = descriptionProperty;
    }


    public void setImageURLProperty(GridProperty imageURLProperty)
    {
        this.imageURLProperty = imageURLProperty;
    }


    public void setLabelProperty(GridProperty labelProperty)
    {
        this.labelProperty = labelProperty;
    }


    public void setActionSpringBeanID(String actionSpringBeanID)
    {
        this.actionSpringBeanID = actionSpringBeanID;
    }


    public String getActionSpringBeanID()
    {
        return this.actionSpringBeanID;
    }


    public String getSpecialactionSpringBeanID()
    {
        return this.specialactionSpringBeanID;
    }


    public void setSpecialactionSpringBeanID(String specialactionSpringBeanID)
    {
        this.specialactionSpringBeanID = specialactionSpringBeanID;
    }
}
