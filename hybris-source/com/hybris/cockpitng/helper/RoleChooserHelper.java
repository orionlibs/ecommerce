/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.helper;

import com.hybris.cockpitng.core.ui.WidgetInstance;

/**
 * The interface describes operations needed to determine if the Role Chooser should be displayed for current user. The
 * functionality is needed in the widget itself but also in places that must adjust their behaviour in case Role Chooser
 * is displayed (for example deep links should be triggered only after the role is chosen).
 */
public interface RoleChooserHelper
{
    String MAIN_SLOT = "mainSlot";
    String AUTO = "auto";
    String ROLE_SELECTOR_SLOT = "roleSelectorSlot";
    String SETTING_ENABLE_ALTERNATIVE_CONTAINER = "enableAlternativeContainer";


    /**
     * @return true in case the widget set on '{@link #MAIN_SLOT}' has the {@link #SETTING_ENABLE_ALTERNATIVE_CONTAINER} set
     *         to true or the setting is set to {@link #AUTO} and the user has multiple has multiple roles and the widget
     *         has a role selector assigned
     */
    boolean isRoleSelectorContainerContainerVisible();


    /**
     * @param widgetInstance
     *           root widget for which the checks should be done
     * @return true in case the {@link #SETTING_ENABLE_ALTERNATIVE_CONTAINER} is set to true or the setting is set to
     *         {@link #AUTO} and the user has multiple has multiple roles and the widget has a role selector assigned
     */
    boolean isRoleSelectorContainerContainerVisible(final WidgetInstance widgetInstance);


    /**
     * @param user
     *           the user for which the check should be done
     * @return true if the user has more than one authority group assigned
     */
    boolean isMultiRoleUser(final String user);
}
