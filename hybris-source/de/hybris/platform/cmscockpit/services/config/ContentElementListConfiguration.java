package de.hybris.platform.cmscockpit.services.config;

import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.services.config.UIComponentConfiguration;
import java.util.Map;

public interface ContentElementListConfiguration extends UIComponentConfiguration
{
    Map<ObjectType, ContentElementConfiguration> getContentElements();
}
