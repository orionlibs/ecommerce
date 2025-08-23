package de.hybris.platform.cockpit.widgets.util.impl;

import de.hybris.platform.cockpit.widgets.WidgetConfig;
import de.hybris.platform.cockpit.widgets.util.WidgetMapProvider;
import java.util.Collections;
import java.util.Map;

public class DefaultWidgetMapProvider implements WidgetMapProvider
{
    private Map<String, WidgetConfig> cockpitWidgetMap = null;


    public Map<String, WidgetConfig> getWidgetMap()
    {
        return getCockpitWidgetMap();
    }


    public void setCockpitWidgetMap(Map<String, WidgetConfig> cockpitWidgetMap)
    {
        this.cockpitWidgetMap = cockpitWidgetMap;
    }


    public Map<String, WidgetConfig> getCockpitWidgetMap()
    {
        return (this.cockpitWidgetMap == null) ? Collections.EMPTY_MAP : this.cockpitWidgetMap;
    }
}
