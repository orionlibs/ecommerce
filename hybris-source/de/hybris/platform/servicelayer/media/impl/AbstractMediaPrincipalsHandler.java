package de.hybris.platform.servicelayer.media.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.servicelayer.security.permissions.PermissionAssignment;
import de.hybris.platform.servicelayer.security.permissions.PermissionManagementService;
import java.util.Collection;
import java.util.stream.Collectors;

public abstract class AbstractMediaPrincipalsHandler
{
    protected PermissionManagementService permissionManagementService;
    protected static final String READ_PERMISSION = "read";


    protected Collection<PermissionAssignment> getPermittedPermissions(MediaModel model)
    {
        Collection<PermissionAssignment> itemPermissions = this.permissionManagementService.getItemPermissions((ItemModel)model);
        return (Collection<PermissionAssignment>)itemPermissions.stream()
                        .filter(p -> (p.getPermissionName().equals("read") && p.isGranted() == true))
                        .collect(Collectors.toSet());
    }


    protected Collection<PrincipalModel> getPermittedPrincipals(MediaModel model)
    {
        return (Collection<PrincipalModel>)getPermittedPermissions(model).stream().map(p -> p.getPrincipal()).collect(Collectors.toSet());
    }


    protected Collection<PermissionAssignment> getDeniedPermissions(MediaModel model)
    {
        Collection<PermissionAssignment> itemPermissions = this.permissionManagementService.getItemPermissions((ItemModel)model);
        return (Collection<PermissionAssignment>)itemPermissions.stream()
                        .filter(p -> (p.getPermissionName().equals("read") && !p.isGranted()))
                        .collect(Collectors.toSet());
    }


    protected Collection<PrincipalModel> getDeniedPrincipals(MediaModel model)
    {
        return (Collection<PrincipalModel>)getDeniedPermissions(model).stream().map(p -> p.getPrincipal()).collect(Collectors.toSet());
    }


    public void setPermissionManagementService(PermissionManagementService permissionManagementService)
    {
        this.permissionManagementService = permissionManagementService;
    }
}
