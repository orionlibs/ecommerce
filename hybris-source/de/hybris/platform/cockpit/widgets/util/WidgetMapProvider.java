package de.hybris.platform.cockpit.widgets.util;

import de.hybris.platform.cockpit.widgets.WidgetConfig;
import java.util.Map;

public interface WidgetMapProvider
{
    Map<String, WidgetConfig> getWidgetMap();
}
