/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.viewswitcher;

import com.hybris.backoffice.widgets.viewswitcher.permissions.ViewSwitcherUtils;
import com.hybris.cockpitng.core.ui.WidgetInstance;
import com.hybris.cockpitng.engine.impl.SwitchContainerRenderer;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class ViewSwitcherContainerRenderer extends SwitchContainerRenderer
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
                        parentWidgetInstance.getWidget().getWidgetSettings().getString(ViewSwitcherWidgetController.SETTING_CONFIG_CONTEXT),
                        ViewSwitcherWidgetController.DEFAULT_CONFIG_CONTEXT);
    }


    @Required
    public void setViewSwitcherUtils(final ViewSwitcherUtils viewSwitcherUtils)
    {
        this.viewSwitcherUtils = viewSwitcherUtils;
    }


    protected ViewSwitcherUtils getViewSwitcherUtils()
    {
        return viewSwitcherUtils;
    }
}
