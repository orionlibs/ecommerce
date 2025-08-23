/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.perspectivechooser.perspectiveresolver;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.backoffice.navigation.NavigationTree;
import com.hybris.cockpitng.config.perspectivechooser.jaxb.Authority;
import com.hybris.cockpitng.config.perspectivechooser.jaxb.Perspective;
import com.hybris.cockpitng.config.perspectivechooser.jaxb.PerspectiveChooser;
import com.hybris.cockpitng.core.config.CockpitConfigurationException;
import com.hybris.cockpitng.core.config.CockpitConfigurationNotFoundException;
import com.hybris.cockpitng.core.config.CockpitConfigurationService;
import com.hybris.cockpitng.core.config.ConfigContext;
import com.hybris.cockpitng.core.config.impl.DefaultConfigContext;
import com.hybris.cockpitng.core.user.AuthorityGroupService;
import com.hybris.cockpitng.core.user.CockpitUserService;
import com.hybris.cockpitng.core.user.impl.AuthorityGroup;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of the {@link DefaultPerspectiveResolver} that uses Cockpit UI configuration to resolve default
 * perspective for current business role.
 *
 * <pre>
 *  &lt;context component="perspective-chooser" principal="full"&gt;
 *    &lt;ps:perspective-chooser xmlns:ps="http://www.hybris.com/cockpitng/config/perspectiveChooser"&gt;
 *      &lt;ps:defaultPerspective name="Home"/&gt;
 *    &lt;/ps:perspective-chooser&gt;
 *  &lt;/context&gt;
 * </pre>
 */
public class AuthorityGroupDefaultPerspectiveResolver implements DefaultPerspectiveResolver
{
    private static final Logger LOG = LoggerFactory.getLogger(AuthorityGroupDefaultPerspectiveResolver.class);
    private static final String PERSPECTIVE_CHOOSER = "perspective-chooser";
    private static final String CANNOT_LOAD_PERSPECTIVE_CHOOSER_MESSAGE = "Cannot retrieve perspective chooser widget configuration";
    private static final String NO_CONFIG_FOR_CURRENT_USER_MESSAGE = "No config found for current user.";
    /**
     * @deprecated since 6.7 - not used anymore, configuration is provided by WidgetInstanceManager
     */
    @Deprecated(since = "6.7", forRemoval = true)
    private CockpitConfigurationService cockpitConfigurationService;
    private final CockpitUserService cockpitUserService;
    private final AuthorityGroupService authorityGroupService;


    public AuthorityGroupDefaultPerspectiveResolver(final CockpitUserService cockpitUserService,
                    final AuthorityGroupService authorityGroupService)
    {
        this.cockpitUserService = cockpitUserService;
        this.authorityGroupService = authorityGroupService;
    }


    /**
     * @deprecated since 6.7 - not used anymore
     */
    @Deprecated(since = "6.7", forRemoval = true)
    public AuthorityGroupDefaultPerspectiveResolver(final CockpitConfigurationService cockpitConfigurationService,
                    final CockpitUserService cockpitUserService, final AuthorityGroupService authorityGroupService)
    {
        this.cockpitConfigurationService = cockpitConfigurationService;
        this.cockpitUserService = cockpitUserService;
        this.authorityGroupService = authorityGroupService;
    }


    @Override
    public List<NavigationNode> getPermittedPerspectives(final NavigationTree navigationTree)
    {
        List<NavigationNode> ret = Lists.newArrayList();
        if(navigationTree == null || CollectionUtils.isEmpty(navigationTree.getRootNodes()))
        {
            return ret;
        }
        ret.addAll(navigationTree.getRootNodes());
        final String currentUser = cockpitUserService.getCurrentUser();
        final AuthorityGroup authorityGroup = authorityGroupService.getActiveAuthorityGroupForUser(currentUser);
        final List<String> authoritiesWithUser = Lists.newArrayList(currentUser);
        if(authorityGroup != null)
        {
            authoritiesWithUser.addAll(authorityGroup.getAuthorities());
        }
        final Set<String> perspectivesForAuthority = getPerspectivesForAuthorities(authoritiesWithUser);
        if(CollectionUtils.isNotEmpty(perspectivesForAuthority))
        {
            ret = navigationTree.getRootNodes().stream()
                            .filter(navigationNode -> perspectivesForAuthority.contains(navigationNode.getId())).collect(Collectors.toList());
        }
        return ret;
    }


    /**
     * @deprecated since 6.5, allowed perspectives configuration has been moved to View Switcher widget
     */
    @Deprecated(since = "6.5", forRemoval = true)
    protected Set<String> getPerspectivesForAuthorities(final List<String> authorities)
    {
        final Set<String> ret = Sets.newHashSet();
        Map<String, Set<String>> perspectivesByAuthority = Maps.newHashMap();
        final PerspectiveChooser perspectiveChooser = loadPerspectiveChooserConfig();
        if(perspectiveChooser != null)
        {
            perspectivesByAuthority = perspectiveChooser.getAuthority().stream().collect(Collectors.toMap(Authority::getName,
                            perspective -> perspective.getPerspective().stream().map(Perspective::getId).collect(Collectors.toSet())));
        }
        for(final String authority : authorities)
        {
            final Set<String> perspectives = perspectivesByAuthority.get(authority);
            if(perspectives != null)
            {
                ret.addAll(perspectives);
            }
        }
        return ret;
    }


