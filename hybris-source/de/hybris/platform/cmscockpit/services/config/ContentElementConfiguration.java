package de.hybris.platform.cmscockpit.services.config;

import de.hybris.platform.cockpit.services.config.UIComponentConfiguration;

public interface ContentElementConfiguration extends UIComponentConfiguration
{
    String getName();


    String getDescription();


    String getImage();


    String getRefImage();


    String getImageSmall();


    Object getType();
}
