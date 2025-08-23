package de.hybris.platform.cockpit.widgets.adapters.impl;

import de.hybris.platform.cockpit.widgets.adapters.WidgetAdapter;
import de.hybris.platform.cockpit.widgets.controllers.WidgetController;
import de.hybris.platform.cockpit.widgets.models.WidgetModel;

public abstract class AbstractWidgetAdapter<T extends WidgetModel, U extends WidgetController> implements WidgetAdapter<T, U>
{
    private T model;
    private U controller;
    private String controllerCtx;
    private boolean autoControllerEnabled = true;


    public T getWidgetModel()
    {
        return this.model;
    }


    public void setWidgetModel(T model)
    {
        this.model = model;
    }


    public void setControllerCtx(String ctx)
    {
        this.controllerCtx = ctx;
    }


    public String getControllerCtx()
    {
        return this.controllerCtx;
    }


    public void setWidgetController(U controller)
    {
        this.controller = controller;
    }


    public U getWidgetController()
    {
        return this.controller;
    }


    public boolean isAutoControllerEnabled()
    {
        return this.autoControllerEnabled;
    }


    public void setAutoControllerEnabled(boolean autoRegistrationEnabled)
    {
        this.autoControllerEnabled = autoRegistrationEnabled;
    }
}
