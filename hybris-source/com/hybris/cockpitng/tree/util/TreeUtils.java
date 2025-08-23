/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.tree.util;

import com.google.common.base.Stopwatch;
import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.backoffice.navigation.NavigationNodeDecoratorUtils;
import com.hybris.backoffice.navigation.impl.SimpleNode;
import com.hybris.cockpitng.tree.node.DynamicNode;
import com.hybris.cockpitng.widgets.common.explorertree.model.RefreshableTreeModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zul.AbstractTreeModel;
import org.zkoss.zul.TreeModel;
import org.zkoss.zul.event.TreeDataEvent;
import org.zkoss.zul.ext.Openable;
import org.zkoss.zul.ext.Selectable;
import org.zkoss.zul.ext.SelectionControl;
import org.zkoss.zul.ext.TreeOpenableModel;
import org.zkoss.zul.ext.TreeSelectableModel;

/**
 * Provides useful functionalities for zk trees, especially for searching within treenodes.
 */
public final class TreeUtils
{
    private static final Map<String, String[]> CAMEL_CASE_MATCHES_CACHE = new ConcurrentHashMap<>();
    private static final Logger LOG = LoggerFactory.getLogger(TreeUtils.class);


    private TreeUtils()
    {
        // Utility Class
    }


    /**
     * Camelcase string matching.
     *
     * @param value
     *           the string that should be checked.
     * @param searchString
     *           the "camel-cased" search string.
     * @return true, if the search string is a camelcase representation of the value.
     */
    public static boolean matchCamelCase(final String value, final String searchString)
    {
        boolean ret = false;
        final String[] valueTokens = value.split("(?=\\p{Lu})");
        final String[] searchTokens = CAMEL_CASE_MATCHES_CACHE.computeIfAbsent(searchString, s -> s.split("(?=\\p{Lu})"));
        if(searchTokens.length <= valueTokens.length && searchTokens.length > 0)
        {
            boolean matchCC = false;
            for(final String searchToken : searchTokens)
            {
                for(final String valueToken : valueTokens)
                {
                    matchCC |= matchFilterStringSimple(valueToken, searchToken);
                }
            }
            ret = matchCC;
        }
        return ret;
    }


    /**
     * The call is equal to calling {@link #matchFilterString(Object, String, boolean, MatchMode)} with match mode set to
     * {@link MatchMode#CONTAINS}.
     */
    public static boolean matchFilterString(final Object data, final String filterString, final boolean camelCase)
    {
        return matchFilterString(data, filterString, camelCase, MatchMode.CONTAINS);
    }


    /**
     * Checks, if argument data can be represented by argument filterString, i.e. it first checks, if data is a
     * {@link String}, then invokes {@link TreeUtils#matchFilterString(Object, String, boolean)} and optionally invokes
     * {@link TreeUtils#matchCamelCase(String, String)}, depending on argument camelCase.
     */
    public static boolean matchFilterString(final Object data, final String filterString, final boolean camelCase,
                    final MatchMode mode)
    {
        boolean match = false;
        if(data instanceof String)
        {
            match = matchFilterStringSimple((String)data, filterString, mode);
            if(!match && camelCase)
            {
                match = matchCamelCase((String)data, filterString);
            }
        }
        return match;
    }


    /**
     * The call is equal to calling {@link #matchFilterStringSimple(String, String, MatchMode)} with match mode set to
     * {@link MatchMode#CONTAINS}.
     */
    public static boolean matchFilterStringSimple(final String value, final String filterString)
    {
        return matchFilterStringSimple(value, filterString, MatchMode.CONTAINS);
    }


    /**
     * Checks, if argument value starts (case-insensitive) with argument filterString.
     */
    public static boolean matchFilterStringSimple(final String value, final String filterString, final MatchMode mode)
    {
        switch(mode)
        {
            case CONTAINS:
                return StringUtils.containsIgnoreCase(value, filterString);
            case STARTS_WITH:
                return StringUtils.startsWithIgnoreCase(value, filterString);
            default:
                return StringUtils.containsIgnoreCase(value, filterString);
        }
    }


