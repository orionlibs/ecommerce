/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.engine.impl;

import com.google.common.collect.Lists;
import com.hybris.cockpitng.components.Widgetchildren;
import com.hybris.cockpitng.components.Widgetslot;
import com.hybris.cockpitng.config.dashboard.jaxb.Dashboard;
import com.hybris.cockpitng.config.dashboard.jaxb.Grid;
import com.hybris.cockpitng.config.dashboard.jaxb.Placement;
import com.hybris.cockpitng.config.dashboard.jaxb.UnassignedBehavior;
import com.hybris.cockpitng.core.WidgetController;
import com.hybris.cockpitng.core.config.CockpitConfigurationException;
import com.hybris.cockpitng.core.config.impl.DefaultConfigContext;
import com.hybris.cockpitng.core.security.WidgetAuthorizationService;
import com.hybris.cockpitng.core.ui.WidgetInstance;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.layout.ElementPlacement;
import com.hybris.cockpitng.layout.LayoutManager;
import com.hybris.cockpitng.layout.Point;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.cockpitng.util.UITools;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.DropEvent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Div;

/**
 * Renders a {@link Widgetchildren} component as a Dashboard (column-span and drag'n'drop capabilities) and its children
 * as Panels
 */
public class DashboardContainerRenderer extends AbstractChildrenContainerRenderer
{
    public static final String SCREEN_WIDTH = "screenWidth";
    public static final String ON_CHANGE_DND_SUPPORT = "onChangeDndSupport";
    public static final String YW_DASHBOARD_TOP_CONTAINER = "yw-dashboard-top-container";
    public static final String DASHBOARD_DEFAULT_CONFIG_CONTEXT = "dashboard";
    public static final String YW_DASHBOARD_DND_ENABLED = "yw-dashboard-dnd-enabled";
    public static final String DND_ENABLED_MODEL_KEY = "dnd_enabled_model_key";
    public static final String DND_KEY = "true";
    public static final String YW_DASHBOARD_FLUID = "yw-dashboard-fluid";
    public static final String DASHBOARD_GRID_CURRENT_MIN_WIDTH = "dashboardGridCurrentMinWidth";
    public static final String DASHBOARD_GRID_CURRENT_MAX_WIDTH = "dashboardGridCurrentMaxWidth";
    public static final String CSS_PX = "px";
    public static final String DASHBOARD_CONFIG_CONTEXT = "dashboardConfigContext";
    private static final Logger LOG = LoggerFactory.getLogger(DashboardContainerRenderer.class);
    private LayoutManager<WidgetInstance> layoutManager;
    private WidgetAuthorizationService widgetAuthorizationService;


