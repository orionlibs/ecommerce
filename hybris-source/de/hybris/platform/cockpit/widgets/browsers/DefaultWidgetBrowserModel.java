package de.hybris.platform.cockpit.widgets.browsers;

import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.events.CockpitEventAcceptor;
import de.hybris.platform.cockpit.session.impl.DefaultConfigurableBrowserModel;
import de.hybris.platform.cockpit.widgets.WidgetConfig;
import de.hybris.platform.cockpit.widgets.WidgetFactory;
import de.hybris.platform.cockpit.widgets.events.WidgetFocusEvent;
import de.hybris.platform.cockpit.widgets.impl.DefaultWidgetFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;

public class DefaultWidgetBrowserModel extends DefaultConfigurableBrowserModel implements WidgetBrowserModel
{
    private final Map<String, WidgetConfig> widgetMap = new HashMap<>();
    private final List<CockpitEventAcceptor> acceptors = new ArrayList<>();
    private WidgetFactory widgetFactory;
    private String focusedWidgetCode = null;
    private String viewTemplateURI;


    public String getFocusedWidgetCode()
    {
        return this.focusedWidgetCode;
    }


    public void focusWidget(String widgetCode)
    {
        if(!StringUtils.equals(widgetCode, this.focusedWidgetCode))
        {
            this.focusedWidgetCode = widgetCode;
            WidgetFocusEvent event = new WidgetFocusEvent(this, widgetCode);
            for(CockpitEventAcceptor acceptor : this.acceptors)
            {
                acceptor.onCockpitEvent((CockpitEvent)event);
            }
        }
    }


    public WidgetConfig getWidgetConfig(String widgetCode)
    {
        return this.widgetMap.get(widgetCode);
    }


    public Map<String, WidgetConfig> getWidgetMap()
    {
        return Collections.unmodifiableMap(this.widgetMap);
    }


    public void setWidgetMap(Map<String, WidgetConfig> widgetMap)
    {
        this.widgetMap.clear();
        if(widgetMap != null && !widgetMap.isEmpty())
        {
            this.widgetMap.putAll(widgetMap);
        }
    }


    public void addCockpitEventAcceptor(CockpitEventAcceptor acceptor)
    {
        if(acceptor != null)
        {
            this.acceptors.add(acceptor);
        }
    }


    public void removeCockpitEventAcceptor(CockpitEventAcceptor acceptor)
    {
        this.acceptors.remove(acceptor);
    }


    public void setWidgetFactory(WidgetFactory widgetFactory)
    {
        this.widgetFactory = widgetFactory;
    }


    public WidgetFactory getWidgetFactory()
    {
        if(this.widgetFactory == null)
        {
            this.widgetFactory = (WidgetFactory)new DefaultWidgetFactory();
        }
        return this.widgetFactory;
    }


    public void setViewTemplateURI(String viewUri)
    {
        this.viewTemplateURI = viewUri;
    }


    public String getViewTemplateURI()
    {
        return this.viewTemplateURI;
    }
}
