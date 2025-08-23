/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.attributechooser;

import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import com.hybris.cockpitng.i18n.CockpitLocaleService;
import com.hybris.cockpitng.tree.util.TreeUtils;
import com.hybris.cockpitng.util.UITools;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zul.AbstractTreeModel;
import org.zkoss.zul.DefaultTreeModel;
import org.zkoss.zul.DefaultTreeNode;
import org.zkoss.zul.TreeModel;
import org.zkoss.zul.TreeNode;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.event.TreeDataEvent;
import org.zkoss.zul.ext.SelectionControl;

/**
 * Default implementation of {@link NodeOperationsHandler}
 */
public class AttributesChooserNodesOperationsHandler implements NodeOperationsHandler
{
    public static final String PARTIALLY_SELECTED_CLASS = "z-treerow-partially-selected";
    private CockpitLocaleService cockpitLocaleService;


    @Override
    public void moveNodesBetweenTrees(final AbstractTreeModel<TreeNode<Attribute>> sourceModel,
                    final AbstractTreeModel<TreeNode<Attribute>> targetModel)
    {
        final Set<TreeNode<Attribute>> sourceOpenObjects = sourceModel.getOpenObjects();
        final Set<TreeNode<Attribute>> targetOpenObjects = targetModel.getOpenObjects();
        final Set<TreeNode<Attribute>> targetSelection = targetModel.getSelection();
        final Set<TreeNode<Attribute>> sourceSelection = sourceModel.getSelection();
        final Predicate<TreeNode<Attribute>> isNotSelectedByParent = selectedNode -> !sourceSelection
                        .contains(selectedNode.getParent());
        sourceSelection.stream()//
                        .filter(Objects::nonNull)//
                        .filter(isNotSelectedByParent.and(this::isNodeSelectable))//
                        .collect(Collectors.toList())//
                        .forEach(selectedNode -> moveNode(targetModel, selectedNode));
        refreshFilteredTree(sourceModel);
        sort(targetModel, true);
        sourceModel.setOpenObjects(sourceOpenObjects);
        targetModel.setOpenObjects(targetOpenObjects);
        targetModel.setSelection(targetSelection);
    }


    protected void refreshFilteredTree(final AbstractTreeModel<TreeNode<Attribute>> sourceModel)
    {
        if(sourceModel instanceof TreeUtils.FilteredTreeModel)
        {
            ((TreeUtils.FilteredTreeModel)sourceModel).rebuild();
        }
        sourceModel.fireEvent(TreeDataEvent.STRUCTURE_CHANGED, null, 0, 0);
    }


    protected void removeFromParent(final TreeNode<Attribute> node)
    {
        if(node.getParent() != null)
        {
            if(node.getParent().getData() != null)
            {
                node.getParent().getData().getSubAttributes().remove(node.getData());
            }
            node.getParent().remove(node);
        }
    }


    protected void moveNode(final AbstractTreeModel<TreeNode<Attribute>> targetModel, final TreeNode<Attribute> selectedNode)
    {
        final TreeNode<Attribute> oldParent = selectedNode.getParent();
        if(oldParent == null || selectedNode.getData() == null)
        {
            return; // cannot move mandatory items or already detached node
        }
        final Optional<TreeNode<Attribute>> existingNode = findRecursively(targetModel.getRoot(), selectedNode.getData());
        if(existingNode.isPresent())
        {
            mergeChildren(selectedNode, existingNode.get());
        }
        else
        {
            attachWithMissingParents(targetModel, selectedNode);
        }
        if(oldParent.getChildCount() == 0)
        {
            removeFromParent(oldParent);
        }
    }


