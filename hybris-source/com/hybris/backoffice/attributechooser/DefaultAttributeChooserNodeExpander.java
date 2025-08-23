/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.attributechooser;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.zul.AbstractTreeModel;
import org.zkoss.zul.Tree;
import org.zkoss.zul.TreeNode;

/**
 * Default implementation of {@link AttributeChooserNodeExpander} which opens nodes matching to filter text.
 */
public class DefaultAttributeChooserNodeExpander implements AttributeChooserNodeExpander
{
    public void filterTree(final Tree tree, final AttributesChooserConfig config, final String filterText)
    {
        if(config.isOpenFirstMultiChildNode())
        {
            final AbstractTreeModel<TreeNode<Attribute>> model = getModel(tree);
            if(model == null)
            {
                return;
            }
            openMultiChildLevelByFilterText(model, model.getRoot(), filterText);
        }
    }


    protected void openMultiChildLevelByFilterText(final AbstractTreeModel<TreeNode<Attribute>> model,
                    final TreeNode<Attribute> root, final String filterText)
    {
        if(root != null)
        {
            if(!isLocalized(root) && root.getChildCount() != 0)
            {
                for(final TreeNode<Attribute> rootChild : root.getChildren())
                {
                    openMultiChildLevelByFilterText(model, rootChild, filterText);
                }
            }
            if(root.getData() != null && StringUtils.containsIgnoreCase(root.getData().getDisplayName(), filterText))
            {
                if(!isLocalized(root))
                {
                    model.addOpenObject(root);
                }
                openNodeWithParents(model, root.getParent());
            }
        }
    }


    private void openNodeWithParents(final AbstractTreeModel<TreeNode<Attribute>> model, final TreeNode<Attribute> root)
    {
        if(root != null && root.getChildCount() != 0)
        {
            model.addOpenObject(root);
            openNodeWithParents(model, root.getParent());
        }
    }


    private boolean isLocalized(final TreeNode<Attribute> root)
    {
        return CollectionUtils.emptyIfNull(root.getChildren()).stream()
                        .anyMatch(attribute -> StringUtils.isNotEmpty(attribute.getData().getIsoCode()));
    }


    protected AbstractTreeModel<TreeNode<Attribute>> getModel(final Tree tree)
    {
        if(tree.getModel() instanceof AbstractTreeModel)
        {
            return ((AbstractTreeModel)tree.getModel());
        }
        return null;
    }
}
