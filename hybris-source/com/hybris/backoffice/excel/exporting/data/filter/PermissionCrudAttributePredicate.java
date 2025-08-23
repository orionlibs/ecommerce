package com.hybris.backoffice.excel.exporting.data.filter;

import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.servicelayer.security.permissions.PermissionCRUDService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class PermissionCrudAttributePredicate implements ExcelExportAttributePredicate
{
    private static final Logger LOG = LoggerFactory.getLogger(PermissionCrudAttributePredicate.class);
    private PermissionCRUDService permissionCRUDService;


    public boolean test(AttributeDescriptorModel attributeDescriptorModel)
    {
        boolean canReadAttribute = getPermissionCRUDService().canReadAttribute(attributeDescriptorModel);
        if(!canReadAttribute)
        {
            logWarn("Insufficient permissions for attribute: '{}'", new Object[] {attributeDescriptorModel.getQualifier()});
        }
        return canReadAttribute;
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
