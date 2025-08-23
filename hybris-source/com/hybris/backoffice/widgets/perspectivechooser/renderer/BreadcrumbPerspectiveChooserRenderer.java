/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.perspectivechooser.renderer;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hybris.backoffice.actionbar.impl.ActionComponentUtils;
import com.hybris.backoffice.actionbar.impl.LabelImageActionComponent;
import com.hybris.backoffice.actionbar.impl.TreeitemActionComponent;
import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.backoffice.navigation.bar.NavigationContext;
import com.hybris.backoffice.navigation.bar.NavigationUtils;
import com.hybris.cockpitng.util.UITools;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Caption;
import org.zkoss.zul.DefaultTreeModel;
import org.zkoss.zul.DefaultTreeNode;
import org.zkoss.zul.Div;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;
import org.zkoss.zul.Window;
import org.zkoss.zul.impl.LabelImageElement;

public class BreadcrumbPerspectiveChooserRenderer implements PerspectiveChooserRenderer
{
    private static final String SCLASS_BREADCRUMB_EXPANDED = "yw-perspective-breadcrumb-expanded";
    private static final String SCLASS_BREADCRUMB = "yw-perspective-breadcrumb";
    private static final String SCLASS_BREADCRUMB_SPACER = "yw-perspective-breadcrumb-spacer";
    private static final String SCLASS_BREADCRUMB_SPACER_ARROW = "yw-perspective-breadcrumb-spacerarrow";
    private static final String SCLASS_BREADCRUMB_ARROW = "yw-perspective-breadcrumb-arrow";
    private static final String SCASS_Z_ICON_ANGLE_DOWN = "z-icon-angle-down";
    private static final String SCASS_Z_MENU_ICON = "z-menu-icon";
    private static final String SCLASS_PERSPECTIVES_POPUP = "yw-perspective-popup";
    private static final String SCLASS_PERSPECTIVES_POPUP_LABEL = "yw-perspective-popup-treecell";
    private static final String SCLASS_PERSPECTIVES_POPUP_LEVEL = "yw-perspective-popup-level%d";
    private static final String ATTRIBUTE_PREVIOUS_SELECTIONS = "previousSelections";


    protected static boolean isDirectoryOrHasChildren(final NavigationNode node)
    {
        return node != null && (node.isDirectory() || CollectionUtils.isNotEmpty(node.getChildren()));
    }


    @Override
    public void updatePerspectiveSelection(final Component parent, final NavigationContext context)
    {
        parent.getChildren().clear();
        renderSelectionPath(parent, context);
    }


    @Override
    public void renderTree(final Component parent, final NavigationContext context)
    {
        renderSelectionPath(parent, context);
    }


    protected void nodeSelected(final Component parent, final NavigationContext context, final HtmlBasedComponent item)
    {
        final NavigationNode node = ActionComponentUtils.getDefinition(item);
        if(!isDirectoryOrHasChildren(node))
        {
            NavigationUtils.nodeSelected(parent, context, item);
            updatePerspectiveSelection(parent, context);
        }
        else
        {
            final HtmlBasedComponent nodeElement = getSelectedPerspectiveItem(parent, item);
            if(nodeElement != null)
            {
                nodeSelected(parent, context, nodeElement);
            }
        }
    }


    protected HtmlBasedComponent getSelectedPerspectiveItem(final Component parent, final HtmlBasedComponent item)
    {
        final NavigationNode node = ActionComponentUtils.getDefinition(item);
        if(!isDirectoryOrHasChildren(node))
        {
            return item;
        }
        else
        {
            final Object previousSelections = parent.getAttribute(ATTRIBUTE_PREVIOUS_SELECTIONS);
            if((previousSelections instanceof Map) && ((Map)previousSelections).containsKey(node.getId()))
            {
                return (HtmlBasedComponent)((Map)previousSelections).get(node.getId());
            }
            else if(item instanceof Treeitem)
            {
                final Treeitem treeitem = (Treeitem)item;
                if(!treeitem.isLoaded())
                {
                    treeitem.getTree().renderItem(treeitem);
                }
                return ((Treeitem)item).getTreechildren().getItems().iterator().next();
            }
            else
            {
                return null;
            }
        }
    }


