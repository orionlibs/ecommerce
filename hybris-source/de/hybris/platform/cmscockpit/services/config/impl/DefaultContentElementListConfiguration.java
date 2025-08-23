package de.hybris.platform.cmscockpit.services.config.impl;

import de.hybris.platform.cmscockpit.services.config.ContentElementConfiguration;
import de.hybris.platform.cmscockpit.services.config.ContentElementListConfiguration;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import java.util.HashMap;
import java.util.Map;

public class DefaultContentElementListConfiguration implements ContentElementListConfiguration
{
    private Map<ObjectType, ContentElementConfiguration> contentElements = new HashMap<>();


    public DefaultContentElementListConfiguration(Map<ObjectType, ContentElementConfiguration> contentElements)
    {
        this.contentElements = contentElements;
    }


    public Map<ObjectType, ContentElementConfiguration> getContentElements()
    {
        return this.contentElements;
    }


    public void setContentElements(Map<ObjectType, ContentElementConfiguration> contentElements)
    {
        this.contentElements = contentElements;
    }
}
