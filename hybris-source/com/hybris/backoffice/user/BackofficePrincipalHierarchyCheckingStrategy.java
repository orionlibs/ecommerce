package com.hybris.backoffice.user;

import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.security.permissions.PermissionCheckValue;
import de.hybris.platform.servicelayer.security.permissions.PermissionChecker;
import de.hybris.platform.servicelayer.security.strategies.impl.DefaultPrincipalHierarchyCheckingStrategy;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.springframework.beans.factory.annotation.Required;

public class BackofficePrincipalHierarchyCheckingStrategy extends DefaultPrincipalHierarchyCheckingStrategy
{
    private BackofficeRoleService backofficeRoleService;
    private BackofficeUserService backofficeUserService;
    private final ThreadLocal<Set<PrincipalGroupModel>> activeRoleHierarchy = new ThreadLocal<>();


    public PermissionCheckValue checkPermissionsForPrincipalHierarchy(PermissionChecker permissionChecker, PrincipalModel principal, String permissionName)
    {
        this.activeRoleHierarchy.set(new HashSet<>());
        try
        {
            return super.checkPermissionsForPrincipalHierarchy(permissionChecker, principal, permissionName);
        }
        finally
        {
            this.activeRoleHierarchy.remove();
        }
    }


    protected Collection<PrincipalGroupModel> getDirectGroupsForPrincipal(PrincipalModel principal)
    {
        ServicesUtil.validateParameterNotNull(principal, "Principal cannot be null");
        Collection<PrincipalGroupModel> allDirectGroups = super.getDirectGroupsForPrincipal(principal);
        if(getBackofficeRoleService().shouldTreatRolesAsGroups())
        {
            return allDirectGroups;
        }
        if(principal instanceof PrincipalGroupModel && isInActiveRoleHierarchy((PrincipalGroupModel)principal))
        {
            ((Set<PrincipalGroupModel>)this.activeRoleHierarchy.get()).addAll(allDirectGroups);
            return allDirectGroups;
        }
        Collection<PrincipalGroupModel> filteredGroups = getBackofficeRoleService().filterOutRolePrincipals(allDirectGroups);
        if(principal instanceof UserModel && getBackofficeUserService().isCurrentUser((UserModel)principal))
        {
            Objects.requireNonNull(filteredGroups);
            getBackofficeRoleService().getActiveRoleModel().ifPresent(filteredGroups::add);
        }
        return filteredGroups;
    }


    protected boolean isInActiveRoleHierarchy(PrincipalGroupModel principal)
    {
        if(principal == null)
        {
            return false;
        }
        return (((Set)this.activeRoleHierarchy.get()).contains(principal) || getBackofficeRoleService().isActiveRole(principal.getUid()));
    }


    protected BackofficeRoleService getBackofficeRoleService()
    {
        return this.backofficeRoleService;
    }


    @Required
    public void setBackofficeRoleService(BackofficeRoleService backofficeRoleService)
    {
        this.backofficeRoleService = backofficeRoleService;
    }


    private BackofficeUserService getBackofficeUserService()
    {
        return this.backofficeUserService;
    }


    @Required
    public void setBackofficeUserService(BackofficeUserService backofficeUserService)
    {
        this.backofficeUserService = backofficeUserService;
    }
}
