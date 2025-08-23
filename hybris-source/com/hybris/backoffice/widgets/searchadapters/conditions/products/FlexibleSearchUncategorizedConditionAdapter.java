/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.searchadapters.conditions.products;

import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchData;
import com.hybris.backoffice.widgets.advancedsearch.impl.SearchConditionData;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import de.hybris.platform.core.model.product.ProductModel;

/**
 * {@link UncategorizedConditionAdapter} responsible for handling node representing uncategorized products.
 * The handler adds condition to {@link AdvancedSearchData} that product doesn't have assigned any category.
 * Moreover it finds and invokes handler for parent object which may represents CatalogModel or CatalogVersionModel
 */
public class FlexibleSearchUncategorizedConditionAdapter extends UncategorizedConditionAdapter
{
    protected SearchConditionData buildUncategorizedSearchCondition()
    {
        return createSearchConditions(ProductModel.SUPERCATEGORIES, null,
                        ValueComparisonOperator.IS_EMPTY);
    }
}
