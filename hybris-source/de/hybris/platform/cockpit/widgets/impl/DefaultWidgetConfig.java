package de.hybris.platform.cockpit.widgets.impl;

import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.cockpit.widgets.WidgetConfig;
import de.hybris.platform.cockpit.widgets.adapters.WidgetAdapter;
import de.hybris.platform.cockpit.widgets.controllers.WidgetController;
import de.hybris.platform.cockpit.widgets.models.WidgetModel;
import de.hybris.platform.cockpit.widgets.renderers.WidgetRenderer;
import org.springframework.beans.factory.annotation.Required;

public class DefaultWidgetConfig<T extends Widget> implements WidgetConfig<T>
{
    private WidgetModel model;
    private String title;
    private String controllerCtx;
    private WidgetController widgetController;
    private Class<T> widgetClass;
    private WidgetRenderer widgetRenderer;
    private String sclass;
    private boolean lazyLoading;
    private WidgetAdapter adapter;
    private boolean focusable = true;


    public void setFocusable(boolean focusable)
    {
        this.focusable = focusable;
    }


    public boolean isFocusable()
    {
        return this.focusable;
    }


    public void setWidgetTitle(String title)
    {
        this.title = title;
    }


    public String getWidgetTitle()
    {
        return this.title;
    }


    public String getControllerCtx()
    {
        return this.controllerCtx;
    }


    public void setControllerCtx(String controllerCtx)
    {
        this.controllerCtx = controllerCtx;
    }


    public WidgetController getWidgetController()
    {
        return this.widgetController;
    }


    public void setWidgetController(WidgetController widgetController)
    {
        this.widgetController = widgetController;
    }


    public Class<T> getWidgetClass()
    {
        return this.widgetClass;
    }


    @Required
    public void setWidgetClass(Class<T> widgetClass)
    {
        this.widgetClass = widgetClass;
    }


    public WidgetRenderer getWidgetRenderer()
    {
        return this.widgetRenderer;
    }


    @Required
    public void setWidgetRenderer(WidgetRenderer widgetRenderer)
    {
        this.widgetRenderer = widgetRenderer;
    }


    public WidgetModel getWidgetModel()
    {
        return this.model;
    }


    public void setWidgetModel(WidgetModel model)
    {
        this.model = model;
    }


    public void setWidgetAdapter(WidgetAdapter adapter)
    {
        this.adapter = adapter;
    }


    public WidgetAdapter getWidgetAdapter()
    {
        return this.adapter;
    }


    public void setWidgetSclass(String sclass)
    {
        this.sclass = sclass;
    }


    public String getWidgetSclass()
    {
        return this.sclass;
    }


    public boolean isLazyLoadingEnabled()
    {
        return this.lazyLoading;
    }


    public void setLazyLoadingEnabled(boolean enabled)
    {
        this.lazyLoading = enabled;
    }
}
