/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.collectionbrowser;

import com.hybris.cockpitng.core.events.CockpitEvent;
import com.hybris.cockpitng.widgets.collectionbrowser.mold.CollectionBrowserMoldContext;
import com.hybris.cockpitng.widgets.collectionbrowser.mold.CollectionBrowserMoldStrategy;
import com.hybris.cockpitng.widgets.collectionbrowser.mold.impl.SinglePage;
import com.hybris.cockpitng.widgets.navigation.NavigationItemSelectorContext;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.Ordered;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Label;

class EmptyCollectionBrowserMoldStrategy implements CollectionBrowserMoldStrategy
{
    public static final String NULL_MOLD_TOOLTIP = "mold.null.tooltip";
    public static final String NULL_MOLD_LABEL = "mold.null.label";
    private CollectionBrowserMoldContext context;
    private Label label;
    private String typeCode;


    @Override
    public void setContext(final CollectionBrowserMoldContext context)
    {
        this.context = context;
    }


    @Override
    public void setPage(final SinglePage singlePage)
    {
        // NOOP
    }


    @Override
    public void render(final Component parent, final SinglePage singlePage)
    {
        label = new Label(context == null ? StringUtils.EMPTY : context.getWidgetInstanceManager().getLabel(NULL_MOLD_LABEL));
        label.setSclass("yw-collectionbrowser-empty");
        parent.appendChild(label);
        setPage(singlePage);
    }


    @Override
    public void previousItemSelectorInvocation()
    {
        // NOOP
    }


    @Override
    public void nextItemSelectorInvocation()
    {
        // NOOP
    }


    @Override
    public void release()
    {
        if(label != null)
        {
            label.setParent(null);
            label = null;
        }
    }


    @Override
    public boolean isHandlingObjectEvents(final String typeCode)
    {
        return false;
    }


    @Override
    public void handleObjectDeleteEvent(final CockpitEvent event)
    {
        // NOOP
    }


    @Override
    public void handleObjectUpdateEvent(final CockpitEvent event)
    {
        // NOOP
    }


    @Override
    public void handleObjectCreateEvent(final CockpitEvent event)
    {
        // NOOP
    }


    @Override
    public void selectItems(final Set<?> items)
    {
        // NOOP
    }


    @Override
    public void deselectItems()
    {
        // NOOP
    }


    @Override
    public String getName()
    {
        return "null-mold";
    }


    @Override
    public void setTypeCode(final String typeCode)
    {
        this.typeCode = typeCode;
    }


    @Override
    public String getTypeCode()
    {
        return typeCode;
    }


    @Override
    public String getTooltipText()
    {
        if(context != null)
        {
            return context.getWidgetInstanceManager().getLabel(NULL_MOLD_TOOLTIP);
        }
        return StringUtils.EMPTY;
    }


    @Override
    public NavigationItemSelectorContext getNavigationItemSelectorContext()
    {
        return null;
    }


    @Override
    public void reset()
    {
        // NOOP
    }


    @Override
    public int getOrder()
    {
        return Ordered.LOWEST_PRECEDENCE;
    }


    @Override
    public void setOrder(final int order)
    {
        throw new UnsupportedOperationException();
    }
}
