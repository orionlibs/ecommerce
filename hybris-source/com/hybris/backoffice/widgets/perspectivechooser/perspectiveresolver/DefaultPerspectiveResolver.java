/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.perspectivechooser.perspectiveresolver;

import com.hybris.backoffice.actionbar.ActionDefinition;
import com.hybris.backoffice.actionbar.ActionsResolver;
import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.backoffice.navigation.NavigationTree;
import com.hybris.backoffice.navigation.impl.DefaultNavigationTree;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;

/**
 * Interface to be implemented by objects that can resolve default perspective from {@link NavigationTree}.
 */
public interface DefaultPerspectiveResolver extends ActionsResolver
{
    /**
     * Resolve default perspective.
     * <p>
     * To allow chaining, a DefaultPerspectiveResolver implementation should return {@code null} when a default perspective
     * cannot be found.
     * </p>
     *
     * @param navigationTree
     *           navigation tree containing perspective, from which a default one should be picked
     * @return the NavigationNode, or {@code null} if not found
     * @see AuthorityGroupDefaultPerspectiveResolver
     * @see SequenceDefaultPerspectiveResolver
     * @deprecated since 6.7 - use
     *             {@link DefaultPerspectiveResolver#resolveDefaultPerspective(NavigationTree, WidgetInstanceManager)}
     */
    @Deprecated(since = "6.7", forRemoval = true)
    NavigationNode resolveDefaultPerspective(final NavigationTree navigationTree);


    /**
     * Resolve default perspective.
     * <p>
     * To allow chaining, a DefaultPerspectiveResolver implementation should return {@code null} when a default perspective
     * cannot be found.
     * </p>
     *
     * @param navigationTree
     *           navigation tree containing perspective, from which a default one should be picked
     * @param widgetInstanceManager
     *           widget instance manager
     * @return the NavigationNode, or {@code null} if not found
     * @see AuthorityGroupDefaultPerspectiveResolver
     * @see SequenceDefaultPerspectiveResolver
     */
    default NavigationNode resolveDefaultPerspective(final NavigationTree navigationTree,
                    final WidgetInstanceManager widgetInstanceManager)
    {
        return resolveDefaultPerspective(navigationTree);
    }


    /**
     * Returns all permitted perspectives.
     * <p>
     * To allow chaining, a DefaultPerspectiveResolver implementation should return {@code null} or empty list when a
     * permitted perspectives cannot be found.
     * </p>
     *
     * @param navigationTree
     *           full navigation tree containing perspective, from which a permitted one should be picked
     * @return the list of permitted NavigationNode, or empty list if not found
     * @see AuthorityGroupDefaultPerspectiveResolver
     * @see com.hybris.backoffice.widgets.viewswitcher.ViewSwitcherWidgetController#filterPossibleWidgets(List)
     * @deprecated since 6.5, allowed perspectives configuration has been moved to View Switcher widget
     */
    @Deprecated(since = "6.5", forRemoval = true)
    default List<NavigationNode> getPermittedPerspectives(final NavigationTree navigationTree)
    {
        return navigationTree != null && CollectionUtils.isNotEmpty(navigationTree.getRootNodes()) ? navigationTree.getRootNodes()
                        : Collections.emptyList();
    }


    /**
     * Goes through all provided actions and forms then in the shape of navigation action tree.
     *
     * @param actions
     *           actions to be formed
     * @return navigation tree
     * @deprecated since 6.7 - use {@link DefaultPerspectiveResolver#formTree(Collection, WidgetInstanceManager)}
     */
    @Deprecated(since = "6.7", forRemoval = true)
    @Override
    default NavigationTree formTree(final Collection<? extends ActionDefinition> actions)
    {
        return new DefaultNavigationTree();
    }


    /**
     * Goes through all provided actions and forms then in the shape of navigation action tree.
     *
     * @param actions
     *           actions to be formed
     * @param widgetInstanceManager
     *           widget instance manager
     * @return navigation tree
     */
    @Override
    default NavigationTree formTree(final Collection<? extends ActionDefinition> actions,
                    final WidgetInstanceManager widgetInstanceManager)
    {
        return formTree(actions);
    }
}
