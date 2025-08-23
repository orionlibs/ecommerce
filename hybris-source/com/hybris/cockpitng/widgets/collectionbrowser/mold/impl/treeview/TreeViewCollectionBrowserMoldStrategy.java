/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.collectionbrowser.mold.impl.treeview;

import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import com.hybris.cockpitng.core.context.impl.DefaultCockpitContext;
import com.hybris.cockpitng.core.events.CockpitEvent;
import com.hybris.cockpitng.core.model.WidgetModel;
import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import com.hybris.cockpitng.type.ObjectValueService;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.util.YTestTools;
import com.hybris.cockpitng.widgets.collectionbrowser.CollectionBrowserController;
import com.hybris.cockpitng.widgets.collectionbrowser.mold.impl.SinglePage;
import com.hybris.cockpitng.widgets.collectionbrowser.mold.impl.common.AbstractMoldStrategy;
import com.hybris.cockpitng.widgets.common.NotifyingWidgetComponentRenderer;
import com.hybris.cockpitng.widgets.common.WidgetComponentRenderer;
import com.hybris.cockpitng.widgets.common.WidgetComponentRendererListener;
import com.hybris.cockpitng.widgets.navigation.NavigationItemSelectorContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zul.Tree;
import org.zkoss.zul.TreeModel;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;
import org.zkoss.zul.ext.Selectable;

/**
 * Tree view mold strategy for Collection Browser widget
 *
 * Renders data in tree form with paging by creating children nodes from references<br>
 * Uses zk {@link Tree} component
 */
public class TreeViewCollectionBrowserMoldStrategy extends AbstractMoldStrategy<Treeitem, Void, TreeViewCollectionBrowserNode>
{
    public static final String MODEL_TREE = "treeModel";
    public static final String TOOLTIP_PROPERTY_KEY = "mold.treeview.tooltip";
    public static final String SCLASS_TREE_TABLE = "yw-coll-browser-tree-view-table";
    public static final String SCLASS_TREE_TABLE_EMPTY = "yw-coll-browser-tree-view-table-empty";
    protected static final String COMPONENT_ATTRIBUTE_DATA = "data";
    private static final String SETTING_ITEM_RENDERER = "treeItemRenderer";
    private static final Logger LOG = LoggerFactory.getLogger(TreeViewCollectionBrowserMoldStrategy.class);
    public static final String MODEL_OPEN_OBJECTS = "openObjects";
    protected WidgetComponentRenderer<Treeitem, Void, TreeViewCollectionBrowserNode> renderer;
    protected Component parent;
    protected TypeFacade typeFacade;
    protected ObjectValueService objectValueService;
    protected Tree tree;
    private int order;
    private String typeCode;


    @Override
    public void render(final Component parent, final SinglePage singlePage)
    {
        this.parent = parent;
        initialize();
        parent.appendChild(tree);
        setPage(singlePage);
    }


    protected Component getParent()
    {
        return parent;
    }


    @Override
    public void reset()
    {
        resetModel();
        resetView();
    }


    protected void resetModel()
    {
        getWidgetModel().setValue(MODEL_TREE, null);
        getWidgetModel().setValue(MODEL_OPEN_OBJECTS, null);
    }


    protected void resetView()
    {
        if(tree != null)
        {
            tree.setModel(null);
        }
    }


    protected void initialize()
    {
        if(tree == null)
        {
            tree = new Tree();
        }
        YTestTools.modifyYTestId(tree, String.format("mainTree-%s", getName()));
        tree.setVflex(true);
        tree.setSclass(SCLASS_TREE_TABLE);
        tree.addEventListener(Events.ON_SELECT, this::onSelectEvent);
        tree.setNonselectableTags("*");
    }


