/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.common.explorertree;

import static com.hybris.cockpitng.widgets.common.explorertree.model.DefaultExplorerTreeModel.ZK_SORT_ASCENDING;

import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.backoffice.navigation.NavigationNodeDecorator;
import com.hybris.backoffice.navigation.NavigationNodeDecoratorUtils;
import com.hybris.backoffice.navigation.NavigationTree;
import com.hybris.backoffice.navigation.TreeNodeSelector;
import com.hybris.backoffice.navigation.impl.SimpleNode;
import com.hybris.cockpitng.annotations.GlobalCockpitEvent;
import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.annotations.ViewEvents;
import com.hybris.cockpitng.components.Actions;
import com.hybris.cockpitng.components.Widgetslot;
import com.hybris.cockpitng.config.explorertree.jaxb.ExplorerTree;
import com.hybris.cockpitng.core.config.CockpitConfigurationException;
import com.hybris.cockpitng.core.config.CockpitConfigurationNotFoundException;
import com.hybris.cockpitng.core.config.impl.DefaultConfigContext;
import com.hybris.cockpitng.core.context.CockpitContext;
import com.hybris.cockpitng.core.context.impl.DefaultCockpitContext;
import com.hybris.cockpitng.core.events.CockpitEvent;
import com.hybris.cockpitng.core.events.CockpitEventQueue;
import com.hybris.cockpitng.core.events.impl.DefaultCockpitEvent;
import com.hybris.cockpitng.core.util.CockpitProperties;
import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectCRUDHandler;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacade;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectNotFoundException;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.testing.annotation.InextensibleMethod;
import com.hybris.cockpitng.tree.factory.NavigationTreeFactory;
import com.hybris.cockpitng.tree.node.DynamicNode;
import com.hybris.cockpitng.tree.node.TypeNode;
import com.hybris.cockpitng.tree.util.TreeModelDecorator;
import com.hybris.cockpitng.tree.util.TreeUtils;
import com.hybris.cockpitng.tree.util.TreeUtils.FilterStringResolver;
import com.hybris.cockpitng.tree.util.TreeUtils.FilteredTreeModel;
import com.hybris.cockpitng.util.BackofficeSpringUtil;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.cockpitng.util.cache.AsyncWarmUpCache;
import com.hybris.cockpitng.util.cache.WidgetAsyncWarmUpCache;
import com.hybris.cockpitng.util.notifications.NotificationService;
import com.hybris.cockpitng.widgets.common.explorertree.comparator.NodeHierarchyComparator;
import com.hybris.cockpitng.widgets.common.explorertree.data.ActionAwareNode;
import com.hybris.cockpitng.widgets.common.explorertree.data.PartitionNodeData;
import com.hybris.cockpitng.widgets.common.explorertree.model.DefaultExplorerTreeModel;
import com.hybris.cockpitng.widgets.common.explorertree.model.RefreshableTreeModel;
import com.hybris.cockpitng.widgets.common.explorertree.refreshstrategy.ExplorerTreeRefreshStrategy;
import com.hybris.cockpitng.widgets.common.explorertree.renderer.DefaultTreeitemRenderer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.event.KeyEvent;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.AbstractTreeModel;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.TreeModel;
import org.zkoss.zul.Treecol;
import org.zkoss.zul.Treecols;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.event.TreeDataEvent;

/**
 * Explorer tree widget controller.
 */
public class ExplorerTreeController extends DefaultWidgetController
{
    public static final String SOCKET_IN_NODE_ID_SELECTED = "nodeIdSelected";
    public static final String SOCKET_IN_SET_TREE_CONTEXT = "setTreeContext";
    public static final String SOCKET_IN_SELECTION_CONTEXT = "selectionContext";
    public static final String SOCKET_IN_CLEAR = "clear";
    public static final String SOCKET_IN_CLEAR_SELECTED_NODE = "clearSelectedNode";
    public static final String SOCKET_IN_REFRESH_BY_SELECTED_NODE = "refreshBySelectedNode";
    public static final String SOCKET_OUT_NODE_SELECTED = "nodeSelected";
    public static final String SOCKET_OUT_DATA_SELECTED = "dataSelected";
    public static final String SETTING_CONFIGURATION_CONTEXT = "explorerTreeConfigCtx";
    public static final String SETTING_MIN_SEARCH_INPUT_LENGTH = "minSearchInputLength";
    public static final String SETTING_FILTER_SETTING_MODE = "defaultSearchDepth";
    public static final String SETTING_TRAVERSED_ITEMS_LIMIT_DURING_FILTER = "traversedItemsLimitDuringFilter";
    public static final String SETTING_EXPANDED_ITEMS_SOFT_LIMIT_AFTER_FILTER = "expandedItemsAfterFilterSoftLimit";
    public static final String SETTING_ACTIONS_COMPONENT = "actionsComponent";
    public static final String SETTING_AUTO_SEARCH_ENABLED = "autoSearchEnabled";
    public static final String SETTING_AUTO_SORT_ENABLED = "autoSortEnabled";
    public static final String ON_NODE_SELECTION_REQUEST = "onNodeSelectionRequest";
    public static final String ON_NODE_REFRESH_REQUEST = "onNodeRefreshRequest";
    public static final String ON_EXPLORER_TREE_FILTER_CHANGE = "onExplorerTreeFilterChange";
    public static final String MODEL_FILTER_CHANGING_TEXT = "filterChangingText";
    public static final String MODEL_FILTER_SETTING_MODE = "filterSettingMode";
    public static final String MODEL_SELECTION_CONTEXT = "selectionContext";
    public static final String MODEL_LABEL_CACHE = "labelCache";
    public static final String DYNAMIC_NODE_SELECTION_CONTEXT = "dynamicNodeSelectionContext";
    public static final String EXPLORER_TREE_COMPONENT = "explorerTree";
    public static final String FILTER_TEXTBOX_COMPONENT_ID = "filterTextbox";
    public static final String SELECTED_NODE = "selectedNode";
    public static final String TREE_CONTEXT = "treeContext";
    public static final String REFRESH_STRATEGIES_SETTING = "refreshStrategies";
    public static final String RELATED_OBJECTS_TO_UPDATE_CTX_PROPERTY = "relatedObjectsToUpdate";
    public static final String TREE_ITEM_RENDERER_SETTING = "treeItemRenderer";
    public static final String CANNOT_RETRIEVE_EXPLORER_TREE_WIDGET_CONFIGURATION_MSG = "Cannot retrieve explorer tree widget configuration";
    public static final String SELECTED_OBJECT_EXPRESSION = "selectionContext.selectedObject";
    /**
     * @deprecated since 2005, no longer used - tree accepts all nodes after hitting the limit instead of throwing exception
     */
    @Deprecated(since = "2005", forRemoval = true)
    public static final String FILTER_EXCEEDED_TRAVERSED_ITEMS_LIMIT_EVENT_TYPE = "FilterExceededTraversedItemsLimit";
    private static final String ZK_AUTO_SORT_ATTRIBUTE = "org.zkoss.zul.tree.autoSort";
    private static final String PROPERTY_LABEL_CACHE_ENABLED = "cockpitng.explorertree.labelcache.enabled";
    private static final boolean PROPERTY_LABEL_CACHE_ENABLED_DEFAULT_VALUE = true;
    private static final String PROPERTY_LABEL_CACHE_WARMUP_ENABLED = "cockpitng.explorertree.labelcache.warmup.enabled";
    private static final boolean PROPERTY_LABEL_CACHE_WARMUP_ENABLED_DEFAULT_VALUE = true;
    private static final Logger LOG = LoggerFactory.getLogger(ExplorerTreeController.class);
    @WireVariable
    private transient NavigationTreeFactory navigationTreeFactory;
    @WireVariable
    private transient LabelService labelService;
    @WireVariable
    private transient PermissionFacade permissionFacade;
    @WireVariable
    private transient TypeFacade typeFacade;
    @WireVariable
    private transient ObjectFacade objectFacade;
    @WireVariable
    private transient NotificationService notificationService;
    @WireVariable
    private transient CockpitProperties cockpitProperties;
    @WireVariable
    private transient CockpitEventQueue cockpitEventQueue;
    @Wire
    private Tree explorerTree;
    @Wire
    private Div filterDiv;
    @Wire
    private Vlayout mainCnt;
    @Wire
    private Bandbox filterTextbox;
    @Wire
    private Checkbox searchMode;
    @Wire
    private Div toolbarContainer;
    @Wire
    private Actions primaryActions;
    @Wire
    private Actions secondaryActions;
    @Wire
    private Widgetslot toolbarSlot;
    private AbstractTreeModel<NavigationNode> treeModel;
    private final ReentrantLock writerLock = new ReentrantLock();
    private final transient NodeHierarchyComparator nodeComparator = new NodeHierarchyComparator();


