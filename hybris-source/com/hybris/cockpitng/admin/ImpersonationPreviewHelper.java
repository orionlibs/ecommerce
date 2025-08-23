/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.admin;

import com.hybris.cockpitng.util.CockpitSessionService;
import org.apache.commons.lang3.BooleanUtils;

/**
 * Checks is the "Impersonated role preview mode" is enabled for the current session. It allows to set/unset the flag
 * for the user session.
 */
public class ImpersonationPreviewHelper
{
    private static final String ROLE_PREVIEW_ENABLED = "rolePreviewEnabled";
    private CockpitSessionService cockpitSessionService;


    /**
     * Checks is the "Impersonated role preview mode" is enabled for the current session.
     *
     * @return boolean
     */
    public boolean isImpersonatedPreviewEnabled()
    {
        final Boolean impersonationPreviewEnabled = (Boolean)cockpitSessionService.getAttribute(ROLE_PREVIEW_ENABLED);
        return !BooleanUtils.isFalse(impersonationPreviewEnabled);
    }


    /**
     * Enables "Impersonated role preview mode".
     */
    public void enableImpersonatedPreview()
    {
        setImpersonatedPreviewFlag(true);
    }


    /**
     * Disables "Impersonated role preview mode".
     */
    public void disableImpersonatedPreview()
    {
        setImpersonatedPreviewFlag(false);
    }


    /**
     *
     * @param value boolean if the role preview is enabled or not
     */
    public void setImpersonatedPreviewFlag(final boolean value)
    {
        cockpitSessionService.setAttribute(ROLE_PREVIEW_ENABLED, value);
    }


    /**
     * @param cockpitSessionService the cockpitSessionService to set
     */
    public void setCockpitSessionService(final CockpitSessionService cockpitSessionService)
    {
        this.cockpitSessionService = cockpitSessionService;
    }
}
