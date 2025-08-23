/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.theme;

import java.util.Map;
import org.springframework.lang.NonNull;

/**
 * Provides functionality for getting current style url.
 */
public interface ThemeStyleService
{
    /**
     * Get current style url.
     *
     * @return current style url
     */
    @NonNull
    String getCurrentThemeStyle();


    /**
     * Get current theme id.
     *
     * @return current theme id
     */
    @NonNull
    String getCurrentThemeId();


    /**
     * Get current theme style map.
     *
     * @return current theme style map
     */
    @NonNull
    Map<String, String> getCurrentThemeStyleMap();
}
