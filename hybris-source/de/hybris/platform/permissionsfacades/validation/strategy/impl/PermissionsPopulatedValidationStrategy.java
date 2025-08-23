package de.hybris.platform.permissionsfacades.validation.strategy.impl;

import de.hybris.platform.permissionsfacades.data.TypePermissionsData;
import de.hybris.platform.permissionsfacades.data.TypePermissionsDataList;
import de.hybris.platform.permissionsfacades.validation.exception.PermissionsRequiredException;
import de.hybris.platform.permissionsfacades.validation.strategy.TypePermissionsListValidationStrategy;
import java.util.List;
import java.util.stream.IntStream;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

public class PermissionsPopulatedValidationStrategy implements TypePermissionsListValidationStrategy
{
    public void validate(TypePermissionsDataList permissions)
    {
        if(permissions != null && CollectionUtils.isNotEmpty(permissions.getPermissionsList()))
        {
            List<TypePermissionsData> permissionsList = permissions.getPermissionsList();
            IntStream.range(0, permissionsList.size()).filter(index -> (((TypePermissionsData)permissionsList.get(index)).getPermissions() == null))
                            .findFirst().ifPresent(index -> {
                                String type = ((TypePermissionsData)permissionsList.get(index)).getType();
                                if(StringUtils.isBlank(type))
                                {
                                    throw new PermissionsRequiredException(index + 1);
                                }
                                throw new PermissionsRequiredException(type);
                            });
        }
    }
}
