package de.hybris.platform.cmscockpit.services.config;

import de.hybris.platform.cockpit.services.config.UIComponentConfiguration;
import java.util.Map;

public interface ContentEditorConfiguration extends UIComponentConfiguration
{
    String getEditorCode(String paramString);


    boolean isEditorVisible(String paramString);


    String getCockpitTemplate();


    Map<String, String> getEditorMap();


    Map<String, String> getParameterMap(String paramString);


    boolean isHideReadOnly();


    boolean isHideEmpty();


    boolean isGroupCollections();


    boolean isDefaultConfiguration();
}
