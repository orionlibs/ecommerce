/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.refineby.renderer;

import com.hybris.cockpitng.dataaccess.context.Context;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.search.data.facet.FacetData;
import com.hybris.cockpitng.search.data.facet.FacetValueData;
import com.hybris.cockpitng.util.UITools;
import java.util.Set;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

public class ColourFacetRenderer extends StandardFacetRenderer
{
    public static final String YW_COLOUR_FACET_CONTAINER = "yw-colour-facet-container";
    public static final String YW_COLOUR_FACET_BOX = "yw-colour-facet-box";
    public static final String YW_COLOUR_FACET_BOX_SELECTED = "yw-colour-facet-box-selected";
    public static final String RENDER_COLOR_NAME_SETTING_KEY = "renderColorName";


    @Override
    public void renderFacet(final Component parent, final FacetData facet, final boolean selected, final WidgetInstanceManager wim,
                    final Context context)
    {
        if(!shouldFacetBeRendered(facet, wim))
        {
            return;
        }
        final Label facetLabel = createFacetLabel(calculateFacetLabel(facet, wim));
        parent.appendChild(facetLabel);
        final Div facetValueContainer = new Div();
        facetValueContainer.setSclass(YW_COLOUR_FACET_CONTAINER);
        parent.appendChild(facetValueContainer);
        final Set<String> selectedFacets = findSelectedFacets(facet.getName(), wim);
        facet.getFacetValues().stream().forEach(facetValue -> {
            final Component checkbox = createSingleValue(facet, facetValue, selectedFacets.contains(facetValue.getName()), wim,
                            context);
            facetValueContainer.appendChild(checkbox);
        });
    }


    protected Component createSingleValue(final FacetData facet, final FacetValueData facetValueData, final Boolean isChecked,
                    final WidgetInstanceManager wim, final Context context)
    {
        final Div check = new Div();
        final boolean shouldColorNameBeRendered = shouldRenderColorName(context);
        if(shouldColorNameBeRendered)
        {
            final Label colorLabel = new Label(calculateFacetValueLabel(facetValueData, wim));
            check.appendChild(colorLabel);
        }
        applyBackgroundColor(check, context, facetValueData.getName());
        check.setTooltiptext(calculateFacetValueLabel(facetValueData, wim));
        check.setSclass(YW_COLOUR_FACET_BOX);
        if(isChecked)
        {
            UITools.modifySClass(check, YW_COLOUR_FACET_BOX_SELECTED, true);
        }
        check.addEventListener(Events.ON_CLICK, e -> select(facet.getName(), facetValueData.getName(), wim));
        return check;
    }


    protected boolean shouldRenderColorName(final Context context)
    {
        final Object shouldColorNameBeRendered = context.getAttribute(RENDER_COLOR_NAME_SETTING_KEY);
        if(shouldColorNameBeRendered != null)
        {
            return Boolean.valueOf(shouldColorNameBeRendered.toString());
        }
        return false;
    }


    protected void applyBackgroundColor(final Div check, final Context context, final String facetValueName)
    {
        final Object facetColor = context.getAttribute(facetValueName);
        if(facetColor instanceof String)
        {
            check.setStyle(String.format("background-color: %s;", facetColor));
        }
    }
}
