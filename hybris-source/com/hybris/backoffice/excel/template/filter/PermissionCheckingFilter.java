package com.hybris.backoffice.excel.template.filter;

import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.servicelayer.security.permissions.PermissionCRUDService;
import javax.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Required;

public class PermissionCheckingFilter implements ExcelFilter<AttributeDescriptorModel>
{
    private PermissionCRUDService permissionCRUDService;


    public boolean test(@Nonnull AttributeDescriptorModel attributeDescriptor)
    {
        return this.permissionCRUDService.canReadAttribute(attributeDescriptor);
    }


    @Required
    public void setPermissionCRUDService(PermissionCRUDService permissionCRUDService)
    {
        this.permissionCRUDService = permissionCRUDService;
    }
}
