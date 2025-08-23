/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.theme.impl;

import com.hybris.cockpitng.core.theme.ThemeStyleService;
import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation of service to get and set the current theme.
 */
public class DefaultCockpitThemeStyleService implements ThemeStyleService
{
    private static final String STYLE_FORMAT_DEFAULT = "./cng/css/themes/%s/variables.css";
    private String currentThemeId;


    @Override
    public String getCurrentThemeStyle()
    {
        return String.format(STYLE_FORMAT_DEFAULT, currentThemeId);
    }


    @Override
    public Map<String, String> getCurrentThemeStyleMap()
    {
        return new HashMap<>();
    }


    @Override
    public String getCurrentThemeId()
    {
        return currentThemeId;
    }


    public void setCurrentThemeId(final String currentThemeId)
    {
        this.currentThemeId = currentThemeId;
    }
}
