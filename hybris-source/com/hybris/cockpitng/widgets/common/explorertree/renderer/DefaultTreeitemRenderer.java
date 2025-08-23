/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.common.explorertree.renderer;

import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.backoffice.navigation.NavigationNodeDecoratorUtils;
import com.hybris.backoffice.tree.model.UncategorizedNode;
import com.hybris.cockpitng.components.Actions;
import com.hybris.cockpitng.core.context.impl.DefaultCockpitContext;
import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.dnd.DragAndDropStrategy;
import com.hybris.cockpitng.testing.annotation.InextensibleMethod;
import com.hybris.cockpitng.tree.node.TypeNode;
import com.hybris.cockpitng.util.BackofficeSpringUtil;
import com.hybris.cockpitng.util.ClickTrackingTools;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.util.YTestTools;
import com.hybris.cockpitng.widgets.common.explorertree.ExplorerTreeController;
import com.hybris.cockpitng.widgets.common.explorertree.NavigationNodeLabelProvider;
import com.hybris.cockpitng.widgets.common.explorertree.data.ActionAwareNode;
import com.hybris.cockpitng.widgets.common.explorertree.data.PartitionNodeData;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.OpenEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.AbstractTreeModel;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;
import org.zkoss.zul.Treerow;

/**
 * Default renderer for the explorer tree widget.
 */
public class DefaultTreeitemRenderer implements TreeitemRenderer<NavigationNode>
{
    private static final String SCLASS_YW_TREE_NODE_SCLASS = "yw-navigationNode-level";
    private static final String SCLASS_YW_OPENED_SCLASS = "yw-opened";
    private static final String SCLASS_YW_HIGHLIGHTED_SCLASS = "yw-highlighted";
    private static final String SCLASS_YW_SELECTED_SCLASS = "yw-selected";
    private static final String SCLASS_YW_NAVIGATION_NODE_CANNOT_ACCESS = "yw-navigationNode-cannot-access";
    private static final String SCLASS_YW_EXPLORER_TREE_INLINE_ACTIONS = "yw-explorerTree-inlineActions";
    private static final String CLIENT_EVENT_ON_HIDE = "onClientHide";
    private final ExplorerTreeController controller;


    public DefaultTreeitemRenderer(final ExplorerTreeController controller)
    {
        Validate.notNull(String.format("%s requires non-null ExplorerTreeController", DefaultTreeitemRenderer.class.getName()));
        this.controller = controller;
    }


    private AbstractTreeModel<NavigationNode> getTreeModel()
    {
        return getController().getTreeModel();
    }