    @Override
    public void initialize(final Component comp)
    {
        setTreeModel(createTreeModel());
        filterTextbox.addEventListener(Events.ON_CANCEL, event -> {
            filterTextbox.setValue(StringUtils.EMPTY);
            Events.echoEvent(ON_EXPLORER_TREE_FILTER_CHANGE, filterTextbox, StringUtils.EMPTY);
        });
        filterTextbox.addEventListener(Events.ON_OPEN,
                        event -> searchMode.setChecked(TreeUtils.SearchDepth.SHALLOW.equals(getCurrentSearchDepth())));
        searchMode.addEventListener(Events.ON_CHECK, event -> {
            final TreeUtils.SearchDepth searchDepth = searchMode.isChecked() ? TreeUtils.SearchDepth.SHALLOW
                            : TreeUtils.SearchDepth.DEEP;
            setValue(MODEL_FILTER_SETTING_MODE, searchDepth);
            if(getTreeModel() instanceof FilteredTreeModel)
            {
                ((FilteredTreeModel<NavigationNode>)getTreeModel()).setSearchDepth(searchDepth);
            }
            filterTextbox.setOpen(false);
        });
        this.restoreState(false);
        if(labelCacheEnabled())
        {
            initializeLabelCache();
        }
    }


    protected AbstractTreeModel<NavigationNode> createTreeModel()
    {
        return new DefaultExplorerTreeModel(createNavigationTree(getTreeContextFromModel()));
    }


    protected void initializeLabelCache()
    {
        if(getValue(MODEL_LABEL_CACHE, AsyncWarmUpCache.class) != null)
        {
            getLabelCache().clear();
        }
        if(labelCacheEnabled() && labelCacheWarmupEnabled())
        {
            warmUpLabelCache(getLabelCache());
        }
    }


    /**
     * Creates an instance of the tree model. Override this method to create your own tree model.
     *
     * @return the tree model
     */
    protected AbstractTreeModel<NavigationNode> createFilteredNavigationTreeModel()
    {
        String filterString = getModel().getValue(MODEL_FILTER_CHANGING_TEXT, String.class);
        if(filterString == null)
        {
            filterString = StringUtils.EMPTY;
        }
        final FilteredTreeModel<NavigationNode> filteredTreeModel = new FilteredTreeModel<>(getUndecoratedTreeModel(), filterString,
                        false, getFilteringStringResolver(), getWidgetSettings().getInt(SETTING_TRAVERSED_ITEMS_LIMIT_DURING_FILTER));
        filteredTreeModel.setSearchDepth(getCurrentSearchDepth());
        return filteredTreeModel;
    }


    protected FilterStringResolver<NavigationNode> getFilteringStringResolver()
    {
        if(labelCacheEnabled())
        {
            return node -> getLabelCache().get(node, () -> calculateLabel(node));
        }
        return this::calculateLabel;
    }


    protected AsyncWarmUpCache<NavigationNode, String> getLabelCache()
    {
        if(getValue(MODEL_LABEL_CACHE, AsyncWarmUpCache.class) == null && labelCacheEnabled())
        {
            final AsyncWarmUpCache<NavigationNode, String> labelCache = createLabelCache();
            setValue(MODEL_LABEL_CACHE, labelCache);
        }
        return getValue(MODEL_LABEL_CACHE, AsyncWarmUpCache.class);
    }


    protected AsyncWarmUpCache<NavigationNode, String> createLabelCache()
    {
        return new WidgetAsyncWarmUpCache<>(getWidgetInstanceManager());
    }


    protected void warmUpLabelCache(final AsyncWarmUpCache<NavigationNode, String> labelCache)
    {
        if(labelCache != null)
        {
            labelCache.warmUp(new CachedNodesIterator(), this::calculateLabel);
        }
    }


    protected void warmUpLabelCache(final NavigationNode startingNode)
    {
        final AsyncWarmUpCache<NavigationNode, String> labelCache = getLabelCache();
        if(labelCache != null)
        {
            labelCache.warmUp(new CachedNodesIterator(startingNode), this::calculateLabel);
        }
    }


    protected boolean labelCacheEnabled()
    {
        return getCockpitProperties().getBoolean(PROPERTY_LABEL_CACHE_ENABLED, PROPERTY_LABEL_CACHE_ENABLED_DEFAULT_VALUE);
    }


    protected boolean labelCacheWarmupEnabled()
    {
        return labelCacheEnabled() && getCockpitProperties().getBoolean(PROPERTY_LABEL_CACHE_WARMUP_ENABLED,
                        PROPERTY_LABEL_CACHE_WARMUP_ENABLED_DEFAULT_VALUE);
    }


