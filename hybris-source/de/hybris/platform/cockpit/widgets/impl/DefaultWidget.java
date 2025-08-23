package de.hybris.platform.cockpit.widgets.impl;

import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.cockpit.widgets.controllers.WidgetController;
import de.hybris.platform.cockpit.widgets.events.WidgetModelEvent;
import de.hybris.platform.cockpit.widgets.events.WidgetModelListener;
import de.hybris.platform.cockpit.widgets.models.WidgetModel;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.api.HtmlBasedComponent;

public class DefaultWidget<T extends WidgetModel, U extends WidgetController> extends AbstractWidget<T, U>
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultWidget.class);


    public void initialize(Map<String, Object> params)
    {
        if(!isInitialized())
        {
            if(getWidgetRenderer() == null)
            {
                LOG.warn("No renderer specified. Ignoring...");
            }
            else
            {
                setCaption(getWidgetRenderer().createCaption((Widget)this));
                setContent(getWidgetRenderer().createContent((Widget)this));
            }
            if(getWidgetModel() != null)
            {
                getWidgetModel().addWidgetModelListener((WidgetModelListener)this);
            }
            super.initialize(params);
        }
    }


    public void update()
    {
        if(isInitialized())
        {
            if(getWidgetRenderer() == null)
            {
                LOG.warn("Ignoring update. Reason: No renderer specified.");
            }
            else
            {
                HtmlBasedComponent content = getWidgetRenderer().createContent((Widget)this);
                setContent(content);
            }
        }
    }


    public void cleanup()
    {
        if(getWidgetModel() != null)
        {
            getWidgetModel().removeWidgetModelLlistener((WidgetModelListener)this);
        }
    }


    public void onModelEvent(WidgetModelEvent widgetEvent)
    {
        update();
    }
}
