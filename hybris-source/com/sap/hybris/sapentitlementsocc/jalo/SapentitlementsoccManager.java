/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.sap.hybris.sapentitlementsocc.jalo;

import com.sap.hybris.sapentitlementsocc.constants.SapentitlementsoccConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;

public class SapentitlementsoccManager extends GeneratedSapentitlementsoccManager
{
    public static final SapentitlementsoccManager getInstance()
    {
        ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
        return (SapentitlementsoccManager)em.getExtension(SapentitlementsoccConstants.EXTENSIONNAME);
    }
}
