/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.util;

import com.hybris.cockpitng.components.Widgetslot;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.engine.impl.DefaultWidgetInstanceManager;
import com.hybris.cockpitng.renderers.header.DefaultWidgetCaptionWrapper;
import com.hybris.cockpitng.renderers.header.WidgetCaptionRenderer;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

public final class WidgetSlotUtils
{
    /**
     * Private constructor to prevent initialization of this utils class.
     */
    private WidgetSlotUtils()
    {
        throw new AssertionError("Utility class should not be instantiated");
    }


    /**
     * Creates caption based on the child widget's title for its parent widget. This implementation supports collapsible
     * container aware widgets.
     *
     * @param slot
     *           Widget to listen for title changes.
     * @param fallbackTitle
     *           Title to be used for the case the embedded widget slot does not provide any title.
     * @return Caption component with the updated title for the parent widget.
     * @see WidgetCaptionRenderer
     */
    public static Component createCaption(final Widgetslot slot, final String fallbackTitle,
                    final DefaultWidgetCaptionWrapper callback)
    {
        final Object widgetController = slot.getAttribute(Widgetslot.ATTRIBUTE_WIDGET_CONTROLLER);
        final boolean captionAwareWidget = widgetController instanceof WidgetCaptionRenderer;
        if(captionAwareWidget)
        {
            final Div container = new Div();
            container.setSclass("widgetSlotLabelHolder");
            container.appendChild(((WidgetCaptionRenderer)widgetController).renderCaption(callback));
            return container;
        }
        else
        {
            final String titleAttribute = (String)slot.getAttribute(DefaultWidgetInstanceManager.CNG_CURRENT_TITLE);
            final String initialLabel = StringUtils.isEmpty(titleAttribute) ? fallbackTitle : titleAttribute;
            final Label label = new Label(initialLabel);
            final TitleChangeListener listener = new TitleChangeListener(label);
            slot.setAttribute(WidgetInstanceManager.CNG_TITLE_CHANGE_LISTENER, listener);
            return label;
        }
    }


    /**
     * Listens for title changes in the child slot.
     */
    private static class TitleChangeListener implements EventListener<Event>
    {
        private final Label label;


        public TitleChangeListener(final Label label)
        {
            this.label = label;
        }


        @Override
        public void onEvent(final Event event)
        {
            label.setValue((String)event.getData());
        }
    }
}
