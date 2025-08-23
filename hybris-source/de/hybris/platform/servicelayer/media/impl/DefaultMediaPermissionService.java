package de.hybris.platform.servicelayer.media.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.servicelayer.media.MediaPermissionService;
import de.hybris.platform.servicelayer.security.permissions.PermissionAssignment;
import de.hybris.platform.servicelayer.security.permissions.PermissionCheckingService;
import de.hybris.platform.servicelayer.security.permissions.PermissionManagementService;
import java.util.Collection;
import java.util.HashSet;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class DefaultMediaPermissionService implements MediaPermissionService
{
    private static final Logger LOG = Logger.getLogger(DefaultMediaPermissionService.class);
    private PermissionCheckingService permissionCheckingService;
    private PermissionManagementService permissionManagementService;


    public boolean isReadAccessGranted(MediaModel mediaItem, PrincipalModel principal)
    {
        return this.permissionCheckingService.checkItemPermission((ItemModel)mediaItem, principal, "read").isGranted();
    }


    public void grantReadPermission(MediaModel mediaItem, PrincipalModel principal)
    {
        checkOrCreatePermission("read");
        PermissionAssignment permissionAssignment = new PermissionAssignment("read", principal);
        this.permissionManagementService.addItemPermission((ItemModel)mediaItem, new PermissionAssignment[] {permissionAssignment});
    }


    public void denyReadPermission(MediaModel mediaItem, PrincipalModel principal)
    {
        checkOrCreatePermission("read");
        PermissionAssignment permissionAssignment = new PermissionAssignment("read", principal, true);
        this.permissionManagementService.addItemPermission((ItemModel)mediaItem, new PermissionAssignment[] {permissionAssignment});
    }


    public Collection<PrincipalModel> getPermittedPrincipals(MediaModel mediaItem)
    {
        Collection<PrincipalModel> permittedPrincipals = new HashSet<>();
        Collection<PermissionAssignment> permissionAssignments = this.permissionManagementService.getItemPermissionsForName((ItemModel)mediaItem, new String[] {"read"});
        for(PermissionAssignment assignment : permissionAssignments)
        {
            if(assignment.isGranted())
            {
                permittedPrincipals.add(assignment.getPrincipal());
            }
        }
        return permittedPrincipals;
    }


    private void checkOrCreatePermission(String permissionName)
    {
        if(this.permissionManagementService.getDefinedPermissions().contains(permissionName))
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Permission " + permissionName + " already exists");
            }
        }
        else
        {
            this.permissionManagementService.createPermission(permissionName);
            if(LOG.isDebugEnabled())
            {
                LOG.debug("New Permission object " + permissionName + " has been created");
            }
        }
    }


    @Required
    public void setPermissionCheckingService(PermissionCheckingService permissionCheckingService)
    {
        this.permissionCheckingService = permissionCheckingService;
    }


    @Required
    public void setPermissionManagementService(PermissionManagementService permissionManagementService)
    {
        this.permissionManagementService = permissionManagementService;
    }


    public Collection<PrincipalModel> getDeniedPrincipals(MediaModel mediaItem)
    {
        Collection<PrincipalModel> deniedPrincipals = new HashSet<>();
        Collection<PermissionAssignment> permissionAssignments = this.permissionManagementService.getItemPermissionsForName((ItemModel)mediaItem, new String[] {"read"});
        for(PermissionAssignment assignment : permissionAssignments)
        {
            if(assignment.isDenied())
            {
                deniedPrincipals.add(assignment.getPrincipal());
            }
        }
        return deniedPrincipals;
    }


    public void setPermittedPrincipals(MediaModel mediaItem, Collection<PrincipalModel> principals)
    {
        Collection<PrincipalModel> existingGrantedPrincipals = getPermittedPrincipals(mediaItem);
        for(PrincipalModel existingGrantedPrincipal : existingGrantedPrincipals)
        {
            if(!principals.contains(existingGrantedPrincipal))
            {
                this.permissionManagementService.removeItemPermission((ItemModel)mediaItem, new PermissionAssignment[] {new PermissionAssignment("read", existingGrantedPrincipal)});
            }
        }
        for(PrincipalModel principal : principals)
        {
            grantReadPermission(mediaItem, principal);
        }
    }


    public void setDeniedPrincipals(MediaModel mediaItem, Collection<PrincipalModel> principals)
    {
        Collection<PrincipalModel> existingDeniedPrincipals = getDeniedPrincipals(mediaItem);
        for(PrincipalModel existingDeniedPrincipal : existingDeniedPrincipals)
        {
            if(!principals.contains(existingDeniedPrincipal))
            {
                this.permissionManagementService.removeItemPermission((ItemModel)mediaItem, new PermissionAssignment[] {new PermissionAssignment("read", existingDeniedPrincipal)});
            }
        }
        for(PrincipalModel principal : principals)
        {
            denyReadPermission(mediaItem, principal);
        }
    }
}