    @Override
    public void render(final Treeitem treeItem, final NavigationNode navNode, final int index)
    {
        YTestTools.modifyYTestId(treeItem, "TreeTest" + navNode.getName());
        treeItem.setValue(navNode);
        final Treerow treeRow = new Treerow();
        treeRow.setParent(treeItem);
        ClickTrackingTools.modifyClickTrackingId(treeRow, navNode.getNameLocKey());
        final DragAndDropStrategy dragAndDropStrategy = getController().getDragAndDropStrategy();
        if(dragAndDropStrategy != null)
        {
            final DefaultCockpitContext context = new DefaultCockpitContext();
            if(navNode.getParent() != null)
            {
                if(navNode.getParent().getData() instanceof PartitionNodeData)
                {
                    context.setParameter("parentObject", ((PartitionNodeData)navNode.getParent().getData()).getParent());
                }
                else
                {
                    context.setParameter("parentObject", navNode.getParent().getData());
                }
            }
            dragAndDropStrategy.makeDroppable(treeRow, navNode.getData(), context);
            dragAndDropStrategy.makeDraggable(treeRow, navNode.getData(), context);
        }
        final Treecell treeCell = new Treecell();
        treeCell.setParent(treeRow);
        // browser side can not correctly handle the style class if it contains dot.
        final String nodeIdWithoutDot = navNode.getId().replace(".", "_");
        UITools.modifySClass(treeCell, "y-tree-icon-" + nodeIdWithoutDot, true);
        this.applyCss(treeItem, treeItem.getTree().getModel().getPath(navNode));
        final String label = getNavigationNodeLabelProvider().getLabel(navNode);
        final Label treeCellLabel = new Label(label);
        YTestTools.modifyYTestId(treeCellLabel, navNode.getId());
        final Div treeCellInnerCnt = new Div();
        treeCellInnerCnt.setSclass("yw-treeCellInner");
        final Div iconDiv = new Div();
        iconDiv.setParent(treeCellInnerCnt);
        iconDiv.setSclass("y-tree-icon y-tree-icon-" + nodeIdWithoutDot);
        for(int i = 0; i < treeItem.getLevel(); i++)
        {
            final Div spacerTreecell = new Div();
            spacerTreecell.setSclass("yw-spacerCell yw-spacer" + i);
            treeCellInnerCnt.appendChild(spacerTreecell);
        }
        treeCellInnerCnt.appendChild(treeCellLabel);
        actionsHandling(treeCell, treeCellInnerCnt, navNode);
        treeCell.appendChild(treeCellInnerCnt);
        if(navNode.getParent() != null)
        {
            final List<NavigationNode> children = navNode.getParent().getChildren();
            if(CollectionUtils.isNotEmpty(children) && navNode.equals(children.get(children.size() - 1)))
            {
                UITools.modifySClass(treeRow, "yw-navigationnode-last", true);
            }
        }
        if(index == 0)
        {
            UITools.modifySClass(treeRow, "yw-navigationnode-first", true);
        }
        if(navNode instanceof TypeNode && !getController().hasAccessToTheNode(navNode))
        {
            UITools.modifySClass(treeRow, "yw-no-permission-label", true);
        }
        final Div wrapper = new Div();
        wrapper.setSclass("explorer-tree-node-gradient-parent");
        final Div gradientDiv = new Div();
        gradientDiv.setSclass("explorer-tree-node-gradient");
        wrapper.appendChild(gradientDiv);
        wrapper.setParent(treeCell);
        treeItem.setTooltiptext(label);
        treeItem.addEventListener(Events.ON_OPEN, event -> delegateOpening(treeItem, treeRow, event));
        treeItem.addEventListener(Events.ON_CLICK, this::delegateSelecting);
        treeItem.addEventListener(Events.ON_OK, this::delegateSelecting);
    }


    protected void actionsHandling(final Treecell treecell, final Div treeCellInnerCnt, final NavigationNode navNode)
    {
        treecell.addEventListener(Events.ON_MOUSE_OVER, e -> {
            if(isActionAware(navNode))
            {
                handleMouseOver(treecell, treeCellInnerCnt, navNode);
            }
        });
        treecell.addEventListener(Events.ON_MOUSE_OUT, e -> {
            if(getActionsAttribute(treecell) != null)
            {
                getActionsAttribute(treecell).setVisible(false);
            }
        });
    }


    private boolean isActionAware(final Object navigationNode)
    {
        return Optional.of(navigationNode).filter(ActionAwareNode.class::isInstance).map(ActionAwareNode.class::cast)
                        .map(ActionAwareNode::isActionAware).orElse(true)
                        && Optional.of(navigationNode).filter(NavigationNode.class::isInstance).map(NavigationNode.class::cast)
                        .map(NavigationNode::getData).map(this::isActionAware).orElse(true);
    }


    protected void handleMouseOver(final Treecell treecell, final Div treeCellInnerCnt, final NavigationNode navNode)
    {
        final Object data = navNode.getData();
        if(!isInCurrentActionModel(data))
        {
            getController().getWidgetInstanceManager().getModel().setValue("explorerTreeItem", data);
            getController().getWidgetInstanceManager().getModel().setValue("explorerTreeItems", Lists.newArrayList(data));
        }
        if(getActionsAttribute(treecell) != null)
        {
            final Actions actions = getActionsAttribute(treecell);
            actions.setVisible(true);
            addClientHideEvent(actions);
            return;
        }
        final String actionsComponent = getController().getWidgetSettings()
                        .getString(ExplorerTreeController.SETTING_ACTIONS_COMPONENT);
        if(StringUtils.isEmpty(actionsComponent) || navNode instanceof UncategorizedNode)
        {
            return;
        }
        final Actions actions = createActions(data, actionsComponent);
        if(actions.getConfiguration() == null)
        {
            return;
        }
        treeCellInnerCnt.appendChild(actions);
        treecell.setAttribute("actions", actions);
    }


