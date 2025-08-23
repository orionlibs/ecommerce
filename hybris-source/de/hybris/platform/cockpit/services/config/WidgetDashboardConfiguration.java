package de.hybris.platform.cockpit.services.config;

import de.hybris.platform.cockpit.widgets.portal.PortalWidgetCoordinate;
import java.util.Map;

public interface WidgetDashboardConfiguration extends UIComponentConfiguration
{
    String getContainerLayoutID();


    Map<String, PortalWidgetCoordinate> getCoordinatesMap();
}
