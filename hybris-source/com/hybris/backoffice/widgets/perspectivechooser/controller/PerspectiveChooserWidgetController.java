/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.perspectivechooser.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.hybris.backoffice.actionbar.ActionDefinition;
import com.hybris.backoffice.actionbar.ActionbarListener;
import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.backoffice.navigation.NavigationTree;
import com.hybris.backoffice.navigation.bar.NavigationContext;
import com.hybris.backoffice.navigation.impl.DefaultNavigationTree;
import com.hybris.backoffice.services.PerspectiveInfoService;
import com.hybris.backoffice.widgets.perspectivechooser.perspectiveresolver.DefaultPerspectiveResolver;
import com.hybris.backoffice.widgets.perspectivechooser.renderer.PerspectiveChooserRenderer;
import com.hybris.cockpitng.annotations.GlobalCockpitEvent;
import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.core.events.CockpitEvent;
import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.util.DefaultWidgetController;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;

public class PerspectiveChooserWidgetController extends DefaultWidgetController
{
    private static final long serialVersionUID = -7945356610350446541L;
    public static final String SELECT_PERSPECTIVE_OUT = "selectPerspective";
    /**
     * @deprecated since 6.6 no longer user.
     */
    @Deprecated(since = "6.6", forRemoval = true)
    public static final String POSSIBLE_PERSPECTIVES_SOCKET = "possiblePerspectives";
    public static final String PERSPECTIVE_SELECTED_SOCKET = "perspectiveSelected";
    private static final String MODEL_VALUE_NAVIGATION_TREE = "navigationTree";
    private static final String MODEL_VALUE_SELECTED_NODE = "selectedNode";
    private static final String EVENT_NAME = "onAuthorityGroupChangeEvent";
    public static final String SOCKET_INPUT_ACTIONS = "actions";
    @Wire
    protected transient HtmlBasedComponent perspectiveMenuContainer;
    @WireVariable
    private transient Set<DefaultPerspectiveResolver> perspectiveResolvers;
    private transient PerspectiveInfoService perspectiveInfoService;
    private transient PerspectiveChooserRenderer perspectiveChooserRenderer;
    private transient NavigationContext chooserContext;


    @GlobalCockpitEvent(eventName = EVENT_NAME, scope = CockpitEvent.SESSION)
    public void reset(final CockpitEvent event)
    {
        setSelectedNode(null);
        final NavigationTree navigationTree = getNavigationTree();
        setSelectedNode(getLastSelectedPerspectiveNode(navigationTree));
        setDefaultPerspective(navigationTree);
    }


    @SocketEvent(socketId = SOCKET_INPUT_ACTIONS)
    public void setActions(final Collection<ActionDefinition> actions)
    {
        final NavigationTree navigationTree = formNavigationTree(actions);
        final NavigationTree decoratedNavigationTree = decorateNavigationTree(navigationTree);
        setNavigationTree(decoratedNavigationTree);
        setSelectedNode(getLastSelectedPerspectiveNode(decoratedNavigationTree));
        setDefaultPerspective(decoratedNavigationTree);
    }


    @SocketEvent(socketId = PERSPECTIVE_SELECTED_SOCKET)
    public void perspectiveSelected(final NavigationNode navigationNode)
    {
        final NavigationTree navigationTree = getNavigationTree();
        if(navigationNode == null)
        {
            setSelectedNode(getLastSelectedPerspectiveNode(navigationTree));
            return;
        }
        // always save id, because there is a chance that user selected a perspective which exists, but is not available
        // in navigation tree (e.g. list storing perspectives has not been synchronized yet)
        perspectiveInfoService.setSelectedId(navigationNode.getId());
        final NavigationNode selectedNode = getSelectedNode();
        if((selectedNode == null || !selectedNode.getId().equals(navigationNode.getId()))
                        && contains(navigationTree, navigationNode))
        {
            setSelectedNode(navigationNode);
        }
        getPerspectiveChooserRenderer().updatePerspectiveSelection(getPerspectivesToolbar(), getChooserContext());
    }


