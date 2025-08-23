/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.cockpitng.dnd.handlers;

import com.hybris.backoffice.widgets.contextpopulator.ContextPopulator;
import com.hybris.cockpitng.core.context.CockpitContext;
import com.hybris.cockpitng.dnd.DragAndDropContext;
import com.hybris.cockpitng.dnd.DropOperationData;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Drop handler responsible for appending/replacing supercategories for product.
 */
public class ProductToCategoryDropHandler extends AbstractReferenceDropHandler<ProductModel, CategoryModel>
{
    /**
     * @deprecated since 1905, do not use in subclasses
     */
    @Deprecated(since = "1905", forRemoval = true)
    public static final Logger LOG = LoggerFactory.getLogger(ProductToCategoryDropHandler.class);
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductToCategoryDropHandler.class);
    public static final String APPEND_NOTIFICATION_KEY = "dragAndDropProductToCategoryAppend";
    public static final String REPLACE_NOTIFICATION_KEY = "dragAndDropProductToCategoryReplace";


    @Override
    protected List<DropOperationData<ProductModel, CategoryModel, Object>> handleAppend(final List<ProductModel> dragged,
                    final CategoryModel targetCategory, final DragAndDropContext context)
    {
        final List<DropOperationData<ProductModel, CategoryModel, Object>> result = new ArrayList<>();
        for(final ProductModel draggedProduct : dragged)
        {
            final ProductModel modifiedProduct = copyProductToCategory(draggedProduct, targetCategory, context);
            final DropOperationData<ProductModel, CategoryModel, Object> singleResult = new DropOperationData<>(draggedProduct,
                            targetCategory, modifiedProduct, context, APPEND_NOTIFICATION_KEY);
            result.add(singleResult);
        }
        return result;
    }


    @Override
    protected List<DropOperationData<ProductModel, CategoryModel, Object>> handleReplace(final List<ProductModel> dragged,
                    final CategoryModel targetCategory, final DragAndDropContext context)
    {
        final List<DropOperationData<ProductModel, CategoryModel, Object>> result = new ArrayList<>();
        for(final ProductModel productModel : dragged)
        {
            final ProductModel modifiedProduct = moveProductToCategory(productModel, targetCategory, context);
            final DropOperationData<ProductModel, CategoryModel, Object> operationData = new DropOperationData<>(productModel,
                            targetCategory, modifiedProduct, context, REPLACE_NOTIFICATION_KEY);
            result.add(operationData);
        }
        return result;
    }


    protected ProductModel copyProductToCategory(final ProductModel product, final CategoryModel category,
                    final DragAndDropContext context)
    {
        final List<CategoryModel> supercategories = new ArrayList<>(product.getSupercategories());
        if(!supercategories.contains(category))
        {
            supercategories.add(category);
            product.setSupercategories(supercategories);
        }
        return product;
    }


    protected ProductModel moveProductToCategory(final ProductModel product, final CategoryModel category,
                    final DragAndDropContext context)
    {
        final CockpitContext dragContext = context.getDraggedContext();
        if(dragContext != null)
        {
            final Object selectedObject = dragContext.getParameter(ContextPopulator.SELECTED_OBJECT);
            if(selectedObject instanceof CategoryModel)
            {
                handleMoveProductToCategory(product, category, (CategoryModel)selectedObject);
            }
            else if(CollectionUtils.isEmpty(product.getSupercategories()))
            {
                product.setSupercategories(Collections.singleton(category));
            }
            else
            {
                LOGGER.warn("Unexpected context object passed: {}", selectedObject);
            }
        }
        return product;
    }


    protected void handleMoveProductToCategory(final ProductModel product, final CategoryModel category,
                    final CategoryModel selectedCategory)
    {
        final Collection<CategoryModel> subcategories = selectedCategory.getAllSubcategories();
        final Collection<CategoryModel> supercategories = new HashSet<>(product.getSupercategories());
        supercategories.remove(selectedCategory);
        supercategories.removeAll(subcategories);
        supercategories.add(category);
        product.setSupercategories(supercategories);
    }


    @Override
    public List<String> findSupportedTypes()
    {
        return Collections.singletonList(ProductModel._TYPECODE);
    }
}
