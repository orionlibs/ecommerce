package de.hybris.platform.cockpit.services.config;

public interface GridViewConfiguration extends UIComponentConfiguration
{
    GridProperty getImageURLProperty();


    GridProperty getLabelProperty();


    GridProperty getDescriptionProperty();


    GridProperty getShortInfoProperty();


    String getActionSpringBeanID();


    String getSpecialactionSpringBeanID();
}