    /**
     * Callback method for Tree {@link Events#ON_SELECT} event.
     *
     * @param event
     *           {@link org.zkoss.zk.ui.event.SelectEvent}
     */
    protected void onSelectEvent(final Event event)
    {
        final List<Object> selectedItems = new ArrayList<>(getContext().getSelectedItems());
        final Set<AbstractTreeViewNode> selectedObjects = ((SelectEvent)event).getSelectedObjects();
        final Set<AbstractTreeViewNode> unselectedObjects = ((SelectEvent)event).getUnselectedObjects();
        selectedObjects.stream().map(AbstractTreeViewNode::getData).filter(data -> data != null && !selectedItems.contains(data))
                        .forEach(selectedItems::add);
        unselectedObjects.stream().map(AbstractTreeViewNode::getData).forEach(selectedItems::remove);
        getContext().notifyItemsSelected(selectedItems);
    }


    @Override
    public void release()
    {
        if(getParent() != null)
        {
            getParent().getChildren().remove(tree);
            tree = null;
        }
        getWidgetModel().setValue(MODEL_TREE, null);
    }


    @Override
    public void setPage(final SinglePage singlePage)
    {
        Validate.notNull("Page may not be null", singlePage);
        final List<?> list = getPermissionFacade().canReadType(singlePage.getTypeCode()) ? singlePage.getList()
                        : Lists.newArrayList();
        tree.setModel(null);
        final TreeViewCollectionBrowserTreeModel<? extends TreeViewCollectionBrowserNode> model = createTreeModel(
                        (List<Object>)list, singlePage);
        getWidgetModel().setValue(MODEL_TREE, model);
        setTreeRenderer();
        setTreeStyle(list);
        model.setMultiple(getContext().isMultiSelectEnabled());
        tree.setCheckmark(getContext().isMultiSelectEnabled());
        tree.setModel(model);
        restoreSelection();
        restoreOpenNodes();
    }


    protected void restoreSelection()
    {
        selectItems(new LinkedHashSet<>(getContext().getSelectedItems()));
    }


    protected void restoreOpenNodes()
    {
        final TreeViewCollectionBrowserTreeModel treeModel = (TreeViewCollectionBrowserTreeModel)getModel();
        final Collection openObjects = getWidgetModel().getValue(MODEL_OPEN_OBJECTS, Collection.class);
        if(openObjects != null)
        {
            openObjects.stream().forEach(object -> treeModel.addOpenObject(object));
        }
    }


    @Override
    public boolean isHandlingObjectEvents(final String typeCode)
    {
        return true;
    }


    @Override
    protected void handleCollectionUpdate(final Collection<Object> collection)
    {
        final TreeViewCollectionBrowserTreeModel<TreeViewCollectionBrowserNode> treeModel = (TreeViewCollectionBrowserTreeModel)tree
                        .getModel();
        if(treeModel != null)
        {
            for(final Object item : collection)
            {
                updateModelData(treeModel, item);
            }
        }
    }


    protected void updateModelData(final TreeViewCollectionBrowserTreeModel<TreeViewCollectionBrowserNode> treeModel,
                    final Object data)
    {
        final List<TreeViewCollectionBrowserNode> topLevelNodes = treeModel.getTopLevelNodes();
        updateAllCachedNodesData(topLevelNodes, data);
        tree.setModel(treeModel);
    }


    protected void updateAllCachedNodesData(final List<TreeViewCollectionBrowserNode> topLevelNodes, final Object data)
    {
        for(final TreeViewCollectionBrowserNode node : topLevelNodes)
        {
            updateAllCachedNodesData(node.getCachedChildren(), data);
        }
        updateNodesData(topLevelNodes, data);
    }


    protected void updateNodesData(final List<TreeViewCollectionBrowserNode> nodes, final Object data)
    {
        for(int i = 0; i < nodes.size(); i++)
        {
            final TreeViewCollectionBrowserNode node = nodes.get(i);
            if(Objects.equals(data, node.getData()))
            {
                final TreeViewCollectionBrowserHelper helper = new TreeViewCollectionBrowserHelper(
                                getContext().getWidgetInstanceManager(), getTypeFacade(), getPermissionFacade(), getObjectValueService());
                final TreeViewCollectionBrowserNode newNode = new TreeViewCollectionBrowserNode(helper, data, node.isAccessible(),
                                node.getParent());
                nodes.set(i, newNode);
            }
        }
    }


