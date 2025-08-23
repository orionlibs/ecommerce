package de.hybris.platform.servicelayer.security.permissions.impl;

import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.security.permissions.PermissionCRUDService;
import de.hybris.platform.servicelayer.security.permissions.PermissionCheckingService;
import org.springframework.beans.factory.annotation.Required;

public class DefaultPermissionCRUDService implements PermissionCRUDService
{
    private PermissionCheckingService permissionCheckingService;


    public boolean canReadType(ComposedTypeModel type)
    {
        return this.permissionCheckingService.checkTypePermission(type, "read").isGranted();
    }


    public boolean canReadType(String typeCode)
    {
        return this.permissionCheckingService.checkTypePermission(typeCode, "read").isGranted();
    }


    public boolean canReadAttribute(AttributeDescriptorModel attributeDescriptor)
    {
        return this.permissionCheckingService.checkAttributeDescriptorPermission(attributeDescriptor, "read")
                        .isGranted();
    }


    public boolean canReadAttribute(String typeCode, String attributeQualifier)
    {
        return this.permissionCheckingService
                        .checkAttributeDescriptorPermission(typeCode, attributeQualifier, "read").isGranted();
    }


    public boolean canChangeType(ComposedTypeModel type)
    {
        return this.permissionCheckingService.checkTypePermission(type, "change").isGranted();
    }


    public boolean canChangeType(String typeCode)
    {
        return this.permissionCheckingService.checkTypePermission(typeCode, "change").isGranted();
    }


    public boolean canChangeAttribute(AttributeDescriptorModel attributeDescriptor)
    {
        return this.permissionCheckingService.checkAttributeDescriptorPermission(attributeDescriptor, "change")
                        .isGranted();
    }


    public boolean canChangeAttribute(String typeCode, String attributeQualifier)
    {
        return this.permissionCheckingService.checkAttributeDescriptorPermission(typeCode, attributeQualifier, "change")
                        .isGranted();
    }


    public boolean canCreateTypeInstance(ComposedTypeModel type)
    {
        return this.permissionCheckingService.checkTypePermission(type, "create").isGranted();
    }


    public boolean canCreateTypeInstance(String typeCode)
    {
        return this.permissionCheckingService.checkTypePermission(typeCode, "create").isGranted();
    }


    public boolean canRemoveTypeInstance(ComposedTypeModel type)
    {
        return this.permissionCheckingService.checkTypePermission(type, "remove").isGranted();
    }


    public boolean canRemoveTypeInstance(String typeCode)
    {
        return this.permissionCheckingService.checkTypePermission(typeCode, "remove").isGranted();
    }


    public boolean canChangeTypePermission(ComposedTypeModel type)
    {
        return this.permissionCheckingService.checkTypePermission(type, "changerights").isGranted();
    }


    public boolean canChangeTypePermission(String typeCode)
    {
        return this.permissionCheckingService.checkTypePermission(typeCode, "changerights").isGranted();
    }


    public boolean canChangeAttributePermission(AttributeDescriptorModel attributeDescriptor)
    {
        return this.permissionCheckingService.checkAttributeDescriptorPermission(attributeDescriptor, "changerights")
                        .isGranted();
    }


    public boolean canChangeAttributePermission(String typeCode, String attributeQualifier)
    {
        return this.permissionCheckingService.checkAttributeDescriptorPermission(typeCode, attributeQualifier, "changerights")
                        .isGranted();
    }


    public PermissionCheckingService getPermissionCheckingService()
    {
        return this.permissionCheckingService;
    }


    @Required
    public void setPermissionCheckingService(PermissionCheckingService permissionCheckingService)
    {
        this.permissionCheckingService = permissionCheckingService;
    }
}
