/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.viewswitcher.permissions;

import com.hybris.cockpitng.core.Widget;
import com.hybris.cockpitng.core.ui.WidgetInstance;
import java.util.List;

/**
 * Facade used to access View Switcher widget configuration from outside of the widget controller
 */
public interface ViewSwitcherUtils
{
    /**
     * Get list of widgets configured to be accessible for current user's authority
     *
     * @param configContextCode
     *           component name of the configuration to load
     * @param widgetInstance
     *           widget instance of configuration to load
     * @param widgets
     *           list of widgets to check against configured permissions
     * @return list of accessible widgets
     */
    default List<Widget> getAccessibleWidgets(final String configContextCode, final WidgetInstance widgetInstance,
                    final List<Widget> widgets)
    {
        return getAccessibleWidgets(configContextCode, widgets);
    }


    /**
     * @deprecated Since 6.6 please use {@link #getAccessibleWidgets(String, WidgetInstance, List)} Get list of widgets
     *             configured to be accessible for current user's authority
     * @param configContextCode
     *           component name of the configuration to load
     * @param widgets
     *           list of widgets to check against configured permissions
     * @return list of accessible widgets
     */
    @Deprecated(since = "6.6", forRemoval = true)
    List<Widget> getAccessibleWidgets(String configContextCode, List<Widget> widgets);


    /**
     * Get list of widget instances configured to be accessible for current user's authority
     *
     * @param configContextCode
     *           component name of the configuration to load
     * @param widgetInstance
     *           widget instance of configuration to load
     * @param widgetInstances
     *           list of widget instances to check against configured permissions
     * @return list of accessible widget instances
     */
    default List<WidgetInstance> getAccessibleWidgetInstances(final String configContextCode, final WidgetInstance widgetInstance,
                    final List<WidgetInstance> widgetInstances)
    {
        return getAccessibleWidgetInstances(configContextCode, widgetInstances);
    }


    /**
     * @deprecated Since 6.6 please use {@link #getAccessibleWidgetInstances(String, WidgetInstance, List)} Get list of
     *             widget instances configured to be accessible for current user's authority
     * @param configContextCode
     *           component name of the configuration to load
     * @param widgetInstances
     *           list of widget instances to check against configured permissions
     * @return list of accessible widget instances
     */
    @Deprecated(since = "6.6", forRemoval = true)
    List<WidgetInstance> getAccessibleWidgetInstances(String configContextCode, List<WidgetInstance> widgetInstances);
}
