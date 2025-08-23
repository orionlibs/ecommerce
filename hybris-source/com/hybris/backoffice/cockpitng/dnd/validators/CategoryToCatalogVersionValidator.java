/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.cockpitng.dnd.validators;

import com.hybris.cockpitng.dnd.BackofficeDragAndDropContext;
import com.hybris.cockpitng.dnd.DragAndDropActionType;
import com.hybris.cockpitng.dnd.DragAndDropContext;
import com.hybris.cockpitng.dnd.DropOperationData;
import com.hybris.cockpitng.validation.impl.DefaultValidationInfo;
import com.hybris.cockpitng.validation.model.ValidationInfo;
import com.hybris.cockpitng.validation.model.ValidationSeverity;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import java.util.Collections;
import java.util.List;
import org.zkoss.util.resource.Labels;

public class CategoryToCatalogVersionValidator implements DragAndDropValidator
{
    public static final String NO_APPEND_MSG = "dnd.validation.category.to.catalogversion.no.append.msg";
    public static final String HAS_SUPERCATEGORY_MSG = "dnd.validation.category.to.catalogversion.has.supercategory.msg";
    public static final String NOT_ALLOW_CHANGE_CATALOGVERSION = "dnd.validation.category.to.catalogversion.changed.catelogversion.msg";


    @Override
    public boolean isApplicable(final DropOperationData operationData, final DragAndDropContext dragAndDropContext)
    {
        return CategoryModel.class.isAssignableFrom(operationData.getDragged().getClass())
                        && CatalogVersionModel.class.isAssignableFrom(operationData.getTarget().getClass());
    }


    @Override
    public List<ValidationInfo> validate(final DropOperationData operationData, final DragAndDropContext dragAndDropContext)
    {
        if(dragAndDropContext instanceof BackofficeDragAndDropContext
                        && ((BackofficeDragAndDropContext)dragAndDropContext).getActionType().equals(DragAndDropActionType.APPEND))
        {
            return Collections.singletonList(createValidationInfo(ValidationSeverity.ERROR, NO_APPEND_MSG));
        }
        if(!validateCatalogVersion((CategoryModel)operationData.getDragged(), (CatalogVersionModel)operationData.getTarget()))
        {
            return Collections.singletonList(createValidationInfo(ValidationSeverity.ERROR, NOT_ALLOW_CHANGE_CATALOGVERSION));
        }
        final CategoryModel modified = (CategoryModel)operationData.getModified();
        if(modified != null && !modified.getSupercategories().isEmpty())
        {
            return Collections.singletonList(createValidationInfo(ValidationSeverity.WARN, HAS_SUPERCATEGORY_MSG));
        }
        else
        {
            return Collections.emptyList();
        }
    }


    private boolean validateCatalogVersion(final CategoryModel category, final CatalogVersionModel catalogVersion)
    {
        return category.getCatalogVersion().equals(catalogVersion);
    }


    private DefaultValidationInfo createValidationInfo(final ValidationSeverity severity, final String labelKey,
                    final Object... labelArgs)
    {
        final DefaultValidationInfo validationInfo = new DefaultValidationInfo();
        validationInfo.setValidationMessage(Labels.getLabel(labelKey, labelArgs));
        validationInfo.setValidationSeverity(severity);
        return validationInfo;
    }
}
