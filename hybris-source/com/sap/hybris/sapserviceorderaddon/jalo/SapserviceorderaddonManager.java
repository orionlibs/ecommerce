/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.sap.hybris.sapserviceorderaddon.jalo;

import com.sap.hybris.sapserviceorderaddon.constants.SapserviceorderaddonConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;

/**
 * Manager class
 */
public class SapserviceorderaddonManager extends GeneratedSapserviceorderaddonManager
{
    /**
     * Get Instance
     *
     * @return manager
     */
    public static final SapserviceorderaddonManager getInstance()
    {
        final ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
        return (SapserviceorderaddonManager)em.getExtension(SapserviceorderaddonConstants.EXTENSIONNAME);
    }
}