    @Override
    public void selectItems(final Set<?> items)
    {
        if(CollectionUtils.isEmpty(items))
        {
            deselectItems();
        }
        else
        {
            final Object model = getModel();
            if(model instanceof TreeViewCollectionBrowserTreeModel)
            {
                final TreeViewCollectionBrowserTreeModel treeModel = (TreeViewCollectionBrowserTreeModel)model;
                treeModel.setDataSelection(items);
            }
            else if(model instanceof Selectable)
            {
                items.stream().findFirst().ifPresent(item -> {
                    if(item instanceof AbstractTreeViewNode)
                    {
                        ((Selectable<?>)model).setSelection((Collection)items);
                    }
                });
            }
        }
    }


    @Override
    public void deselectItems()
    {
        if(tree != null)
        {
            final TreeModel<?> treeModel = tree.getModel();
            if(treeModel instanceof Selectable)
            {
                ((Selectable<?>)treeModel).clearSelection();
            }
        }
    }


    @Override
    public void focusItem(final Object oldFocus, final Object newFocus)
    {
        if(tree != null)
        {
            final Component oldItem = getComponent(oldFocus);
            setItemFocused(oldItem, false);
            final Component newItem = getComponent(newFocus);
            setItemFocused(newItem, true);
        }
    }


    protected void setItemFocused(final Component item, final boolean focused)
    {
        if(item instanceof HtmlBasedComponent)
        {
            UITools.modifySClass((HtmlBasedComponent)item, SCLASS_CELL_FOCUSED, focused);
        }
    }


    protected Component getComponent(final Object item)
    {
        final Object model = getModel();
        if(model instanceof TreeViewCollectionBrowserTreeModel)
        {
            final TreeViewCollectionBrowserNode node = ((TreeViewCollectionBrowserTreeModel)model).getNode(item);
            if(node != null && tree.getTreechildren() != null)
            {
                final int[] path = ((TreeViewCollectionBrowserTreeModel)model).getPath(node);
                final Treeitem treeitem = getItem(tree.getTreechildren(),
                                ((TreeViewCollectionBrowserTreeModel)model).getTopLevelNodes(), path);
                if(treeitem != null)
                {
                    return treeitem.getTreerow();
                }
            }
        }
        return null;
    }


    protected Treeitem getItem(final Treechildren treechildren, final Collection<TreeViewCollectionBrowserNode> nodes,
                    final int[] modelPath)
    {
        if(modelPath.length > 0)
        {
            final int index = modelPath[0];
            final Optional<TreeViewCollectionBrowserNode> nodeFromPath = nodes.stream().skip(index).findFirst();
            final Optional<Treeitem> itemForNode = nodeFromPath.flatMap(node -> treechildren.getItems().stream()
                            .filter(childItem -> Objects.equals(childItem.getAttribute(COMPONENT_ATTRIBUTE_DATA), node)).findFirst());
            if(itemForNode.isPresent())
            {
                if(modelPath.length > 1 && nodeFromPath.isPresent())
                {
                    return getItem(itemForNode.get().getTreechildren(), nodeFromPath.get().getChildren(),
                                    ArrayUtils.remove(modelPath, 0));
                }
                else
                {
                    return itemForNode.get();
                }
            }
        }
        return null;
    }


    /**
     * Callback method for Treeitem {@link Events#ON_CLICK} event.
     *
     * @param event
     *           {@link org.zkoss.zk.ui.event.MouseEvent}
     */
    protected void onTreeitemClickEvent(final Event event)
    {
        final AbstractTreeViewNode node = (AbstractTreeViewNode)event.getTarget().getAttribute(COMPONENT_ATTRIBUTE_DATA);
        if(isNodeClickable(event.getTarget(), node))
        {
            getContext().notifyItemClicked(node.getData());
        }
    }


