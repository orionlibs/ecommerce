/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.viewswitcher.permissions.impl;

import com.google.common.collect.Lists;
import com.hybris.backoffice.widgets.viewswitcher.permissions.ViewSwitcherUtils;
import com.hybris.cockpitng.config.viewswitcher.jaxb.Authority;
import com.hybris.cockpitng.config.viewswitcher.jaxb.View;
import com.hybris.cockpitng.config.viewswitcher.jaxb.ViewSwitcher;
import com.hybris.cockpitng.core.Widget;
import com.hybris.cockpitng.core.config.CockpitConfigurationException;
import com.hybris.cockpitng.core.config.CockpitConfigurationService;
import com.hybris.cockpitng.core.config.impl.DefaultConfigContext;
import com.hybris.cockpitng.core.ui.WidgetInstance;
import com.hybris.cockpitng.core.user.AuthorityGroupService;
import com.hybris.cockpitng.core.user.CockpitUserService;
import com.hybris.cockpitng.core.user.impl.AuthorityGroup;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

/**
 * Default implementation of an interface used access View Switcher widget configuration from outside of the widget
 * controller
 */
public class DefaultViewSwitcherUtils implements ViewSwitcherUtils
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultViewSwitcherUtils.class);
    private CockpitConfigurationService cockpitConfigurationService;
    private CockpitUserService cockpitUserService;
    private AuthorityGroupService authorityGroupService;


    /**
     * @deprecated Since 6.6 please use {@link #getAccessibleWidgets(String, WidgetInstance, List)}
     */
    @Deprecated(since = "6.6", forRemoval = true)
    @Override
    public List<Widget> getAccessibleWidgets(final String configContextCode, final List<Widget> widgets)
    {
        return getAccessibleWidgets(configContextCode, null, widgets);
    }


    @Override
    public List<Widget> getAccessibleWidgets(final String configContextCode, final WidgetInstance widgetInstance,
                    final List<Widget> widgets)
    {
        return getAccessible(configContextCode, widgetInstance, widgets, Widget::getId);
    }


    /**
     * @deprecated Since 6.6 please use {@link #getAccessibleWidgetInstances(String, WidgetInstance, List)}
     */
    @Deprecated(since = "6.6", forRemoval = true)
    @Override
    public List<WidgetInstance> getAccessibleWidgetInstances(final String configContextCode,
                    final List<WidgetInstance> widgetInstances)
    {
        return getAccessibleWidgetInstances(configContextCode, null, widgetInstances);
    }


    @Override
    public List<WidgetInstance> getAccessibleWidgetInstances(final String configContextCode, final WidgetInstance widgetInstance,
                    final List<WidgetInstance> widgetInstances)
    {
        return getAccessible(configContextCode, widgetInstance, widgetInstances, instance -> instance.getWidget().getId());
    }


    protected <E> List<E> getAccessible(final String configContextCode, final List<E> elements,
                    final Function<E, String> idProvider)
    {
        return getAccessible(configContextCode, null, elements, idProvider);
    }


    protected <E> List<E> getAccessible(final String configContextCode, final WidgetInstance widgetInstance,
                    final List<E> elements, final Function<E, String> idProvider)
    {
        final Optional<Set<String>> viewsAllowedByConfiguration = getViewsAllowedByConfiguration(configContextCode, widgetInstance);
        if(viewsAllowedByConfiguration.isPresent())
        {
            return elements.stream().filter(element -> viewsAllowedByConfiguration.get().contains(idProvider.apply(element)))
                            .collect(Collectors.toList());
        }
        return elements;
    }


    protected Optional<Set<String>> getViewsAllowedByConfiguration(final String configContextCode)
    {
        return getViewsAllowedByConfiguration(configContextCode, null);
    }


    protected ViewSwitcher loadConfiguration(final String configContextCode)
    {
        return loadConfiguration(configContextCode, null);
    }


    protected Optional<Set<String>> getViewsFilteredByCurrentUserAuthorities(final ViewSwitcher configuration)
    {
        final List<String> authorities = getCurrentUserAuthorities();
        if(CollectionUtils.isNotEmpty(configuration.getAuthority()))
        {
            final Set<String> views = configuration.getAuthority().stream()
                            .filter(authority -> authorities.contains(authority.getName())).map(Authority::getView).flatMap(Collection::stream)
                            .map(View::getId).collect(Collectors.toSet());
            return views.isEmpty() ? Optional.empty() : Optional.of(views);
        }
        return Optional.empty();
    }


    protected Optional<Set<String>> getViewsAllowedByConfiguration(final String configContextCode,
                    final WidgetInstance widgetInstance)
    {
        final ViewSwitcher configuration = loadConfiguration(configContextCode, widgetInstance);
        return getViewsFilteredByCurrentUserAuthorities(configuration);
    }


    protected ViewSwitcher loadConfiguration(final String configContextCode, final WidgetInstance widgetInstance)
    {
        try
        {
            return getCockpitConfigurationService().loadConfiguration(new DefaultConfigContext(configContextCode),
                            ViewSwitcher.class, widgetInstance);
        }
        catch(final CockpitConfigurationException e)
        {
            LOG.warn("Could not load View Switcher widget configuration", e);
            return new ViewSwitcher();
        }
    }


    protected List<String> getCurrentUserAuthorities()
    {
        final String currentUser = getCockpitUserService().getCurrentUser();
        final AuthorityGroup authorityGroup = getAuthorityGroupService().getActiveAuthorityGroupForUser(currentUser);
        final List<String> authoritiesWithUser = Lists.newArrayList(currentUser);
        if(authorityGroup != null)
        {
            authoritiesWithUser.addAll(authorityGroup.getAuthorities());
        }
        return authoritiesWithUser;
    }


    protected CockpitConfigurationService getCockpitConfigurationService()
    {
        return cockpitConfigurationService;
    }


    @Required
    public void setCockpitConfigurationService(final CockpitConfigurationService cockpitConfigurationService)
    {
        this.cockpitConfigurationService = cockpitConfigurationService;
    }


    protected CockpitUserService getCockpitUserService()
    {
        return cockpitUserService;
    }


    @Required
    public void setCockpitUserService(final CockpitUserService cockpitUserService)
    {
        this.cockpitUserService = cockpitUserService;
    }


    protected AuthorityGroupService getAuthorityGroupService()
    {
        return authorityGroupService;
    }


    @Required
    public void setAuthorityGroupService(final AuthorityGroupService authorityGroupService)
    {
        this.authorityGroupService = authorityGroupService;
    }
}
