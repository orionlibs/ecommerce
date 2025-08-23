package de.hybris.platform.cockpit.widgets.impl;

import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.events.CockpitEventAcceptor;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.cockpit.widgets.WidgetConfig;
import de.hybris.platform.cockpit.widgets.WidgetContainer;
import de.hybris.platform.cockpit.widgets.WidgetFactory;
import de.hybris.platform.cockpit.widgets.events.WidgetFocusEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;

public class DefaultWidgetContainer<T extends Widget> implements WidgetContainer<T>
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultWidgetContainer.class);
    private final transient Map<String, T> widgetMap = new HashMap<>();
    private WidgetFactory factory;
    private String focusedWidgetCode;
    private final List<CockpitEventAcceptor> cockpitEventAcceptors = new ArrayList<>();


    public DefaultWidgetContainer(WidgetFactory factory)
    {
        this.factory = factory;
    }


    public WidgetFactory getWidgetFactory()
    {
        return this.factory;
    }


    public void setWidgetFactory(WidgetFactory widgetFactory)
    {
        this.factory = widgetFactory;
    }


    public void cleanup()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Cleaning up widget container...");
        }
        Collection<T> values = getWidgetMap().values();
        for(Widget widgetComponent : values)
        {
            if(widgetComponent != null)
            {
                widgetComponent.cleanup();
            }
        }
        getWidgetMap().clear();
    }


    protected Map<String, T> getWidgetMap()
    {
        return this.widgetMap;
    }


    public void focusWidget(String focusWidgetCode)
    {
        if(StringUtils.isNotBlank(focusWidgetCode) && !StringUtils.equals(this.focusedWidgetCode, focusWidgetCode))
        {
            this.focusedWidgetCode = focusWidgetCode;
            Set<String> widgetCodes = getWidgetMap().keySet();
            for(String widgetCode : widgetCodes)
            {
                Widget widget = (Widget)getWidget(widgetCode);
                if(UITools.isFromOtherDesktop((Component)widget))
                {
                    LOG.warn("Invalidated widget component '" + widget + "' found. Ignoring...");
                    continue;
                }
                widget.handleFocus(focusWidgetCode.equals(widgetCode));
            }
            notifyListeners((CockpitEvent)new WidgetFocusEvent(this, focusWidgetCode));
        }
    }


    protected void notifyListeners(CockpitEvent event)
    {
        List<CockpitEventAcceptor> acceptors = new ArrayList<>(getCockpitEventAcceptors());
        for(CockpitEventAcceptor cockpitEventAcceptor : acceptors)
        {
            cockpitEventAcceptor.onCockpitEvent(event);
        }
    }


    public String getFocusedWidgetCode()
    {
        return this.focusedWidgetCode;
    }


    public T getWidget(String widgetCode)
    {
        return getWidgetMap().get(widgetCode);
    }


    public Map<String, T> initialize(Map<String, WidgetConfig> widgetConfigs)
    {
        this.widgetMap.clear();
        if(widgetConfigs != null && !widgetConfigs.isEmpty())
        {
            Set<String> widgetCodes = widgetConfigs.keySet();
            for(String widgetCode : widgetCodes)
            {
                T widget = createWidget(widgetConfigs.get(widgetCode), widgetCode);
                if(widget != null)
                {
                    this.widgetMap.put(widgetCode, widget);
                }
            }
        }
        return this.widgetMap;
    }


    protected T createWidget(WidgetConfig widgetConfig, String widgetCode)
    {
        Widget widget1;
        T widget = null;
        if(getWidgetFactory() == null)
        {
            LOG.warn("Can not create widget with code '" + widgetCode + "'. Reason: No widget factory available.");
        }
        else if(widgetConfig == null)
        {
            LOG.warn("No widget with code '" + widgetCode + "' found. Ignoring...");
        }
        else
        {
            widget1 = getWidgetFactory().createWidget(widgetCode, widgetConfig);
            Map<String, Object> widgetModel = new HashMap<>();
            widget1.setWidgetContainer(this);
            widget1.initialize(widgetModel);
        }
        return (T)widget1;
    }


    public void addCockpitEventAcceptor(CockpitEventAcceptor acceptor)
    {
        if(!this.cockpitEventAcceptors.contains(acceptor))
        {
            this.cockpitEventAcceptors.add(acceptor);
        }
    }


    public void removeCockpitEventAcceptor(CockpitEventAcceptor acceptor)
    {
        this.cockpitEventAcceptors.remove(acceptor);
    }


    protected List<CockpitEventAcceptor> getCockpitEventAcceptors()
    {
        return this.cockpitEventAcceptors;
    }
}
