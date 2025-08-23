/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.common;

import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Div;

/**
 * Renderer which allows to group renderers
 *
 * @param <PARENT>
 *           type of parent component on which renderer is able to render
 * @param <CONFIG>
 *           type of configuration for renderer
 * @param <DATA>
 *           type of data that may be rendered
 */
public class CompoundWidgetComponentRenderer<PARENT extends Component, CONFIG, DATA>
                extends AbstractWidgetComponentRenderer<PARENT, CONFIG, DATA>
{
    public static final String YW_COMPOUND_RENDERER_CONTAINER = "yw-compound-renderer-container";
    private List<WidgetComponentRenderer> renderers;


    @Override
    public void render(final PARENT parent, final CONFIG configuration, final DATA data, final DataType dataType,
                    final WidgetInstanceManager widgetInstanceManager)
    {
        renderers.forEach(renderer -> {
            final Component div = createRendererContainer();
            parent.appendChild(div);
            renderer.render(div, configuration, data, dataType, widgetInstanceManager);
        });
        fireComponentRendered(parent, configuration, data);
    }


    protected Component createRendererContainer()
    {
        final Div div = new Div();
        div.setSclass(YW_COMPOUND_RENDERER_CONTAINER);
        return div;
    }


    @Required
    public void setRenderers(final List<WidgetComponentRenderer> renderers)
    {
        this.renderers = renderers;
    }
}