    /**
     * Checks whether specified node is eligible for {@link FilteredTreeModel} to filter. Method does not check filtering
     * itself, yet whether filter can even be applied on node.
     *
     * @param node
     *           node to be checked
     * @return <code>true</code> if node can potentially be tested against filter query
     */
    public static boolean isEligibleForMatching(final NavigationNode node)
    {
        if(node instanceof DynamicNode)
        {
            final int indexingDepth = ((DynamicNode)node).getIndexingDepth();
            return indexingDepth < 0 || indexingDepth >= getDynamicNodeDepth((DynamicNode)node);
        }
        return true;
    }


    protected static int getDynamicNodeDepth(final DynamicNode node)
    {
        if(node.getParent() instanceof DynamicNode)
        {
            return getDynamicNodeDepth((DynamicNode)node.getParent()) + 1;
        }
        else
        {
            return 0;
        }
    }


    public enum MatchMode
    {
        STARTS_WITH, CONTAINS
    }


    /**
     * Determines whether filtering should be done up until first match or without any limitations
     */
    public enum SearchDepth
    {
        /**
         * Filtering up until first match - children of first matching node are not filtered at all
         */
        SHALLOW,
        /**
         * Nodes are filtered on all levels
         */
        DEEP
    }


    /**
     * Used by {@link FilteredTreeModel} to determine the string representation of a treenode that should be used for
     * filtering.
     */
    public interface FilterStringResolver<E>
    {
        String getStringRepresentation(E node);
    }


    /**
     * Subclass of {@link AbstractTreeModel} that can be used as a filter.
     */
    public static class FilteredTreeModel<E> extends AbstractTreeModel<E> implements TreeModelDecorator, RefreshableTreeModel<E>
    {
        private static final int TRAVERSED_ITEMS_UNLIMITED = -1;
        private final transient TreeModel<E> originalModel;
        private final String filterString;
        private final boolean camelCase;
        private final transient FilterStringResolver<E> filterStringResolver;
        private final transient Map<E, Boolean> matchingBranch = new HashMap<>();
        private final MatchMode matchMode;
        private final int traversedItemsLimit;
        private final transient Map<E, List<E>> childrenMap;
        private final boolean navigationTreePerformanceOptimizationsActivated;
        private final transient NavigationTreeModelGetPathOptimizer pathOptimizer;
        private SearchDepth searchDepth = SearchDepth.DEEP;


        /**
         * Constructor that creates a filtered version of an {@link AbstractTreeModel}.
         *
         * @param originalModel
         *           the treemodel that should be filtered.
         * @param filterString
         *           the string that must match the treeitems.
         * @param camelCase
         *           whether camelcase matching should be used or not.
         * @param filterStringResolver
         *           needed, if treenodes not of type {@link String}.
         */
        public FilteredTreeModel(final TreeModel<E> originalModel, final String filterString, final boolean camelCase,
                        final FilterStringResolver<E> filterStringResolver)
        {
            this(originalModel, filterString, camelCase, filterStringResolver, MatchMode.CONTAINS, TRAVERSED_ITEMS_UNLIMITED);
        }


        /**
         * Constructor that creates a filtered version of an {@link AbstractTreeModel}.
         *
         * @param originalModel
         *           the treemodel that should be filtered.
         * @param filterString
         *           the string that must match the treeitems.
         * @param camelCase
         *           whether camelcase matching should be used or not.
         * @param filterStringResolver
         *           needed, if treenodes not of type {@link String}.
         * @param matchMode
         *           type of match used during search.
         */
        public FilteredTreeModel(final TreeModel<E> originalModel, final String filterString, final boolean camelCase,
                        final FilterStringResolver<E> filterStringResolver, final MatchMode matchMode)
        {
            this(originalModel, filterString, camelCase, filterStringResolver, matchMode, TRAVERSED_ITEMS_UNLIMITED);
        }


        /**
         * Constructor that creates a filtered version of an {@link AbstractTreeModel}.
         *
         * @param originalModel
         *           the treemodel that should be filtered.
         * @param filterString
         *           the string that must match the treeitems.
         * @param camelCase
         *           whether camelcase matching should be used or not.
         * @param filterStringResolver
         *           needed, if treenodes not of type {@link String}.
         * @param traversedItemsLimit
         *           the maximum number of items which can be traversed during filtering. A negative value means that all items
         *           can be visited.
         */
        public FilteredTreeModel(final TreeModel<E> originalModel, final String filterString, final boolean camelCase,
                        final FilterStringResolver<E> filterStringResolver, final int traversedItemsLimit)
        {
            this(originalModel, filterString, camelCase, filterStringResolver, MatchMode.CONTAINS, traversedItemsLimit);
        }