    private void setDefaultPerspective(final NavigationTree navigationTree)
    {
        if(getSelectedNode() == null)
        {
            final Optional<NavigationNode> defaultNode = Optional.ofNullable(navigationTree)
                            .map(tree -> perspectiveResolvers.stream()
                                            .map(resolver -> resolver.resolveDefaultPerspective(tree, getWidgetInstanceManager()))
                                            .filter(Objects::nonNull).findFirst().orElse(null));
            if(!defaultNode.isPresent())
            {
                updateView();
            }
            else
            {
                setSelectedNode(defaultNode.get());
                updateView();
                sendOutput(SELECT_PERSPECTIVE_OUT, defaultNode.get());
            }
        }
        else
        {
            updateView();
            sendOutput(SELECT_PERSPECTIVE_OUT, getSelectedNode());
        }
        getPerspectiveChooserRenderer().updatePerspectiveSelection(getPerspectivesToolbar(), getChooserContext());
    }


    private void updateView()
    {
        getPerspectivesToolbar().getChildren().clear();
        final NavigationTree tree = getNavigationTree();
        if(tree != null)
        {
            getPerspectiveChooserRenderer().renderTree(getPerspectivesToolbar(), getChooserContext());
        }
    }


    protected NavigationContext getChooserContext()
    {
        if(chooserContext == null)
        {
            chooserContext = new ChooserContext();
        }
        return chooserContext;
    }


    protected void doSelectNode(final NavigationNode node)
    {
        setSelectedNode(node);
        sendOutput(SELECT_PERSPECTIVE_OUT, node);
    }


    protected boolean isSelected(final NavigationNode node)
    {
        Validate.notNull("Node may not be null", node);
        final NavigationNode selectedNode = getSelectedNode();
        return node.equals(selectedNode);
    }


    protected boolean isSelectionParent(final NavigationNode node)
    {
        Validate.notNull("Node may not be null", node);
        NavigationNode selectedNode = getSelectedNode();
        if(selectedNode != null)
        {
            while(selectedNode.getParent() != null && !Objects.equals(node, selectedNode))
            {
                selectedNode = selectedNode.getParent();
            }
            return Objects.equals(node, selectedNode);
        }
        else
        {
            return false;
        }
    }


    private static boolean contains(final NavigationTree navigationTree, final NavigationNode navigationNode)
    {
        if(navigationTree == null)
        {
            return false;
        }
        final List<NavigationNode> nodes = navigationTree.getRootNodes();
        return containsRecursive(nodes, navigationNode);
    }


    private static boolean containsRecursive(final List<NavigationNode> nodes, final NavigationNode navigationNode)
    {
        Validate.notNull("All arguments must be not-null", nodes, navigationNode);
        for(final NavigationNode n : nodes)
        {
            if(navigationNode.equals(n))
            {
                return true;
            }
        }
        for(final NavigationNode n : nodes)
        {
            final List<NavigationNode> children = n.getChildren();
            if(!children.isEmpty() && containsRecursive(n.getChildren(), navigationNode))
            {
                return true;
            }
        }
        return false;
    }


    private NavigationNode getSelectedNode()
    {
        return getValue(MODEL_VALUE_SELECTED_NODE, NavigationNode.class);
    }


    void setSelectedNode(final NavigationNode node)
    {
        if(getSelectedNode() != node)
        {
            final String id = node != null ? node.getId() : null;
            perspectiveInfoService.setSelectedId(id);
            setValue(MODEL_VALUE_SELECTED_NODE, node);
        }
    }


    NavigationNode getLastSelectedPerspectiveNode(final NavigationTree tree)
    {
        if(tree == null || !perspectiveInfoService.hasSelectedId())
        {
            return null;
        }
        final String id = perspectiveInfoService.getSelectedId();
        final Optional<NavigationNode> node = tree.getRootNodes().stream().filter(x -> id.equals(x.getId())).findAny();
        return node.orElse(null);
    }