    protected void renderSelectionPath(final Component parent, final NavigationContext context)
    {
        final Optional<NavigationNode> selected = context.getActionTree().getRootNodes().stream()
                        .filter(node -> context.isSelected(node) || context.isSelectionParent(node)).findFirst();
        if(selected.isPresent())
        {
            renderSelectionPath(parent, context, selected.get());
            storeSelection(parent);
        }
    }


    protected void storeSelection(final Component parent)
    {
        Object attribute = parent.getAttribute(ATTRIBUTE_PREVIOUS_SELECTIONS);
        if(!(attribute instanceof Map))
        {
            attribute = Maps.newHashMap();
            parent.setAttribute(ATTRIBUTE_PREVIOUS_SELECTIONS, attribute);
        }
        final Map<String, HtmlBasedComponent> previousSelections = (Map<String, HtmlBasedComponent>)attribute;
        final HtmlBasedComponent selected = NavigationUtils.getSelectedItem(parent);
        if(selected instanceof Treeitem)
        {
            Treeitem selectedItem = (Treeitem)selected;
            while(selectedItem.getParentItem() != null)
            {
                final NavigationNode node = ActionComponentUtils.getDefinition(selectedItem.getParentItem());
                previousSelections.put(node.getId(), selectedItem);
                selectedItem = selectedItem.getParentItem();
            }
        }
    }


    protected void renderSelectionPath(final Component parent, final NavigationContext context,
                    final NavigationNode navigationNode)
    {
        renderBreadcrumb(parent, context, navigationNode);
        final Optional<NavigationNode> selected = navigationNode.getChildren().stream()
                        .filter(node -> context.isSelected(node) || context.isSelectionParent(node)).findFirst();
        if(selected.isPresent())
        {
            renderSelectionPath(parent, context, selected.get());
        }
    }


    protected Collection<NavigationNode> getSiblings(final NavigationContext context, final NavigationNode navigationNode)
    {
        if(navigationNode.getParent() == null)
        {
            return context.getActionTree().getRootNodes();
        }
        else
        {
            return navigationNode.getParent().getChildren();
        }
    }


    protected void renderBreadcrumb(final Component parent, final NavigationContext context, final NavigationNode navigationNode)
    {
        final HtmlBasedComponent breadcrumb;
        if(CollectionUtils.isNotEmpty(getSiblings(context, navigationNode)))
        {
            breadcrumb = renderMenuBreadcrumb(parent, context, navigationNode);
        }
        else
        {
            breadcrumb = renderFlatBreadcrumb(parent, context, navigationNode);
        }
        UITools.modifySClass(breadcrumb, SCLASS_BREADCRUMB, true);
        renderBreadcrumbArrow(breadcrumb);
    }


    protected HtmlBasedComponent renderFlatBreadcrumb(final Component parent, final NavigationContext context,
                    final NavigationNode navigationNode)
    {
        final Div breadcrumb = new Div();
        final LabelImageElement item = new PerspectiveElement();
        item.setParent(breadcrumb);
        breadcrumb.setParent(parent);
        return breadcrumb;
    }


    protected Window createPopup(final HtmlBasedComponent breadcrumb)
    {
        final Window popup = new Window()
        {
            @Override
            public boolean setVisible(final boolean visible)
            {
                UITools.modifySClass(breadcrumb, SCLASS_BREADCRUMB_EXPANDED, visible);
                return super.setVisible(visible);
            }
        };
        popup.setVisible(false);
        popup.setClosable(false);
        popup.setBorder(false);
        popup.setMaximizable(false);
        popup.setMaximizable(false);
        popup.setPosition("parent");
        return popup;
    }


    protected TreeitemRenderer<DefaultTreeNode<NavigationNode>> createTreeRenderer(final Component parent,
                    final NavigationContext context)
    {
        return new TreeRenderer(parent, context);
    }


