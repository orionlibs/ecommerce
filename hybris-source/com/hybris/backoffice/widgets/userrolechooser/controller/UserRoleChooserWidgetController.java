/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.userrolechooser.controller;

import com.hybris.backoffice.widgets.userrolechooser.model.UserRoleModel;
import com.hybris.cockpitng.admin.CockpitMainWindowComposer;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.core.events.CockpitEventQueue;
import com.hybris.cockpitng.core.events.impl.DefaultCockpitEvent;
import com.hybris.cockpitng.core.user.AuthorityGroupService;
import com.hybris.cockpitng.core.user.CockpitUserService;
import com.hybris.cockpitng.core.user.impl.AuthorityGroup;
import com.hybris.cockpitng.util.CockpitSessionService;
import com.hybris.cockpitng.util.DefaultWidgetController;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.BookmarkEvent;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Toolbarbutton;

public class UserRoleChooserWidgetController extends DefaultWidgetController
{
    public static final String USER_ROLE_LIST_COMPONENT = "userRoleList";
    public static final String SCLASS_USER_ROLE_CHOOSER_BUTTON_EMPTY = "yw-roleChooser-empty-list-state";
    private static final Logger LOG = LoggerFactory.getLogger(UserRoleChooserWidgetController.class);
    private static final long serialVersionUID = 1L;
    @WireVariable
    private transient CockpitUserService cockpitUserService;
    @WireVariable
    private transient AuthorityGroupService authorityGroupService;
    @WireVariable
    private transient AuthorityGroupService adminModeAuthorityGroupService;
    @WireVariable
    private transient CockpitEventQueue cockpitEventQueue;
    @WireVariable
    private transient CockpitSessionService cockpitSessionService;
    @Wire
    private Listbox userRoleList;
    @Wire
    private Toolbarbutton userRoleChooserBtn;
    @Wire
    private List<AuthorityGroup> availableUserRoles;
    private transient AuthorityGroup activeUserRole;


    private void writeObject(final ObjectOutputStream stream) throws NotSerializableException
    {
        throw new NotSerializableException(getClass().getName());
    }


    private void readObject(final ObjectInputStream stream) throws NotSerializableException
    {
        throw new NotSerializableException(getClass().getName());
    }


    @Override
    public void initialize(final Component comp)
    {
        super.initialize(comp);
        loadAvailableRoles();
        createUserRoleList();
    }


    protected void loadAvailableRoles()
    {
        final String userId = getCockpitUserService().getCurrentUser();
        final AuthorityGroup impersonatedAuthorityGroup = getAdminModeAuthorityGroupService().getActiveAuthorityGroupForUser(userId);
        if(impersonatedAuthorityGroup == null)
        {
            activeUserRole = getAuthorityGroupService().getActiveAuthorityGroupForUser(userId);
            setAvailableUserRoles(getAuthorityGroupService().getAllAuthorityGroupsForUser(userId));
        }
        else
        {
            availableUserRoles = Collections.singletonList(impersonatedAuthorityGroup);
            activeUserRole = impersonatedAuthorityGroup;
        }
    }


    protected void createUserRoleList()
    {
        final ListModelList<UserRoleModel> listModel = new ListModelList<>();
        if(CollectionUtils.isNotEmpty(availableUserRoles))
        {
            for(final AuthorityGroup userRole : availableUserRoles)
            {
                final UserRoleModel userRoleModel = new UserRoleModel();
                userRoleModel.setCode(userRole.getCode());
                userRoleModel.setName(userRole.getName());
                userRoleModel.setDescription(userRole.getDescription());
                userRoleModel.setThumbnailURL(userRole.getThumbnailURL());
                if(activeUserRole != null && userRole.getCode().equals(activeUserRole.getCode()))
                {
                    userRoleModel.setSelected(true);
                }
                listModel.add(userRoleModel);
            }
        }
        else
        {
            getUserRoleChooserBtn().setSclass(SCLASS_USER_ROLE_CHOOSER_BUTTON_EMPTY);
        }
        userRoleList.setModel(listModel);
    }


    protected void changeActiveUserRole(final String code)
    {
        final AuthorityGroup authorityGroup = getAuthorityGroupService().getAuthorityGroup(code);
        if(authorityGroup != null)
        {
            getAuthorityGroupService().setActiveAuthorityGroupForUser(authorityGroup);
        }
        sendRedirect(null);
    }


    protected void sendRedirect(final String target)
    {
        Executions.sendRedirect(target);
    }


    @ViewEvent(componentID = USER_ROLE_LIST_COMPONENT, eventName = Events.ON_SELECT)
    public void onSelectionChanged(final SelectEvent<Listitem, UserRoleModel> event)
    {
        changeActiveUserRole(event);
        sendBookmarks();
    }


    protected void changeActiveUserRole(final SelectEvent<Listitem, UserRoleModel> event)
    {
        final Listitem selectedItem = event.getReference();
        final UserRoleModel selectedUserRole = selectedItem.getValue();
        changeActiveUserRole(selectedUserRole.getCode());
    }


    protected void sendBookmarks()
    {
        final Object event = getCockpitSessionService().getAttribute(CockpitMainWindowComposer.BO_LOGIN_BOOKMARK);
        if(event instanceof BookmarkEvent)
        {
            getCockpitSessionService().setAttribute(CockpitMainWindowComposer.BO_LOGIN_BOOKMARK, null);
            getCockpitEventQueue().publishEvent(new DefaultCockpitEvent(Events.ON_BOOKMARK_CHANGE, event, null));
        }
        else if(event != null)
        {
            LOG.warn("{} session attribute is not of desired type: {}", CockpitMainWindowComposer.BO_LOGIN_BOOKMARK,
                            BookmarkEvent.class);
            getCockpitSessionService().setAttribute(CockpitMainWindowComposer.BO_LOGIN_BOOKMARK, null);
        }
    }


    public Listbox getUserRoleList()
    {
        return userRoleList;
    }


    protected void setAvailableUserRoles(final List<AuthorityGroup> availableUserRoles)
    {
        this.availableUserRoles = Objects.requireNonNullElse(availableUserRoles, Collections.emptyList());
    }


    public AuthorityGroup getActiveUserRole()
    {
        return activeUserRole;
    }


    public CockpitUserService getCockpitUserService()
    {
        return cockpitUserService;
    }


    public AuthorityGroupService getAuthorityGroupService()
    {
        return authorityGroupService;
    }


    public AuthorityGroupService getAdminModeAuthorityGroupService()
    {
        return adminModeAuthorityGroupService;
    }


    protected Toolbarbutton getUserRoleChooserBtn()
    {
        return userRoleChooserBtn;
    }


    public CockpitEventQueue getCockpitEventQueue()
    {
        return cockpitEventQueue;
    }


    public CockpitSessionService getCockpitSessionService()
    {
        return cockpitSessionService;
    }
}
