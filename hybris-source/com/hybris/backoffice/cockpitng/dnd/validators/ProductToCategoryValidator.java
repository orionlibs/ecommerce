/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.cockpitng.dnd.validators;

import com.hybris.backoffice.widgets.contextpopulator.ContextPopulator;
import com.hybris.cockpitng.core.context.CockpitContext;
import com.hybris.cockpitng.dnd.BackofficeDragAndDropContext;
import com.hybris.cockpitng.dnd.DragAndDropActionType;
import com.hybris.cockpitng.dnd.DragAndDropContext;
import com.hybris.cockpitng.dnd.DropOperationData;
import com.hybris.cockpitng.validation.impl.DefaultValidationInfo;
import com.hybris.cockpitng.validation.model.ValidationInfo;
import com.hybris.cockpitng.validation.model.ValidationSeverity;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.variants.model.VariantProductModel;
import java.util.Collections;
import java.util.List;
import org.zkoss.util.resource.Labels;

/**
 * Validator returns errors when user tries to move product variants or user tries to move product to category whereas
 * products is currently assigned to more than one category.
 */
public class ProductToCategoryValidator implements DragAndDropValidator
{
    public static final String DND_VALIDATION_PRODUCT_TO_CATEGORY_WITHOUT_CONTEXT_MSG = "dnd.validation.product.to.category.without.context.msg";
    public static final String DND_VALIDATION_VERIANT_PRODUCT_TO_CATEGORY_MSG = "dnd.validation.variant.product.to.category.msg";


    @Override
    public boolean isApplicable(final DropOperationData operationData, final DragAndDropContext dragAndDropContext)
    {
        return ProductModel.class.isAssignableFrom(operationData.getDragged().getClass())
                        && CategoryModel.class.isAssignableFrom(operationData.getTarget().getClass());
    }


    @Override
    public List<ValidationInfo> validate(final DropOperationData operationData, final DragAndDropContext dragAndDropContext)
    {
        if(operationData.getDragged() instanceof VariantProductModel)
        {
            return Collections
                            .singletonList(createValidationInfo(ValidationSeverity.ERROR, DND_VALIDATION_VERIANT_PRODUCT_TO_CATEGORY_MSG));
        }
        if(isReplaceAction(dragAndDropContext))
        {
            return validateReplaceInternal(operationData, dragAndDropContext);
        }
        return Collections.emptyList();
    }


    protected boolean isReplaceAction(final DragAndDropContext dragAndDropContext)
    {
        return dragAndDropContext instanceof BackofficeDragAndDropContext
                        && ((BackofficeDragAndDropContext)dragAndDropContext).getActionType().equals(DragAndDropActionType.REPLACE);
    }


    private List<ValidationInfo> validateReplaceInternal(final DropOperationData operationData,
                    final DragAndDropContext dragAndDropContext)
    {
        final CockpitContext dragContext = dragAndDropContext.getDraggedContext();
        if(dragContext != null && isApplicable(operationData, dragAndDropContext))
        {
            final Object selectedObject = dragContext.getParameter(ContextPopulator.SELECTED_OBJECT);
            final ProductModel draggedProduct = (ProductModel)operationData.getDragged();
            final CategoryModel targetCategory = (CategoryModel)operationData.getTarget();
            if(shouldRaiseValidationError(selectedObject, draggedProduct, targetCategory))
            {
                return Collections.singletonList(
                                createValidationInfo(ValidationSeverity.ERROR, DND_VALIDATION_PRODUCT_TO_CATEGORY_WITHOUT_CONTEXT_MSG));
            }
        }
        return Collections.emptyList();
    }


    private static boolean shouldRaiseValidationError(final Object selectedObject, final ProductModel draggedProduct,
                    final CategoryModel targetCategory)
    {
        return !(selectedObject instanceof CategoryModel)
                        && (!containsOnlyTargetCategory(draggedProduct, targetCategory) || draggedProduct.getSupercategories().size() > 1);
    }


    private static boolean containsOnlyTargetCategory(final ProductModel product, final CategoryModel category)
    {
        return product.getSupercategories().size() == 1 && product.getSupercategories().contains(category);
    }


    protected DefaultValidationInfo createValidationInfo(final ValidationSeverity severity, final String labelKey,
                    final Object... labelArgs)
    {
        final DefaultValidationInfo validationInfo = new DefaultValidationInfo();
        validationInfo.setValidationMessage(getLabel(labelKey, labelArgs));
        validationInfo.setValidationSeverity(severity);
        return validationInfo;
    }


    protected String getLabel(final String labelKey, final Object... labelArgs)
    {
        return Labels.getLabel(labelKey, labelArgs);
    }
}
