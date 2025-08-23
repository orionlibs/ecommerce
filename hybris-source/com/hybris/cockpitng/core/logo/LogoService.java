/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.logo;

/**
 * Provides functionality for getting Logo url.
 */
public interface LogoService
{
    /**
     * Get shell bar logo url
     *
     * @return the logo url of shell bar.
     */
    String getShellBarLogoUrl();


    /**
     * Get login page logo url
     *
     * @return the logo url of login page.
     */
    String getLoginPageLogoUrl();
}
