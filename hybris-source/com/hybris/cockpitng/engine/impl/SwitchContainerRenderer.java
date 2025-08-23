/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.engine.impl;

import com.hybris.cockpitng.components.Widgetchildren;
import com.hybris.cockpitng.components.Widgetslot;
import com.hybris.cockpitng.core.security.WidgetAuthorizationService;
import com.hybris.cockpitng.core.ui.WidgetInstance;
import com.hybris.cockpitng.util.CockpitSessionService;
import com.hybris.cockpitng.util.UITools;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class SwitchContainerRenderer extends AbstractChildrenContainerRenderer
{
    private static final Logger LOGGER = LoggerFactory.getLogger(SwitchContainerRenderer.class);
    private static final String SCLASS_SWITCH = "y-switch";
    private static final String SCLASS_SWITCH_ACTIVE = "y-switch-active";
    private static final String SCLASS_SWITCH_INACTIVE = "y-switch-inactive";
    private static final Pattern PATTERN_SWITCH_ACTIVE = Pattern.compile("\\b" + SCLASS_SWITCH_ACTIVE + "\\b");
    private static final Pattern PATTERN_SWITCH_INACTIVE = Pattern.compile("\\b" + SCLASS_SWITCH_INACTIVE + "\\b");
    private CockpitSessionService sessionService;
    private WidgetAuthorizationService widgetAuthorizationService;


    @Override
    public void render(final Widgetchildren childrenComponent, final List<WidgetInstance> children, final Map<String, Object> ctx)
    {
        final List<WidgetInstance> accessibleChildren = getAccessibleWidgets(childrenComponent.getParentWidgetInstance(), children);
        removeUnknownWidgetslots(childrenComponent, accessibleChildren);
        addMissingWidgetslots(childrenComponent, accessibleChildren);
        manageActiveState(childrenComponent, accessibleChildren);
    }


    protected void removeUnknownWidgetslots(final Widgetchildren childrenComponent, final List<WidgetInstance> children)
    {
        getSlots(childrenComponent).filter(cmp -> children.indexOf(cmp.getWidgetInstance()) < 0)
                        .forEach(slot -> slot.setParent(null));
    }


    protected void addMissingWidgetslots(final Widgetchildren childrenComponent, final List<WidgetInstance> children)
    {
        final Set<WidgetInstance> displayedWidgetInstances = getSlots(childrenComponent).map(Widgetslot::getWidgetInstance)
                        .collect(Collectors.toSet());
        children.stream().filter(widget -> !displayedWidgetInstances.contains(widget))
                        .forEach(widget -> addWidgetslot(childrenComponent, widget));
    }


    protected Widgetslot addWidgetslot(final Widgetchildren childrenComponent, final WidgetInstance widgetInstance)
    {
        final Widgetslot widgetContainer = createWidgetslot(childrenComponent, widgetInstance);
        setActive(widgetContainer, false);
        childrenComponent.appendChild(widgetContainer);
        return widgetContainer;
    }


    protected Widgetslot createWidgetslot(final Widgetchildren childrenComponent, final WidgetInstance widgetInstance)
    {
        final Widgetslot widgetContainer = new Widgetslot();
        widgetContainer.setWidgetInstance(widgetInstance);
        widgetContainer.setParentChildrenContainer(childrenComponent);
        widgetContainer.afterCompose();
        return widgetContainer;
    }


    protected void manageActiveState(final Widgetchildren childrenComponent, final List<WidgetInstance> children)
    {
        final WidgetInstance parentWidgetInstance = childrenComponent.getParentWidgetInstance();
        final int selection = parentWidgetInstance.getSelectedChildIndex();
        final Optional<Widgetslot> toDeactivate = getSlots(childrenComponent, getIsActivePredicate()).findFirst();
        if(!toDeactivate.isPresent() || children.indexOf(toDeactivate.get().getWidgetInstance()) != selection)
        {
            Optional<Widgetslot> toActivate = getSlots(childrenComponent, getIsInactivePredicate(), (slot) -> {
                final WidgetInstance widgetInstance = slot.getWidgetInstance();
                final int index = children.indexOf(widgetInstance);
                return index == selection;
            }).findFirst();
            if(!toActivate.isPresent() && selection > -1)
            {
                if(children.size() > selection)
                {
                    LOGGER.warn("[{}] Unknown selection requested. Creating new widgetslot for: {}",
                                    childrenComponent.getParentWidgetContainer().getWidgetInstance().getId(), children.get(selection).getId());
                    final Widgetslot widgetContainer = addWidgetslot(childrenComponent, children.get(selection));
                    toActivate = Optional.of(widgetContainer);
                }
                else
                {
                    LOGGER.warn("[{}] Unknown selection requested at index {}, while only available: {}",
                                    childrenComponent.getParentWidgetContainer().getWidgetInstance().getId(), selection, children);
                }
            }
            toDeactivate.ifPresent((slot) -> setActive(slot, false));
            toActivate.ifPresent((slot) -> setActive(slot, true));
        }
    }


    protected Stream<Widgetslot> getSlots(final Widgetchildren childrenComponent, final Predicate<Widgetslot>... predicates)
    {
        final Predicate<Widgetslot> all = Arrays.asList(predicates).stream().reduce((p1, p2) -> p1.and(p2)).orElse(el -> true);
        return childrenComponent.getChildren().stream().map(cmp -> (Widgetslot)cmp).filter(all);
    }


    protected Predicate<Widgetslot> getIsInactivePredicate()
    {
        return cmp -> PATTERN_SWITCH_INACTIVE.matcher(cmp.getSclass()).find();
    }


    protected Predicate<Widgetslot> getIsActivePredicate()
    {
        return cmp -> PATTERN_SWITCH_ACTIVE.matcher(cmp.getSclass()).find();
    }


    protected List<WidgetInstance> getAccessibleWidgets(final WidgetInstance parentWidgetInstance,
                    final List<WidgetInstance> widgets)
    {
        if(isAdminMode())
        {
            return widgets;
        }
        return widgets.stream()
                        .filter(widgetInstance -> getWidgetAuthorizationService().isAccessAllowed(widgetInstance.getWidget()))
                        .collect(Collectors.toList());
    }


    /**
     * @deprecated since 6.5
     * @see #getSlots(Widgetchildren, Predicate[])
     */
    @Deprecated(since = "6.5", forRemoval = true)
    protected List<Widgetslot> getAllWidgetslots(final Widgetchildren childrenComponent)
    {
        return getSlots(childrenComponent, cmp -> cmp.getSclass().contains(SCLASS_SWITCH)).collect(Collectors.toList());
    }


    /**
     * @deprecated since 6.5
     * @see #getSlots(Widgetchildren, Predicate[])
     * @see #getIsInactivePredicate()
     */
    @Deprecated(since = "6.5", forRemoval = true)
    protected List<Widgetslot> getInactiveWidgetslots(final Widgetchildren childrenComponent)
    {
        return getSlots(childrenComponent, getIsInactivePredicate()).collect(Collectors.toList());
    }


    /**
     * @deprecated since 6.5
     * @see #getSlots(Widgetchildren, Predicate[])
     * @see #getIsActivePredicate()
     */
    @Deprecated(since = "6.5", forRemoval = true)
    protected Optional<Widgetslot> getActiveWidgetslot(final Widgetchildren childrenComponent)
    {
        return getSlots(childrenComponent, getIsActivePredicate()).findFirst();
    }


    protected void setActive(final Widgetslot slot, final boolean active)
    {
        UITools.modifySClass(slot, SCLASS_SWITCH, true);
        UITools.modifySClass(slot, SCLASS_SWITCH_ACTIVE, active);
        UITools.modifySClass(slot, SCLASS_SWITCH_INACTIVE, !active);
    }


    protected boolean isAdminMode()
    {
        return Boolean.TRUE.equals(getSessionService().getAttribute("cockpitAdminMode"));
    }


    @Required
    public void setSessionService(final CockpitSessionService sessionService)
    {
        this.sessionService = sessionService;
    }


    protected CockpitSessionService getSessionService()
    {
        return sessionService;
    }


    @Required
    public void setWidgetAuthorizationService(final WidgetAuthorizationService widgetAuthorizationService)
    {
        this.widgetAuthorizationService = widgetAuthorizationService;
    }


    protected WidgetAuthorizationService getWidgetAuthorizationService()
    {
        return widgetAuthorizationService;
    }
}
