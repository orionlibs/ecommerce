package de.hybris.platform.cockpit.widgets.renderers;

import org.zkoss.zk.ui.api.HtmlBasedComponent;

public interface WidgetRenderer<T extends de.hybris.platform.cockpit.widgets.Widget>
{
    HtmlBasedComponent createCaption(T paramT);


    HtmlBasedComponent createContent(T paramT);
}