    private void attachWithMissingParents(final AbstractTreeModel<TreeNode<Attribute>> targetModel,
                    final TreeNode<Attribute> selectedNode)
    {
        final TreeNode<Attribute> lastExistingParent = getLastExistingParentFromRootSide(targetModel, selectedNode);
        TreeNode<Attribute> lastSubNode = selectedNode;
        for(final Attribute parentData : lastSubNode.getData().getAllParents())
        {
            if(Objects.equals(parentData, lastExistingParent.getData()))
            {
                lastExistingParent.getData().addSubAttribute(lastSubNode.getData());
                lastExistingParent.add(lastSubNode);
                return;
            }
            else
            {
                final Attribute clonedParent = cloneGroupingAttribute(parentData);
                lastSubNode.getData().setParent(clonedParent);
                lastSubNode = new DefaultTreeNode<>(clonedParent, Lists.newArrayList(lastSubNode));
            }
        }
        if(Objects.equals(targetModel.getRoot(), lastExistingParent))
        {
            lastExistingParent.add(lastSubNode);
        }
    }


    private TreeNode<Attribute> getLastExistingParentFromRootSide(final AbstractTreeModel<TreeNode<Attribute>> targetModel,
                    final TreeNode<Attribute> selectedNode)
    {
        TreeNode<Attribute> lastFoundParentInTarget = targetModel.getRoot();
        final List<Attribute> parents = selectedNode.getData().getAllParents();
        Collections.reverse(parents);
        for(final Attribute parentAttribute : parents)
        {
            final Optional<TreeNode<Attribute>> groupingNodeByAttribute = findRecursively(lastFoundParentInTarget, parentAttribute);
            if(groupingNodeByAttribute.isPresent())
            {
                lastFoundParentInTarget = groupingNodeByAttribute.get();
            }
            else
            {
                break;
            }
        }
        return lastFoundParentInTarget;
    }


    private void mergeChildren(final TreeNode<Attribute> source, final TreeNode<Attribute> target)
    {
        final List<TreeNode<Attribute>> childrenCopy = source.getChildren() != null ? new ArrayList<>(source.getChildren())
                        : Collections.emptyList();
        for(final TreeNode<Attribute> child : childrenCopy)
        {
            if(child.getData().hasSubAttributes())
            {
                final Optional<TreeNode<Attribute>> nodeInTarget = findRecursively(target, child.getData());
                if(nodeInTarget.isPresent())
                {
                    mergeChildren(child, nodeInTarget.get());
                    continue;
                }
            }
            target.add(child);
            target.getData().addSubAttribute(child.getData());
        }
        removeFromParent(source);
    }


    protected Attribute cloneGroupingAttribute(final Attribute originParent)
    {
        final Attribute newParent = new Attribute(originParent.getQualifier(), originParent.getDisplayName(),
                        originParent.isMandatory());
        newParent.setIsoCode(originParent.getIsoCode());
        return newParent;
    }


    protected Optional<TreeNode<Attribute>> findRecursively(final TreeNode<Attribute> root, final Attribute data)
    {
        if(root == null || root.getChildren() == null)
        {
            return Optional.empty();
        }
        for(final TreeNode<Attribute> child : root.getChildren())
        {
            if(Objects.equals(data, child.getData()))
            {
                return Optional.of(child);
            }
            else
            {
                final Optional<TreeNode<Attribute>> recursively = findRecursively(child, data);
                if(recursively.isPresent())
                {
                    return recursively;
                }
            }
        }
        return Optional.empty();
    }


    @Override
    public void updateGroupingNodesSelection(final AbstractTreeModel<TreeNode<Attribute>> treeModel,
                    final SelectEvent<Treeitem, TreeNode<Attribute>> event)
    {
        final TreeNode<Attribute> clickedTreeNode = getTreeNode(treeModel, event.getReference());
        final var treeitem = event.getReference();
        final boolean selected = treeModel.getSelection().contains(clickedTreeNode);
        UITools.modifySClass((HtmlBasedComponent)treeitem.getTreerow(), PARTIALLY_SELECTED_CLASS, false);
        if(selected)
        {
            selectAllSubNodesRecursively(treeModel, treeitem);
            handleParentSelectedStatusWhenSelect(treeModel, treeitem.getParentItem());
        }
        else
        {
            deselectAllSubNodesRecursively(treeModel, treeitem);
            handleParentSelectedStatusWhenDeSelect(treeModel, treeitem.getParentItem());
        }
    }


