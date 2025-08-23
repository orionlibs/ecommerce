package com.hybris.backoffice.user;

import de.hybris.platform.core.Constants;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.impl.DefaultUserService;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Required;

public class BackofficeUserService extends DefaultUserService
{
    public static final String BACKOFFICE_ADMIN_GROUP = "backofficeadmingroup";
    private static final String CORE_ADMIN_GROUP = Constants.USER.ADMIN_USERGROUP;
    private BackofficeRoleService backofficeRoleService;


    public boolean isAdmin(UserModel user)
    {
        if(getBackofficeRoleService().shouldTreatRolesAsGroups() || !isCurrentUser(user))
        {
            return super.isAdmin(user);
        }
        return (isAdminGroupInActiveRoleHierarchy() || isAdminGroupInNonRoleHierarchy(user));
    }


    protected boolean isCurrentUser(UserModel user)
    {
        return Objects.equals(getCurrentUser(), user);
    }


    protected boolean isAdminGroupInActiveRoleHierarchy()
    {
        return getBackofficeRoleService().getActiveRolePrincipalsHierarchy().stream().map(PrincipalModel::getUid)
                        .anyMatch(this::isAdminGroup);
    }


    protected boolean isAdminGroupInNonRoleHierarchy(UserModel user)
    {
        if(user == null)
        {
            return false;
        }
        return getBackofficeRoleService().getNonRolePrincipalsHierarchy((PrincipalModel)user).stream().map(PrincipalModel::getUid)
                        .anyMatch(this::isAdminGroup);
    }


    protected boolean isAdminGroup(String groupId)
    {
        return ("backofficeadmingroup".equals(groupId) || CORE_ADMIN_GROUP.equals(groupId));
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
}