    protected HtmlBasedComponent renderMenuBreadcrumb(final Component parent, final NavigationContext context,
                    final NavigationNode navigationNode)
    {
        final Div breadcrumb = new Div();
        final PerspectiveElement item = new PerspectiveElement();
        ActionComponentUtils.formatNodeElement(new LabelImageActionComponent(item), context, navigationNode);
        if(!isShowDefaultIcons(context))
        {
            item.setImage(null);
        }
        item.setParent(breadcrumb);
        final Div icon = new Div();
        UITools.modifySClass(icon, SCASS_Z_ICON_ANGLE_DOWN, true);
        UITools.modifySClass(icon, SCASS_Z_MENU_ICON, true);
        icon.setParent(breadcrumb);
        final Window popup = createPopup(breadcrumb);
        popup.setParent(breadcrumb);
        UITools.modifySClass(popup, SCLASS_PERSPECTIVES_POPUP, true);
        final Tree tree = new Tree();
        final DefaultTreeNode<NavigationNode> root = new DefaultTreeNode<>(null, Lists.newArrayList());
        final DefaultTreeModel model = new DefaultTreeModel(root);
        tree.setItemRenderer(createTreeRenderer(parent, context));
        tree.setModel(model);
        createPerspectiveTree(root, getSiblings(context, navigationNode));
        tree.setParent(popup);
        breadcrumb.setParent(parent);
        final EventListener<Event> listener = e -> popup.doPopup();
        breadcrumb.addEventListener(Events.ON_CLICK, listener);
        icon.addEventListener(Events.ON_CLICK, listener);
        item.addEventListener(Events.ON_CLICK, listener);
        return breadcrumb;
    }


    protected boolean isShowDefaultIcons(final NavigationContext context)
    {
        return false; //UX: Don't need to render icon here any more.
    }


    protected void createPerspectiveTree(final DefaultTreeNode<NavigationNode> root, final Collection<NavigationNode> children)
    {
        root.getChildren().addAll(children.stream().map(node -> {
            final DefaultTreeNode<NavigationNode> child;
            if(isDirectoryOrHasChildren(node))
            {
                child = new DefaultTreeNode(node, Lists.newArrayList());
                createPerspectiveTree(child, node.getChildren());
            }
            else
            {
                child = new DefaultTreeNode<>(node);
            }
            return child;
        }).collect(Collectors.toList()));
    }


    protected void renderBreadcrumbArrow(final Component parent)
    {
        final Div spacerDiv = new Div();
        spacerDiv.setSclass(SCLASS_BREADCRUMB_SPACER);
        parent.appendChild(spacerDiv);
        final Div spacerArrowDiv = new Div();
        spacerArrowDiv.setSclass(SCLASS_BREADCRUMB_SPACER_ARROW);
        parent.appendChild(spacerArrowDiv);
        final Div arrowDiv = new Div();
        arrowDiv.setSclass(SCLASS_BREADCRUMB_ARROW);
        parent.appendChild(arrowDiv);
    }


    protected String getLabel(final NavigationContext context, final String locKey, final String fallback)
    {
        if(StringUtils.isBlank(locKey))
        {
            return fallback;
        }
        else
        {
            final String label = context.getWidgetInstanceManager().getLabel(locKey);
            if(StringUtils.isEmpty(label))
            {
                return (StringUtils.isEmpty(fallback) ? locKey : fallback);
            }
            else
            {
                return label;
            }
        }
    }


    protected class TreeRenderer implements TreeitemRenderer<DefaultTreeNode<NavigationNode>>
    {
        private final NavigationContext context;
        private final Component parent;


        public TreeRenderer(final Component parent, final NavigationContext context)
        {
            this.context = context;
            this.parent = parent;
        }


        @Override
        public void render(final Treeitem treeitem, final DefaultTreeNode<NavigationNode> treeNode, final int i) throws Exception
        {
            final NavigationNode navigationNode = treeNode.getData();
            final TreeitemActionComponent component = new TreeitemActionComponent(treeitem);
            ActionComponentUtils.formatNodeElement(component, context, navigationNode);
            if(!isShowDefaultIcons(context))
            {
                removeComponentImage(component);
            }
            treeitem.addEventListener(Events.ON_CLICK, event -> nodeSelected(parent, context, treeitem));
            UITools.modifySClass(component.getComponent(), String.format(SCLASS_PERSPECTIVES_POPUP_LEVEL, treeitem.getLevel()),
                            true);
            UITools.modifySClass(component.getLabelCell(), SCLASS_PERSPECTIVES_POPUP_LABEL, true);
        }


        protected void removeComponentImage(final TreeitemActionComponent component)
        {
            component.setImage(null);
        }
    }


    protected class PerspectiveElement extends Caption
    {
        @Override
        public void beforeParentChanged(final Component parent)
        {
            // not implemented
        }
    }
}
