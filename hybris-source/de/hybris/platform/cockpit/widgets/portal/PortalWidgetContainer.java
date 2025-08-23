package de.hybris.platform.cockpit.widgets.portal;

import de.hybris.platform.cockpit.components.AdvancedPortallayout;
import de.hybris.platform.cockpit.components.impl.DefaultAdvancedPortallayout;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.cockpit.widgets.WidgetFactory;
import de.hybris.platform.cockpit.widgets.impl.DefaultWidgetContainer;
import de.hybris.platform.cockpit.widgets.portal.impl.DefaultContainerLayout;
import de.hybris.platform.cockpit.widgets.portal.impl.DefaultPortalWidgetCoordinate;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import org.apache.commons.lang.StringUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Panelchildren;

public class PortalWidgetContainer<T extends Widget> extends DefaultWidgetContainer<T>
{
    private boolean panelsMaximizable = true;
    private boolean panelsMinimizable = true;
    private boolean panelsClosable = false;
    private ContainerLayout containerLayout;
    private final Map<String, PortalWidgetCoordinate> coordinatesMap = new HashMap<>();


    public PortalWidgetContainer(WidgetFactory factory)
    {
        super(factory);
    }


    public AdvancedPortallayout createPortalLayout(ContainerLayout style, PortalWidgetContainerListener listener)
    {
        this.containerLayout = style;
        if(this.containerLayout == null)
        {
            this.containerLayout = DefaultContainerLayout.ONE_COLUMN;
        }
        int columns = this.containerLayout.getColumns();
        DefaultAdvancedPortallayout portallayout = new DefaultAdvancedPortallayout(columns);
        portallayout.setWidths(this.containerLayout.getWidths());
        Map<String, T> widgetMap = getWidgetMap();
        SortedMap<PortalWidgetCoordinate, String> sortedCoordinates = getSortedCoordinates(widgetMap.keySet());
        for(Map.Entry<PortalWidgetCoordinate, String> coordEntry : sortedCoordinates.entrySet())
        {
            PortalWidgetCoordinate coordinate = coordEntry.getKey();
            Widget widget = (Widget)widgetMap.get(coordEntry.getValue());
            if(widget == null)
            {
                continue;
            }
            Panel panel = wrapIntoPanel((T)widget);
            portallayout.addPanel(panel, (coordinate.getColumn() < columns) ? coordinate.getColumn() : 0);
        }
        portallayout.addEventListener("onPortalMove", (EventListener)new Object(this, portallayout, listener));
        return (AdvancedPortallayout)portallayout;
    }


    private String getWidgetID(Panel panel)
    {
        return (String)panel.getAttribute("widgetID");
    }


    private void recalculateCoordinates(AdvancedPortallayout portallayout)
    {
        Map<Panel, int[]> positions = portallayout.getPositions();
        for(Map.Entry<Panel, int[]> entry : positions.entrySet())
        {
            this.coordinatesMap.put(getWidgetID(entry.getKey()), new DefaultPortalWidgetCoordinate(((int[])entry.getValue())[0], ((int[])entry
                            .getValue())[1]));
        }
    }


    protected Panel wrapIntoPanel(T widget)
    {
        Panel panel = new Panel();
        UITools.modifySClass((HtmlBasedComponent)panel, "portalWidgetPanel", true);
        if(StringUtils.isNotBlank(widget.getSclass()))
        {
            UITools.modifySClass((HtmlBasedComponent)panel, widget.getSclass(), true);
        }
        if(widget.getCaption() == null)
        {
            panel.setTitle(widget.getWidgetTitle());
            panel.setMaximizable(isPanelsMaximizable());
            panel.setCollapsible(isPanelsMinimizable());
            panel.setClosable(isPanelsClosable());
        }
        else
        {
            Caption caption = new Caption();
            caption.appendChild((Component)widget.getCaption());
            panel.appendChild((Component)caption);
        }
        Panelchildren pchildren = new Panelchildren();
        panel.appendChild((Component)pchildren);
        pchildren.appendChild((Component)widget.getContent());
        panel.setAttribute("widgetID", widget.getWidgetCode());
        return panel;
    }


    public void setPanelsMaximizable(boolean panelsMaximizable)
    {
        this.panelsMaximizable = panelsMaximizable;
    }


    public boolean isPanelsMaximizable()
    {
        return this.panelsMaximizable;
    }


    public void setPanelsMinimizable(boolean panelsMinimizable)
    {
        this.panelsMinimizable = panelsMinimizable;
    }


    public boolean isPanelsMinimizable()
    {
        return this.panelsMinimizable;
    }


    public void setPanelsClosable(boolean panelsClosable)
    {
        this.panelsClosable = panelsClosable;
    }


    public boolean isPanelsClosable()
    {
        return this.panelsClosable;
    }


    public void setContainerLayout(ContainerLayout containerLayout)
    {
        this.containerLayout = containerLayout;
    }


    public ContainerLayout getContainerLayout()
    {
        return this.containerLayout;
    }


    protected SortedMap<PortalWidgetCoordinate, String> getSortedCoordinates(Collection<String> allWidgetCodes)
    {
        Map<String, PortalWidgetCoordinate> allWidgetCoordMap = new HashMap<>();
        for(String widgetCode : allWidgetCodes)
        {
            allWidgetCoordMap.put(widgetCode, new DefaultPortalWidgetCoordinate());
        }
        allWidgetCoordMap.putAll(this.coordinatesMap);
        SortedMap<PortalWidgetCoordinate, String> sorted = new TreeMap<>((Comparator<? super PortalWidgetCoordinate>)new Object(this));
        for(Map.Entry<String, PortalWidgetCoordinate> coord : allWidgetCoordMap.entrySet())
        {
            sorted.put(coord.getValue(), coord.getKey());
        }
        return sorted;
    }


    public void setWidgetPositions(Map<String, PortalWidgetCoordinate> positions)
    {
        this.coordinatesMap.clear();
        if(positions != null)
        {
            this.coordinatesMap.putAll(positions);
        }
    }


    public Map<String, PortalWidgetCoordinate> getWidgetPositions()
    {
        return Collections.unmodifiableMap(this.coordinatesMap);
    }
}