        /**
         * Constructor that creates a filtered version of an {@link AbstractTreeModel}.
         *
         * @param originalModel
         *           the treemodel that should be filtered.
         * @param filterString
         *           the string that must match the treeitems.
         * @param camelCase
         *           whether camelcase matching should be used or not.
         * @param filterStringResolver
         *           needed, if treenodes not of type {@link String}.
         * @param matchMode
         *           type of match used during search.
         * @param traversedItemsLimit
         *           the maximum number of items which can be traversed during filtering. A negative value means that all items
         *           can be visited.
         */
        public FilteredTreeModel(final TreeModel<E> originalModel, final String filterString, final boolean camelCase,
                        final FilterStringResolver<E> filterStringResolver, final MatchMode matchMode, final int traversedItemsLimit)
        {
            super(originalModel.getRoot());
            this.originalModel = originalModel;
            this.filterString = filterString;
            this.camelCase = camelCase;
            this.filterStringResolver = filterStringResolver;
            this.matchMode = matchMode;
            this.traversedItemsLimit = traversedItemsLimit;
            // if all nodes are instances of the NavigationNode then we can use some performance optimizations during traversing the tree
            // if the root node is an instance of the NavigationNode then we are sure that all nodes in tree are navigation nodes
            navigationTreePerformanceOptimizationsActivated = originalModel.getRoot() instanceof NavigationNode;
            if(navigationTreePerformanceOptimizationsActivated)
            {
                childrenMap = new NavigationNodeDecoratorsSupportedHashMap();
                pathOptimizer = new NavigationTreeModelGetPathOptimizer(node -> (List<NavigationNode>)getChildren((E)node));
            }
            else
            {
                childrenMap = new HashMap<>();
                pathOptimizer = null;
            }
        }


        /**
         * Checks whether tree is filtered up until first match or independent of node depth
         *
         * @return search depth of tree model
         */
        public SearchDepth getSearchDepth()
        {
            return searchDepth;
        }


        /**
         * Determines whether tree is filtered up until first match or independent of node depth
         *
         * @param searchDepth
         *           new search depth for this tree
         */
        public void setSearchDepth(final SearchDepth searchDepth)
        {
            this.searchDepth = searchDepth;
            clearChildrenMap(getRoot());
            fireEvent(TreeDataEvent.STRUCTURE_CHANGED, null, -1, -1);
        }


        /**
         * Rebuilds this filtered tree (executing filtering again). It also empties open and selection paths.
         *
         * @throws ExceededTraversedItemsLimitException
         *            if a number of the traversed items exceeded the limit.
         * @deprecated since 1808, create a new instance of {@link FilteredTreeModel} instead
         */
        @Deprecated(since = "1808", forRemoval = true)
        public void rebuild()
        {
            childrenMap.clear();
            _opens.clear();
            _selection.clear();
            filterTree();
        }


