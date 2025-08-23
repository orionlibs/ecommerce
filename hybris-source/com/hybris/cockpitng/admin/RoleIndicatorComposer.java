/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.admin;

import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.components.Widgetslot;
import com.hybris.cockpitng.core.user.AuthorityGroupService;
import com.hybris.cockpitng.core.user.CockpitUserService;
import com.hybris.cockpitng.core.user.impl.AuthorityGroup;
import com.hybris.cockpitng.util.ViewAnnotationAwareComposer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

/**
 * Composer of the current impersonated authority group indicator.
 */
public class RoleIndicatorComposer extends ViewAnnotationAwareComposer
{
    private static final Logger LOG = LoggerFactory.getLogger(RoleIndicatorComposer.class);
    private static final String STOP_IMPERSONATION_PREVIEW = "stopImpersonationPreview";
    private static final long serialVersionUID = 3489544301030232027L;
    private Div roleIndicatorOverlay;
    private Label roleIndicator;
    private transient AuthorityGroupService adminModeAuthorityGroupService;
    private transient CockpitUserService cockpitUserService;
    private transient CockpitAdminService cockpitAdminService;


    @Override
    public void doAfterCompose(final Component component) throws Exception
    {
        super.doAfterCompose(component);
        update();
    }


    /**
     * Displays current impersonated authority group.
     */
    protected void indicateCurrentRole()
    {
        final AuthorityGroup activeAuthorityGroup = getImpersonationAuthorityGroup();
        if(activeAuthorityGroup != null)
        {
            roleIndicator.setValue(resolveGroupLabel(activeAuthorityGroup));
        }
    }


    private AuthorityGroup getImpersonationAuthorityGroup()
    {
        if(adminModeAuthorityGroupService != null)
        {
            return adminModeAuthorityGroupService.getActiveAuthorityGroupForUser(cockpitUserService.getCurrentUser());
        }
        return null;
    }


    /**
     * Determines if the indicator should be visible. By default, the method checks if the current user is ADMIN user and
     * if current mode is NOT admin-mode and if currently selected authority group is impersonated or not.
     */
    protected boolean isIndicatorVisible()
    {
        return !cockpitAdminService.isAdminMode() && cockpitUserService.isAdmin(cockpitUserService.getCurrentUser())
                        && isUsingImpersonatedRole();
    }


    private boolean isUsingImpersonatedRole()
    {
        final AuthorityGroup impersonationAuthorityGroup = getImpersonationAuthorityGroup();
        return impersonationAuthorityGroup != null;
    }


    /**
     * Returns group name or code if name is blank.
     *
     * @param group
     * @return group label
     */
    protected String resolveGroupLabel(final AuthorityGroup group)
    {
        return StringUtils.isBlank(group.getName()) ? group.getCode() : group.getName();
    }


    /**
     * Ends the impersonation preview. Clears the indicator and removes the active impersonation authority group.
     */
    @ViewEvent(eventName = Events.ON_CLICK, componentID = STOP_IMPERSONATION_PREVIEW)
    public void stopImpersonatedPreview()
    {
        roleIndicatorOverlay.setVisible(false);
        if(adminModeAuthorityGroupService != null)
        {
            adminModeAuthorityGroupService.setActiveAuthorityGroupForUser(null);
        }
        roleIndicator.setValue(StringUtils.EMPTY);
        final Component mainSlot = roleIndicatorOverlay.getFellowIfAny(AbstractConnectionWindowComposer.SLOT);
        if(mainSlot instanceof Widgetslot)
        {
            ((Widgetslot)mainSlot).updateView();
        }
        else
        {
            LOG.error("Could not update main slot, not found.");
        }
        final Component roleSelector = roleIndicatorOverlay.getParent().getParent().getFellowIfAny("roleSelector");
        if(roleSelector != null)
        {
            Events.sendEvent("onClear", roleSelector, null);
        }
    }


    /**
     * Updates the current authority group indicator, meaning:
     * <ul>
     * <li>checks visibility</li>
     * <li>updates the current impersonation authority group</li>
     * </ul>
     */
    @ViewEvent(eventName = "onUpdate")
    public void update()
    {
        final boolean visible = isIndicatorVisible();
        if(visible)
        {
            indicateCurrentRole();
        }
        roleIndicatorOverlay.setVisible(visible);
    }
}
