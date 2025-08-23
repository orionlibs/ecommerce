package de.hybris.platform.permissionsfacades.validation.strategy.impl;

import de.hybris.platform.permissionsfacades.data.TypePermissionsDataList;
import de.hybris.platform.permissionsfacades.validation.exception.PermissionsListRequiredException;
import de.hybris.platform.permissionsfacades.validation.strategy.TypePermissionsListValidationStrategy;
import org.apache.commons.collections4.CollectionUtils;

public class PermissionsListPopulatedValidationStrategy implements TypePermissionsListValidationStrategy
{
    public void validate(TypePermissionsDataList permissions)
    {
        if(permissions != null && CollectionUtils.isEmpty(permissions.getPermissionsList()))
        {
            throw new PermissionsListRequiredException();
        }
    }
}
