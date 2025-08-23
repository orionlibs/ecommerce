package de.hybris.platform.cockpit.widgets.impl;

import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.cockpit.widgets.WidgetFactory;
import de.hybris.platform.cockpit.widgets.events.WidgetFocusEvent;
import java.util.Map;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;

public class DefaultWidgetContentBrowser extends AbstractWidgetContentBrowser
{
    protected void createFrame(HtmlBasedComponent parent, Map<String, Widget> widgets)
    {
        if(widgets != null && !widgets.isEmpty())
        {
            for(Widget widget : widgets.values())
            {
                widget.setParent((Component)parent);
            }
        }
    }


    public void onCockpitEvent(CockpitEvent event)
    {
        if(event instanceof WidgetFocusEvent)
        {
            String focusedWidgetCode = ((WidgetFocusEvent)event).getWidgetCode();
            getWidgetContainer().focusWidget(focusedWidgetCode);
            getModel().focusWidget(focusedWidgetCode);
            UISessionUtils.getCurrentSession().getCurrentPerspective().onCockpitEvent(event);
        }
    }


    protected WidgetFactory getWidgetFactory()
    {
        return getModel().getWidgetFactory();
    }
}