    protected boolean isNodeClickable(final Component nodeComponent, final AbstractTreeViewNode node)
    {
        return node != null && isNodeDataTypeClickable(node)
                        && (!getContext().areHyperlinksSupported() || !isLink(nodeComponent));
    }


    private boolean isNodeDataTypeClickable(final AbstractTreeViewNode node)
    {
        return !(node.getData() instanceof DataAttribute) && !(node.getData() instanceof Locale);
    }


    @Override
    public void handleObjectCreateEvent(final CockpitEvent event)
    {
        // NOOP
    }


    @Override
    public void handleObjectDeleteEvent(final CockpitEvent event)
    {
        // NOOP
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


    protected Object getEditableItemFromSelectedItem(final Object selectedItem)
    {
        Object editableItem = selectedItem;
        if(editableItem instanceof TreeViewCollectionBrowserNode)
        {
            editableItem = ((TreeViewCollectionBrowserNode)editableItem).getData();
        }
        try
        {
            if(editableItem != null && isItemAtomic(editableItem))
            {
                editableItem = null;
            }
        }
        catch(final TypeNotFoundException e)
        {
            LOG.debug(e.getLocalizedMessage(), e);
            editableItem = null;
        }
        return editableItem;
    }


    protected boolean isItemAtomic(final Object item) throws TypeNotFoundException
    {
        final String type = getTypeFacade().getType(item);
        final DataType dataType = getTypeFacade().load(type);
        return dataType.isAtomic();
    }


    /**
     * @deprecated since 2005
     * {@link TreeViewCollectionBrowserMoldStrategy#createTreeModel(List, SinglePage)} instead
     */
    @Deprecated(since = "2005", forRemoval = true)
    protected TreeViewCollectionBrowserTreeModel<? extends TreeViewCollectionBrowserNode> createTreeModel(
                    final List<Object> currentPage)
    {
        if(CollectionUtils.isNotEmpty(currentPage))
        {
            return new TreeViewCollectionBrowserTreeModel<>(createNodesFromData(currentPage));
        }
        else
        {
            return new TreeViewCollectionBrowserTreeModel<>(createNoEntriesNode());
        }
    }


    protected TreeViewCollectionBrowserTreeModel<? extends TreeViewCollectionBrowserNode> createTreeModel(
                    final List<Object> currentPage, final SinglePage singlePage)
    {
        if(CollectionUtils.isNotEmpty(currentPage))
        {
            return new TreeViewCollectionBrowserTreeModel<>(createNodesFromData(currentPage));
        }
        else
        {
            return new TreeViewCollectionBrowserTreeModel<>(createNodeWithLabel(chooseEmptyMessageToDisplayFor(singlePage)));
        }
    }


    protected List<TreeViewCollectionBrowserNode> createNodesFromData(final List<Object> data)
    {
        final TreeViewCollectionBrowserHelper helper = new TreeViewCollectionBrowserHelper(getContext().getWidgetInstanceManager(),
                        getTypeFacade(), getPermissionFacade(), getObjectValueService());
        return data.stream().map(object -> new TreeViewCollectionBrowserNode(helper, object, helper.hasReadAccess(object, null)))
                        .collect(Collectors.toList());
    }


    /**
     * @deprecated since 2005
     * {@link TreeViewCollectionBrowserMoldStrategy#createNodeWithLabel(String)} instead
     */
    @Deprecated(since = "2005", forRemoval = true)
    protected List<TreeViewCollectionBrowserNode> createNoEntriesNode()
    {
        final TreeViewCollectionBrowserHelper helper = new TreeViewCollectionBrowserHelper(getContext().getWidgetInstanceManager(),
                        getTypeFacade(), getPermissionFacade(), getObjectValueService());
        return Lists.newArrayList(new TreeViewCollectionBrowserNode(helper, KEY_LABEL_EMPTY_LIST, true));
    }


    protected List<TreeViewCollectionBrowserNode> createNodeWithLabel(final String label)
    {
        final TreeViewCollectionBrowserHelper helper = new TreeViewCollectionBrowserHelper(getContext().getWidgetInstanceManager(),
                        getTypeFacade(), getPermissionFacade(), getObjectValueService());
        return Lists.newArrayList(new TreeViewCollectionBrowserNode(helper, label, true));
    }


    protected void setTreeRenderer()
    {
        tree.setItemRenderer((item, node, index) -> {
            final TreeViewCollectionBrowserNode treeNode = (TreeViewCollectionBrowserNode)node;
            item.addEventListener(Events.ON_OPEN, this::onTreeitemOnOpenEvent);
            DataType dataType = null;
            if(!(treeNode.getData() instanceof DataAttribute))
            {
                dataType = resolveDataType(treeNode.getData());
            }
            getRenderer().render(item, null, treeNode, dataType, getContext().getWidgetInstanceManager());
            item.setAttribute(COMPONENT_ATTRIBUTE_DATA, treeNode);
            item.addEventListener(Events.ON_CLICK, this::onTreeitemClickEvent);
            setItemFocused(item.getTreerow(), Objects.equals(treeNode.getData(), getContext().getFocusedItem()));
            final DefaultCockpitContext context = new DefaultCockpitContext();
            final Map inputContext = getWidgetModel().getValue(CollectionBrowserController.MODEL_SELECTION_CONTEXT, Map.class);
            if(inputContext != null)
            {
                context.setParameters(inputContext);
            }
            if(getContext().getDragAndDropStrategy() != null && item.getTreerow() != null)
            {
                if(getContext().isDragEnabled())
                {
                    getContext().getDragAndDropStrategy().makeDraggable(item.getTreerow(), treeNode.getData(), context);
                }
                if(getContext().isDropEnabled())
                {
                    getContext().getDragAndDropStrategy().makeDroppable(item.getTreerow(), treeNode.getData(), context);
                }
            }
        });
    }


    protected void onTreeitemOnOpenEvent(final Event openEvent)
    {
        TreeViewCollectionBrowserNode treeNode = getTreeNodeByEvent(openEvent);
        if(treeNode == null)
        {
            return;
        }
        Collection openObjects = getWidgetModel().getValue(MODEL_OPEN_OBJECTS, Collection.class);
        Set newOpenedObjects;
        if(openObjects == null)
        {
            newOpenedObjects = new HashSet();
            newOpenedObjects.add(treeNode);
            handleDefaultOpenTreeNode(newOpenedObjects, treeNode);
        }
        else
        {
            newOpenedObjects = (Set<Object>)openObjects.stream().filter(node -> {
                if(node instanceof TreeViewCollectionBrowserNode)
                {
                    TreeViewCollectionBrowserNode openedNode = (TreeViewCollectionBrowserNode)node;
                    return !(openedNode.getData().equals(treeNode.getData()));
                }
                return true;
            }).collect(Collectors.toSet());
            if(newOpenedObjects.size() == openObjects.size())
            {
                newOpenedObjects.add(treeNode);
                handleDefaultOpenTreeNode(newOpenedObjects, treeNode);
            }
        }
        getWidgetModel().setValue(MODEL_OPEN_OBJECTS, newOpenedObjects);
    }


    private void handleDefaultOpenTreeNode(Set openNodes, TreeViewCollectionBrowserNode treeNode)
    {
        if(treeNode.getChildren() != null && treeNode.getChildren().size() == 1)
        {
            openNodes.add(treeNode.getChildren().get(0));
            handleDefaultOpenTreeNode(openNodes, treeNode.getChildren().get(0));
        }
    }


    private TreeViewCollectionBrowserNode getTreeNodeByEvent(final Event event)
    {
        final TreeViewCollectionBrowserTreeModel model = (TreeViewCollectionBrowserTreeModel)getModel();
        final Component component = event.getTarget();
        if(component instanceof Treeitem)
        {
            final Treeitem treeitem = (Treeitem)component;
            final List<Integer> path = new ArrayList<>(treeitem.getLevel() + 1);
            Treeitem currentTreeitem = treeitem;
            while(currentTreeitem.getParentItem() != null)
            {
                path.add(currentTreeitem.getIndex());
                currentTreeitem = currentTreeitem.getParentItem();
            }
            path.add(currentTreeitem.getIndex());
            return model.getChild(Ints.toArray(Lists.reverse(path)));
        }
        return null;
    }


    @Override
    protected String getRendererSetting()
    {
        return SETTING_ITEM_RENDERER;
    }


    @Override
    protected void initializeRenderer(
                    final NotifyingWidgetComponentRenderer<Treeitem, Void, TreeViewCollectionBrowserNode> renderer)
    {
        super.initializeRenderer(renderer);
        if(getContext().areHyperlinksSupported())
        {
            renderer.addRendererListener(createLinkRenderedListener());
        }
        else
        {
            renderer.addRendererListener(createNodeRenderedListener());
        }
    }


    protected WidgetComponentRendererListener<Treeitem, Void, TreeViewCollectionBrowserNode> createLinkRenderedListener()
    {
        return event -> {
            if(event.isFinal() && getEditableItemFromSelectedItem(event.getData()) != null)
            {
                addLink((HtmlBasedComponent)event.getSource(), event.getData());
            }
        };
    }


    protected WidgetComponentRendererListener<Treeitem, Void, TreeViewCollectionBrowserNode> createNodeRenderedListener()
    {
        return event -> {
            if(Treerow.class.isInstance(event.getSource()) && getEditableItemFromSelectedItem(event.getData()) != null)
            {
                UITools.modifySClass((HtmlBasedComponent)event.getSource(), SCLASS_CELL_HYPERLINK, true);
            }
        };
    }


    protected void addLink(final HtmlBasedComponent component, final TreeViewCollectionBrowserNode node)
    {
        addLink(component, node::getData);
    }


    protected DataType resolveDataType(final Object object)
    {
        DataType dataType = null;
        final String type = getTypeFacade().getType(object);
        try
        {
            dataType = getTypeFacade().load(type);
        }
        catch(final TypeNotFoundException ignore)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug(ignore.getMessage(), ignore);
            }
        }
        return dataType;
    }


    protected void setTreeStyle(final List<?> currentPage)
    {
        UITools.modifySClass(tree, SCLASS_TREE_TABLE_EMPTY, CollectionUtils.isEmpty(currentPage));
    }


    protected TypeFacade getTypeFacade()
    {
        return typeFacade;
    }


    public void setTypeFacade(final TypeFacade typeFacade)
    {
        this.typeFacade = typeFacade;
    }


    protected ObjectValueService getObjectValueService()
    {
        return objectValueService;
    }


    public void setObjectValueService(final ObjectValueService objectValueService)
    {
        this.objectValueService = objectValueService;
    }


    protected Object getModel()
    {
        return tree.getModel();
    }


    @Override
    public String getName()
    {
        return "tree-view";
    }


    @Override
    public String getTypeCode()
    {
        return typeCode;
    }


    @Override
    public void setTypeCode(final String typeCode)
    {
        this.typeCode = typeCode;
    }


    @Override
    public String getTooltipText()
    {
        return getContext().getWidgetInstanceManager().getLabel(TOOLTIP_PROPERTY_KEY);
    }


    @Override
    public NavigationItemSelectorContext getNavigationItemSelectorContext()
    {
        return EMPTY_NAVIGATION_ITEM_SELECTOR_CONTEXT;
    }


    @Override
    public int getOrder()
    {
        return order;
    }


    @Required
    @Override
    public void setOrder(final int order)
    {
        this.order = order;
    }


    @Override
    protected WidgetModel getWidgetModel()
    {
        return getContext().getWidgetInstanceManager().getModel();
    }
}