    protected String calculateLabel(final NavigationNode node)
    {
        if(node instanceof NavigationNodeDecorator)
        {
            return ((NavigationNodeDecorator)node).getUiLabel(this);
        }
        if(node instanceof DynamicNode)
        {
            return node.getName();
        }
        return StringUtils.EMPTY;
    }


    protected TreeUtils.SearchDepth getCurrentSearchDepth()
    {
        return Optional.ofNullable(getValue(MODEL_FILTER_SETTING_MODE, TreeUtils.SearchDepth.class))
                        .orElseGet(this::getDefaultSearchDepth);
    }


    protected TreeUtils.SearchDepth getDefaultSearchDepth()
    {
        return Optional.ofNullable(getWidgetSettings().getString(SETTING_FILTER_SETTING_MODE)).map(TreeUtils.SearchDepth::valueOf)
                        .orElse(TreeUtils.SearchDepth.DEEP);
    }


    /**
     * Constructs the navigation tree structure. Override this method to construct your own custom navigation tree
     * structure. To allow search on the tree wrap all nodes in
     * {@link com.hybris.backoffice.navigation.NavigationNodeDecorator}
     *
     * @return new navigation tree structure
     */
    protected NavigationTree createNavigationTree(final CockpitContext context)
    {
        NavigationTree navigationTree = null;
        final Optional<ExplorerTree> explorerTreeConfiguration = loadExplorerTreeConfiguration();
        if(explorerTreeConfiguration.isPresent())
        {
            navigationTree = this.getNavigationTreeFactory().createNavigationTree(explorerTreeConfiguration.get(), context);
        }
        return navigationTree;
    }


