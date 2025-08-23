/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.adminmode;

import static com.hybris.cockpitng.adminmode.CockpitAdminComposer.COCKPIT_CONFIGURATION_SEARCH_NODES_APPROVED;

import com.hybris.cockpitng.core.config.impl.model.ContextSearchNode;
import com.hybris.cockpitng.util.UITools;
import java.util.Collection;
import org.zkoss.zul.DefaultTreeNode;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;
import org.zkoss.zul.Treerow;

/**
 * A renderer used in configuration search simulation. It renders each node in tree containing search progress.
 */
public class ConfigSearchProgressRenderer implements TreeitemRenderer<DefaultTreeNode<ContextSearchNode>>
{
    private static final String SCLASS_NEEDLE_ATTRIBUTE = "y-search-simulate-needle-attribute";
    private static final String SCLASS_NEEDLE_ATTRIBUTE_NAME = "y-search-simulate-needle-attribute-name";
    private static final String SCLASS_NEEDLE_ATTRIBUTE_VALUE = "y-search-simulate-needle-attribute-value";
    private static final String SCLASS_NEEDLE_LEAF = "y-search-simulate-needle-leaf";
    private static final String SCLASS_RESULT_COUNT = "y-search-simulate-needle-result-count";
    private static final String SCLASS_RESULT_EMPTY = "y-search-simulate-needle-result-empty";
    private static final String SCLASS_RESULT_APPROVED = "y-search-simulate-needle-result-approved";


    @Override
    public void render(final Treeitem item, final DefaultTreeNode<ContextSearchNode> node, final int level)
    {
        final Treerow row = new Treerow();
        final Treecell cell = new Treecell();
        row.appendChild(cell);
        item.appendChild(row);
        final ContextSearchNode searchNode = node.getData();
        searchNode.getSearchNeedle().getAttributes()
                        .forEach((attribute, value) -> renderNeedleAttributeNeedle(cell, attribute, value));
        final Label nameLabel = new Label(Integer.toString(searchNode.getNodeResult().size()));
        UITools.addSClass(nameLabel, SCLASS_RESULT_COUNT);
        cell.appendChild(nameLabel);
        UITools.modifySClass(cell, SCLASS_RESULT_EMPTY, searchNode.getNodeResult().isEmpty());
        final Collection<ContextSearchNode> approvedNodes = (Collection<ContextSearchNode>)item.getTree()
                        .getAttribute(COCKPIT_CONFIGURATION_SEARCH_NODES_APPROVED);
        final boolean approved = approvedNodes.stream().anyMatch(approvedNode -> approvedNode == node.getData());
        UITools.modifySClass(cell, SCLASS_RESULT_APPROVED, approved);
        if(!searchNode.getResult().isEmpty() && approved)
        {
            openBranch(item);
        }
        UITools.modifySClass(cell, SCLASS_NEEDLE_LEAF, searchNode.getChildren().isEmpty());
    }


    protected void openBranch(final Treeitem item)
    {
        item.setOpen(true);
        if(item.getParentItem() != null)
        {
            openBranch(item.getParentItem());
        }
    }


    protected void renderNeedleAttributeNeedle(final Treecell item, final String attribute, final String value)
    {
        final Div container = new Div();
        UITools.addSClass(container, SCLASS_NEEDLE_ATTRIBUTE);
        item.appendChild(container);
        final Label nameLabel = new Label(attribute);
        UITools.addSClass(nameLabel, SCLASS_NEEDLE_ATTRIBUTE_NAME);
        container.appendChild(nameLabel);
        final Label valueLabel = new Label(value);
        UITools.addSClass(valueLabel, SCLASS_NEEDLE_ATTRIBUTE_VALUE);
        container.appendChild(valueLabel);
    }
}
