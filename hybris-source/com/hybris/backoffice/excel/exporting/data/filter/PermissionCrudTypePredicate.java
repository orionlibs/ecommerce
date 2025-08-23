package com.hybris.backoffice.excel.exporting.data.filter;

import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.security.permissions.PermissionCRUDService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class PermissionCrudTypePredicate implements ExcelExportTypePredicate
{
    private static final Logger LOG = LoggerFactory.getLogger(PermissionCrudTypePredicate.class);
    private PermissionCRUDService permissionCRUDService;


    public boolean test(ComposedTypeModel type)
    {
        boolean canReadType = getPermissionCRUDService().canReadType(type);
        if(!canReadType)
        {
            logWarn("Insufficient permissions for type: '{}'", new Object[] {type.getCode()});
        }
        return canReadType;
    }


    private void logWarn(String message, Object... params)
    {
        LOG.warn(message, params);
    }


    public PermissionCRUDService getPermissionCRUDService()
    {
        return this.permissionCRUDService;
    }


    @Required
    public void setPermissionCRUDService(PermissionCRUDService permissionCRUDService)
    {
        this.permissionCRUDService = permissionCRUDService;
    }
}
