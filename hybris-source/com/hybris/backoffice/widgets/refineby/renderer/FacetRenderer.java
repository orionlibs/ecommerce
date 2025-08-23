/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.refineby.renderer;

import com.hybris.cockpitng.dataaccess.context.Context;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.search.data.facet.FacetData;
import org.zkoss.zk.ui.Component;

/**
 * Renderer for a particular facet
 */
public interface FacetRenderer
{
    /**
     * Render a single facet
     *
     * @param parent an instance of {@link Component}
     * @param data an instance of {@link FacetData}
     * @param renderSelected an instance of {@link Boolean}
     * @param wim an instance of {@link WidgetInstanceManager}
     * @param context an instance of {@link Context}
     */
    void renderFacet(final Component parent, final FacetData data, final boolean renderSelected, final WidgetInstanceManager wim,
                    final Context context);
}
