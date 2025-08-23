package com.hybris.backoffice.user;

import com.hybris.backoffice.model.user.BackofficeRoleModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import java.util.Collection;
import java.util.Optional;

public interface BackofficeRoleService
{
    boolean shouldTreatRolesAsGroups();


    Optional<BackofficeRoleModel> getActiveRoleModel();


    boolean isActiveRole(String paramString);


    Optional<String> getActiveRole();


    void setActiveRole(String paramString);


    Collection<PrincipalGroupModel> getActiveRolePrincipalsHierarchy();


    Collection<PrincipalModel> getNonRolePrincipalsHierarchy(PrincipalModel paramPrincipalModel);


    Collection<PrincipalGroupModel> filterOutRolePrincipals(Collection<PrincipalGroupModel> paramCollection);
}
