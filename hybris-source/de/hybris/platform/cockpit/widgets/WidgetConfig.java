package de.hybris.platform.cockpit.widgets;

import de.hybris.platform.cockpit.widgets.adapters.WidgetAdapter;
import de.hybris.platform.cockpit.widgets.controllers.WidgetController;
import de.hybris.platform.cockpit.widgets.models.WidgetModel;
import de.hybris.platform.cockpit.widgets.renderers.WidgetRenderer;

public interface WidgetConfig<T extends Widget>
{
    void setWidgetTitle(String paramString);


    String getWidgetTitle();


    void setWidgetClass(Class<T> paramClass);


    Class<T> getWidgetClass();


    void setWidgetRenderer(WidgetRenderer paramWidgetRenderer);


    WidgetRenderer getWidgetRenderer();


    void setWidgetController(WidgetController paramWidgetController);


    WidgetController getWidgetController();


    void setControllerCtx(String paramString);


    String getControllerCtx();


    void setWidgetModel(WidgetModel paramWidgetModel);


    WidgetModel getWidgetModel();


    void setWidgetAdapter(WidgetAdapter paramWidgetAdapter);


    WidgetAdapter getWidgetAdapter();


    void setFocusable(boolean paramBoolean);


    boolean isFocusable();


    String getWidgetSclass();


    void setWidgetSclass(String paramString);


    void setLazyLoadingEnabled(boolean paramBoolean);


    boolean isLazyLoadingEnabled();
}