    protected Optional<ExplorerTree> loadExplorerTreeConfiguration()
    {
        try
        {
            final String componentConfContext = getWidgetSettings().getString(SETTING_CONFIGURATION_CONTEXT);
            final ExplorerTree explorerConfig = getWidgetInstanceManager()
                            .loadConfiguration(new DefaultConfigContext(componentConfContext), ExplorerTree.class);
            if(explorerConfig != null)
            {
                return Optional.of(explorerConfig);
            }
            else
            {
                LOG.warn(CANNOT_RETRIEVE_EXPLORER_TREE_WIDGET_CONFIGURATION_MSG);
            }
        }
        catch(final CockpitConfigurationNotFoundException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug(CANNOT_RETRIEVE_EXPLORER_TREE_WIDGET_CONFIGURATION_MSG, e);
            }
        }
        catch(final CockpitConfigurationException e)
        {
            LOG.warn(CANNOT_RETRIEVE_EXPLORER_TREE_WIDGET_CONFIGURATION_MSG, e);
        }
        return Optional.empty();
    }


    /**
     * Creates the tree item renderer. It will try to load the renderer defined in by the
     * {@link #TREE_ITEM_RENDERER_SETTING} widget setting. If no valid renderer for the given type will be found, then the
     * {@link TreeitemRenderer} will be used instead.
     *
     * @return new tree item renderer
     */
    protected TreeitemRenderer<NavigationNode> createTreeItemRenderer()
    {
        final String rendererBeanId = getWidgetInstanceManager().getWidgetSettings().getString(TREE_ITEM_RENDERER_SETTING);
        try
        {
            return getTreeItemRenderer(rendererBeanId);
        }
        catch(final NoSuchBeanDefinitionException e)
        {
            LOG.warn(String.format("Couldn't load bean %s, DefaultTreeitemRenderer will be used instead.", rendererBeanId), e);
            return new DefaultTreeitemRenderer(this);
        }
    }


    @InextensibleMethod
    TreeitemRenderer<NavigationNode> getTreeItemRenderer(final String treeItemRendererBeanId)
    {
        return BackofficeSpringUtil.getBean(treeItemRendererBeanId, this);
    }


    /**
     * Returns the tree model associated with the tree.
     *
     * @return tree model associated with the tree
     */
    public AbstractTreeModel<NavigationNode> getTreeModel()
    {
        return this.treeModel;
    }


    protected TreeModel<NavigationNode> getUndecoratedTreeModel()
    {
        return (this.getTreeModel() instanceof TreeModelDecorator) ? ((TreeModelDecorator)getTreeModel()).getOriginalModel()
                        : getTreeModel();
    }


    /**
     * Handles the tree item open event. Called by the view (renderer).
     *
     * @param event
     *           the open event
     * @param treeItem
     *           the related tree item
     * @return <code>true</code> if node was opened, <code>false</code> otherwise
     */
    public boolean handleOpen(final Event event, final Treeitem treeItem)
    {
        return true;
    }


    /**
     * Handles the tree selection. Called by the view (renderer).
     *
     * @param event
     *           the selection event
     * @param treeItem
     *           the related tree item
     * @return <code>true</code> if code was selected, <code>false</code> otherwise
     */
    public boolean handleTreeSelection(final Event event, final Treeitem treeItem)
    {
        final NavigationNode node = treeItem.getValue();
        final NavigationNode value = NavigationNodeDecoratorUtils.unwrap(node);
        final boolean isDynamicNode = value instanceof DynamicNode;
        final boolean simpleNodeWithChildren = value instanceof SimpleNode && CollectionUtils.isNotEmpty(value.getChildren());
        final boolean isSelectableNode = value instanceof TypeNode || !simpleNodeWithChildren
                        || (isDynamicNode && ((DynamicNode)value).isSelectable());
        if(isSelectableNode)
        {
            if(simpleNodeWithChildren)
            {
                this.toggleOpen(node);
            }
            if(isDynamicNode || hasAccessToTheNode(value))
            {
                this.select(node, false);
                getModel().setValue(SELECTED_NODE, new TreeNodeSelector(node.getId(), true));
                this.sendOutputEvents(value);
            }
            else
            {
                showMessageBoxNoPermission();
            }
            return true;
        }
        else
        {
            this.toggleOpen(node);
            return false;
        }
    }


    protected void showMessageBoxNoPermission()
    {
        final String msg = getWidgetInstanceManager().getLabel("general.no.permission.to.node");
        final String title = getWidgetInstanceManager().getLabel("general.no.permission.message.box.title");
        Messagebox.show(msg, title, Messagebox.OK, Messagebox.EXCLAMATION);
    }


    @SocketEvent(socketId = SOCKET_IN_REFRESH_BY_SELECTED_NODE)
    public void refreshBySelectedNode()
    {
        this.sendOutputEvents(this.getSelectedNode());
    }


    @SocketEvent(socketId = SOCKET_IN_NODE_ID_SELECTED)
    public void handleExternalNodeIdSelection(final TreeNodeSelector treeNodeSelector)
    {
        NavigationNode nodeToSelect = null;
        if(this.getTreeModel().getRoot() != null)
        {
            nodeToSelect = findNodeById(this.getTreeModel().getRoot(), treeNodeSelector.getNodeId());
        }
        if(nodeToSelect == null)
        {
            final String nodeId = treeNodeSelector == null ? StringUtils.EMPTY : treeNodeSelector.getNodeId();
            LOG.warn("Cannot find node for id: '{}'", nodeId);
            return;
        }
        final boolean selected = select(nodeToSelect, true);
        if(selected)
        {
            if(treeNodeSelector.isTriggerSelectionEvents())
            {
                this.sendOutputEvents(NavigationNodeDecoratorUtils.unwrap(nodeToSelect));
            }
            this.refreshTree();
        }
    }


    @SocketEvent(socketId = SOCKET_IN_SELECTION_CONTEXT)
    public void handleSelectionContext(final Map<String, Object> selectionContext)
    {
        final Map<String, Object> context = Optional.ofNullable(selectionContext).orElse(new HashMap<>());
        setValue(MODEL_SELECTION_CONTEXT, context);
    }


    protected NavigationNode findNodeById(final NavigationNode currentNode, final String nodeId)
    {
        if(currentNode == null)
        {
            LOG.warn("Received null instead of expected instance of NavigationNode type.");
            return null;
        }
        else if(StringUtils.equals(currentNode.getId(), nodeId))
        {
            return currentNode;
        }
        else
        {
            for(final NavigationNode node : currentNode.getChildren())
            {
                final NavigationNode matchingNode = findNodeById(node, nodeId);
                if(matchingNode != null)
                {
                    return matchingNode;
                }
            }
        }
        return null;
    }


    @ViewEvent(componentID = EXPLORER_TREE_COMPONENT, eventName = ON_NODE_SELECTION_REQUEST)
    public void onNodeSelectionRequest(final Event event)
    {
        final NavigationNode node = (NavigationNode)event.getData();
        handleExternalNodeIdSelection(new TreeNodeSelector(node.getId(), true));
    }


    @ViewEvents(
                    {@ViewEvent(componentID = FILTER_TEXTBOX_COMPONENT_ID, eventName = Events.ON_CHANGING)})
    public void disableFilterTextAndInvokeTreeFilter(final InputEvent event)
    {
        if(isSeachOnChangingEnabled())
        {
            final Integer minLength = (Integer)getWidgetInstanceManager().getWidgetSettings()
                            .getOrDefault(SETTING_MIN_SEARCH_INPUT_LENGTH, Integer.valueOf(0));
            if(event.getValue().length() == 0 || event.getValue().length() >= minLength.intValue())
            {
                filterTextbox.setDisabled(true);
                Events.echoEvent(ON_EXPLORER_TREE_FILTER_CHANGE, this.filterTextbox, event.getValue());
            }
        }
    }


    protected boolean isSeachOnChangingEnabled()
    {
        return getWidgetSettings().getBoolean(SETTING_AUTO_SEARCH_ENABLED);
    }


    @ViewEvent(componentID = FILTER_TEXTBOX_COMPONENT_ID, eventName = Events.ON_OK)
    public void onEnterPressed(final KeyEvent event)
    {
        if(!filterTextbox.isDisabled())
        {
            filterTextbox.setDisabled(true);
            Events.echoEvent(ON_EXPLORER_TREE_FILTER_CHANGE, this.filterTextbox,
                            StringUtils.defaultIfBlank(this.filterTextbox.getValue(), StringUtils.EMPTY));
        }
    }


    @ViewEvent(componentID = FILTER_TEXTBOX_COMPONENT_ID, eventName = ON_EXPLORER_TREE_FILTER_CHANGE)
    public void onChangingFilterText(final Event event)
    {
        final String data = (String)event.getData();
        processFilterTextChange(data);
    }


    protected void processFilterTextChange(final String filterText)
    {
        getModel().put(MODEL_FILTER_CHANGING_TEXT, filterText);
        setTreeModel(createFilteredNavigationTreeModel());
        restoreState(true);
        restoreFilterTextbox(filterText.length());
    }


    protected void restoreFilterTextbox(final int cursorPosition)
    {
        filterTextbox.setDisabled(false);
        filterTextbox.setSelectionRange(cursorPosition, cursorPosition);
        filterTextbox.setFocus(true);
    }


    /**
     * Checks whether user has access to the given node.
     *
     * @param selectedNode
     *           node to checked
     * @return <code>true</code>, if user has the access, <code>false</code> otherwise
     */
    public boolean hasAccessToTheNode(final NavigationNode selectedNode)
    {
        if(selectedNode instanceof TypeNode)
        {
            final String nodeType = ((TypeNode)selectedNode).getCode();
            return this.getPermissionFacade().canReadType(nodeType);
        }
        else
        {
            return true;
        }
    }


    /**
     * Selects the given node.
     *
     * @param nodeToSelect
     *           node to be selected
     * @param open
     *           if <code>true</code> the node will be also opened
     * @return <code>true</code> if code was selected, <code>false</code> otherwise
     */
    protected boolean select(final NavigationNode nodeToSelect, final boolean open)
    {
        final AbstractTreeModel<com.hybris.backoffice.navigation.NavigationNode> model = getTreeModel();
        if(model.getRoot() == null)
        {
            LOG.warn("Cannot select explorer's tree node for id: {} - root is null", nodeToSelect.getId());
            return false;
        }
        final int[] path = model.getPath(nodeToSelect);
        if(open)
        {
            addOpenPathWithSuperPaths(path);
        }
        model.clearSelection();
        return model.addSelectionPath(path);
    }


    /**
     * Toggles the open state of the given node.
     *
     * @param node
     *           the node to open/close
     */
    protected void toggleOpen(final NavigationNode node)
    {
        final int[] path = this.getTreeModel().getPath(node);
        if(this.getTreeModel().isPathOpened(path))
        {
            this.getTreeModel().removeOpenPath(path);
        }
        else
        {
            addOpenPathWithSuperPaths(path);
        }
    }


    /**
     * Get current selected Node, if no node selected will return the first one.
     */
    protected NavigationNode getSelectedNode()
    {
        final NavigationNode root = this.getTreeModel().getRoot();
        if(!Objects.nonNull(root))
        {
            return null;
        }
        final TreeNodeSelector selector = getModel().getValue(SELECTED_NODE, TreeNodeSelector.class);
        if(Objects.nonNull(selector))
        {
            final NavigationNode selectedNode = findNodeById(root, selector.getNodeId());
            return Objects.nonNull(selectedNode) ? selectedNode : null;
        }
        else
        {
            final List<NavigationNode> allNodes = root.getChildren();
            if(!allNodes.isEmpty())
            {
                final NavigationNode selectedNode = allNodes.get(0);
                this.select(selectedNode, false);
                return selectedNode;
            }
            return null;
        }
    }


    private int addOpenPathWithSuperPaths(final int[] path)
    {
        if(path != null)
        {
            for(int i = 0; i < path.length; i++)
            {
                getTreeModel().addOpenPath(Arrays.copyOf(path, i + 1));
            }
            return path.length;
        }
        return 0;
    }


    private void restoreState(final boolean stopEventPropagation)
    {
        initializeTree();
        final String filterString = getModel().getValue(MODEL_FILTER_CHANGING_TEXT, String.class);
        filterTextbox.setValue(filterString);
        if(getTreeModel() instanceof FilteredTreeModel)
        {
            expandNodesMatchingFilter();
        }
        else
        {
            expandDefaultNode();
        }
        final TreeNodeSelector loadedSelectedNode = getModel().getValue(SELECTED_NODE, TreeNodeSelector.class);
        if(loadedSelectedNode != null)
        {
            final TreeNodeSelector selectedNode = stopEventPropagation ? new TreeNodeSelector(loadedSelectedNode.getNodeId(), false)
                            : loadedSelectedNode;
            handleExternalNodeIdSelection(selectedNode);
        }
    }


    private void initializeTree()
    {
        final AbstractTreeModel<com.hybris.backoffice.navigation.NavigationNode> model = this.getTreeModel();
        final Tree tree = getExplorerTree();
        tree.clear();
        tree.setItemRenderer(this.createTreeItemRenderer());
        tree.setModel(model);
        if(isAutoSortEnabled())
        {
            enableAutoSort();
        }
    }


    @InextensibleMethod
    private boolean isAutoSortEnabled()
    {
        return getWidgetSettings().getBoolean(SETTING_AUTO_SORT_ENABLED);
    }


    @InextensibleMethod
    private void enableAutoSort()
    {
        explorerTree.setAttribute(ZK_AUTO_SORT_ATTRIBUTE, true);
        final Treecols treecols = new Treecols();
        explorerTree.getChildren().add(0, treecols);
        final Treecol treecol = new Treecol();
        treecols.getChildren().add(treecol);
        treecol.setSortAscending(createDefaultNavigationNodeComparator());
        treecol.setSortDirection(ZK_SORT_ASCENDING);
    }


    @InextensibleMethod
    private Comparator<NavigationNode> createDefaultNavigationNodeComparator()
    {
        final NavigationNodeLabelProvider navigationNodeLabelProvider = getNavigationNodeLabelProvider();
        return Comparator.comparing(navigationNodeLabelProvider::getLabel, String.CASE_INSENSITIVE_ORDER);
    }


    @InextensibleMethod
    NavigationNodeLabelProvider getNavigationNodeLabelProvider()
    {
        return BackofficeSpringUtil.getBean("navigationNodeLabelProvider", this);
    }


    protected void expandDefaultNode()
    {
        final NavigationNode nodeToExpand = findExpandedByDefaultNode();
        if(nodeToExpand != null)
        {
            Events.echoEvent(ON_NODE_SELECTION_REQUEST, this.explorerTree, nodeToExpand);
        }
    }


    protected NavigationNode findExpandedByDefaultNode()
    {
        NavigationNode expandedByDefaultNode = null;
        final NavigationNode root = this.getTreeModel().getRoot();
        if(root != null)
        {
            for(final NavigationNode node : root.getChildren())
            {
                expandedByDefaultNode = findExpandedByDefaultNodeRecursive(node);
                if(expandedByDefaultNode != null)
                {
                    break;
                }
            }
        }
        return expandedByDefaultNode;
    }


    private static NavigationNode findExpandedByDefaultNodeRecursive(final NavigationNode node)
    {
        Validate.notNull("Node may not be null", node);
        if(node.isExpandedByDefault())
        {
            return node;
        }
        if(!(node instanceof DynamicNode))
        {
            for(final NavigationNode child : node.getChildren())
            {
                final NavigationNode expandedNode = findExpandedByDefaultNodeRecursive(child);
                if(expandedNode != null)
                {
                    return expandedNode;
                }
            }
        }
        return null;
    }


    protected void expandNodesMatchingFilter()
    {
        final String filterString = getModel().getValue(MODEL_FILTER_CHANGING_TEXT, String.class);
        if(StringUtils.isNotEmpty(filterString))
        {
            int counter = getWidgetSettings().getInt(SETTING_EXPANDED_ITEMS_SOFT_LIMIT_AFTER_FILTER);
            if(counter == 0)
            {
                return;
            }
            final boolean limitEnabled = counter > 0;
            final FilteredTreeModel<NavigationNode> tree = (FilteredTreeModel<NavigationNode>)getTreeModel();
            for(final NavigationNode node : getMatchingNodes(tree))
            {
                counter -= addOpenPathWithSuperPaths(tree.getPath(node));
                node.getChildren().forEach(childNode -> refreshDynamicNodeCache(childNode));
                if(limitEnabled && counter < 0)
                {
                    break;
                }
            }
        }
    }


    protected Collection<NavigationNode> getMatchingNodes(final FilteredTreeModel<NavigationNode> treeModel)
    {
        if(CollectionUtils.isEmpty(treeModel.getResolvedMatchingNodes()))
        {
            treeModel.getChildren(treeModel.getRoot());
        }
        return treeModel.getResolvedMatchingNodes();
    }


    /**
     * @deprecated since 2005, do not use
     */
    @Deprecated(since = "2005", forRemoval = true)
    protected void expandAllLeaves()
    {
        // Method deprecated, not used - no implementation required
    }


    /**
     * @deprecated since 2005, do not use
     */
    @Deprecated(since = "2005", forRemoval = true)
    protected List<NavigationNode> getAllLeaves()
    {
        return Collections.emptyList();
    }


    protected void refreshTree()
    {
        this.explorerTree.setModel(this.getTreeModel());
    }


    private void sendOutputEvents(final NavigationNode node)
    {
        Object data = null;
        if(node != null)
        {
            data = node.getData();
            if(data == null)
            {
                data = node.getId();
            }
        }
        this.sendOutput(SOCKET_OUT_DATA_SELECTED, data);
        this.sendOutput(SOCKET_OUT_NODE_SELECTED, node);
    }


    @SocketEvent(socketId = SOCKET_IN_SET_TREE_CONTEXT)
    public void setTreeContext(final Collection<Object> contextAttribute)
    {
        final CockpitContext context = getTreeContextFromModel();
        context.setParameter(DYNAMIC_NODE_SELECTION_CONTEXT, contextAttribute);
        setValue(TREE_CONTEXT, context);
        setTreeModel(createTreeModel());
        if(labelCacheEnabled())
        {
            initializeLabelCache();
        }
        this.refreshTree();
        this.expandDefaultNode();
    }


    @SocketEvent(socketId = SOCKET_IN_CLEAR)
    public void clear()
    {
        setValue(SELECTED_NODE, null);
        setValue(MODEL_SELECTION_CONTEXT, null);
        setValue(MODEL_FILTER_CHANGING_TEXT, StringUtils.EMPTY);
        filterTextbox.setValue(StringUtils.EMPTY);
        setTreeContext(Collections.emptyList());
    }


    @SocketEvent(socketId = SOCKET_IN_CLEAR_SELECTED_NODE)
    public void clearSelectedNode()
    {
        setValue(SELECTED_NODE, null);
        setValue(MODEL_SELECTION_CONTEXT, null);
        final AbstractTreeModel<NavigationNode> model = getTreeModel();
        model.clearSelection();
        this.refreshTree();
    }


    protected CockpitContext getTreeContextFromModel()
    {
        CockpitContext context = getValue(TREE_CONTEXT, CockpitContext.class);
        if(context == null)
        {
            context = new DefaultCockpitContext();
        }
        return context;
    }


    @GlobalCockpitEvent(eventName = ObjectCRUDHandler.OBJECTS_UPDATED_EVENT, scope = CockpitEvent.SESSION)
    public void handleObjectsUpdatedEvent(final CockpitEvent event)
    {
        handleObjectsModification(event);
    }


    protected void processFilterTextChange(final Collection<NavigationNode> nodesToRefresh)
    {
        final String filterText = getFilterTextbox().getValue();
        if(!nodesToRefresh.isEmpty() && !filterText.isEmpty())
        {
            processFilterTextChange(filterText);
        }
    }


    @GlobalCockpitEvent(eventName = ObjectCRUDHandler.OBJECTS_DELETED_EVENT, scope = CockpitEvent.SESSION)
    public void handleObjectsDeletedEvent(final CockpitEvent event)
    {
        handleObjectsModification(event);
        clearSelection(event);
    }


    protected void clearSelection(final CockpitEvent event)
    {
        final Object selectedObject = getWidgetInstanceManager().getModel().getValue(SELECTED_OBJECT_EXPRESSION, Object.class);
        if(event.getDataAsCollection().contains(selectedObject))
        {
            setValue(MODEL_SELECTION_CONTEXT, null);
            setValue(SELECTED_NODE, null);
            sendOutput(SOCKET_OUT_NODE_SELECTED, null);
            final AbstractTreeModel<NavigationNode> model = getTreeModel();
            model.clearSelection();
            this.refreshTree();
        }
    }


    protected void handleObjectsModification(final CockpitEvent event)
    {
        final DefaultCockpitEvent cockpitEvent = new DefaultCockpitEvent(ON_NODE_REFRESH_REQUEST, event,
                        getCRUDNotificationSource());
        cockpitEventQueue.publishEvent(cockpitEvent);
    }


    protected String getCRUDNotificationSource()
    {
        return getWidgetslot().getWidgetInstance().getId();
    }


    protected void refreshNode(final NavigationNode nodeToUpdate)
    {
        if(NavigationTreeFactory.ROOT_NODE_ID.equals(nodeToUpdate.getId()))
        {
            loadExplorerTreeConfiguration().ifPresent(
                            explorerConfig -> navigationTreeFactory.refreshRootNode(explorerConfig, nodeToUpdate, getTreeContextFromModel()));
        }
        if(nodeToUpdate.getData() instanceof PartitionNodeData)
        {
            refreshDynamicNode(nodeToUpdate.getParent());
        }
        else
        {
            refreshDynamicNode(nodeToUpdate);
        }
    }


    protected void sendOutputEventsForSelectedNode(final NavigationNode node)
    {
        final TreeNodeSelector selectedNode = getModel().getValue(SELECTED_NODE, TreeNodeSelector.class);
        if(node != null && selectedNode != null && selectedNode.getNodeId().equals(node.getId()))
        {
            sendOutputEvents(node);
        }
    }


    protected void refreshDynamicNode(final NavigationNode nodeToUpdate)
    {
        final NavigationNode unwrappedNode = NavigationNodeDecoratorUtils.unwrap(nodeToUpdate);
        if(getTreeModel() instanceof RefreshableTreeModel)
        {
            ((RefreshableTreeModel)getTreeModel()).refreshChildren(unwrappedNode, unwrappedNode.getChildren());
        }
        findRefreshStrategies().forEach(strategy -> strategy.refreshNode(unwrappedNode));
        if(getTreeModel() instanceof RefreshableTreeModel)
        {
            ListUtils.union(List.of(unwrappedNode), unwrappedNode.getChildren()).forEach(node -> refreshDynamicNodeCache(node));
        }
    }


    private void refreshDynamicNodeCache(final NavigationNode nodeToUpdate)
    {
        final Object nodeData = nodeToUpdate.getData();
        if(nodeData != null
                        && (nodeData instanceof String || nodeData instanceof ActionAwareNode || !objectFacade.isDeleted(nodeData)))
        {
            ((RefreshableTreeModel)getTreeModel()).refreshNodeCache(nodeToUpdate);
        }
    }


    protected void revokeLabelCache(final NavigationNode node)
    {
        final AsyncWarmUpCache<NavigationNode, String> labelCache = getLabelCache();
        if(labelCache != null)
        {
            labelCache.revoke(node);
            node.getChildren().forEach(this::revokeLabelCache);
        }
    }


    protected Object reloadItemModel(final Object modelToRefresh)
    {
        try
        {
            if(objectFacade.isDeleted(modelToRefresh))
            {
                return null;
            }
            else
            {
                return objectFacade.reload(modelToRefresh);
            }
        }
        catch(final ObjectNotFoundException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Error occurred while reloading item model", e);
            }
            return modelToRefresh;
        }
    }


    protected List<Object> findItemModelsToExpandAfterModification()
    {
        return getTreeModel().getOpenObjects().stream().filter(Objects::nonNull).map(NavigationNode::getData).distinct()
                        .collect(Collectors.toList());
    }


    /**
     * @deprecated since 2205. Please use {@link refreshNodesAndLabelCache}
     */
    @Deprecated(since = "2205", forRemoval = true)
    protected List<NavigationNode> findNodesToRefresh(final CockpitEvent event)
    {
        final List<NavigationNode> nodesToRefresh = new ArrayList<>();
        if(getTreeModel() instanceof RefreshableTreeModel)
        {
            final Set<Object> allItemsToRefresh = findItemsToUpdateInRefreshStrategy(event);
            for(final Object objectToUpdate : allItemsToRefresh)
            {
                final List<NavigationNode> foundNodes = ((RefreshableTreeModel)getTreeModel()).findNodesByData(objectToUpdate);
                if(foundNodes != null)
                {
                    nodesToRefresh.addAll(foundNodes);
                    nodesToRefresh.addAll(foundNodes.stream().map(NavigationNode::getParent).collect(Collectors.toList()));
                }
            }
        }
        return nodesToRefresh.stream().filter(Objects::nonNull).distinct().sorted(getNodeComparator()).collect(Collectors.toList());
    }


    protected List<NavigationNode> refreshNodesAndLabelCache(final CockpitEvent event)
    {
        final List<NavigationNode> nodesToRefresh = new ArrayList<>();
        if(getTreeModel() instanceof RefreshableTreeModel)
        {
            final Set<Object> allItemsToRefresh = findItemsToUpdateInRefreshStrategy(event);
            writerLock.lock();
            try
            {
                for(final Object objectToUpdate : allItemsToRefresh)
                {
                    final List<NavigationNode> foundNodes = ((RefreshableTreeModel)getTreeModel()).findNodesByData(objectToUpdate);
                    if(foundNodes != null)
                    {
                        nodesToRefresh.addAll(foundNodes);
                        nodesToRefresh.addAll(foundNodes.stream().map(NavigationNode::getParent).collect(Collectors.toList()));
                    }
                }
                final List<NavigationNode> nodes = nodesToRefresh.stream().filter(Objects::nonNull).distinct()
                                .sorted(getNodeComparator()).collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(nodes))
                {
                    final AsyncWarmUpCache<NavigationNode, String> labelCache = getLabelCache();
                    if(labelCache != null && NavigationTreeFactory.ROOT_NODE_ID.equals(nodes.get(0).getId()))
                    {
                        labelCache.clear();
                    }
                    nodes.stream().distinct().forEach(node -> {
                        revokeLabelCache(node);
                        refreshNode(node);
                        sendOutputEventsForSelectedNode(node);
                    });
                    if(labelCacheEnabled() && labelCacheWarmupEnabled())
                    {
                        warmUpLabelCache(nodes.get(0));
                    }
                    refreshTree();
                }
            }
            finally
            {
                writerLock.unlock();
            }
        }
        return nodesToRefresh.stream().filter(Objects::nonNull).distinct().sorted(getNodeComparator()).collect(Collectors.toList());
    }


    protected Set<Object> findItemsToUpdateInRefreshStrategy(final CockpitEvent event)
    {
        final List<ExplorerTreeRefreshStrategy> refreshStrategies = findRefreshStrategies();
        final List<Object> itemsFromContextToRefresh = extractItemModelsToRefresh(event);
        final Set<Object> itemsFromStrategy = new HashSet<>();
        for(final ExplorerTreeRefreshStrategy strategy : refreshStrategies)
        {
            for(final Object o : itemsFromContextToRefresh)
            {
                itemsFromStrategy.addAll(strategy.findRelatedObjectsToRefresh(o));
            }
        }
        itemsFromStrategy.addAll(itemsFromContextToRefresh);
        return itemsFromStrategy;
    }


    protected List<Object> extractItemModelsToRefresh(final CockpitEvent event)
    {
        final List<Object> itemModelsToUpdate = new ArrayList<>();
        itemModelsToUpdate.addAll(extractModifiedObjectFromEvent(event));
        if(event instanceof DefaultCockpitEvent)
        {
            final Collection relatedObjectsToUpdate = (Collection)((DefaultCockpitEvent)event).getContext()
                            .get(RELATED_OBJECTS_TO_UPDATE_CTX_PROPERTY);
            if(CollectionUtils.isNotEmpty(relatedObjectsToUpdate))
            {
                itemModelsToUpdate.addAll(relatedObjectsToUpdate);
            }
        }
        return itemModelsToUpdate;
    }


    protected List<Object> extractModifiedObjectFromEvent(final CockpitEvent event)
    {
        final Object eventData = event.getData();
        final List<Object> extractedObjects = new ArrayList<>();
        if(eventData != null)
        {
            if(eventData instanceof Collection)
            {
                extractedObjects.addAll((Collection)eventData);
            }
            else
            {
                extractedObjects.add(eventData);
            }
        }
        return extractedObjects;
    }


    protected List<ExplorerTreeRefreshStrategy> findRefreshStrategies()
    {
        final Object refreshStrategiesKey = getWidgetInstanceManager().getWidgetSettings().get(REFRESH_STRATEGIES_SETTING);
        if(refreshStrategiesKey != null && StringUtils.isNotBlank(refreshStrategiesKey.toString()))
        {
            return (List<ExplorerTreeRefreshStrategy>)SpringUtil.getBean(refreshStrategiesKey.toString(), List.class);
        }
        return Collections.emptyList();
    }


    @GlobalCockpitEvent(eventName = ON_NODE_REFRESH_REQUEST, scope = CockpitEvent.DESKTOP)
    public void onNodeRefreshRequest(final CockpitEvent event)
    {
        if(event.getSource().equals(getCRUDNotificationSource()) && getTreeModel() instanceof RefreshableTreeModel
                        && event.getData() instanceof CockpitEvent)
        {
            final CockpitEvent refreshEvent = (CockpitEvent)event.getData();
            final Collection<Object> nodesToScroll = extractModifiedObjectFromEvent(refreshEvent);
            final List<NavigationNode> nodesToRefresh = refreshNodesAndLabelCache(refreshEvent);
            processFilterTextChange(nodesToRefresh);
            final List<Object> itemModelsToExpand = findItemModelsToExpandAfterModification();
            itemModelsToExpand.forEach(this::expandObject);
            nodesToScroll.forEach(this::scrollToObject);
        }
    }


    protected void expandObject(final Object object)
    {
        final List<NavigationNode> foundNodes = ((RefreshableTreeModel)getTreeModel()).findNodesByData(object);
        if(CollectionUtils.isNotEmpty(foundNodes))
        {
            foundNodes.forEach(node -> {
                getTreeModel().addOpenObject(node);
                final int nodeIndex = node.getParent().getChildren().indexOf(node);
                getTreeModel().fireEvent(TreeDataEvent.OPEN_CHANGED, getTreeModel().getPath(node), nodeIndex, nodeIndex);
            });
        }
    }


    protected void scrollToObject(final Object object)
    {
        final List<NavigationNode> foundNodes = ((RefreshableTreeModel)getTreeModel()).findNodesByData(object);
        if(CollectionUtils.isNotEmpty(foundNodes))
        {
            foundNodes.stream().filter(node -> getTreeModel().getPath(node).length > 0).findFirst().ifPresent(this::scrollToObject);
        }
    }


    protected void scrollToObject(final NavigationNode node)
    {
        final Collection<Treeitem> items = explorerTree.getItems();
        for(final Treeitem item : items)
        {
            if(node.equals(item.getValue()))
            {
                Clients.scrollIntoView(item.getFirstChild());
                break;
            }
        }
    }


    protected class CachedNodesIterator implements java.util.Iterator<Collection<NavigationNode>>
    {
        private NavigationNode currentParent;


        public CachedNodesIterator()
        {
            this(getUndecoratedTreeModel().getRoot());
        }


        public CachedNodesIterator(final NavigationNode currentParent)
        {
            this.currentParent = currentParent;
        }


        @Override
        public boolean hasNext()
        {
            return currentParent != null;
        }


        @Override
        public Collection<NavigationNode> next()
        {
            final TreeModel<NavigationNode> undecoratedTreeModel = getUndecoratedTreeModel();
            final int childrenCount = undecoratedTreeModel.getChildCount(currentParent);
            if(currentParent != null && childrenCount > 0)
            {
                final List<NavigationNode> children = IntStream.range(0, childrenCount)
                                .mapToObj(index -> undecoratedTreeModel.getChild(currentParent, index)).filter(Objects::nonNull)
                                .collect(Collectors.toList());
                currentParent = lookupNextNodeParent();
                return children;
            }
            else if(childrenCount == 0)
            {
                currentParent = lookupNextNodeParent();
                return Collections.emptyList();
            }
            else
            {
                throw new NoSuchElementException();
            }
        }


        protected NavigationNode lookupNextNodeParent()
        {
            return lookupNextNodeParent(currentParent);
        }


        protected NavigationNode lookupNextNodeParent(final NavigationNode node)
        {
            if(node == null)
            {
                throw new NoSuchElementException();
            }
            else if(!TreeUtils.isEligibleForMatching(node) || !isIncludedInTreeModel(node))
            {
                return lookupNextNodeParentInOtherBranch(node);
            }
            final NavigationNode child = getFirstChild(node);
            if(child != null)
            {
                return child;
            }
            else
            {
                return lookupNextNodeParentInOtherBranch(node);
            }
        }


        protected boolean isIncludedInTreeModel(final NavigationNode node)
        {
            final int[] path = getUndecoratedTreeModel().getPath(node);
            return node == getUndecoratedTreeModel().getRoot() || (path != null && path.length > 0);
        }


        protected NavigationNode getFirstChild(final NavigationNode node)
        {
            if(getUndecoratedTreeModel().getChildCount(currentParent) > 0)
            {
                return getUndecoratedTreeModel().getChild(currentParent, 0);
            }
            else
            {
                return null;
            }
        }


        protected NavigationNode lookupNextNodeParentInOtherBranch(final NavigationNode node)
        {
            if(node != null)
            {
                final NavigationNode sibling = getNextSibling(node);
                return sibling != null ? sibling : lookupNextNodeParentInOtherBranch(node.getParent());
            }
            return null;
        }


        protected NavigationNode getNextSibling(final NavigationNode node)
        {
            final int[] path = getUndecoratedTreeModel().getPath(node);
            if(path != null && path.length > 0)
            {
                final int currentNodeIndex = nodeInstanceIndex(node);
                final int childCount = getUndecoratedTreeModel().getChildCount(node.getParent());
                if(currentNodeIndex > -1 && currentNodeIndex < childCount - 1)
                {
                    return getUndecoratedTreeModel().getChild(node.getParent(), currentNodeIndex + 1);
                }
            }
            return null;
        }


        protected int nodeInstanceIndex(final NavigationNode node)
        {
            int index = 0;
            for(final NavigationNode n : node.getParent().getChildren())
            {
                if(node == n)
                {
                    return index;
                }
                index++;
            }
            return -1;
        }
    }


    /**
     * See {@link com.hybris.cockpitng.engine.WidgetInstanceManager#getLabel(String, Object[])}.
     *
     * @param key
     *           localized label key.
     */
    @Override
    public String getLabel(final String key)
    {
        return this.getWidgetInstanceManager().getLabel(key);
    }


    /**
     * @return the labelService
     */
    public LabelService getLabelService()
    {
        return labelService;
    }


    /**
     * @param labelService
     *           the labelService to set
     */
    public void setLabelService(final LabelService labelService)
    {
        this.labelService = labelService;
    }


    public PermissionFacade getPermissionFacade()
    {
        return permissionFacade;
    }


    public void setPermissionFacade(final PermissionFacade permissionFacade)
    {
        this.permissionFacade = permissionFacade;
    }


    public TypeFacade getTypeFacade()
    {
        return typeFacade;
    }


    public void setTypeFacade(final TypeFacade typeFacade)
    {
        this.typeFacade = typeFacade;
    }


    public ObjectFacade getObjectFacade()
    {
        return objectFacade;
    }


    public void setObjectFacade(final ObjectFacade objectFacade)
    {
        this.objectFacade = objectFacade;
    }


    public Tree getExplorerTree()
    {
        return this.explorerTree;
    }


    public NavigationTreeFactory getNavigationTreeFactory()
    {
        return navigationTreeFactory;
    }


    public Div getFilterDiv()
    {
        return filterDiv;
    }


    public Vlayout getMainCnt()
    {
        return mainCnt;
    }


    public Bandbox getFilterTextbox()
    {
        return filterTextbox;
    }


    public Checkbox getSearchMode()
    {
        return searchMode;
    }


    public Div getToolbarContainer()
    {
        return toolbarContainer;
    }


    public Actions getPrimaryActions()
    {
        return primaryActions;
    }


    public Actions getSecondaryActions()
    {
        return secondaryActions;
    }


    public Widgetslot getToolbarSlot()
    {
        return toolbarSlot;
    }


    public void setTreeModel(final AbstractTreeModel<NavigationNode> treeModel)
    {
        this.treeModel = treeModel;
    }


    protected NotificationService getNotificationService()
    {
        return notificationService;
    }


    public void setNotificationService(final NotificationService notificationService)
    {
        this.notificationService = notificationService;
    }


    protected CockpitProperties getCockpitProperties()
    {
        return cockpitProperties;
    }


    public void setCockpitProperties(final CockpitProperties cockpitProperties)
    {
        this.cockpitProperties = cockpitProperties;
    }


    protected Comparator<NavigationNode> getNodeComparator()
    {
        return nodeComparator;
    }


    /**
     * @deprecated since 2005. This class is not used anymore
     */
    @Deprecated(since = "2005", forRemoval = true)
    protected static class NodesRefreshData
    {
        private final Collection<Object> nodesToOpen;
        private final Collection<Object> nodesToScroll;


        public NodesRefreshData(final Collection<Object> nodesToOpen, final Collection<Object> nodesToScroll)
        {
            this.nodesToOpen = nodesToOpen;
            this.nodesToScroll = nodesToScroll;
        }


        public Collection<Object> getNodesToOpen()
        {
            return nodesToOpen;
        }


        public Collection<Object> getNodesToScroll()
        {
            return nodesToScroll;
        }
    }
}
