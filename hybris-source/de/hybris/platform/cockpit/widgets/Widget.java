package de.hybris.platform.cockpit.widgets;

import de.hybris.platform.cockpit.components.FocusableComponent;
import de.hybris.platform.cockpit.widgets.events.WidgetModelListener;
import de.hybris.platform.cockpit.widgets.renderers.WidgetRenderer;
import java.util.Map;
import org.zkoss.zk.ui.api.HtmlBasedComponent;

public interface Widget<T extends de.hybris.platform.cockpit.widgets.models.WidgetModel, U extends de.hybris.platform.cockpit.widgets.controllers.WidgetController> extends WidgetModelListener, FocusableComponent
{
    void setWidgetCode(String paramString);


    String getWidgetCode();


    void setWidgetModel(T paramT);


    T getWidgetModel();


    void setWidgetContainer(WidgetContainer paramWidgetContainer);


    WidgetContainer getWidgetContainer();


    void setFocusable(boolean paramBoolean);


    boolean isFocusable();


    void setWidgetTitle(String paramString);


    String getWidgetTitle();


    void initialize(Map<String, Object> paramMap);


    boolean isInitialized();


    HtmlBasedComponent getCaption();


    HtmlBasedComponent getContent();


    void cleanup();


    void setWidgetRenderer(WidgetRenderer paramWidgetRenderer);


    WidgetRenderer getWidgetRenderer();


    void setWidgetController(U paramU);


    U getWidgetController();


    void setControllerCtx(String paramString);


    String getControllerCtx();


    void setLazyLoadingEnabled(boolean paramBoolean);


    boolean isLazyLoadingEnabled();
}
