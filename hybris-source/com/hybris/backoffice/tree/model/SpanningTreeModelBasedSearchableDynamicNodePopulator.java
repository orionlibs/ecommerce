/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.tree.model;

import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.cockpitng.tree.node.DynamicNode;
import com.hybris.cockpitng.tree.node.DynamicNodePopulator;
import com.hybris.cockpitng.tree.node.SearchableDynamicNodePopulator;
import com.hybris.cockpitng.tree.util.TreeUtils;
import de.hybris.platform.category.model.CategoryModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import org.apache.commons.math3.util.Pair;

/**
 * Internal populator that is capable of returning data from result produced by filtering algorithm of
 * {@link CatalogTreeModelPopulator}.
 */
class SpanningTreeModelBasedSearchableDynamicNodePopulator implements SearchableDynamicNodePopulator
{
    private final DynamicNodePopulator populator;
    private final Collection<CategoryModel> categories;
    private final Function<Object, Pair<DynamicNode, List<NavigationNode>>> resolver;


    /**
     * @param populator
     *           dynamic node populator that is capable of resolving data in the non-filtered structure
     * @param includedCategories
     *           collection of categories that should be included with their sub-trees (see
     *           {@link #shouldExplodeResultWithChildCategories(CategoryModel, Collection)}
     * @param resolver
     *           resolver used to fetch pairs of {@link DynamicNode} and List of {@link NavigationNode} as built by
     *           {@link CatalogTreeModelPopulator} during filtering
     */
    SpanningTreeModelBasedSearchableDynamicNodePopulator(final DynamicNodePopulator populator,
                    final Collection<CategoryModel> includedCategories,
                    final Function<Object, Pair<DynamicNode, List<NavigationNode>>> resolver)
    {
        Objects.requireNonNull(populator);
        Objects.requireNonNull(includedCategories);
        Objects.requireNonNull(resolver);
        this.populator = populator;
        this.categories = includedCategories;
        this.resolver = resolver;
    }


    @Override
    public List<NavigationNode> getChildren(final DynamicNode root, final String filterString, final boolean caseSensitive,
                    final TreeUtils.MatchMode matchMode)
    {
        return getChildren(root);
    }


    @Override
    public List<NavigationNode> getChildren(final NavigationNode node)
    {
        final Pair<DynamicNode, List<NavigationNode>> dn = resolver.apply(node.getData());
        if(dn != null && !dn.getSecond().isEmpty())
        {
            final List<NavigationNode> children = dn.getSecond();
            if(shouldExplodeResultWithChildCategories((CategoryModel)node.getData(), categories))
            {
                return getChildrenForExplodingCategory(node, children);
            }
            return children;
        }
        return Collections.unmodifiableList(populator.getChildren(node));
    }


    protected List<NavigationNode> getChildrenForExplodingCategory(final NavigationNode node, final List<NavigationNode> children)
    {
        final List<NavigationNode> res = new ArrayList<>(children);
        for(final NavigationNode child : populator.getChildren(node))
        {
            if(res.stream().noneMatch(r -> r.getData().equals(child.getData())))
            {
                res.add(child);
            }
        }
        return res;
    }


    /**
     * In case category belongs to the given set of categories of their sub-tree, the category should be included with all
     * its children (sub-trees).
     *
     * @param data
     *           category for which the check is conducted
     * @param categories
     *           collection of categories that should be included with all their sub-trees
     * @return true if the category should be included with its sub-tree
     */
    protected boolean shouldExplodeResultWithChildCategories(final CategoryModel data, final Collection<CategoryModel> categories)
    {
        return categories.contains(data) || data.getAllSupercategories().stream().anyMatch(categories::contains);
    }
}
