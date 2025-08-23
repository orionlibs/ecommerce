/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.helper.impl;

import com.hybris.cockpitng.components.Widgetslot;
import com.hybris.cockpitng.core.Widget;
import com.hybris.cockpitng.core.ui.WidgetInstance;
import com.hybris.cockpitng.core.user.AuthorityGroupService;
import com.hybris.cockpitng.core.user.CockpitUserService;
import com.hybris.cockpitng.core.user.impl.AuthorityGroup;
import com.hybris.cockpitng.helper.RoleChooserHelper;
import com.hybris.cockpitng.util.WidgetUtils;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultRoleChooserHelper implements RoleChooserHelper
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultRoleChooserHelper.class);
    private final AuthorityGroupService authorityGroupService;
    private final CockpitUserService cockpitUserService;
    private final WidgetUtils widgetUtils;


    public DefaultRoleChooserHelper(final AuthorityGroupService authorityGroupService, final CockpitUserService cockpitUserService,
                    final WidgetUtils widgetUtils)
    {
        this.authorityGroupService = authorityGroupService;
        this.cockpitUserService = cockpitUserService;
        this.widgetUtils = widgetUtils;
    }


    public boolean isRoleSelectorContainerContainerVisible()
    {
        final Widgetslot mainSlot = getWidgetUtils().getRegisteredWidgetslot(MAIN_SLOT);
        if(mainSlot == null)
        {
            LOG.warn("Could not find widget slot: '{}'", MAIN_SLOT);
            return false;
        }
        final WidgetInstance widgetInstance = mainSlot.getWidgetInstance();
        if(widgetInstance == null)
        {
            LOG.debug("No widget instance available on widget slot: '{}'", MAIN_SLOT);
            return false;
        }
        return isRoleSelectorContainerContainerVisible(widgetInstance);
    }


    public boolean isRoleSelectorContainerContainerVisible(final WidgetInstance widgetInstance)
    {
        if(widgetInstance == null)
        {
            return false;
        }
        final Widget widget = widgetInstance.getWidget();
        if(widget == null)
        {
            return false;
        }
        final String setting = widget.getWidgetSettings().getString(SETTING_ENABLE_ALTERNATIVE_CONTAINER);
        if(setting != null)
        {
            if(AUTO.equalsIgnoreCase(setting.trim()) && hasRoleSelectorSlotChild(widgetInstance))
            {
                final String currentUser = getCockpitUserService().getCurrentUser();
                if(currentUser == null)
                {
                    LOG.error("Current user was null");
                    return false;
                }
                else
                {
                    final AuthorityGroup activeAuthorityGroupForUser = getAuthorityGroupService()
                                    .getActiveAuthorityGroupForUser(currentUser);
                    if(activeAuthorityGroupForUser == null)
                    {
                        return isMultiRoleUser(currentUser);
                    }
                }
            }
            else
            {
                return Boolean.parseBoolean(setting);
            }
        }
        return false;
    }


    protected boolean hasRoleSelectorSlotChild(final WidgetInstance widgetInstance)
    {
        if(widgetInstance != null && widgetInstance.getWidget() != null)
        {
            for(final Widget childWidget : widgetInstance.getWidget().getChildren())
            {
                if(ROLE_SELECTOR_SLOT.equals(childWidget.getSlotId()))
                {
                    return true;
                }
            }
        }
        return false;
    }


    public boolean isMultiRoleUser(final String currentUser)
    {
        final List<AuthorityGroup> allAuthorityGroupsForUser = getAuthorityGroupService().getAllAuthorityGroupsForUser(currentUser);
        return (allAuthorityGroupsForUser != null && allAuthorityGroupsForUser.size() > 1);
    }


    protected AuthorityGroupService getAuthorityGroupService()
    {
        return authorityGroupService;
    }


    protected CockpitUserService getCockpitUserService()
    {
        return cockpitUserService;
    }


    public WidgetUtils getWidgetUtils()
    {
        return widgetUtils;
    }
}
