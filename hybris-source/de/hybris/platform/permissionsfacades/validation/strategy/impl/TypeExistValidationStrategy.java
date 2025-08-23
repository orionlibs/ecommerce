package de.hybris.platform.permissionsfacades.validation.strategy.impl;

import de.hybris.platform.permissionsfacades.data.TypePermissionsData;
import de.hybris.platform.permissionsfacades.data.TypePermissionsDataList;
import de.hybris.platform.permissionsfacades.validation.exception.TypeNotFoundException;
import de.hybris.platform.permissionsfacades.validation.strategy.TypePermissionsListValidationStrategy;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

public class TypeExistValidationStrategy implements TypePermissionsListValidationStrategy
{
    private final TypeService typeService;


    public TypeExistValidationStrategy(TypeService typeService)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("typeService", typeService);
        this.typeService = typeService;
    }


    public void validate(TypePermissionsDataList permissions)
    {
        if(permissions != null && CollectionUtils.isNotEmpty(permissions.getPermissionsList()))
        {
            List<TypePermissionsData> permissionsList = permissions.getPermissionsList();
            permissionsList.stream().map(TypePermissionsData::getType).filter(StringUtils::isNotBlank).forEach(type -> {
                try
                {
                    getTypeService().getComposedTypeForCode(type);
                }
                catch(UnknownIdentifierException e)
                {
                    throw new TypeNotFoundException(type);
                }
            });
        }
    }


    protected TypeService getTypeService()
    {
        return this.typeService;
    }
}
