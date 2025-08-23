/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.cockpitng.core.user.impl;

import com.hybris.backoffice.catalogversioneventhandling.AvailableCatalogVersionsTag;
import com.hybris.backoffice.daos.BackofficeRoleDao;
import com.hybris.backoffice.model.user.BackofficeRoleModel;
import com.hybris.backoffice.spring.security.BackofficeUserDetailsService;
import com.hybris.backoffice.user.BackofficeRoleService;
import com.hybris.cockpitng.core.user.CockpitUserService;
import com.hybris.cockpitng.core.user.impl.AbstractAuthorityGroupService;
import com.hybris.cockpitng.core.user.impl.AuthorityGroup;
import com.hybris.cockpitng.util.CockpitSessionService;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;

/**
 * OnPremise implementation for retrieving and activating authority groups.
 */
public class DefaultPlatformAuthorityGroupService extends AbstractAuthorityGroupService
{
    private static final String COCKPIT_ACTIVE_AUTHORITY_GROUP = "cockpitActiveAuthorityGroup";
    private UserService userService;
    private CockpitSessionService cockpitSessionService;
    private CockpitUserService cockpitUserService;
    private BackofficeRoleDao backofficeRoleDao;
    private BackofficeRoleService backofficeRoleService;
    private SessionService sessionService;
    private SearchRestrictionService searchRestrictionService;
    private AvailableCatalogVersionsTag availableCatalogVersionsTag;
    private BackofficeUserDetailsService userDetailsService;


    @Override
    public AuthorityGroup getActiveAuthorityGroupForUser(final String userId)
    {
        final Object attribute = cockpitSessionService.getAttribute(COCKPIT_ACTIVE_AUTHORITY_GROUP);
        final String currentUser = cockpitUserService.getCurrentUser();
        if(attribute instanceof AuthorityGroup)
        {
            if(userId != null && userId.equals(currentUser))
            {
                return (AuthorityGroup)attribute;
            }
        }
        else if(currentUser != null)
        {
            final List<AuthorityGroup> allAuthorityGroupsForUser = getAllAuthorityGroupsForUser(userId);
            if(allAuthorityGroupsForUser != null && allAuthorityGroupsForUser.size() == 1)
            {
                final AuthorityGroup authorityGroup = allAuthorityGroupsForUser.iterator().next();
                cockpitSessionService.setAttribute(COCKPIT_ACTIVE_AUTHORITY_GROUP, authorityGroup);
                getBackofficeRoleService().setActiveRole(authorityGroup.getCode());
                getUserDetailsService().activateCatalogVersions(getUserService().getCurrentUser());
                return authorityGroup;
            }
        }
        return null;
    }


    protected Set<BackofficeRoleModel> getAllBackofficeRoles()
    {
        return sessionService.executeInLocalView(new SessionExecutionBody()
        {
            @Override
            public Object execute()
            {
                try
                {
                    searchRestrictionService.disableSearchRestrictions();
                    return getBackofficeRoleDao().findAllBackofficeRoles();
                }
                finally
                {
                    searchRestrictionService.enableSearchRestrictions();
                }
            }
        });
    }


    @Override
    public List<AuthorityGroup> getAllAuthorityGroups()
    {
        final Set<BackofficeRoleModel> allBackofficeRoles = getAllBackofficeRoles();
        final List<AuthorityGroup> ret = new ArrayList<>();
        for(final BackofficeRoleModel role : allBackofficeRoles)
        {
            ret.add(wrapBackofficeRoleModel(role));
        }
        return ret;
    }


    @Override
    public List<AuthorityGroup> getAllAuthorityGroupsForUser(final String userId)
    {
        final UserModel userModel = userService.getUserForUID(userId);
        final Set<BackofficeRoleModel> allUserGroupsForUser = userService.getAllUserGroupsForUser(userModel,
                        BackofficeRoleModel.class);
        final List<AuthorityGroup> ret = new ArrayList<>();
        for(final BackofficeRoleModel role : allUserGroupsForUser)
        {
            ret.add(wrapBackofficeRoleModel(role));
        }
        Collections.sort(ret, Comparator.comparing(AuthorityGroup::getCode));
        return ret;
    }


    protected AuthorityGroup wrapBackofficeRoleModel(final BackofficeRoleModel backofficeRoleModel)
    {
        final AuthorityGroup authorityGroup = new AuthorityGroup();
        authorityGroup.setCode(backofficeRoleModel.getUid());
        authorityGroup.setName(backofficeRoleModel.getDisplayName());
        authorityGroup.setDescription(backofficeRoleModel.getDescription());
        final MediaModel profilePicture = backofficeRoleModel.getProfilePicture();
        if(profilePicture != null)
        {
            authorityGroup.setThumbnailURL(profilePicture.getURL());
        }
        final Collection<String> authorities = backofficeRoleModel.getAuthorities();
        if(authorities != null)
        {
            authorityGroup.setAuthorities(new ArrayList<>(backofficeRoleModel.getAuthorities()));
        }
        return authorityGroup;
    }


    @Override
    public AuthorityGroup getAuthorityGroup(final String code)
    {
        final List<AuthorityGroup> authorityGroups = getAllAuthorityGroups();
        return authorityGroups.stream().filter(each -> StringUtils.equals(each.getCode(), code)).findFirst().orElse(null);
    }


    @Override
    public void setActiveAuthorityGroupForUser(final AuthorityGroup authorityGroup)
    {
        cockpitSessionService.setAttribute(COCKPIT_ACTIVE_AUTHORITY_GROUP, authorityGroup);
        if(authorityGroup != null)
        {
            getBackofficeRoleService().setActiveRole(authorityGroup.getCode());
            getUserDetailsService().activateCatalogVersions(getUserService().getCurrentUser());
        }
        else
        {
            getBackofficeRoleService().setActiveRole(null);
        }
        resetGroupChangeListeners();
        getAvailableCatalogVersionsTag().refresh();
    }


    @Required
    public void setCockpitSessionService(final CockpitSessionService cockpitSessionService)
    {
        this.cockpitSessionService = cockpitSessionService;
    }


    @Required
    public void setUserService(final UserService userService)
    {
        this.userService = userService;
    }


    public UserService getUserService()
    {
        return userService;
    }


    protected CockpitUserService getCockpitUserService()
    {
        return this.cockpitUserService;
    }


    @Required
    public void setCockpitUserService(final CockpitUserService cockpitUserService)
    {
        this.cockpitUserService = cockpitUserService;
    }


    protected BackofficeRoleDao getBackofficeRoleDao()
    {
        return backofficeRoleDao;
    }


    @Required
    public void setBackofficeRoleDao(final BackofficeRoleDao backofficeRoleDao)
    {
        this.backofficeRoleDao = backofficeRoleDao;
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


    @Required
    public void setSessionService(final SessionService sessionService)
    {
        this.sessionService = sessionService;
    }


    @Required
    public void setSearchRestrictionService(final SearchRestrictionService searchRestrictionService)
    {
        this.searchRestrictionService = searchRestrictionService;
    }


    protected AvailableCatalogVersionsTag getAvailableCatalogVersionsTag()
    {
        return availableCatalogVersionsTag;
    }


    @Required
    public void setAvailableCatalogVersionsTag(final AvailableCatalogVersionsTag availableCatalogVersionsTag)
    {
        this.availableCatalogVersionsTag = availableCatalogVersionsTag;
    }


    public BackofficeUserDetailsService getUserDetailsService()
    {
        return userDetailsService;
    }


    @Required
    public void setUserDetailsService(final BackofficeUserDetailsService userDetailsService)
    {
        this.userDetailsService = userDetailsService;
    }
}
