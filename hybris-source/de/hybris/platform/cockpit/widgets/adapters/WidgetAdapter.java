package de.hybris.platform.cockpit.widgets.adapters;

import de.hybris.platform.cockpit.events.CockpitEventAcceptor;

public interface WidgetAdapter<T extends de.hybris.platform.cockpit.widgets.models.WidgetModel, U extends de.hybris.platform.cockpit.widgets.controllers.WidgetController> extends CockpitEventAcceptor
{
    void setWidgetModel(T paramT);


    T getWidgetModel();


    void setWidgetController(U paramU);


    U getWidgetController();


    void setControllerCtx(String paramString);


    String getControllerCtx();


    void setAutoControllerEnabled(boolean paramBoolean);


    boolean isAutoControllerEnabled();
}
