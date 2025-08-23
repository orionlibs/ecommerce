package de.hybris.platform.servicelayer.security.strategies.impl;

import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.security.permissions.PermissionCheckValue;
import de.hybris.platform.servicelayer.security.permissions.PermissionChecker;
import de.hybris.platform.servicelayer.security.strategies.PrincipalHierarchyCheckingStrategy;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultPrincipalHierarchyCheckingStrategy implements PrincipalHierarchyCheckingStrategy
{
    private UserService userService;


    public UserService getUserService()
    {
        return this.userService;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    public PermissionCheckValue checkPermissionsForPrincipalHierarchy(PermissionChecker permissionChecker, PrincipalModel principal, String permissionName)
    {
        if(principal instanceof UserModel)
        {
            if(this.userService.isAdmin((UserModel)principal))
            {
                return PermissionCheckValue.ALLOWED;
            }
        }
        PermissionCheckValue result = permissionChecker.checkPermission(principal, permissionName);
        if(result != PermissionCheckValue.NOT_DEFINED)
        {
            return result;
        }
        Set<PrincipalGroupModel> alreadyProcessedGroups = new HashSet<>();
        Collection<PrincipalGroupModel> groups = getDirectGroupsForPrincipal(principal);
        while(result == PermissionCheckValue.NOT_DEFINED && CollectionUtils.isNotEmpty(groups))
        {
            Set<PrincipalGroupModel> nextGroups = null;
            boolean allowedFound = false;
            boolean deniedFound = false;
            for(PrincipalGroupModel grp : groups)
            {
                PermissionCheckValue grpMatch = permissionChecker.checkPermission((PrincipalModel)grp, permissionName);
                if(PermissionCheckValue.ALLOWED == grpMatch)
                {
                    allowedFound = true;
                    continue;
                }
                if(PermissionCheckValue.DENIED == grpMatch)
                {
                    deniedFound = true;
                    continue;
                }
                for(PrincipalGroupModel superGroup : getDirectGroupsForPrincipal((PrincipalModel)grp))
                {
                    if(alreadyProcessedGroups.add(superGroup))
                    {
                        if(nextGroups == null)
                        {
                            nextGroups = new HashSet<>();
                        }
                        nextGroups.add(superGroup);
                    }
                }
            }
            if(allowedFound)
            {
                result = deniedFound ? PermissionCheckValue.CONFLICTING : PermissionCheckValue.ALLOWED;
            }
            else if(deniedFound)
            {
                result = PermissionCheckValue.DENIED;
            }
            groups = nextGroups;
        }
        return result;
    }


    protected Collection<PrincipalGroupModel> getDirectGroupsForPrincipal(PrincipalModel principal)
    {
        return principal.getGroups();
    }
}
