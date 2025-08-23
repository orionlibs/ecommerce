package de.hybris.platform.permissionsfacades.validation.strategy.impl;

import de.hybris.platform.permissionsfacades.data.TypePermissionsData;
import de.hybris.platform.permissionsfacades.data.TypePermissionsDataList;
import de.hybris.platform.permissionsfacades.validation.exception.TypeDuplicatedException;
import de.hybris.platform.permissionsfacades.validation.strategy.TypePermissionsListValidationStrategy;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

public class TypesDuplicatedValidationStrategy implements TypePermissionsListValidationStrategy
{
    public void validate(TypePermissionsDataList permissions)
    {
        if(permissions != null && CollectionUtils.isNotEmpty(permissions.getPermissionsList()))
        {
            ((LinkedHashMap)permissions.getPermissionsList().stream().map(TypePermissionsData::getType).filter(StringUtils::isNotBlank)
                            .collect(Collectors.groupingBy(String::toUpperCase, LinkedHashMap::new, Collectors.toList()))).entrySet().stream()
                            .filter(entry -> (((List)entry.getValue()).size() > 1)).findFirst().ifPresent(entry -> {
                                throw new TypeDuplicatedException((String)((List)entry.getValue()).get(0));
                            });
        }
    }
}
