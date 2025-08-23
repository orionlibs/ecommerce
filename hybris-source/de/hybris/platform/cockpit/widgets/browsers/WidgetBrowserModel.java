package de.hybris.platform.cockpit.widgets.browsers;

import de.hybris.platform.cockpit.events.CockpitEventProducer;
import de.hybris.platform.cockpit.session.ConfigurableBrowserModel;
import de.hybris.platform.cockpit.widgets.WidgetConfig;
import de.hybris.platform.cockpit.widgets.WidgetFactory;
import java.util.Map;

public interface WidgetBrowserModel extends ConfigurableBrowserModel, CockpitEventProducer
{
    WidgetConfig getWidgetConfig(String paramString);


    Map<String, WidgetConfig> getWidgetMap();


    void setWidgetMap(Map<String, WidgetConfig> paramMap);


    void focusWidget(String paramString);


    String getFocusedWidgetCode();


    void setWidgetFactory(WidgetFactory paramWidgetFactory);


    WidgetFactory getWidgetFactory();


    String getViewTemplateURI();


    void setViewTemplateURI(String paramString);
}