    protected void selectAllSubNodesRecursively(final AbstractTreeModel<TreeNode<Attribute>> treeModel, final Treeitem treeitem)
    {
        final var treechildren = treeitem.getTreechildren();
        if(treechildren != null)
        {
            treechildren.getItems().forEach(child -> {
                treeModel.addToSelection(getTreeNode(treeModel, child));
                UITools.modifySClass((HtmlBasedComponent)child.getTreerow(), PARTIALLY_SELECTED_CLASS, false);
                selectAllSubNodesRecursively(treeModel, child);
            });
        }
    }


    protected void deselectAllSubNodesRecursively(final AbstractTreeModel<TreeNode<Attribute>> treeModel, final Treeitem treeitem)
    {
        final var treechildren = treeitem.getTreechildren();
        if(treechildren != null)
        {
            treechildren.getItems().forEach(child -> {
                treeModel.removeFromSelection(getTreeNode(treeModel, child));
                UITools.modifySClass((HtmlBasedComponent)child.getTreerow(), PARTIALLY_SELECTED_CLASS, false);
                deselectAllSubNodesRecursively(treeModel, child);
            });
        }
    }


    protected void handleParentSelectedStatusWhenSelect(final AbstractTreeModel<TreeNode<Attribute>> treeModel,
                    final Treeitem item)
    {
        if(item != null)
        {
            if(new ArrayList<>(item.getTree().getSelectedItems()).containsAll(new ArrayList<>(item.getTreechildren().getItems())))
            {
                UITools.modifySClass((HtmlBasedComponent)item.getTreerow(), PARTIALLY_SELECTED_CLASS, false);
                treeModel.addToSelection(getTreeNode(treeModel, item));
            }
            else
            {
                treeModel.removeFromSelection(getTreeNode(treeModel, item));
                UITools.modifySClass((HtmlBasedComponent)item.getTreerow(), PARTIALLY_SELECTED_CLASS, true);
            }
            handleParentSelectedStatusWhenSelect(treeModel, item.getParentItem());
        }
    }


    protected void handleParentSelectedStatusWhenDeSelect(final AbstractTreeModel<TreeNode<Attribute>> treeModel,
                    final Treeitem item)
    {
        if(item != null)
        {
            final List<Treeitem> selectItems = new ArrayList<>(item.getTree().getSelectedItems());
            selectItems.retainAll(new ArrayList<>(item.getTreechildren().getItems()));
            treeModel.removeFromSelection(getTreeNode(treeModel, item));
            UITools.modifySClass((HtmlBasedComponent)item.getTreerow(), PARTIALLY_SELECTED_CLASS, !selectItems.isEmpty());
            handleParentSelectedStatusWhenDeSelect(treeModel, item.getParentItem());
        }
    }


    protected TreeNode<Attribute> getTreeNode(final AbstractTreeModel<TreeNode<Attribute>> treeModel, final Treeitem treeitem)
    {
        final int[] path = Ints.toArray(getPath(treeitem));
        return treeModel.getChild(path);
    }


    /**
     * @deprecated Since 2205, replaced by {@link AttributesChooserNodesOperationsHandler#getTreeNode(AbstractTreeModel, Treeitem)}.
     */
    @Deprecated(since = "2205", forRemoval = true)
    protected TreeNode<Attribute> getClickedTreeNode(final AbstractTreeModel<TreeNode<Attribute>> treeModel,
                    final SelectEvent<Treeitem, TreeNode<Attribute>> event)
    {
        final Treeitem clickedTreeitem = event.getReference();
        final int[] path = Ints.toArray(getPath(clickedTreeitem));
        return treeModel.getChild(path);
    }


    protected List<Integer> getPath(final Treeitem treeitem)
    {
        final List<Integer> path = new ArrayList<>();
        if(treeitem != null)
        {
            final Treeitem parentItem = treeitem.getParentItem();
            if(parentItem != null)
            {
                final List<Integer> parentPath = getPath(parentItem);
                path.addAll(parentPath);
            }
            path.add(treeitem.getIndex());
        }
        return path;
    }


