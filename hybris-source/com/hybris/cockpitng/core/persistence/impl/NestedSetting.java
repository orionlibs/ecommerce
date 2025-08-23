/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.persistence.impl;

import org.apache.commons.lang3.StringUtils;

class NestedSetting
{
    private boolean isNestedSetting;
    private String widgetId;
    private String onlySettingName;


    public NestedSetting(final String compoundSettingName)
    {
        if(compoundSettingName != null && compoundSettingName.startsWith("/"))
        {
            final String[] split = compoundSettingName.split("/");
            if(split.length > 3)
            {
                // slash can be a part of valid setting or widget ID, then we want to skip that
                isNestedSetting = false;
            }
            else if(split.length == 3 && StringUtils.isNotEmpty(split[1]) && StringUtils.isNotEmpty(split[2]))
            {
                isNestedSetting = true;
                widgetId = split[1];
                onlySettingName = split[2];
            }
        }
    }


    public boolean isNestedSetting()
    {
        return isNestedSetting;
    }


    public String getWidgetId()
    {
        return widgetId;
    }


    public String getOnlySettingName()
    {
        return onlySettingName;
    }
}
