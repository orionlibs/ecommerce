package de.hybris.platform.cockpit.services.config.impl;

import de.hybris.platform.cockpit.services.config.WidgetDashboardConfiguration;
import de.hybris.platform.cockpit.widgets.portal.PortalWidgetCoordinate;
import java.util.Collections;
import java.util.Map;

public class DefaultWidgetDashboardConfiguration implements WidgetDashboardConfiguration
{
    private String containerLayoutID;
    private Map<String, PortalWidgetCoordinate> coordinatesMap;


    public String getContainerLayoutID()
    {
        return this.containerLayoutID;
    }


    public Map<String, PortalWidgetCoordinate> getCoordinatesMap()
    {
        return (this.coordinatesMap == null) ? Collections.EMPTY_MAP : this.coordinatesMap;
    }


    public void setContainerLayoutID(String containerLayoutID)
    {
        this.containerLayoutID = containerLayoutID;
    }


    public void setCoordinatesMap(Map<String, PortalWidgetCoordinate> coordinatesMap)
    {
        this.coordinatesMap = coordinatesMap;
    }
}
