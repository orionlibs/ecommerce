package de.hybris.platform.cockpit.services.config.impl;

import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.services.config.UIComponentConfiguration;
import de.hybris.platform.cockpit.services.config.WidgetDashboardConfiguration;
import de.hybris.platform.cockpit.services.config.jaxb.dashboard.Position;
import de.hybris.platform.cockpit.services.config.jaxb.dashboard.WidgetDashboard;
import de.hybris.platform.cockpit.services.config.jaxb.dashboard.Widgetpositions;
import de.hybris.platform.cockpit.widgets.portal.PortalWidgetCoordinate;
import de.hybris.platform.cockpit.widgets.portal.impl.DefaultPortalWidgetCoordinate;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WidgetDashboardConfigurationFactory extends JAXBBasedUIComponentConfigurationFactory<WidgetDashboardConfiguration, WidgetDashboard>
{
    private static final Logger LOG = LoggerFactory.getLogger(WidgetDashboardConfigurationFactory.class);


    protected WidgetDashboardConfiguration createUIComponent(ObjectTemplate objectTemplate, ObjectTemplate originalObjectTemplate, WidgetDashboard xmlWidget)
    {
        DefaultWidgetDashboardConfiguration ret = new DefaultWidgetDashboardConfiguration();
        try
        {
            ret.setContainerLayoutID(xmlWidget.getContainerlayout().getValue());
            Map<String, PortalWidgetCoordinate> coordinatesMap = new HashMap<>();
            Widgetpositions widgetpositions = xmlWidget.getWidgetpositions();
            if(widgetpositions != null && widgetpositions.getPosition() != null)
            {
                for(Position position : widgetpositions.getPosition())
                {
                    coordinatesMap.put(position.getWidgetID(), new DefaultPortalWidgetCoordinate(position.getColumn().intValue(), position
                                    .getRow().intValue()));
                }
            }
            ret.setCoordinatesMap(coordinatesMap);
        }
        catch(Exception e)
        {
            LOG.error("Could not create configuration object, reason: ", e);
        }
        return (WidgetDashboardConfiguration)ret;
    }


    public UIComponentConfiguration createDefault(ObjectTemplate objectTemplate)
    {
        DefaultWidgetDashboardConfiguration ret = new DefaultWidgetDashboardConfiguration();
        ret.setContainerLayoutID("one_column");
        return (UIComponentConfiguration)ret;
    }


    public Class getComponentClass()
    {
        return WidgetDashboardConfiguration.class;
    }
}
