/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.common;

import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.util.BackofficeSpringUtil;
import com.hybris.cockpitng.widgets.util.WidgetRenderingUtils;
import org.zkoss.zk.ui.Component;

/**
 * It's a renderer that is able to propagate rendering events.
 * <p>
 * ProxyRenderer should be used when nested rendering is used (i.e. EditorAreaRenderer uses TabRenderer). In such
 * situation it is possible that all listeners of parent renderer (i.e. EditorArea's) wishes to be notified of rendering
 * events of nested renderer (i.e. TabRenderer). ProxyRenderer wraps such nested renderer registers as its listener and
 * renders it. If any events are posted by wrapped renderer, then they are passed over to parent renderer.
 * <p>
 * Parent listeners that are notified of nested rendering events gets information about particular component that has
 * been rendered and parent's configuration/data. To get rendering parameters of specific nested renderer, a cause event
 * may be received (see: {@link WidgetComponentRendererEvent#getCause()}.
 */
public class ProxyRenderer<PARENT, CONFIG, DATA>
{
    private static final String BEAN_WIDGET_RENDERING_UTILS = "widgetRenderingUtils";
    private final AbstractWidgetComponentRenderer<PARENT, CONFIG, DATA> renderer;
    private final PARENT parent;
    private final CONFIG config;
    private final DATA data;
    private final WidgetRenderingUtils widgetRenderingUtils;


    public ProxyRenderer(final AbstractWidgetComponentRenderer<PARENT, CONFIG, DATA> parentRenderer, final PARENT parentComponent,
                    final CONFIG parentConfig, final DATA parentData)
    {
        this(parentRenderer, parentComponent, parentConfig, parentData, BackofficeSpringUtil.getBean(BEAN_WIDGET_RENDERING_UTILS));
    }


    public ProxyRenderer(final AbstractWidgetComponentRenderer<PARENT, CONFIG, DATA> parentRenderer, final PARENT parentComponent,
                    final CONFIG parentConfig, final DATA parentData, final WidgetRenderingUtils widgetRenderingUtils)
    {
        this.renderer = parentRenderer;
        this.parent = parentComponent;
        this.config = parentConfig;
        this.data = parentData;
        this.widgetRenderingUtils = widgetRenderingUtils;
    }


    public PARENT getParent()
    {
        return parent;
    }


    public CONFIG getConfig()
    {
        return config;
    }


    public DATA getData()
    {
        return data;
    }


    public <PP, CC, DD> void render(final WidgetComponentRenderer<PP, CC, DD> renderer, final PP parent, final CC config,
                    final DD data, final DataType dataType, final WidgetInstanceManager wim)
    {
        if(renderer instanceof NotifyingWidgetComponentRenderer)
        {
            final WidgetComponentRendererListener<PP, CC, DD> listener = event -> {
                final WidgetComponentRendererEvent<PARENT, CONFIG, DATA> e = new WidgetComponentRendererEvent<>(event.getSource(),
                                this.renderer);
                e.setEventData(this.parent, this.config, this.data);
                e.initCause(event);
                this.renderer.fireComponentRendered(e);
            };
            try
            {
                ((NotifyingWidgetComponentRenderer)renderer).addRendererListener(listener);
                renderer.render(parent, config, data, dataType, wim);
            }
            finally
            {
                ((NotifyingWidgetComponentRenderer)renderer).removeRendererListener(listener);
            }
        }
        else
        {
            renderer.render(parent, config, data, dataType, wim);
        }
        if(shouldPropagateMarkedComponents(parent))
        {
            getWidgetRenderingUtils().moveMarkedComponents((Component)parent, (Component)this.parent);
        }
    }


    protected <PP> boolean shouldPropagateMarkedComponents(final PP parent)
    {
        return this.parent instanceof Component && parent instanceof Component && getWidgetRenderingUtils() != null;
    }


    protected WidgetRenderingUtils getWidgetRenderingUtils()
    {
        return widgetRenderingUtils;
    }
}
