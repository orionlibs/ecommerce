/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.cockpitng.dnd.validators;

import com.hybris.backoffice.cockpitng.dataaccess.facades.permissions.DefaultPlatformPermissionFacadeStrategy;
import com.hybris.cockpitng.dnd.DragAndDropContext;
import com.hybris.cockpitng.dnd.DropOperationData;
import com.hybris.cockpitng.validation.impl.DefaultValidationInfo;
import com.hybris.cockpitng.validation.model.ValidationInfo;
import com.hybris.cockpitng.validation.model.ValidationSeverity;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.zkoss.util.resource.Labels;

public class WritePermissionValidator implements DragAndDropValidator
{
    private static final String NO_PERMISSION_ERROR = "dnd.validation.permission.error.msg";
    private UserService userService;
    private CatalogVersionService catalogVersionService;
    private DefaultPlatformPermissionFacadeStrategy permissionFacadeStrategy;


    @Override
    public boolean isApplicable(final DropOperationData operationData, final DragAndDropContext dragAndDropContext)
    {
        return CategoryModel.class.isAssignableFrom(operationData.getDragged().getClass()) || ProductModel.class.isAssignableFrom(operationData.getDragged().getClass());
    }


    @Override
    public List<ValidationInfo> validate(final DropOperationData operationData, final DragAndDropContext dragAndDropContext)
    {
        final Object dragged = operationData.getDragged();
        Boolean validateResult = validateTypePermission(dragged);
        if(Boolean.TRUE.equals(validateResult) && CategoryModel.class.isAssignableFrom(operationData.getDragged().getClass()))
        {
            validateResult = validateCatalogVersionPermission(((CategoryModel)dragged).getCatalogVersion());
        }
        else if(Boolean.TRUE.equals(validateResult) && ProductModel.class.isAssignableFrom(operationData.getDragged().getClass()))
        {
            validateResult = validateCatalogVersionPermission(((ProductModel)dragged).getCatalogVersion());
        }
        return Boolean.TRUE.equals(validateResult) ? Collections.emptyList() : getPermissionErrorInfo();
    }


    private boolean validateCatalogVersionPermission(final CatalogVersionModel catalogVersion)
    {
        final UserModel currentUser = getUserService().getCurrentUser();
        final Collection<CatalogVersionModel> allWritableCatalogVersions = getAllWritableCatalogVersions(currentUser);
        return allWritableCatalogVersions.contains(catalogVersion);
    }


    private boolean validateTypePermission(final Object dragged)
    {
        return getPermissionFacadeStrategy().canChangeInstance(dragged);
    }


    private Collection<CatalogVersionModel> getAllWritableCatalogVersions(final UserModel currentUser)
    {
        return getUserService().isAdmin(currentUser) ? getCatalogVersionService().getAllCatalogVersions()
                        : getCatalogVersionService().getAllWritableCatalogVersions(currentUser);
    }


    private List<ValidationInfo> getPermissionErrorInfo(final Object... labelArgs)
    {
        final DefaultValidationInfo validationInfo = new DefaultValidationInfo();
        validationInfo.setValidationMessage(Labels.getLabel(NO_PERMISSION_ERROR, labelArgs));
        validationInfo.setValidationSeverity(ValidationSeverity.ERROR);
        return Collections.singletonList(validationInfo);
    }


    protected UserService getUserService()
    {
        return userService;
    }


    public void setUserService(final UserService userService)
    {
        this.userService = userService;
    }


    protected CatalogVersionService getCatalogVersionService()
    {
        return catalogVersionService;
    }


    public void setCatalogVersionService(final CatalogVersionService catalogVersionService)
    {
        this.catalogVersionService = catalogVersionService;
    }


    protected DefaultPlatformPermissionFacadeStrategy getPermissionFacadeStrategy()
    {
        return permissionFacadeStrategy;
    }


    public void setPermissionFacadeStrategy(DefaultPlatformPermissionFacadeStrategy permissionFacadeStrategy)
    {
        this.permissionFacadeStrategy = permissionFacadeStrategy;
    }
}
