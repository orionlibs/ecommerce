/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.attributechooser;

import java.util.Collection;
import java.util.List;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zul.AbstractTreeModel;
import org.zkoss.zul.DefaultTreeNode;
import org.zkoss.zul.TreeNode;
import org.zkoss.zul.Treeitem;

/**
 * Handler which provides tree model nodes operations.
 */
public interface NodeOperationsHandler
{
    /**
     * Moves selected nodes from source tree to target tree. If moved node is a sub node and target tree doesn't have
     * grouping node it will be created.
     *
     * @param sourceModel
     *           source tree from which selected nodes will be moved.
     * @param targetModel
     *           target tree where nodes will be moved.
     */
    void moveNodesBetweenTrees(AbstractTreeModel<TreeNode<Attribute>> sourceModel,
                    AbstractTreeModel<TreeNode<Attribute>> targetModel);


    /**
     * Updates groping nodes selection based on select event data. If all sub nodes are selected then grouping node is
     * also selected. If grouping node is selected then all sub nodes are selected. etc.
     *
     * @param treeModel
     *           tree model which contains nodes to be updated.
     * @param event
     *           select event.
     */
    void updateGroupingNodesSelection(final AbstractTreeModel<TreeNode<Attribute>> treeModel,
                    final SelectEvent<Treeitem, TreeNode<Attribute>> event);


    /**
     * Sorts tree model based on nodes data.
     *
     * @param treeModel
     *           tree model to sort.
     * @param ascending
     *           sort direction.
     */
    void sort(final AbstractTreeModel<TreeNode<Attribute>> treeModel, final boolean ascending);


    /**
     * Filter tree model.
     *
     * @param model
     *           source tree model.
     * @param searchText
     *           filter text.
     * @return filtered model.
     */
    AbstractTreeModel<TreeNode<Attribute>> filterTreeModel(final AbstractTreeModel<TreeNode<Attribute>> model,
                    final String searchText);


    /**
     * Extracts data from tree model's nodes.
     *
     * @param treeModel
     *           source tree model.
     * @return list of attributes from tree data.
     */
    List<Attribute> extractAttributes(final AbstractTreeModel<TreeNode<Attribute>> treeModel);


    /**
     * Creates tree nodes with for given attributes.
     *
     * @param attributes
     *           all available attributes.
     * @return list of tree nodes.
     */
    List<DefaultTreeNode<Attribute>> createTreeNodes(final Collection<Attribute> attributes);
}
