package de.hybris.platform.cockpit.widgets.browsers;

import de.hybris.platform.cockpit.services.config.UIComponentConfiguration;
import de.hybris.platform.cockpit.services.config.WidgetDashboardConfiguration;
import de.hybris.platform.cockpit.services.config.impl.DefaultWidgetDashboardConfiguration;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.widgets.WidgetConfig;
import de.hybris.platform.cockpit.widgets.portal.ContainerLayout;
import de.hybris.platform.cockpit.widgets.portal.PortalWidgetCoordinate;
import de.hybris.platform.cockpit.widgets.portal.impl.DefaultContainerLayout;
import de.hybris.platform.cockpit.widgets.util.ContainerLayoutConfiguration;
import de.hybris.platform.cockpit.widgets.util.WidgetMapProvider;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.zkoss.util.resource.Labels;

public class WidgetDashboardBrowserModel extends DefaultWidgetBrowserModel
{
    private ContainerLayout currentContainerLayout;
    private ContainerLayoutConfiguration containerLayoutConfig;
    private WidgetMapProvider widgetMapProvider;
    private Map<String, PortalWidgetCoordinate> widgetPositions;


    public void updateItems()
    {
        super.updateItems();
        loadConfig();
    }


    public void setCurrentContainerLayout(ContainerLayout currentContainerLayout, boolean storeConfig)
    {
        this.currentContainerLayout = currentContainerLayout;
        if(getArea() != null)
        {
            getArea().update();
        }
        if(storeConfig)
        {
            storeConfig();
        }
    }


    public void setCurrentContainerLayout(ContainerLayout currentContainerLayout)
    {
        setCurrentContainerLayout(currentContainerLayout, true);
    }


    public ContainerLayout getCurrentContainerLayout()
    {
        return (this.currentContainerLayout == null) ? DefaultContainerLayout.ONE_COLUMN : this.currentContainerLayout;
    }


    public List<ContainerLayout> getContainerLayouts()
    {
        if(getContainerLayoutConfiguration() == null)
        {
            return Collections.singletonList(getCurrentContainerLayout());
        }
        return getContainerLayoutConfiguration().getContainerLayouts();
    }


    public String getLabel()
    {
        int dashboardIndex = getDashboardIndex();
        return Labels.getLabel("cockpit.browser.dashboard.label") + Labels.getLabel("cockpit.browser.dashboard.label");
    }


    protected void storeConfig()
    {
        DefaultWidgetDashboardConfiguration dashboardConfiguration = new DefaultWidgetDashboardConfiguration();
        if(getCurrentContainerLayout() != null)
        {
            dashboardConfiguration.setContainerLayoutID(getCurrentContainerLayout().getID());
        }
        dashboardConfiguration.setCoordinatesMap(getWidgetPositions());
        UISessionUtils.getCurrentSession().getUiConfigurationService().setLocalComponentConfiguration((UIComponentConfiguration)dashboardConfiguration,
                        UISessionUtils.getCurrentSession().getUser(),
                        UISessionUtils.getCurrentSession().getTypeService().getObjectTemplate("item"), generateConfigComponentCode(), WidgetDashboardConfiguration.class);
    }


    protected void loadConfig()
    {
        WidgetDashboardConfiguration componentConfiguration = (WidgetDashboardConfiguration)UISessionUtils.getCurrentSession().getUiConfigurationService().getComponentConfiguration(UISessionUtils.getCurrentSession().getTypeService().getObjectTemplate("item"),
                        generateConfigComponentCode(), WidgetDashboardConfiguration.class);
        if(componentConfiguration != null)
        {
            setCurrentContainerLayout(getContainerLayout(componentConfiguration.getContainerLayoutID()), false);
            setWidgetPositions(componentConfiguration.getCoordinatesMap(), false);
        }
    }


    protected String generateConfigComponentCode()
    {
        return "widgetDashboard__" + getBrowserCode() + "_" + getDashboardIndex();
    }


    protected int getDashboardIndex()
    {
        int index = 0;
        return 0;
    }


    protected ContainerLayout getContainerLayout(String id)
    {
        for(ContainerLayout layout : getContainerLayouts())
        {
            if(id.equals(layout.getID()))
            {
                return layout;
            }
        }
        return null;
    }


    public Map<String, WidgetConfig> getWidgetMap()
    {
        return (this.widgetMapProvider == null) ? super.getWidgetMap() : this.widgetMapProvider.getWidgetMap();
    }


    public void setWidgetPositions(Map<String, PortalWidgetCoordinate> widgetPositions)
    {
        setWidgetPositions(widgetPositions, true);
    }


    public void setWidgetPositions(Map<String, PortalWidgetCoordinate> widgetPositions, boolean storeConfig)
    {
        this.widgetPositions = widgetPositions;
        if(storeConfig)
        {
            storeConfig();
        }
    }


    public Map<String, PortalWidgetCoordinate> getWidgetPositions()
    {
        return (this.widgetPositions == null) ? Collections.EMPTY_MAP : this.widgetPositions;
    }


    public void setWidgetMapProvider(WidgetMapProvider widgetMapProvider)
    {
        this.widgetMapProvider = widgetMapProvider;
    }


    public WidgetMapProvider getWidgetMapProvider()
    {
        return this.widgetMapProvider;
    }


    public void setContainerLayoutConfiguration(ContainerLayoutConfiguration containerLayoutConfig)
    {
        this.containerLayoutConfig = containerLayoutConfig;
    }


    public ContainerLayoutConfiguration getContainerLayoutConfiguration()
    {
        return this.containerLayoutConfig;
    }
}