    /**
     * @deprecated since 6.7 - use
     *             {@link AuthorityGroupDefaultPerspectiveResolver#loadPerspectiveChooserConfig(WidgetInstanceManager)}
     */
    @Deprecated(since = "6.7", forRemoval = true)
    protected PerspectiveChooser loadPerspectiveChooserConfig()
    {
        PerspectiveChooser config = null;
        try
        {
            config = cockpitConfigurationService.loadConfiguration(getPerspectiveChooserContext(), PerspectiveChooser.class);
        }
        catch(final CockpitConfigurationNotFoundException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug(NO_CONFIG_FOR_CURRENT_USER_MESSAGE, e);
            }
        }
        catch(final CockpitConfigurationException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.warn(CANNOT_LOAD_PERSPECTIVE_CHOOSER_MESSAGE, e);
            }
            else
            {
                LOG.warn(CANNOT_LOAD_PERSPECTIVE_CHOOSER_MESSAGE);
            }
        }
        return config;
    }


    protected PerspectiveChooser loadPerspectiveChooserConfig(final WidgetInstanceManager widgetInstanceManager)
    {
        PerspectiveChooser config = null;
        try
        {
            config = widgetInstanceManager.loadConfiguration(getPerspectiveChooserContext(), PerspectiveChooser.class);
        }
        catch(final CockpitConfigurationNotFoundException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug(NO_CONFIG_FOR_CURRENT_USER_MESSAGE, e);
            }
        }
        catch(final CockpitConfigurationException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.warn(CANNOT_LOAD_PERSPECTIVE_CHOOSER_MESSAGE, e);
            }
            else
            {
                LOG.warn(CANNOT_LOAD_PERSPECTIVE_CHOOSER_MESSAGE);
            }
        }
        return config;
    }


    protected ConfigContext getPerspectiveChooserContext()
    {
        final DefaultConfigContext context = new DefaultConfigContext(PERSPECTIVE_CHOOSER);
        final String currentUser = cockpitUserService.getCurrentUser();
        final AuthorityGroup authorityGroup = authorityGroupService.getActiveAuthorityGroupForUser(currentUser);
        if(authorityGroup == null)
        {
            context.setPrincipal(currentUser);
        }
        else
        {
            context.setPrincipal(authorityGroup.getCode());
        }
        return context;
    }


    /**
     * @deprecated since 6.7 - use
     *             {@link AuthorityGroupDefaultPerspectiveResolver#resolveDefaultPerspective(NavigationTree, WidgetInstanceManager)}
     */
    @Deprecated(since = "6.7", forRemoval = true)
    @Override
    public NavigationNode resolveDefaultPerspective(final NavigationTree navigationTree)
    {
        final PerspectiveChooser config = loadPerspectiveChooserConfig();
        return resolveDefaultPerspective(navigationTree, config);
    }


    @Override
    public NavigationNode resolveDefaultPerspective(final NavigationTree navigationTree,
                    final WidgetInstanceManager widgetInstanceManager)
    {
        final PerspectiveChooser config = loadPerspectiveChooserConfig(widgetInstanceManager);
        return resolveDefaultPerspective(navigationTree, config);
    }


    protected NavigationNode resolveDefaultPerspective(final NavigationTree navigationTree, final PerspectiveChooser configuration)
    {
        NavigationNode ret = null;
        if(configuration != null && configuration.getDefaultPerspective() != null)
        {
            String perspectiveCode = configuration.getDefaultPerspective().getName();
            if(StringUtils.isBlank(perspectiveCode))
            {
                perspectiveCode = configuration.getDefaultPerspective().getId();
            }
            if(perspectiveCode != null)
            {
                ret = findLeafByName(navigationTree, perspectiveCode);
            }
        }
        return ret;
    }


    protected NavigationNode findLeafByName(final NavigationTree tree, final String navigationNodeName)
    {
        return findLeafByNameRecursive(tree.getRootNodes(), navigationNodeName);
    }


    private NavigationNode findLeafByNameRecursive(final List<NavigationNode> nodes, final String navigationNodeName)
    {
        if(nodes.isEmpty())
        {
            return null;
        }
        for(final NavigationNode node : nodes)
        {
            final List<NavigationNode> children = node.getChildren();
            if(children.isEmpty())
            {
                if(navigationNodeName.equals(node.getName()) || navigationNodeName.equals(node.getId()))
                {
                    return node;
                }
            }
            else
            {
                final NavigationNode resultNode = findLeafByNameRecursive(children, navigationNodeName);
                if(resultNode != null)
                {
                    return resultNode;
                }
            }
        }
        return null;
    }
}
