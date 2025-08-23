/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.theme.impl;

import com.hybris.backoffice.model.ThemeModel;
import com.hybris.backoffice.theme.BackofficeThemeService;
import com.hybris.backoffice.widgets.branding.customthemes.themes.Util;
import com.hybris.cockpitng.core.theme.ThemeStyleService;
import java.util.Map;

/**
 * Service used to set the theme used by backoffice or get the theme currently used by backoffice
 */
public class BackofficeThemeStyleService implements ThemeStyleService
{
    private BackofficeThemeService backofficeThemeService;
    private Util customThemeUtil;


    @Override
    public String getCurrentThemeStyle()
    {
        final ThemeModel theme = backofficeThemeService.getCurrentTheme();
        if(theme.getStyle() == null)
        {
            return backofficeThemeService.getDefaultTheme().getStyle().getURL();
        }
        return theme.getStyle().getURL();
    }


    @Override
    public String getCurrentThemeId()
    {
        return backofficeThemeService.getCurrentTheme().getCode();
    }


    @Override
    public Map<String, String> getCurrentThemeStyleMap()
    {
        return customThemeUtil.convertStyleMediaToVariableMap(backofficeThemeService.getCurrentTheme().getStyle());
    }


    public void setBackofficeThemeService(final BackofficeThemeService backofficeThemeService)
    {
        this.backofficeThemeService = backofficeThemeService;
    }


    public void setCustomThemeUtil(final Util customThemeUtil)
    {
        this.customThemeUtil = customThemeUtil;
    }
}

