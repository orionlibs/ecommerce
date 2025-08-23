/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.backofficemainlayout;

import com.hybris.cockpitng.core.Widget;
import com.hybris.cockpitng.core.ui.WidgetInstance;
import com.hybris.cockpitng.core.user.AuthorityGroupService;
import com.hybris.cockpitng.core.user.CockpitUserService;
import com.hybris.cockpitng.helper.RoleChooserHelper;
import com.hybris.cockpitng.util.DefaultWidgetController;
import org.zkoss.zk.ui.Component;

public class BackofficeMainLayoutController extends DefaultWidgetController
{
    /**
     * This field is not used anymore.
     *
     * @deprecated since 2005
     */
    @Deprecated(since = "2005", forRemoval = true)
    public static final String SETTING_ENABLE_ALTERNATIVE_CONTAINER = "enableAlternativeContainer";
    private static final String ROLE_SELECTOR_SLOT = "roleSelectorSlot";
    private static final long serialVersionUID = -2232221166450619482L;
    private transient AuthorityGroupService authorityGroupService;
    private transient CockpitUserService cockpitUserService;
    private transient RoleChooserHelper roleChooserHelper;
    private Component mainContainer;
    private Component roleSelectorContainer;


    @Override
    public void initialize(final Component comp)
    {
        if(isRoleSelectorContainerContainerVisible())
        {
            mainContainer.setVisible(false);
            roleSelectorContainer.setVisible(true);
        }
        else
        {
            mainContainer.setVisible(true);
            roleSelectorContainer.setVisible(false);
        }
    }


    protected boolean isRoleSelectorContainerContainerVisible()
    {
        return getRoleChooserHelper().isRoleSelectorContainerContainerVisible(getWidgetslot().getWidgetInstance());
    }


    /**
     * @deprecated since 2005 not used anymore
     */
    @Deprecated(since = "2005", forRemoval = true)
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


    public Component getRoleSelectorContainer()
    {
        return roleSelectorContainer;
    }


    public CockpitUserService getCockpitUserService()
    {
        return cockpitUserService;
    }


    public Component getMainContainer()
    {
        return mainContainer;
    }


    public AuthorityGroupService getAuthorityGroupService()
    {
        return authorityGroupService;
    }


    public RoleChooserHelper getRoleChooserHelper()
    {
        return roleChooserHelper;
    }
}