    /**
     * @deprecated Since 2205, replaced by {@link AttributesChooserNodesOperationsHandler#selectAllSubNodesRecursively(AbstractTreeModel, Treeitem)}.
     */
    @Deprecated(since = "2205", forRemoval = true)
    protected void selectAllSubNodesRecursively(final TreeNode<Attribute> selectedNode,
                    final Set<TreeNode<Attribute>> effectiveSelection)
    {
        if(selectedNode.getChildCount() != 0)
        {
            selectedNode.getChildren().forEach(child -> {
                effectiveSelection.add(child);
                selectAllSubNodesRecursively(child, effectiveSelection);
            });
        }
    }


    /**
     * @deprecated Since 2205, replaced by {@link AttributesChooserNodesOperationsHandler#handleParentSelectedStatusWhenSelect(AbstractTreeModel, Treeitem)}.
     */
    @Deprecated(since = "2205", forRemoval = true)
    protected void selectParents(final TreeNode<Attribute> selectedNode, final Set<TreeNode<Attribute>> effectiveSelection)
    {
        final TreeNode<Attribute> parent = selectedNode.getParent();
        if(parent != null && effectiveSelection.containsAll(parent.getChildren()))
        {
            effectiveSelection.add(parent);
            selectParents(parent, effectiveSelection);
        }
    }


    /**
     * @deprecated Since 2205, replaced by {@link AttributesChooserNodesOperationsHandler#handleParentSelectedStatusWhenDeSelect(AbstractTreeModel, Treeitem)}.
     */
    @Deprecated(since = "2205", forRemoval = true)
    protected void deselectAllParents(final TreeNode<Attribute> deselected, final Set<TreeNode<Attribute>> effectiveSelection,
                    final TreeNode<Attribute> clickedTreeNode)
    {
        if(deselected != null)
        {
            final TreeNode<Attribute> parent = deselected.getParent();
            if(parent != null)
            {
                if(!Objects.equals(parent, clickedTreeNode))
                {
                    effectiveSelection.remove(parent);
                }
                deselectAllParents(parent, effectiveSelection, clickedTreeNode);
            }
        }
    }


    /**
     * @deprecated Since 2205, replaced by {@link AttributesChooserNodesOperationsHandler#deselectAllSubNodesRecursively(AbstractTreeModel, Treeitem)}.
     */
    @Deprecated(since = "2205", forRemoval = true)
    protected void deselectAllSubNodesRecursively(final TreeNode<Attribute> deselected,
                    final Set<TreeNode<Attribute>> effectiveSelection, final TreeNode<Attribute> clickedTreeNode)
    {
        if(deselected.getChildCount() != 0)
        {
            deselected.getChildren().forEach(child -> {
                if(!Objects.equals(child, clickedTreeNode))
                {
                    effectiveSelection.remove(child);
                }
                deselectAllSubNodesRecursively(child, effectiveSelection, clickedTreeNode);
            });
        }
    }


    @Override
    public void sort(final AbstractTreeModel<TreeNode<Attribute>> treeModel, final boolean ascending)
    {
        if(treeModel instanceof DefaultTreeModel)
        {
            sort((DefaultTreeModel<Attribute>)treeModel, ascending);
        }
        else if(treeModel instanceof TreeUtils.FilteredTreeModel)
        {
            final DefaultTreeModel<Attribute> model = (DefaultTreeModel)((TreeUtils.FilteredTreeModel)treeModel)
                            .getOriginalModel();
            sort(model, ascending);
            refreshFilteredTree(treeModel);
        }
    }


    protected void sort(final DefaultTreeModel<Attribute> model, final boolean ascending)
    {
        final Collator localeAwareStringComparator = Collator.getInstance(getCockpitLocaleService().getCurrentLocale());
        final Comparator<TreeNode<Attribute>> displayNameComparator = Comparator.comparing(
                        (TreeNode<Attribute> node) -> node.getData().getDisplayName(), Comparator.nullsLast(localeAwareStringComparator));
        final Comparator<TreeNode<Attribute>> isoCodeComparator = Comparator.comparing(node -> node.getData().getIsoCode(),
                        Comparator.nullsLast(Comparator.naturalOrder()));
        final Comparator<TreeNode<Attribute>> treeNodeComparator = displayNameComparator.thenComparing(isoCodeComparator);
        model.clearOpen();
        model.sort(treeNodeComparator, ascending);
    }


