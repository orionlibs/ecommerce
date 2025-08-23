package de.hybris.platform.cockpit.widgets.impl;

import de.hybris.platform.cockpit.events.CockpitEventAcceptor;
import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.cockpit.widgets.WidgetConfig;
import de.hybris.platform.cockpit.widgets.WidgetFactory;
import de.hybris.platform.cockpit.widgets.adapters.WidgetAdapter;
import de.hybris.platform.cockpit.widgets.controllers.WidgetController;
import de.hybris.platform.cockpit.widgets.models.WidgetModel;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultWidgetFactory implements WidgetFactory
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultWidgetFactory.class);


    public Widget createWidget(String widgetCode, WidgetConfig config)
    {
        Widget widget = null;
        try
        {
            widget = createWidgetInstance(config);
            fillWidget(widgetCode, config, widget);
        }
        catch(Exception e)
        {
            LOG.error("Creation of widget failed.", e);
        }
        return widget;
    }


    private <T extends Widget> T createWidgetInstance(WidgetConfig config) throws InstantiationException, IllegalAccessException
    {
        Widget widget1;
        T widget = null;
        if(config == null)
        {
            throw new IllegalArgumentException("Configuration can not be null.");
        }
        Class<T> widgetClass = config.getWidgetClass();
        if(widgetClass != null)
        {
            widget1 = (Widget)widgetClass.newInstance();
        }
        return (T)widget1;
    }


    private void fillWidget(String widgetCode, WidgetConfig config, Widget widget)
    {
        if(config == null)
        {
            throw new IllegalArgumentException("Configuration can not be null.");
        }
        if(widget == null)
        {
            throw new IllegalArgumentException("Widget can not be null.");
        }
        WidgetController controller = config.getWidgetController();
        String controllerCtx = config.getControllerCtx();
        WidgetAdapter adapter = config.getWidgetAdapter();
        WidgetModel model = config.getWidgetModel();
        widget.setWidgetCode(widgetCode);
        widget.setWidgetModel(model);
        widget.setWidgetTitle(config.getWidgetTitle());
        widget.setControllerCtx(controllerCtx);
        widget.setWidgetController(controller);
        widget.setWidgetRenderer(config.getWidgetRenderer());
        widget.setLazyLoadingEnabled(config.isLazyLoadingEnabled());
        widget.setSclass(config.getWidgetSclass());
        widget.setFocusable(config.isFocusable());
        setupAdapter(adapter, controller, model, controllerCtx);
    }


    private void setupAdapter(WidgetAdapter adapter, WidgetController controller, WidgetModel model, String controllerCtx)
    {
        if(adapter != null)
        {
            if(controller != null && StringUtils.isNotBlank(controllerCtx))
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Registering widget adapter '" + adapter + "' with controller '" + controller + "'...");
                }
                controller.addCockpitEventAcceptor(controllerCtx, (CockpitEventAcceptor)adapter);
            }
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Registering widget model '" + model + "' with adapter '" + adapter + "'...");
            }
            adapter.setWidgetModel(model);
            adapter.setWidgetController(controller);
            adapter.setControllerCtx(controllerCtx);
        }
    }
}
