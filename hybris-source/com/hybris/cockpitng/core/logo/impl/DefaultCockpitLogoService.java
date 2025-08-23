/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.logo.impl;

import com.hybris.cockpitng.core.logo.LogoService;
import org.apache.commons.lang3.StringUtils;

/**
 * Default implementation of service to get and set the current theme.
 */
public class DefaultCockpitLogoService implements LogoService
{
    public String getShellBarLogoUrl()
    {
        return StringUtils.EMPTY;
    }


    public String getLoginPageLogoUrl()
    {
        return StringUtils.EMPTY;
    }
}