    @Override
    public void render(final Widgetchildren childrenComponent, final List<WidgetInstance> allChildren,
                    final Map<String, Object> ctx)
    {
        final List<WidgetInstance> children = allChildren.stream()
                        .filter(wi -> widgetAuthorizationService.isAccessAllowed(wi.getWidget())).collect(Collectors.toList());
        final Div container = new Div();
        container.setSclass(YW_DASHBOARD_TOP_CONTAINER);
        if(!children.isEmpty())
        {
            final Dashboard dashboard = findDashboardConfiguration(childrenComponent);
            final Grid grid = findGridByScreenSize(dashboard);
            UITools.modifySClass(container, YW_DASHBOARD_FLUID, grid.isFluid());
            final List<ElementPlacement<WidgetInstance>> placements = readPlacements(children, grid);
            final Collection<HtmlBasedComponent> draggableElements = getLayoutManager().positionElements(container, placements,
                            (ctr, placement) -> {
                                if(grid != null && ctr instanceof HtmlBasedComponent)
                                {
                                    final int placementHeight = placement == null ? 1 : placement.getHeight();
                                    ((HtmlBasedComponent)ctr).setHeight((placementHeight * grid.getRowHeight()) + CSS_PX);
                                }
                                if(placement != null)
                                {
                                    final Widgetslot widgetslot = new Widgetslot();
                                    widgetslot.setWidgetInstance(placement.getElement());
                                    widgetslot.setParentChildrenContainer(childrenComponent);
                                    widgetslot.setDroppable(DND_KEY);
                                    widgetslot.addEventListener(Events.ON_DROP, (EventListener<DropEvent>)this::swapTargetSource);
                                    widgetslot.afterCompose();
                                    return widgetslot;
                                }
                                return null;
                            });
            childrenComponent.addEventListener(ON_CHANGE_DND_SUPPORT, e -> {
                final Object data = e.getData();
                final boolean enabled = data instanceof Boolean && (Boolean)data;
                makeDraggable(draggableElements, enabled);
                UITools.modifySClass(container, YW_DASHBOARD_DND_ENABLED, enabled);
            });
            final WidgetInstanceManager wim = getWidgetInstanceManager(childrenComponent);
            if(wim != null)
            {
                final boolean dndEnabled = Boolean.TRUE.equals(wim.getModel().getValue(DND_ENABLED_MODEL_KEY, Object.class));
                makeDraggable(draggableElements, dndEnabled);
                UITools.modifySClass(container, YW_DASHBOARD_DND_ENABLED, dndEnabled);
                wim.getModel().setValue(DASHBOARD_GRID_CURRENT_MIN_WIDTH, grid.getMinScreenWidth());
                wim.getModel().setValue(DASHBOARD_GRID_CURRENT_MAX_WIDTH, grid.getMaxScreenWidth());
            }
        }
        childrenComponent.appendChild(container);
    }


    protected void makeDraggable(final Collection<HtmlBasedComponent> draggableElements, final boolean enabled)
    {
        for(final HtmlBasedComponent draggable : draggableElements)
        {
            draggable.setDraggable(enabled ? DND_KEY : null);
        }
    }


    protected void swapTargetSource(final DropEvent drop)
    {
        final Component a = drop.getTarget();
        final Component b = drop.getDragged().getNextSibling();
        if(a == b || !a.hasAttribute(LayoutManager.DND_KEY) || !b.hasAttribute(LayoutManager.DND_KEY)
                        || !ObjectUtils.equals(a.getAttribute(LayoutManager.DND_KEY), b.getAttribute(LayoutManager.DND_KEY)))
        {
            LOG.info("Dragged and dropped elements are not compatible; skipping");
            return;
        }
        final Component aParent = a.getParent();
        final Component bParent = b.getParent();
        final int aPos = aParent.getChildren().indexOf(a);
        final int bPos = bParent.getChildren().indexOf(b);
        bParent.getChildren().add(bPos, a);
        aParent.getChildren().add(aPos, b);
        a.setParent(bParent);
        b.setParent(aParent);
    }


    protected Dashboard findDashboardConfiguration(final Widgetchildren childrenComponent)
    {
        final WidgetInstanceManager wim = getWidgetInstanceManager(childrenComponent);
        if(wim != null)
        {
            return loadDashboardConfiguration(wim);
        }
        return null;
    }


    protected List<ElementPlacement<WidgetInstance>> readPlacements(final List<WidgetInstance> children, final Grid grid)
    {
        if(grid == null)
        {
            return prepareDefaultPlacements(children);
        }
        else
        {
            final List<ElementPlacement<WidgetInstance>> placements = Lists.newArrayList();
            Integer minX = null;
            Integer minY = null;
            for(final WidgetInstance child : children)
            {
                final ElementPlacement<WidgetInstance> placement = getPlacement(child, grid);
                if(placement != null)
                {
                    placements.add(placement);
                    if(placement.getTopLeft() != Point.NULL_POINT && placement.getBottomRight() != Point.NULL_POINT)
                    {
                        minX = minX == null ? placement.getTopLeft().getX() : Math.min(minX, placement.getTopLeft().getX());
                        minY = minY == null ? placement.getTopLeft().getY() : Math.min(minY, placement.getTopLeft().getY());
                    }
                }
            }
            if(minX != null && minY != null && (minX != 0 || minY != 0))
            {
                for(final ElementPlacement<WidgetInstance> placement : placements)
                {
                    final Point p1 = placement.getBottomRight();
                    final Point p2 = placement.getTopLeft();
                    if(p1 != Point.NULL_POINT && p2 != Point.NULL_POINT)
                    {
                        p1.setX(p1.getX() - minX);
                        p1.setY(p1.getY() - minY);
                        p2.setX(p2.getX() - minX);
                        p2.setY(p2.getY() - minY);
                    }
                }
            }
            return placements;
        }
    }


