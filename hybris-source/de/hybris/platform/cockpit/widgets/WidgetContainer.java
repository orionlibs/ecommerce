package de.hybris.platform.cockpit.widgets;

import de.hybris.platform.cockpit.events.CockpitEventAcceptor;
import java.util.Map;

public interface WidgetContainer<T extends Widget>
{
    Map<String, T> initialize(Map<String, WidgetConfig> paramMap);


    void cleanup();


    T getWidget(String paramString);


    String getFocusedWidgetCode();


    void focusWidget(String paramString);


    void setWidgetFactory(WidgetFactory paramWidgetFactory);


    WidgetFactory getWidgetFactory();


    void addCockpitEventAcceptor(CockpitEventAcceptor paramCockpitEventAcceptor);


    void removeCockpitEventAcceptor(CockpitEventAcceptor paramCockpitEventAcceptor);
}