        private void filterTree()
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug(String.format("Filtering \"%s\"...", filterString));
            }
            final Stopwatch stopwatch = Stopwatch.createStarted();
            filter(originalModel.getRoot());
            stopwatch.stop();
            if(LOG.isDebugEnabled())
            {
                LOG.debug(String.format("Filtering \"%s\" took %d ms.", filterString, stopwatch.elapsed(TimeUnit.MILLISECONDS)));
            }
            fireEvent(TreeDataEvent.STRUCTURE_CHANGED, null, -1, -1);
        }


        protected boolean filter(final E node)
        {
            return matchesFilter(node) || (isEligibleForMatching(node) && !getChildren(node).isEmpty());
        }


        protected boolean isTraverseLimitExceeded()
        {
            return traversedItemsLimit > TRAVERSED_ITEMS_UNLIMITED && matchingBranch.size() >= traversedItemsLimit;
        }


        /**
         * Checks whether particular node matches filter.
         *
         * @param node
         *           node to be checked
         * @return <code>true</code> if node data matches filter
         */
        public boolean matchesFilter(final E node)
        {
            if(node == null)
            {
                return false;
            }
            if(!isEligibleForMatching(node))
            {
                return false;
            }
            return matchesFilterString(node);
        }


        protected boolean isEligibleForMatching(final E node)
        {
            return !(node instanceof NavigationNode) || TreeUtils.isEligibleForMatching((NavigationNode)node);
        }


        /**
         * @deprecated since 2005
         */
        @Deprecated(since = "2005", forRemoval = true)
        protected int getDynamicNodeDepth(final DynamicNode node)
        {
            return TreeUtils.getDynamicNodeDepth(node);
        }


        protected boolean matchesFilterString(final E node)
        {
            final Object data = filterStringResolver == null ? node : filterStringResolver.getStringRepresentation(node);
            return matchFilterString(data, filterString, camelCase, matchMode);
        }


        @Override
        public TreeModel<E> getOriginalModel()
        {
            return originalModel;
        }


        protected <M extends TreeSelectableModel & Selectable<E>> Optional<M> getOriginalSelectableModel()
        {
            return Optional.of(getOriginalModel()).filter(TreeSelectableModel.class::isInstance).filter(Selectable.class::isInstance)
                            .map(model -> (M)model);
        }


        protected <M extends TreeOpenableModel & Openable<E>> Optional<M> getOriginalOpenableModel()
        {
            return Optional.of(getOriginalModel()).filter(TreeOpenableModel.class::isInstance).filter(Openable.class::isInstance)
                            .map(model -> (M)model);
        }


        protected <M extends RefreshableTreeModel<E>> Optional<M> getOriginalRefreshableModel()
        {
            return Optional.of(getOriginalModel()).filter(RefreshableTreeModel.class::isInstance).map(model -> (M)model);
        }


        /**
         * @deprecated since 2005, do not use
         */
        @Deprecated(since = "2005", forRemoval = true)
        public List<E> getMatchingNodes()
        {
            throw new UnsupportedOperationException();
        }


        @Override
        public E getChild(final E parent, final int index)
        {
            return getChildren(parent).get(index);
        }


        @Override
        public int getChildCount(final E parent)
        {
            return getChildren(parent).size();
        }


        @Override
        public boolean isLeaf(final E node)
        {
            return getChildCount(node) == 0;
        }


        /**
         * Gets all nodes that matches the query that has already been resolved.
         * <P>
         * Please bear in mind that this method does not return <strong>all</strong> matching nodes, yet only a subset of those
         * that were already processed.
         * </P>
         *
         * @return collection of nodes that were processed and matches filter query
         */
        public Collection<E> getResolvedMatchingNodes()
        {
            if(childrenMap.isEmpty())
            {
                filterTree();
            }
            return Collections.unmodifiableCollection(new ArrayList<>(childrenMap.keySet()));
        }


        /**
         * Gets filtered children of specified node.
         *
         * @param parent
         *           parent node
         * @return list of children matching current filter query
         */
        public List<E> getChildren(final E parent)
        {
            if(!childrenMap.containsKey(parent))
            {
                final List<E> children = filterChildren(parent);
                if(children.isEmpty())
                {
                    childrenMap.remove(parent);
                }
                else
                {
                    childrenMap.put(parent, children);
                    expandChildrenMapToContainExpandedByDefaultNodes(children);
                }
                return children;
            }
            else
            {
                return childrenMap.get(parent);
            }
        }


        protected void expandChildrenMapToContainExpandedByDefaultNodes(final List<E> elements)
        {
            final Deque<E> toCheck = new LinkedList<>();
            toCheck.addAll(elements);
            while(!toCheck.isEmpty())
            {
                final E parent = toCheck.pop();
                if(parent instanceof SimpleNode && ((SimpleNode)parent).isExpandedByDefault() && !childrenMap.containsKey(parent))
                {
                    final List<E> children = (List<E>)((SimpleNode)parent).getChildren();
                    expandNodeAndAllAncestors(parent);
                    childrenMap.put(parent, children);
                    toCheck.addAll(children);
                }
            }
        }


        /**
         * If the node matches the search criteria then its ancestors should be expanded. Notice that
         * {@link #addOpenObject(Object)} checks whether given node matches search criteria, what is not true for ancestors of
         * matched node. A particular ancestor does not need to match search criteria, therefore addOpenObject from super-class
         * is called here. The algorithm expands parents one by one. If a given parent is already expanded then the algorithm
         * stops the work to prevent opening the same path many times.
         */
        protected void expandNodeAndAllAncestors(E node)
        {
            if(node instanceof NavigationNode)
            {
                NavigationNode parent = ((NavigationNode)node).getParent();
                while(parent != null && !isObjectOpened(parent))
                {
                    super.addOpenObject((E)parent);
                    parent = parent.getParent();
                }
            }
        }


        protected List<E> filterChildren(final E node)
        {
            if(node instanceof DynamicNode && ((DynamicNode)node).isSearchable())
            {
                return (List<E>)((DynamicNode)node).getChildren(filterString, camelCase, matchMode);
            }
            return filterChildrenByTraversing(node);
        }


        protected List<E> filterChildrenByTraversing(final E node)
        {
            Boolean shallowSearchFulfilled = false;
            try
            {
                final int[] path = getOriginalModel().getPath(node);
                if(node == getRoot() || (path != null && path.length > 0))
                {
                    if(node != getRoot())
                    {
                        final int[] parentPath = ArrayUtils.remove(path, path.length - 1);
                        final E parent = getOriginalModel().getChild(parentPath);
                        shallowSearchFulfilled = parent != null && matchingBranch.containsKey(parent)
                                        && (matchingBranch.get(parent) || matchesFilter(node));
                    }
                    final boolean filterRequired = !isTraverseLimitExceeded()
                                    && (SearchDepth.DEEP.equals(getSearchDepth()) || !shallowSearchFulfilled);
                    return IntStream.range(0, getOriginalModel().getChildCount(node))
                                    .mapToObj(index -> getOriginalModel().getChild(node, index))
                                    .filter(child -> !filterRequired || filter(child)).collect(Collectors.toList());
                }
                else
                {
                    return Collections.emptyList();
                }
            }
            finally
            {
                matchingBranch.put(node, shallowSearchFulfilled);
            }
        }


        @Override
        public int[] getPath(final E child)
        {
            if(navigationTreePerformanceOptimizationsActivated)
            {
                return pathOptimizer.getPath((NavigationNode)child);
            }
            return super.getPath(child);
        }


        @Override
        public void refreshChildren(final E node, final List<E> children)
        {
            final Optional<? extends Openable<E>> openableModel = getOriginalOpenableModel();
            final List<E> openObjects = openableModel.map(Openable::getOpenObjects).<List<E>>map(ArrayList::new)
                            .orElse(Collections.emptyList());
            openableModel.ifPresent(Openable::clearOpen);
            final Optional<? extends Selectable<E>> selectableModel = getOriginalSelectableModel();
            final List<E> selection = selectableModel.map(Selectable::getSelection).<List<E>>map(ArrayList::new)
                            .orElse(Collections.emptyList());
            selectableModel.ifPresent(Selectable::clearSelection);
            clearChildrenMap(node);
            children.forEach(this::clearChildrenMap);
            getOriginalRefreshableModel().ifPresent(refreshable -> refreshable.refreshChildren(node, children));
            fireEvent(TreeDataEvent.STRUCTURE_CHANGED, getPath(node), -1, -1);
            openableModel.ifPresent(model -> model.setOpenObjects(openObjects));
            selectableModel.ifPresent(model -> model.setSelection(selection));
        }


        @Override
        public void refreshNodeCache(final E node)
        {
            getOriginalRefreshableModel().ifPresent(refreshable -> refreshable.refreshNodeCache(node));
        }


        protected void clearChildrenMap(final E node)
        {
            final List<E> children = childrenMap.remove(node);
            matchingBranch.remove(node);
            if(children != null)
            {
                children.forEach(this::clearChildrenMap);
            }
        }


        @Override
        public List<E> findNodesByData(final Object data)
        {
            final RefreshableTreeModel<E> refreshableTreeModel = getOriginalRefreshableModel()
                            .orElseThrow(() -> new IllegalArgumentException("Cannot look for nodes of non-refreshable filtered model"));
            final List<E> nodesByData = Optional.ofNullable(refreshableTreeModel.findNodesByData(data))
                            .orElse(Collections.emptyList());
            return nodesByData.stream().filter(node -> node == getRoot() || ArrayUtils.isNotEmpty(getPath(node)))
                            .collect(Collectors.toList());
        }


        @Override
        public Set<E> getOpenObjects()
        {
            return getOriginalOpenableModel().map(Openable::getOpenObjects)
                            .map(objects -> objects.stream().filter(this::matchesFilter).collect(Collectors.toSet()))
                            .orElseGet(super::getOpenObjects);
        }


        @Override
        public void setOpenObjects(final Collection<? extends E> nodes)
        {
            final Optional<? extends Openable<E>> openableModel = getOriginalOpenableModel();
            if(openableModel.isPresent())
            {
                openableModel.get().clearOpen();
                nodes.stream().filter(this::matchesFilter).forEach(node -> {
                    openableModel.get().addOpenObject(node);
                    fireOpenChanged(getPath(node));
                });
            }
            else
            {
                super.setOpenObjects(nodes);
            }
        }


        @Override
        public boolean isObjectOpened(final Object node)
        {
            final Optional<? extends Openable<E>> openableModel = getOriginalOpenableModel();
            return (openableModel.isPresent() && matchesFilter((E)node) && openableModel.get().isObjectOpened(node))
                            || (super.isObjectOpened(node));
        }


        @Override
        public boolean addOpenObject(final E node)
        {
            final Optional<? extends Openable<E>> openableModel = getOriginalOpenableModel();
            if(openableModel.isPresent())
            {
                final boolean opened = matchesFilter(node) && openableModel.get().addOpenObject(node);
                if(opened)
                {
                    fireOpenChanged(getPath(node));
                }
                return opened;
            }
            else
            {
                return super.addOpenObject(node);
            }
        }


        @Override
        public boolean removeOpenObject(final Object node)
        {
            final Optional<? extends Openable<E>> openableModel = getOriginalOpenableModel();
            if(openableModel.isPresent())
            {
                final boolean closed = matchesFilter((E)node) && openableModel.get().removeOpenObject(node);
                if(closed)
                {
                    fireOpenChanged(getPath((E)node));
                }
                return closed;
            }
            else
            {
                return super.removeOpenObject(node);
            }
        }


        @Override
        public boolean addOpenPath(final int[] path)
        {
            final Optional<? extends TreeOpenableModel> openableModel = getOriginalOpenableModel();
            if(openableModel.isPresent())
            {
                final boolean opened = openableModel.get().addOpenPath(toOriginalPath(path));
                if(opened)
                {
                    fireOpenChanged(path);
                }
                return opened;
            }
            else
            {
                return super.addOpenPath(path);
            }
        }


        @Override
        public boolean addOpenPaths(final int[][] paths)
        {
            final Optional<? extends TreeOpenableModel> openableModel = getOriginalOpenableModel();
            if(openableModel.isPresent())
            {
                final boolean opened = openableModel.get().addOpenPaths(toOriginalPaths(paths));
                if(opened)
                {
                    Stream.of(paths).forEach(this::fireOpenChanged);
                }
                return opened;
            }
            else
            {
                return super.addOpenPaths(paths);
            }
        }


        @Override
        public boolean removeOpenPath(final int[] path)
        {
            final Optional<? extends TreeOpenableModel> openableModel = getOriginalOpenableModel();
            if(openableModel.isPresent())
            {
                final boolean closed = openableModel.get().removeOpenPath(toOriginalPath(path));
                if(closed)
                {
                    fireOpenChanged(path);
                }
                return closed;
            }
            else
            {
                return super.removeOpenPath(path);
            }
        }


        @Override
        public boolean removeOpenPaths(final int[][] paths)
        {
            final Optional<? extends TreeOpenableModel> openableModel = getOriginalOpenableModel();
            if(openableModel.isPresent())
            {
                final boolean closed = openableModel.get().removeOpenPaths(toOriginalPaths(paths));
                if(closed)
                {
                    Stream.of(paths).forEach(this::fireOpenChanged);
                }
                return closed;
            }
            else
            {
                return super.removeOpenPaths(paths);
            }
        }


        @Override
        public boolean isPathOpened(final int[] path)
        {
            return getOriginalOpenableModel().map(openable -> openable.isPathOpened(toOriginalPath(path)))
                            .orElseGet(() -> super.isPathOpened(path));
        }


        @Override
        public boolean isOpenEmpty()
        {
            return getOpenCount() == 0;
        }


        @Override
        public void clearOpen()
        {
            final Optional<? extends TreeOpenableModel> openableModel = getOriginalOpenableModel();
            if(openableModel.isPresent())
            {
                final int[][] openPaths = getOpenPaths();
                openableModel.get().clearOpen();
                Stream.of(openPaths).forEach(this::fireOpenChanged);
            }
            else
            {
                super.clearOpen();
            }
        }


        @Override
        public int getOpenCount()
        {
            final int[][] opened = getOpenPaths();
            return opened != null ? opened.length : 0;
        }


        @Override
        public int[] getOpenPath()
        {
            final int[][] paths = getOpenPaths();
            return paths != null && paths.length > 0 ? paths[0] : new int[0];
        }


        @Override
        public int[][] getOpenPaths()
        {
            return getOriginalOpenableModel().map(openable -> fromOriginalPaths(openable.getOpenPaths()))
                            .orElseGet(super::getOpenPaths);
        }


        @Override
        public Set<E> getSelection()
        {
            return getOriginalSelectableModel().map(Selectable::getSelection)
                            .map(objects -> objects.stream().filter(this::matchesFilter).collect(Collectors.toSet()))
                            .orElseGet(super::getSelection);
        }


        @Override
        public void setSelection(final Collection<? extends E> nodes)
        {
            final Optional<? extends Selectable<E>> selectableModel = getOriginalSelectableModel();
            if(selectableModel.isPresent())
            {
                selectableModel.get().clearSelection();
                nodes.stream().filter(this::matchesFilter).forEach(node -> {
                    selectableModel.get().addToSelection(node);
                    fireSelectionChanged(getPath(node));
                });
            }
            else
            {
                super.setSelection(nodes);
            }
        }


        @Override
        public boolean isSelected(final Object node)
        {
            final Optional<? extends Selectable<E>> selectableModel = getOriginalSelectableModel();
            return (selectableModel.isPresent() && matchesFilter((E)node) && selectableModel.get().isSelected(node))
                            || (super.isSelected(node));
        }


        @Override
        public boolean addToSelection(final E node)
        {
            final Optional<? extends Selectable<E>> selectableModel = getOriginalSelectableModel();
            if(selectableModel.isPresent())
            {
                final boolean selected = matchesFilter(node) && selectableModel.get().addToSelection(node);
                if(selected)
                {
                    fireSelectionChanged(getPath(node));
                }
                return selected;
            }
            else
            {
                return super.addToSelection(node);
            }
        }


        @Override
        public boolean removeFromSelection(final Object node)
        {
            final Optional<? extends Selectable<E>> selectableModel = getOriginalSelectableModel();
            if(selectableModel.isPresent())
            {
                final boolean deselected = matchesFilter((E)node) && selectableModel.get().removeFromSelection(node);
                if(deselected)
                {
                    fireSelectionChanged(getPath((E)node));
                }
                return deselected;
            }
            else
            {
                return super.removeFromSelection(node);
            }
        }


        @Override
        public SelectionControl<E> getSelectionControl()
        {
            return getOriginalSelectableModel().map(Selectable::getSelectionControl).orElseGet(super::getSelectionControl);
        }


        @Override
        public void setSelectionControl(final SelectionControl selectionControl)
        {
            getOriginalSelectableModel().ifPresentOrElse(selectable -> selectable.setSelectionControl(selectionControl),
                            () -> super.setSelectionControl(selectionControl));
        }


        @Override
        public boolean isMultiple()
        {
            return getOriginalSelectableModel().map(Selectable::isMultiple).orElseGet(super::isMultiple);
        }


        @Override
        public void setMultiple(final boolean multiple)
        {
            getOriginalSelectableModel().ifPresentOrElse(selectable -> selectable.setMultiple(multiple),
                            () -> super.setMultiple(multiple));
        }


        @Override
        public boolean addSelectionPath(final int[] path)
        {
            final Optional<? extends TreeSelectableModel> selectableModel = getOriginalSelectableModel();
            if(selectableModel.isPresent())
            {
                final boolean selected = selectableModel.get().addSelectionPath(toOriginalPath(path));
                if(selected)
                {
                    fireSelectionChanged(path);
                }
                return selected;
            }
            else
            {
                return super.addSelectionPath(path);
            }
        }


        @Override
        public boolean addSelectionPaths(final int[][] paths)
        {
            final Optional<? extends TreeSelectableModel> selectableModel = getOriginalSelectableModel();
            if(selectableModel.isPresent())
            {
                final boolean selected = selectableModel.get().addSelectionPaths(toOriginalPaths(paths));
                if(selected)
                {
                    Stream.of(paths).forEach(this::fireSelectionChanged);
                }
                return selected;
            }
            else
            {
                return super.addSelectionPaths(paths);
            }
        }


        @Override
        public boolean removeSelectionPath(final int[] path)
        {
            final Optional<? extends TreeSelectableModel> selectableModel = getOriginalSelectableModel();
            if(selectableModel.isPresent())
            {
                final boolean deselected = selectableModel.get().removeSelectionPath(toOriginalPath(path));
                if(deselected)
                {
                    fireSelectionChanged(path);
                }
                return deselected;
            }
            else
            {
                return super.removeSelectionPath(path);
            }
        }


        @Override
        public boolean removeSelectionPaths(final int[][] paths)
        {
            final Optional<? extends TreeSelectableModel> selectableModel = getOriginalSelectableModel();
            if(selectableModel.isPresent())
            {
                final boolean deselected = selectableModel.get().removeSelectionPaths(toOriginalPaths(paths));
                if(deselected)
                {
                    Stream.of(paths).forEach(this::fireSelectionChanged);
                }
                return deselected;
            }
            else
            {
                return super.addSelectionPaths(paths);
            }
        }


        @Override
        public boolean isPathSelected(final int[] path)
        {
            return getOriginalSelectableModel().map(selectable -> selectable.isPathSelected(toOriginalPath(path)))
                            .orElseGet(() -> super.isPathSelected(path));
        }


        @Override
        public boolean isSelectionEmpty()
        {
            return getSelectionCount() == 0;
        }


        @Override
        public void clearSelection()
        {
            final Optional<? extends TreeSelectableModel> selectableModel = getOriginalSelectableModel();
            if(selectableModel.isPresent())
            {
                final int[][] selectionPaths = getSelectionPaths();
                selectableModel.get().clearSelection();
                if(selectionPaths != null)
                {
                    Stream.of(selectionPaths).forEach(this::fireSelectionChanged);
                }
            }
            else
            {
                super.clearSelection();
            }
        }


        @Override
        public int[] getSelectionPath()
        {
            final int[][] paths = getSelectionPaths();
            return paths != null && paths.length > 0 ? paths[0] : new int[0];
        }


        @Override
        public int[][] getSelectionPaths()
        {
            return getOriginalSelectableModel().map(selectable -> fromOriginalPaths(selectable.getSelectionPaths()))
                            .orElseGet(super::getSelectionPaths);
        }


        @Override
        public int getSelectionCount()
        {
            final int[][] selection = getSelectionPaths();
            return selection != null ? selection.length : 0;
        }


        /**
         * Calculates a path valid for this model basing on a path valid for original model
         *
         * @param originalPath
         *           path valid for original model
         * @return path valid for this model or empty array if path was filtered out
         */
        public int[] fromOriginalPath(final int[] originalPath)
        {
            final E child = getOriginalModel().getChild(originalPath);
            if(child != null)
            {
                return getPath(child);
            }
            else
            {
                return new int[] {};
            }
        }


        /**
         * Calculates a path valid for original model basing on a path valid for this model
         *
         * @param path
         *           path valid for this model
         * @return path valid for original model
         */
        public int[] toOriginalPath(final int[] path)
        {
            final E child = getChild(path);
            return getOriginalModel().getPath(child);
        }


        /**
         * Calculates paths valid for this model basing on paths valid for original model
         *
         * @param originalPaths
         *           paths valid for original model
         * @return paths valid for this model (may contain empty arrays if particular path was filtered out)
         */
        public int[][] fromOriginalPaths(final int[][] originalPaths)
        {
            return originalPaths != null
                            ? Arrays.stream(originalPaths).map(this::fromOriginalPath).filter(Objects::nonNull).filter(path -> path.length > 0)
                            .collect(Collectors.toList()).toArray(new int[][] {})
                            : null;
        }


        /**
         * Calculates paths valid for this model basing on paths valid for original model
         *
         * @param paths
         *           paths valid for original model
         * @return paths valid for this model
         */
        public int[][] toOriginalPaths(final int[][] paths)
        {
            return paths != null
                            ? Arrays.stream(paths).map(this::toOriginalPath).collect(Collectors.toList()).toArray(new int[][] {})
                            : null;
        }
    }


    /**
     * A special map type used to unpacking decorated nodes in the original tree. It overrides only those methods which are
     * used by the FilteredTreeModel.
     */
    private static class NavigationNodeDecoratorsSupportedHashMap extends HashMap
    {
        @Override
        public Object put(final Object key, final Object value)
        {
            return super.put(NavigationNodeDecoratorUtils.unwrap((NavigationNode)key), value);
        }


        @Override
        public Object get(final Object key)
        {
            return super.get(NavigationNodeDecoratorUtils.unwrap((NavigationNode)key));
        }
    }
}
