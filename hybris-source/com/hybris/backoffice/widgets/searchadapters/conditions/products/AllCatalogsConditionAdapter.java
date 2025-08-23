/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.searchadapters.conditions.products;

import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.backoffice.tree.model.CatalogTreeModelPopulator;
import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchData;
import com.hybris.backoffice.widgets.searchadapters.conditions.SearchConditionAdapter;
import org.apache.commons.lang3.StringUtils;

/**
 * {@link SearchConditionAdapter} responsible for handling AllCatalogs node.
 */
public class AllCatalogsConditionAdapter extends SearchConditionAdapter
{
    @Override
    public boolean canHandle(final NavigationNode node)
    {
        return StringUtils.endsWith(node.getId(), CatalogTreeModelPopulator.ALL_CATALOGS_NODE_ID);
    }


    @Override
    public void addSearchCondition(final AdvancedSearchData searchData, final NavigationNode node)
    {
        // do nothing. allCatalogs shouldn't add any conditions
    }
}
