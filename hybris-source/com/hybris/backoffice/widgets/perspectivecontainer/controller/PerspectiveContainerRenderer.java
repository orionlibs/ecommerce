/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.perspectivecontainer.controller;

import com.hybris.backoffice.widgets.viewswitcher.permissions.ViewSwitcherUtils;
import com.hybris.cockpitng.core.ui.WidgetInstance;
import com.hybris.cockpitng.engine.impl.TabContainerRenderer;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class PerspectiveContainerRenderer extends TabContainerRenderer
{
    private ViewSwitcherUtils viewSwitcherUtils;


    @Override
    protected List<WidgetInstance> getAccessibleWidgets(final WidgetInstance parentWidgetInstance,
                    final List<WidgetInstance> widgets)
    {
        final List<WidgetInstance> accessibleWidgets = super.getAccessibleWidgets(parentWidgetInstance, widgets);
        return getViewSwitcherUtils().getAccessibleWidgetInstances(getConfigurationContextCode(parentWidgetInstance),
                        parentWidgetInstance, accessibleWidgets);
    }


    protected String getConfigurationContextCode(final WidgetInstance parentWidgetInstance)
    {
        return StringUtils.defaultIfBlank(
                        parentWidgetInstance.getWidget().getWidgetSettings()
                                        .getString(PerspectiveContainerWidgetController.SETTING_CONFIG_CONTEXT),
                        PerspectiveContainerWidgetController.DEFAULT_CONFIG_CONTEXT);
    }


    protected ViewSwitcherUtils getViewSwitcherUtils()
    {
        return viewSwitcherUtils;
    }


    @Required
    public void setViewSwitcherUtils(final ViewSwitcherUtils viewSwitcherUtils)
    {
        this.viewSwitcherUtils = viewSwitcherUtils;
    }
}