    private static Actions getActionsAttribute(final Treecell treecell)
    {
        return (Actions)treecell.getAttribute("actions");
    }


    protected void addClientHideEvent(final Actions actions)
    {
        final String uuid = actions.getUuid();
        final String jsLine1 = String.format("var actionsObserver = new MutationObserver(function(mutations) {" + //
                                        "mutations.forEach(function(mutationRecord) {" + //
                                        "zAu.send(new zk.Event(zk.Widget.$('#%s'), '%s', null, {toServer:true}));" + //
                                        "});});", //
                        uuid, CLIENT_EVENT_ON_HIDE); //
        final String jsLine2 = String.format("var target = document.getElementById('%s');", uuid);
        final String jsLine3 = "var documentObserver = new MutationObserver(function(mutations, observer) {" +
                        jsLine2 +
                        "if (target != null) {actionsObserver.observe(target, { attributes : true, attributeFilter : ['style'] }); observer.disconnect();}});";
        final String jsLine4 = "documentObserver.observe(document, { childList : true, subtree : true});";
        Clients.evalJavaScript("if (typeof MutationObserver != 'undefined') {" + jsLine1 + jsLine3 + jsLine4 + "}");
    }


    private boolean isInCurrentActionModel(final Object data)
    {
        return Objects.equals(getController().getWidgetInstanceManager().getModel().getValue("explorerTreeItem", Object.class),
                        data);
    }


    private void closePopup(final List<Component> children)
    {
        if(CollectionUtils.isEmpty(children))
        {
            return;
        }
        final Optional<Popup> popupOptional = children.stream().filter(Popup.class::isInstance).map(Popup.class::cast).findAny();
        if(popupOptional.isPresent())
        {
            popupOptional.get().close();
            return;
        }
        children.stream().map(Component::getChildren).forEach(this::closePopup);
    }


    protected Actions createActions(final Object data, final String actionsComponent)
    {
        final Actions actions = new Actions();
        actions.setGroup("common");
        actions.setWidgetInstanceManager(getController().getWidgetInstanceManager());
        actions.setConfig(String.format("component=%s,type=%s", actionsComponent, getController().getTypeFacade().getType(data)));
        UITools.addSClass(actions, SCLASS_YW_EXPLORER_TREE_INLINE_ACTIONS);
        actions.reload();
        actions.addEventListener(CLIENT_EVENT_ON_HIDE, e -> closePopup(actions.getChildren()));
        return actions;
    }


    /**
     * @deprecated since 2005, do not use
     */
    @Deprecated(since = "2005", forRemoval = true)
    protected int[] findPath(final NavigationNode node)
    {
        final Deque<Integer> indexes = new ArrayDeque<>();
        NavigationNode current = node;
        while(current != null)
        {
            final NavigationNode parent = current.getParent();
            if(parent != null)
            {
                final int indexOfNode = findIndexOfNode(parent.getChildren(), current);
                if(indexOfNode >= 0)
                {
                    indexes.push(Integer.valueOf(indexOfNode));
                }
                else
                {
                    break;
                }
            }
            current = parent;
        }
        return indexes.stream().mapToInt(Integer::intValue).toArray();
    }


    private int findIndexOfNode(final List<NavigationNode> children, final NavigationNode node)
    {
        final NavigationNode target = NavigationNodeDecoratorUtils.unwrap(node);
        for(int currentIndex = 0; currentIndex < children.size(); currentIndex++)
        {
            final NavigationNode child = NavigationNodeDecoratorUtils.unwrap(children.get(currentIndex));
            if(target.equals(child))
            {
                return currentIndex;
            }
        }
        return -1;
    }


    private void delegateOpening(final Treeitem treeItem, final Treerow treeRow, final Event event)
    {
        if(getController().handleOpen(event, treeItem) && event instanceof OpenEvent)
        {
            UITools.modifySClass(treeRow, SCLASS_YW_OPENED_SCLASS, ((OpenEvent)event).isOpen());
        }
    }


