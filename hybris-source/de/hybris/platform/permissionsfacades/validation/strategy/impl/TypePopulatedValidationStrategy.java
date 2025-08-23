package de.hybris.platform.permissionsfacades.validation.strategy.impl;

import de.hybris.platform.permissionsfacades.data.TypePermissionsData;
import de.hybris.platform.permissionsfacades.data.TypePermissionsDataList;
import de.hybris.platform.permissionsfacades.validation.exception.TypeRequiredException;
import de.hybris.platform.permissionsfacades.validation.strategy.TypePermissionsListValidationStrategy;
import java.util.List;
import java.util.stream.IntStream;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

public class TypePopulatedValidationStrategy implements TypePermissionsListValidationStrategy
{
    public void validate(TypePermissionsDataList permissions)
    {
        if(permissions != null && CollectionUtils.isNotEmpty(permissions.getPermissionsList()))
        {
            List<TypePermissionsData> permissionsList = permissions.getPermissionsList();
            IntStream.range(0, permissionsList.size()).filter(idx -> StringUtils.isBlank(((TypePermissionsData)permissionsList.get(idx)).getType())).findFirst()
                            .ifPresent(idx -> {
                                throw new TypeRequiredException(idx + 1);
                            });
        }
    }
}
