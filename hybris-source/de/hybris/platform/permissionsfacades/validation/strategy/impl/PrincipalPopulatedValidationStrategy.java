package de.hybris.platform.permissionsfacades.validation.strategy.impl;

import de.hybris.platform.permissionsfacades.data.TypePermissionsDataList;
import de.hybris.platform.permissionsfacades.validation.exception.PrincipalRequiredException;
import de.hybris.platform.permissionsfacades.validation.strategy.TypePermissionsListValidationStrategy;
import org.codehaus.plexus.util.StringUtils;

public class PrincipalPopulatedValidationStrategy implements TypePermissionsListValidationStrategy
{
    public void validate(TypePermissionsDataList permissions)
    {
        if(permissions != null && StringUtils.isBlank(permissions.getPrincipalUid()))
        {
            throw new PrincipalRequiredException();
        }
    }
}
