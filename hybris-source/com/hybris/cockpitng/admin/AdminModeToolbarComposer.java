/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.admin;

import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.components.Widgetslot;
import com.hybris.cockpitng.core.user.AuthorityGroupService;
import com.hybris.cockpitng.core.user.CockpitUserService;
import com.hybris.cockpitng.core.user.impl.AuthorityGroup;
import com.hybris.cockpitng.core.util.Resettable;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.util.ViewAnnotationAwareComposer;
import java.util.Collections;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.CheckEvent;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Div;

/**
 * Composer for the toolbar in admin mode.
 */
public class AdminModeToolbarComposer extends ViewAnnotationAwareComposer
{
    private static final Logger LOG = LoggerFactory.getLogger(AdminModeToolbarComposer.class);
    private static final String ROLE_PREVIEW_CHECKBOX = "rolePreviewCheckbox";
    private static final String ROLE_SELECTOR = "roleSelector";
    private static final long serialVersionUID = -4628814192252072524L;
    private static final String NULL_ROLE = StringUtils.EMPTY;
    private transient AuthorityGroupService adminModeAuthorityGroupService;
    private transient CockpitUserService cockpitUserService;
    private transient ImpersonationPreviewHelper impersonationPreviewHelper;
    private transient CockpitAdminService cockpitAdminService;
    private Checkbox rolePreviewCheckbox;
    private Combobox roleSelector;
    private Checkbox showSlotIdChk;
    private Widgetslot slot;
    private Caption widgetClipboardCaption;
    private Div widgetClipboard;
    private transient List<Resettable> groupChangeListeners;


    @Override
    public void doAfterCompose(final Component comp) throws Exception
    {
        super.doAfterCompose(comp);
        if(adminModeAuthorityGroupService != null)
        {
            populateRoleSelector();
            updateRoleSelector();
        }
        updateRolePreviewCheckbox();
    }


    /**
     * Renders content of clipboard.
     *
     * @param clipboardComponent clipborad component
     * @param caption clipboard caption
     */
    public void renderWidgetClipboard(final Component clipboardComponent, final Caption caption)
    {
        cockpitAdminService.renderWidgetClipboard(clipboardComponent, caption);
    }


    /**
     * Populates the role selector combo with all available authority groups defined.
     */
    protected void populateRoleSelector()
    {
        final List<AuthorityGroup> allAuthorityGroups = adminModeAuthorityGroupService.getAllAuthorityGroups();
        if(CollectionUtils.isNotEmpty(allAuthorityGroups))
        {
            sortAuthorityGroups(allAuthorityGroups);
            for(final AuthorityGroup authorityGroup : allAuthorityGroups)
            {
                final Comboitem comboitem = new Comboitem(getDisplayLabel(authorityGroup));
                comboitem.setValue(authorityGroup.getCode());
                roleSelector.appendChild(comboitem);
            }
        }
    }


    private String getDisplayLabel(final AuthorityGroup authorityGroup)
    {
        if(authorityGroup == null)
        {
            throw new IllegalArgumentException("AuthorityGroup must not be null.");
        }
        return StringUtils.isEmpty(authorityGroup.getName()) ? authorityGroup.getCode() : authorityGroup.getName();
    }


    public void showSlotIds(final Widgetslot slot)
    {
        UITools.modifySClass(slot, "showSlotIDs", showSlotIdChk.isChecked());
    }


    /**
     * Performs simple alphanumeric sorting (by code) of authority groups.
     *
     * @param allAuthorityGroups
     * @throws IllegalStateException if authority group with code equal null will exist in the list.
     */
    protected void sortAuthorityGroups(final List<AuthorityGroup> allAuthorityGroups)
    {
        Collections.sort(allAuthorityGroups, (authorityGroup1, authorityGroup2) -> {
            final String label1 = getDisplayLabel(authorityGroup1);
            final String label2 = getDisplayLabel(authorityGroup2);
            return label1 == null ? -1 : label1.compareTo(label2);
        });
    }


