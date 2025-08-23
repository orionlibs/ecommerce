/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.cockpitng.user;

import com.hybris.backoffice.cockpitng.user.cache.BackofficeCockpitUserServiceCache;
import com.hybris.backoffice.user.BackofficeRoleService;
import com.hybris.backoffice.user.BackofficeUserService;
import com.hybris.cockpitng.core.user.CockpitUserService;
import com.hybris.cockpitng.core.util.CockpitProperties;
import com.hybris.cockpitng.util.CockpitSessionService;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.internal.jalo.ServicelayerManager;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

/**
 * Implementation of {@link CockpitUserService} for the hybris platform, using {@link UserService}.
 */
public class BackofficeCockpitUserService implements CockpitUserService
{
    private static final Logger LOG = LoggerFactory.getLogger(BackofficeCockpitUserService.class);
    public static final String BACKOFFICE_ADMIN_GROUP = BackofficeUserService.BACKOFFICE_ADMIN_GROUP;
    public static final String IS_ADMIN_SESSION_KEY = BackofficeCockpitUserService.class.getName() + "_isAdminUser";
    protected static final String CURRENT_USER_VERIFIES_ANONYMOUS_USER_PROPERTY = "gettingcurrentuser.annonymoususer.verification";
    private UserService userService;
    private CockpitSessionService cockpitSessionService;
    private CockpitProperties cockpitProperties;
    private BackofficeRoleService backofficeRoleService;
    private ServicelayerManager servicelayerManager;
    private BackofficeCockpitUserServiceCache backofficeCockpitUserServiceCache;
    private Map<String, Boolean> adminsCache;


    @Override
    public String getCurrentUser()
    {
        final UserModel currentUser = getUserService().getCurrentUser();
        if(currentUser == null || isVerifiedAnonymousUser(currentUser))
        {
            return null;
        }
        else
        {
            return currentUser.getUid();
        }
    }


    protected boolean isVerifiedAnonymousUser(final UserModel user)
    {
        final boolean verifyAnonymoususer = getCockpitProperties().getBoolean(CURRENT_USER_VERIFIES_ANONYMOUS_USER_PROPERTY, false);
        return verifyAnonymoususer && getUserService().isAnonymousUser(user);
    }


    @Override
    public void setCurrentUser(final String userID)
    {
        try
        {
            final UserModel user = userID == null ? null : getUserService().getUserForUID(userID);
            getUserService().setCurrentUser(user);
        }
        catch(final UnknownIdentifierException e)
        {
            LOG.error(String.format("Could not set current user '%s', user not found.", userID));
        }
    }


    @Override
    public boolean isAdmin(final String userID)
    {
        return backofficeCockpitUserServiceCache.isAdmin(userID, this::checkIsAdmin);
    }


    protected Boolean checkIsAdmin(final String userID)
    {
        if(userID == null)
        {
            return false;
        }
        if(getBackofficeRoleService().shouldTreatRolesAsGroups())
        {
            return isAdminLegacy(userID);
        }
        UserModel user = null;
        try
        {
            user = getUserService().getUserForUID(userID);
        }
        catch(final UnknownIdentifierException ex)
        {
            LOG.debug(ex.getLocalizedMessage(), ex);
        }
        return user != null && getUserService().isAdmin(user);
    }


    protected boolean isAdminLegacy(final String userID)
    {
        final Object cachedIsAdmin = getCockpitSessionService().getAttribute(IS_ADMIN_SESSION_KEY);
        if(cachedIsAdmin instanceof Boolean)
        {
            return ((Boolean)cachedIsAdmin).booleanValue();
        }
        boolean isAdmin = false;
        try
        {
            final UserModel currentUser = getUserService().getUserForUID(userID);
            if(currentUser != null)
            {
                final Set<UserGroupModel> allUserGroupsForUser = getUserService().getAllUserGroupsForUser(currentUser);
                if(CollectionUtils.isNotEmpty(allUserGroupsForUser))
                {
                    isAdmin = allUserGroupsForUser.stream()
                                    .anyMatch(group -> StringUtils.equals(BACKOFFICE_ADMIN_GROUP, group.getUid()));
                }
            }
        }
        catch(final UnknownIdentifierException e)
        {
            LOG.warn(e.getMessage(), e);
        }
        getCockpitSessionService().setAttribute(IS_ADMIN_SESSION_KEY, Boolean.valueOf(isAdmin));
        return isAdmin;
    }


    @Override
    public boolean isLocalizedEditorInitiallyExpanded()
    {
        return getServicelayerManager().getOrCreateUserProfile().isExpandInitial();
    }


    public UserService getUserService()
    {
        return userService;
    }


    @Required
    public void setUserService(final UserService userService)
    {
        this.userService = userService;
    }


    public CockpitProperties getCockpitProperties()
    {
        return cockpitProperties;
    }


    @Required
    public void setCockpitProperties(final CockpitProperties cockpitProperties)
    {
        this.cockpitProperties = cockpitProperties;
    }


    public CockpitSessionService getCockpitSessionService()
    {
        return cockpitSessionService;
    }


    @Required
    public void setCockpitSessionService(final CockpitSessionService cockpitSessionService)
    {
        this.cockpitSessionService = cockpitSessionService;
    }


    protected BackofficeRoleService getBackofficeRoleService()
    {
        return backofficeRoleService;
    }


    @Required
    public void setBackofficeRoleService(final BackofficeRoleService backofficeRoleService)
    {
        this.backofficeRoleService = backofficeRoleService;
    }


    /**
     * @deprecated since 2105, not used any more
     * @return
     */
    @Deprecated(since = "2105", forRemoval = true)
    protected Map<String, Boolean> getAdminsCache()
    {
        return adminsCache;
    }


    /**
     * @deprecated since 2105, not used any more
     * @param adminsCache
     */
    @Deprecated(since = "2105", forRemoval = true)
    public void setAdminsCache(final Map<String, Boolean> adminsCache)
    {
        this.adminsCache = adminsCache;
    }


    protected ServicelayerManager getServicelayerManager()
    {
        if(servicelayerManager == null)
        {
            servicelayerManager = ServicelayerManager.getInstance();
        }
        return servicelayerManager;
    }


    protected BackofficeCockpitUserServiceCache getBackofficeCockpitUserServiceCache()
    {
        return backofficeCockpitUserServiceCache;
    }


    @Required
    public void setBackofficeCockpitUserServiceCache(final BackofficeCockpitUserServiceCache backofficeCockpitUserServiceCache)
    {
        this.backofficeCockpitUserServiceCache = backofficeCockpitUserServiceCache;
    }
}
