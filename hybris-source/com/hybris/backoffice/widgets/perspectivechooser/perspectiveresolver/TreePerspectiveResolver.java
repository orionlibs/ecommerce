/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.perspectivechooser.perspectiveresolver;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hybris.backoffice.actionbar.ActionDefinition;
import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.backoffice.navigation.NavigationTree;
import com.hybris.backoffice.navigation.impl.DefaultNavigationTree;
import com.hybris.backoffice.navigation.impl.SimpleNode;
import com.hybris.cockpitng.config.perspectivechooser.jaxb.Format;
import com.hybris.cockpitng.config.perspectivechooser.jaxb.PerspectiveChooser;
import com.hybris.cockpitng.core.config.CockpitConfigurationException;
import com.hybris.cockpitng.core.config.CockpitConfigurationService;
import com.hybris.cockpitng.core.config.impl.DefaultConfigContext;
import com.hybris.cockpitng.core.persistence.packaging.CockpitResourceLoader;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TreePerspectiveResolver extends SequenceDefaultPerspectiveResolver
{
    private static final Logger LOG = LoggerFactory.getLogger(AuthorityGroupDefaultPerspectiveResolver.class);
    private static final Pattern PATTERN_PATH_ELEMENTS = Pattern.compile("([^/]|(//))+");
    private static final String PERSPECTIVE_CHOOSER = "perspective-chooser";
    private static final String CANNOT_LOAD_PERSPECTIVE_CHOOSER_MESSAGE = "Cannot retrieve perspective chooser widget configuration";
    /**
     * @deprecated since 6.7 - not used anymore, configuration is provided by WidgetInstanceManager
     */
    @Deprecated(since = "6.7", forRemoval = true)
    private CockpitConfigurationService cockpitConfigurationService;
    private final CockpitResourceLoader resourceLoader;


    public TreePerspectiveResolver(final CockpitResourceLoader resourceLoader)
    {
        this.resourceLoader = resourceLoader;
    }


    /**
     * @deprecated since 6.7 - not used anymore
     */
    @Deprecated(since = "6.7", forRemoval = true)
    public TreePerspectiveResolver(final CockpitConfigurationService cockpitConfigurationService,
                    final CockpitResourceLoader resourceLoader)
    {
        this.cockpitConfigurationService = cockpitConfigurationService;
        this.resourceLoader = resourceLoader;
    }


    protected NavigationNode formAction(final ActionDefinition action, final Map<String, Format> formats)
    {
        final SimpleNode node = SimpleNode.create(action);
        formatNode(node, formats);
        return node;
    }


    protected void formatNode(final SimpleNode node, final Map<String, Format> formats)
    {
        if(formats.containsKey(node.getId()))
        {
            final Format format = formats.get(node.getId());
            if(StringUtils.isNotBlank(format.getTitle()))
            {
                node.setName(format.getTitle());
                node.setNameLocKey(null);
            }
            if(StringUtils.isNotBlank(format.getTitleKey()))
            {
                node.setNameLocKey(format.getTitleKey());
            }
            if(StringUtils.isNotBlank(format.getDescription()))
            {
                node.setDescription(format.getDescription());
                node.setDescriptionLocKey(null);
            }
            if(StringUtils.isNotBlank(format.getDescriptionKey()))
            {
                node.setDescriptionLocKey(format.getDescriptionKey());
            }
            if(StringUtils.isNotBlank(format.getIcon()))
            {
                node.setIconUri(format.getIcon());
                node.setIconUriLocKey(null);
            }
            if(StringUtils.isNotBlank(format.getIconKey()))
            {
                node.setIconUriLocKey(format.getIconKey());
            }
        }
        if(StringUtils.isBlank(node.getName()))
        {
            node.setName(node.getId());
        }
        if(StringUtils.isBlank(node.getIconUri()) && StringUtils.isBlank(node.getIconUriLocKey()))
        {
            final String imgUrl = "/cng/images/" + node.getId() + ".png";
            if(resourceLoader.hasResource(imgUrl))
            {
                node.setIconUri(imgUrl);
            }
        }
    }


    /**
     * @deprecated since 6.7 - use {@link TreePerspectiveResolver#formTree(Collection, WidgetInstanceManager)}
     */
    @Deprecated(since = "6.7", forRemoval = true)
    @Override
    public NavigationTree formTree(final Collection<? extends ActionDefinition> actions)
    {
        final PerspectiveChooser configuration;
        final Map<String, Format> formats = Maps.newHashMap();
        final DefaultConfigContext context = new DefaultConfigContext(PERSPECTIVE_CHOOSER);
        try
        {
            configuration = cockpitConfigurationService.loadConfiguration(context, PerspectiveChooser.class);
            formats.putAll(configuration.getFormat().stream().collect(Collectors.toMap(Format::getId, format -> format)));
        }
        catch(final CockpitConfigurationException e)
        {
            LOG.error(CANNOT_LOAD_PERSPECTIVE_CHOOSER_MESSAGE, e);
        }
        return buildTree(actions, formats);
    }


    @Override
    public NavigationTree formTree(final Collection<? extends ActionDefinition> actions,
                    final WidgetInstanceManager widgetInstanceManager)
    {
        final Map<String, Format> formats = Maps.newHashMap();
        loadConfiguration(widgetInstanceManager).ifPresent(configuration -> formats
                        .putAll(configuration.getFormat().stream().collect(Collectors.toMap(Format::getId, format -> format))));
        return buildTree(actions, formats);
    }


    protected Optional<PerspectiveChooser> loadConfiguration(final WidgetInstanceManager widgetInstanceManager)
    {
        PerspectiveChooser configuration = null;
        final DefaultConfigContext context = new DefaultConfigContext(PERSPECTIVE_CHOOSER);
        try
        {
            configuration = widgetInstanceManager.loadConfiguration(context, PerspectiveChooser.class);
        }
        catch(final CockpitConfigurationException e)
        {
            LOG.debug(CANNOT_LOAD_PERSPECTIVE_CHOOSER_MESSAGE, e);
        }
        return Optional.ofNullable(configuration);
    }


    protected NavigationTree buildTree(final Collection<? extends ActionDefinition> actions, final Map<String, Format> formats)
    {
        final DefaultNavigationTree tree = new DefaultNavigationTree();
        tree.setRootNodes(Lists.newArrayList());
        final Map<String, NavigationNode> nodes = new HashMap<>();
        actions.forEach(action -> {
            final NavigationNode node = formAction(action, formats);
            nodes.put(action.getId(), node);
            if(action.getGroup() == null)
            {
                tree.getRootNodes().add(node);
            }
            else
            {
                try
                {
                    formTreeNode(tree, node, formats, nodes);
                }
                catch(final CockpitConfigurationException ex)
                {
                    LOG.error(ex.getLocalizedMessage(), ex);
                }
            }
        });
        return tree;
    }


    protected void formTreeNode(final NavigationTree tree, final NavigationNode node, final Map<String, Format> formats,
                    final Map<String, NavigationNode> nodes) throws CockpitConfigurationException
    {
        NavigationNode currentNode = null;
        final Matcher pathMatcher = PATTERN_PATH_ELEMENTS.matcher(node.getGroup());
        while(pathMatcher.find())
        {
            final String id = pathMatcher.group();
            if(!nodes.containsKey(id))
            {
                final SimpleNode simpleNode = new SimpleNode(id);
                formatNode(simpleNode, formats);
                simpleNode.setDirectory(true);
                nodes.put(id, simpleNode);
                if(currentNode != null)
                {
                    currentNode.addChild(simpleNode);
                }
                else
                {
                    tree.getRootNodes().add(simpleNode);
                }
                currentNode = simpleNode;
            }
            else
            {
                currentNode = nodes.get(id);
            }
            if(!currentNode.isDirectory())
            {
                throw new CockpitConfigurationException("Unable to add perspective into other perspective: " + node.getId());
            }
        }
        if(currentNode != null)
        {
            currentNode.addChild(node);
        }
        else
        {
            tree.getRootNodes().add(node);
        }
    }
}