    protected Grid findGridByScreenSize(final Dashboard dashboard)
    {
        if(dashboard == null)
        {
            return null;
        }
        final Object val = Sessions.getCurrent().getAttribute(SCREEN_WIDTH);
        if(val instanceof Integer)
        {
            final int width = (Integer)val;
            for(final Grid grid : dashboard.getGrid())
            {
                final Integer min = grid.getMinScreenWidth();
                final Integer max = grid.getMaxScreenWidth();
                if((min == null || min <= width) && (max == null || max >= width))
                {
                    return grid;
                }
            }
        }
        return findDefaultGrid(dashboard);
    }


    protected Grid findDefaultGrid(final Dashboard dashboard)
    {
        for(final Grid candidate : dashboard.getGrid())
        {
            if(StringUtils.equals(dashboard.getDefaultGridId(), candidate.getId()))
            {
                return candidate;
            }
        }
        return dashboard.getGrid().stream().findFirst().orElse(null);
    }


    protected Dashboard loadDashboardConfiguration(final WidgetInstanceManager wim)
    {
        try
        {
            final String component = StringUtils.defaultIfBlank(wim.getWidgetSettings().getString(DASHBOARD_CONFIG_CONTEXT),
                            DASHBOARD_DEFAULT_CONFIG_CONTEXT);
            return wim.loadConfiguration(new DefaultConfigContext(component), Dashboard.class);
        }
        catch(final CockpitConfigurationException cce)
        {
            LOG.warn(cce.getMessage(), cce);
        }
        return null;
    }


    protected List<ElementPlacement<WidgetInstance>> prepareDefaultPlacements(final List<WidgetInstance> children)
    {
        return children.stream().map(child -> new ElementPlacement<>(child, Point.NULL_POINT, Point.NULL_POINT))
                        .collect(Collectors.toList());
    }


    protected ElementPlacement<WidgetInstance> getPlacement(final WidgetInstance child, final Grid grid)
    {
        for(final Placement placement : grid.getPlacement())
        {
            if(StringUtils.equals(child.getId(), placement.getWidgetId()))
            {
                final int x1 = placement.getX();
                final int x2 = x1 + placement.getWidth() - 1;
                final int y1 = placement.getY();
                final int y2 = y1 + placement.getHeight() - 1;
                return new ElementPlacement<>(child, new Point(x1, y1), new Point(x2, y2));
            }
        }
        if(UnassignedBehavior.APPEND.equals(grid.getUnassigned()))
        {
            return new ElementPlacement<>(child, Point.NULL_POINT, Point.NULL_POINT);
        }
        return null;
    }


    public LayoutManager<WidgetInstance> getLayoutManager()
    {
        return layoutManager;
    }


    @Required
    public void setLayoutManager(final LayoutManager<WidgetInstance> layoutManager)
    {
        this.layoutManager = layoutManager;
    }


    protected WidgetInstanceManager getWidgetInstanceManager(final Widgetchildren childrenComponent)
    {
        final Widgetslot parentWidgetContainer = childrenComponent.getParentWidgetContainer();
        if(parentWidgetContainer != null)
        {
            final WidgetController controller = parentWidgetContainer.getViewController();
            if(controller instanceof DefaultWidgetController)
            {
                return ((DefaultWidgetController)controller).getWidgetInstanceManager();
            }
        }
        return null;
    }


    public WidgetAuthorizationService getWidgetAuthorizationService()
    {
        return widgetAuthorizationService;
    }


    @Required
    public void setWidgetAuthorizationService(final WidgetAuthorizationService widgetAuthorizationService)
    {
        this.widgetAuthorizationService = widgetAuthorizationService;
    }
}
