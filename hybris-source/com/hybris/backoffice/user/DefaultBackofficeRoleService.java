package com.hybris.backoffice.user;

import com.google.common.collect.Sets;
import com.hybris.backoffice.model.user.BackofficeRoleModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultBackofficeRoleService implements BackofficeRoleService
{
    private static final String SESSION_ATTRIBUTE_ACTIVE_AUTHORITY_GROUP_ID = "cockpitActiveAuthorityGroupId";
    private static final String CONFIG_KEY_LEGACY_ADMIN_CHECK_ENABLED = "backoffice.admincheck.legacy.enabled";
    private static final boolean DEFAULT_VALUE_LEGACY_ADMIN_CHECK_ENABLED = false;
    private SessionService sessionService;
    private UserService userService;


    public boolean shouldTreatRolesAsGroups()
    {
        return Config.getBoolean("backoffice.admincheck.legacy.enabled", false);
    }


    public Optional<BackofficeRoleModel> getActiveRoleModel()
    {
        String authorityGroupId = (String)getSessionService().getAttribute("cockpitActiveAuthorityGroupId");
        if(StringUtils.isNotBlank(authorityGroupId))
        {
            return Optional.of((BackofficeRoleModel)getUserService().getUserGroupForUID(authorityGroupId));
        }
        return Optional.empty();
    }


    public Optional<String> getActiveRole()
    {
        String authorityGroupId = (String)getSessionService().getAttribute("cockpitActiveAuthorityGroupId");
        if(StringUtils.isNotBlank(authorityGroupId))
        {
            return Optional.of(authorityGroupId);
        }
        return Optional.empty();
    }


    public void setActiveRole(String roleId)
    {
        if(StringUtils.isNotBlank(roleId))
        {
            UserGroupModel groupModel = getUserService().getUserGroupForUID(roleId);
            if(!(groupModel instanceof BackofficeRoleModel))
            {
                throw new IllegalArgumentException("Provided UID does not belong to BackofficeRole: " + roleId);
            }
            getSessionService().setAttribute("cockpitActiveAuthorityGroupId", roleId);
        }
        else
        {
            getSessionService().removeAttribute("cockpitActiveAuthorityGroupId");
        }
    }


    public boolean isActiveRole(String roleId)
    {
        if(StringUtils.isBlank(roleId))
        {
            throw new IllegalArgumentException("UID cannot be empty");
        }
        return StringUtils.equals(getActiveRole().orElse(""), roleId);
    }


    public Collection<PrincipalGroupModel> getActiveRolePrincipalsHierarchy()
    {
        Collection<PrincipalGroupModel> result = new HashSet<>();
        getActiveRoleModel().ifPresent(role -> {
            result.addAll(role.getAllGroups());
            result.add(role);
        });
        return result;
    }


    public Collection<PrincipalModel> getNonRolePrincipalsHierarchy(PrincipalModel principal)
    {
        Set<PrincipalModel> relatedPrincipals = Sets.newHashSet((Object[])new PrincipalModel[] {principal});
        if(principal.getGroups() != null)
        {
            Objects.requireNonNull(BackofficeRoleModel.class);
            Objects.requireNonNull(relatedPrincipals);
            principal.getGroups().stream().filter(Predicate.not(BackofficeRoleModel.class::isInstance)).flatMap(group -> getNonRolePrincipalsHierarchy((PrincipalModel)group).stream()).forEach(relatedPrincipals::add);
        }
        return relatedPrincipals;
    }


    public Collection<PrincipalGroupModel> filterOutRolePrincipals(Collection<PrincipalGroupModel> principals)
    {
        Objects.requireNonNull(BackofficeRoleModel.class);
        return (Collection<PrincipalGroupModel>)principals.stream().filter(Predicate.not(BackofficeRoleModel.class::isInstance)).collect(Collectors.toSet());
    }


    protected SessionService getSessionService()
    {
        return this.sessionService;
    }


    @Required
    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
    }


    protected UserService getUserService()
    {
        return this.userService;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }
}
