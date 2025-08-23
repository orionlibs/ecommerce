/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.baseeditorarea;

import com.hybris.cockpitng.engine.WidgetInstanceManager;
import org.apache.commons.lang3.BooleanUtils;

public class DefaultEditorAreaControllerSettingsLoader
{
    private static final String SETTING_INIT_VIEW_MODE = "initViewMode";
    static final String SETTING_FLUID_MIN_WIDTH = "minFluidWidthTreshhold";
    static final String SETTING_FLUID_MAX_WIDTH = "maxFluidWidthTreshhold";
    private static final String SETTING_EMBEDED_TITLE = "embededTitle";
    private static final String SETTING_EMBEDED_TITLE_PREFIX = "embededTitlePrefix";
    private static final String SETTING_ENABLE_EMBEDED_TITLE = "enableEmbededTitle";
    private final WidgetInstanceManager widgetInstanceManager;


    public DefaultEditorAreaControllerSettingsLoader(final DefaultEditorAreaController controller)
    {
        widgetInstanceManager = controller.getWidgetInstanceManager();
    }


    public boolean isEssentialsInOverviewTab()
    {
        return widgetInstanceManager.getWidgetSettings().getBoolean(DefaultEditorAreaController.SETTING_ESSENTIALS_IN_OVERVIEW_TAB);
    }


    public String getInitViewMode()
    {
        return widgetInstanceManager.getWidgetSettings().getString(SETTING_INIT_VIEW_MODE);
    }


    public int getMinWidthThreshold()
    {
        final int minFromSetting = widgetInstanceManager.getWidgetSettings().getInt(SETTING_FLUID_MIN_WIDTH);
        if(minFromSetting > 0)
        {
            return minFromSetting;
        }
        return DefaultEditorAreaController.FLUID_MIN_WIDTH;
    }


    public int getMaxWidthThreshold()
    {
        final int maxFromSetting = widgetInstanceManager.getWidgetSettings().getInt(SETTING_FLUID_MAX_WIDTH);
        if(maxFromSetting > 0)
        {
            return maxFromSetting;
        }
        return DefaultEditorAreaController.FLUID_MAX_WIDTH;
    }


    public boolean isEnableEmbeddedTitle()
    {
        return BooleanUtils
                        .isTrue(Boolean.valueOf(widgetInstanceManager.getWidgetSettings().getBoolean(SETTING_ENABLE_EMBEDED_TITLE)));
    }


    public String getEmbeddedTitle()
    {
        return widgetInstanceManager.getWidgetSettings().getString(SETTING_EMBEDED_TITLE);
    }


    public String getEmbeddedTitlePrefix()
    {
        return widgetInstanceManager.getWidgetSettings().getString(SETTING_EMBEDED_TITLE_PREFIX);
    }


    public boolean isTitleFromContext()
    {
        return widgetInstanceManager.getWidgetSettings().getBoolean(DefaultEditorAreaController.SETTING_TITLE_BY_CTX);
    }
}