    /**
     * Changes the active (impersonated) authority group according to the selected value in role selector combobox.
     *
     * @param inputEvent
     */
    @ViewEvent(eventName = Events.ON_CHANGE, componentID = ROLE_SELECTOR)
    public void changeRole(final InputEvent inputEvent)
    {
        if(adminModeAuthorityGroupService != null)
        {
            final String roleCode = roleSelector.getSelectedItem().getValue();
            AuthorityGroup authorityGroup = null;
            if(StringUtils.isNotEmpty(roleCode))
            {
                authorityGroup = adminModeAuthorityGroupService.getAuthorityGroup(roleCode);
            }
            adminModeAuthorityGroupService.setActiveAuthorityGroupForUser(authorityGroup);
            final Component mainSlot = rolePreviewCheckbox.getFellowIfAny(AbstractConnectionWindowComposer.SLOT);
            if(mainSlot instanceof Widgetslot)
            {
                ((Widgetslot)mainSlot).updateView();
            }
            else
            {
                LOG.error("Could not update main slot, not found.");
            }
            updateRolePreviewCheckbox();
        }
        // Workaround for SS-158.
        rolePreviewCheckbox.setFocus(true);
        if(cockpitAdminService.isSymbolicAdminMode())
        {
            cockpitAdminService.refreshCockpit();
        }
        if(groupChangeListeners != null)
        {
            for(final Resettable listener : groupChangeListeners)
            {
                listener.reset();
            }
        }
    }


    private void updateRolePreviewCheckbox()
    {
        rolePreviewCheckbox.setDisabled(getCurrentImpersonationRole() == null);
        rolePreviewCheckbox.setChecked(impersonationPreviewHelper.isImpersonatedPreviewEnabled());
    }


    /**
     * Updates the role selector combo value according to current active authority group.
     */
    @ViewEvent(eventName = "onUpdate", componentID = ROLE_SELECTOR)
    public void updateRoleSelector()
    {
        String value = NULL_ROLE;
        final AuthorityGroup activeImpersonationAuthorityGroup = getCurrentImpersonationRole();
        if(activeImpersonationAuthorityGroup != null)
        {
            value = getDisplayLabel(activeImpersonationAuthorityGroup);
        }
        updateRolePreviewCheckbox();
        roleSelector.setValue(value);
    }


    /**
     * Returns current impersonation role
     *
     * @return activeImpersonationAuthorityGroup
     */
    protected AuthorityGroup getCurrentImpersonationRole()
    {
        AuthorityGroup activeImpersonationAuthorityGroup = null;
        if(adminModeAuthorityGroupService != null)
        {
            activeImpersonationAuthorityGroup = adminModeAuthorityGroupService.getActiveAuthorityGroupForUser(cockpitUserService
                            .getCurrentUser());
        }
        return activeImpersonationAuthorityGroup;
    }


    /**
     * Clears value of the role selector combobox.
     */
    @ViewEvent(eventName = "onClear", componentID = ROLE_SELECTOR)
    public void clear()
    {
        roleSelector.setValue(NULL_ROLE);
    }


    /**
     * Sets the impersonated preview flag value according to the rolePreviewCheckbox value.
     *
     * @param inputEvent
     */
    @ViewEvent(eventName = Events.ON_CHECK, componentID = ROLE_PREVIEW_CHECKBOX)
    public void toggleRolePreviewEnabled(final CheckEvent inputEvent)
    {
        impersonationPreviewHelper.setImpersonatedPreviewFlag(inputEvent.isChecked());
    }


    @ViewEvent(eventName = Events.ON_CHECK, componentID = "showSlotIdChk")
    public void onChangeOfRoleSelector()
    {
        showSlotIds(slot);
    }


    @ViewEvent(eventName = Events.ON_CREATE, componentID = "widgetClipboard")
    public void onCreateWidgetClipboard()
    {
        renderWidgetClipboard(widgetClipboard, widgetClipboardCaption);
    }


    /**
     * @return the groupChangeListeners
     */
    public List<Resettable> getGroupChangeListeners()
    {
        return groupChangeListeners;
    }


    /**
     * @param groupChangeListeners the groupChangeListeners to set
     */
    public void setGroupChangeListeners(final List<Resettable> groupChangeListeners)
    {
        this.groupChangeListeners = groupChangeListeners;
    }
}
