/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */

package com.hybris.backoffice.widgets.searchadapters.conditions.products;

import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.backoffice.navigation.impl.SimpleNode;
import com.hybris.backoffice.tree.model.CatalogTreeModelPopulator;
import com.hybris.backoffice.tree.model.UncategorizedNode;
import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchData;
import com.hybris.backoffice.widgets.advancedsearch.impl.SearchConditionData;
import com.hybris.backoffice.widgets.searchadapters.conditions.SearchConditionAdapter;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 * {@link SearchConditionAdapter} responsible for handling node representing uncategorized products.
 * The handler adds condition to {@link AdvancedSearchData} that product doesn't have assigned any category.
 * Moreover it finds and invokes handler for parent object which may represents CatalogModel or CatalogVersionModel
 */
public abstract class UncategorizedConditionAdapter extends SearchConditionAdapter
{
    public static final String PARENT_NODE_ID = "parentNode";
    private List<SearchConditionAdapter> conditionsAdapters;


    @Override
    public boolean canHandle(final NavigationNode node)
    {
        return StringUtils.endsWith(node.getId(), CatalogTreeModelPopulator.UNCATEGORIZED_PRODUCTS_NODE_ID)
                        && node.getData() instanceof UncategorizedNode;
    }


    protected abstract SearchConditionData buildUncategorizedSearchCondition();


    @Override
    public void addSearchCondition(final AdvancedSearchData searchData, final NavigationNode node)
    {
        final UncategorizedNode uncategorizedNode = (UncategorizedNode)node.getData();
        final Object parentItem = uncategorizedNode.getParentItem();
        final List<SearchConditionData> conditionList = new ArrayList<>();
        if(parentItem != null)
        {
            final AdvancedSearchData innerSearchData = new AdvancedSearchData();
            final SimpleNode simpleNode = new SimpleNode(PARENT_NODE_ID);
            simpleNode.setData(parentItem);
            conditionsAdapters.stream().filter(adapter -> adapter.canHandle(simpleNode)).findFirst()
                            .ifPresent(adapter -> adapter.addSearchCondition(innerSearchData, simpleNode));
            innerSearchData.getSearchFields().stream().map(searchField -> innerSearchData.getConditions(searchField)).forEach(conditionList::addAll);
        }
        final SearchConditionData uncategorizedSearchCondition = buildUncategorizedSearchCondition();
        if(uncategorizedSearchCondition != null)
        {
            conditionList.add(uncategorizedSearchCondition);
        }
        searchData.addConditionList(ValueComparisonOperator.AND, conditionList);
    }


    public List<SearchConditionAdapter> getConditionsAdapters()
    {
        return conditionsAdapters;
    }


    public void setConditionsAdapters(final List<SearchConditionAdapter> conditionsAdapters)
    {
        this.conditionsAdapters = conditionsAdapters;
    }
}