    private void delegateSelecting(final Event event)
    {
        final Treeitem treeitem = (Treeitem)event.getTarget();
        if(getController().handleTreeSelection(event, treeitem))
        {
            updateSelection(treeitem);
        }
        UITools.modifySClass(treeitem.getTreerow(), SCLASS_YW_OPENED_SCLASS, treeitem.isOpen());
    }


    private void applyCss(final Treeitem treeItem, final int[] path)
    {
        final Treerow treeRow = treeItem.getTreerow();
        UITools.modifySClass(treeRow, SCLASS_YW_TREE_NODE_SCLASS + (treeItem.getLevel() + 1), true);
        if(getTreeModel().isPathOpened(path))
        {
            UITools.modifySClass(treeRow, SCLASS_YW_OPENED_SCLASS, true);
        }
        else
        {
            UITools.modifySClass(treeRow, SCLASS_YW_OPENED_SCLASS, false);
        }
        if(getTreeModel().isPathSelected(path))
        {
            UITools.modifySClass(treeRow, SCLASS_YW_HIGHLIGHTED_SCLASS, false);
            UITools.modifySClass(treeRow, SCLASS_YW_SELECTED_SCLASS, true);
        }
        else
        {
            UITools.modifySClass(treeRow, SCLASS_YW_SELECTED_SCLASS, false);
            if(isPathHighlighted(path))
            {
                UITools.modifySClass(treeRow, SCLASS_YW_HIGHLIGHTED_SCLASS, true);
            }
            else
            {
                UITools.modifySClass(treeRow, SCLASS_YW_HIGHLIGHTED_SCLASS, false);
            }
        }
        applyCssForNotAccessibleNodes(treeItem, treeRow);
    }


    private void applyCssForNotAccessibleNodes(final Treeitem treeItem, final Treerow treerow)
    {
        final Object value = NavigationNodeDecoratorUtils.unwrap(treeItem.getValue());
        if(value instanceof TypeNode)
        {
            final TypeNode typeNode = (TypeNode)value;
            final boolean canReadType = getController().getPermissionFacade().canReadType(typeNode.getCode());
            if(!canReadType)
            {
                UITools.modifySClass(treerow, SCLASS_YW_NAVIGATION_NODE_CANNOT_ACCESS, true);
            }
        }
    }


    private void updateSelection(final Treeitem selectedTreeItem)
    {
        Treeitem currentItem = selectedTreeItem;
        for(final Object object : selectedTreeItem.getTree().getItems())
        {
            final Treeitem treeItem = (Treeitem)object;
            if(treeItem.isVisible())
            {
                final Treerow row = treeItem.getTreerow();
                UITools.modifySClass(row, SCLASS_YW_HIGHLIGHTED_SCLASS, false);
                UITools.modifySClass(row, SCLASS_YW_SELECTED_SCLASS, false);
            }
        }
        currentItem = currentItem.getParentItem();
        while(currentItem != null)
        {
            if(!currentItem.isOpen() || !currentItem.isVisible())
            {
                break;
            }
            UITools.modifySClass(currentItem.getTreerow(), SCLASS_YW_HIGHLIGHTED_SCLASS, true);
            currentItem = currentItem.getParentItem();
        }
        final Treerow treeRow = selectedTreeItem.getTreerow();
        UITools.modifySClass(treeRow, SCLASS_YW_SELECTED_SCLASS, true);
    }


    private boolean isPathHighlighted(final int[] path)
    {
        if(path != null && path.length > 0)
        {
            final int[] selectionPath = getTreeModel().getSelectionPath();
            if(selectionPath != null && selectionPath.length > path.length && isSubpath(path, selectionPath))
            {
                return true;
            }
        }
        return false;
    }


    private boolean isSubpath(final int[] path, final int[] subpath)
    {
        if(path.length >= subpath.length)
        {
            for(int i = 0; i < subpath.length; i++)
            {
                if(path[i] != subpath[i])
                {
                    return false;
                }
            }
            return true;
        }
        return false;
    }


    protected ExplorerTreeController getController()
    {
        return controller;
    }


    @InextensibleMethod
    NavigationNodeLabelProvider getNavigationNodeLabelProvider()
    {
        return BackofficeSpringUtil.getBean("navigationNodeLabelProvider", getController());
    }
}