    @Override
    public AbstractTreeModel<TreeNode<Attribute>> filterTreeModel(final AbstractTreeModel<TreeNode<Attribute>> model,
                    final String searchText)
    {
        final Set<TreeNode<Attribute>> openObjects = model.getOpenObjects();
        final TreeModel<TreeNode<Attribute>> unfiltered = model instanceof TreeUtils.FilteredTreeModel
                        ? ((TreeUtils.FilteredTreeModel)model).getOriginalModel()
                        : model;
        final TreeUtils.FilteredTreeModel<TreeNode<Attribute>> filtered = new TreeUtils.FilteredTreeModel<>(unfiltered, searchText,
                        false, getAttributeSearchTextResolver());
        filtered.setMultiple(true);
        filtered.setOpenObjects(openObjects);
        filtered.setSelectionControl(getSelectionControl(filtered));
        return filtered;
    }


    protected SelectionControl<TreeNode<Attribute>> getSelectionControl(final AbstractTreeModel<TreeNode<Attribute>> model)
    {
        return new AbstractTreeModel.DefaultSelectionControl<TreeNode<Attribute>>(model)
        {
            private static final long serialVersionUID = 1L;


            @Override
            public boolean isSelectable(final TreeNode<Attribute> e)
            {
                return isNodeSelectable(e);
            }
        };
    }


    protected boolean isNodeSelectable(final TreeNode<Attribute> treeNode)
    {
        return treeNode.getData() != null && !treeNode.getData().isMandatory()
                        && (!isLocalizedRoot(treeNode) || areChildrenSelectable(treeNode));
    }


    protected boolean isLocalizedRoot(final TreeNode<Attribute> treeNode)
    {
        return treeNode.getData() != null && treeNode.getData().hasSubAttributes();
    }


    protected boolean areChildrenSelectable(final TreeNode<Attribute> treeNode)
    {
        if(CollectionUtils.isNotEmpty(treeNode.getChildren()))
        {
            for(final TreeNode<Attribute> child : treeNode.getChildren())
            {
                if(!isNodeSelectable(child))
                {
                    return false;
                }
            }
        }
        return true;
    }


    protected TreeUtils.FilterStringResolver<TreeNode<Attribute>> getAttributeSearchTextResolver()
    {
        return node -> node.getData() != null ? node.getData().getDisplayName() : StringUtils.EMPTY;
    }


    @Override
    public List<Attribute> extractAttributes(final AbstractTreeModel<TreeNode<Attribute>> treeModel)
    {
        return treeModel.getRoot().getChildren().stream().map(TreeNode::getData).collect(Collectors.toList());
    }


    @Override
    public List<DefaultTreeNode<Attribute>> createTreeNodes(final Collection<Attribute> attributes)
    {
        if(CollectionUtils.isEmpty(attributes))
        {
            return Collections.emptyList();
        }
        return attributes.stream().map(attribute -> {
            if(attribute.hasSubAttributes())
            {
                final List<DefaultTreeNode<Attribute>> localizedChildren = createTreeNodes(attribute.getSubAttributes());
                if(CollectionUtils.isNotEmpty(localizedChildren))
                {
                    return new DefaultTreeNode<>(attribute, localizedChildren);
                }
            }
            return new DefaultTreeNode<>(attribute);
        }).filter(obj -> !Objects.isNull(obj)).collect(Collectors.toList());
    }


    public CockpitLocaleService getCockpitLocaleService()
    {
        return cockpitLocaleService;
    }


    @Required
    public void setCockpitLocaleService(final CockpitLocaleService cockpitLocaleService)
    {
        this.cockpitLocaleService = cockpitLocaleService;
    }
}