    private NavigationTree getNavigationTree()
    {
        return getValue(MODEL_VALUE_NAVIGATION_TREE, NavigationTree.class);
    }


    void setNavigationTree(final NavigationTree navigationTree)
    {
        setValue(MODEL_VALUE_NAVIGATION_TREE, navigationTree);
    }


    protected void mergeNodes(final NavigationNode destination, final NavigationNode addon)
    {
        addon.getChildren().forEach(child -> {
            final int index = destination.getChildren().indexOf(child);
            if(index > -1)
            {
                mergeNodes(destination.getChildren().get(index), child);
            }
            else
            {
                destination.addChild(child);
            }
        });
    }


    protected NavigationTree formNavigationTree(final Collection<ActionDefinition> actions)
    {
        final DefaultNavigationTree result = new DefaultNavigationTree();
        result.setRootNodes(Lists.newArrayList());
        perspectiveResolvers
                        .forEach(resolver -> resolver.formTree(actions, getWidgetInstanceManager()).getRootNodes().forEach(node -> {
                            final int index = result.getRootNodes().indexOf(node);
                            if(index > -1)
                            {
                                mergeNodes(result.getRootNodes().get(index), node);
                            }
                            else
                            {
                                result.getRootNodes().add(node);
                            }
                        }));
        return result;
    }


    protected NavigationTree decorateNavigationTree(final NavigationTree navigationTree)
    {
        return () -> {
            final List<NavigationNode> ret = Lists.newArrayList();
            if(navigationTree != null && CollectionUtils.isNotEmpty(navigationTree.getRootNodes()))
            {
                getPermittedPerspectives(navigationTree).forEach(node -> {
                    final int index = ret.indexOf(node);
                    if(index > -1)
                    {
                        mergeNodes(ret.get(index), node);
                    }
                    else
                    {
                        ret.add(node);
                    }
                });
            }
            return ret;
        };
    }


    /**
     * @deprecated since 6.5, allowed perspectives configuration has been moved to View Switcher widget
     * @see com.hybris.backoffice.widgets.viewswitcher.ViewSwitcherWidgetController#filterPossibleWidgets
     */
    @Deprecated(since = "6.5", forRemoval = true)
    protected List<NavigationNode> getPermittedPerspectives(final NavigationTree navigationTree)
    {
        final LinkedHashSet<NavigationNode> allowedPerspectives = Sets.newLinkedHashSet();
        for(final DefaultPerspectiveResolver resolver : perspectiveResolvers)
        {
            final List<NavigationNode> perspectives = resolver.getPermittedPerspectives(navigationTree);
            if(CollectionUtils.isNotEmpty(perspectives))
            {
                allowedPerspectives.addAll(perspectives);
                break;
            }
        }
        return Lists.newArrayList(allowedPerspectives);
    }


    public HtmlBasedComponent getPerspectivesToolbar()
    {
        return perspectiveMenuContainer;
    }


    public PerspectiveInfoService getPerspectiveInfoService()
    {
        return perspectiveInfoService;
    }


    protected PerspectiveChooserRenderer getPerspectiveChooserRenderer()
    {
        return perspectiveChooserRenderer;
    }


    protected class ChooserContext implements NavigationContext
    {
        @Override
        public ActionbarListener getActionListener()
        {
            return node -> doSelectNode((NavigationNode)node);
        }


        @Override
        public WidgetInstanceManager getWidgetInstanceManager()
        {
            return PerspectiveChooserWidgetController.this.getWidgetInstanceManager();
        }


        @Override
        public NavigationTree getActionTree()
        {
            return PerspectiveChooserWidgetController.this.getNavigationTree();
        }


        @Override
        public boolean isSelected(final NavigationNode node)
        {
            return PerspectiveChooserWidgetController.this.isSelected(node);
        }


        @Override
        public boolean isSelectionParent(final NavigationNode node)
        {
            return PerspectiveChooserWidgetController.this.isSelectionParent(node);
        }
    }
}
