/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.rules.cps.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.sap.productconfig.rules.cps.constants.SapproductconfigrulescpsConstants;

/**
 * Jalo Manager class for <code>SapproductconfigrulescpsManager</code> extension.
 */
public class SapproductconfigrulescpsManager extends GeneratedSapproductconfigrulescpsManager
{
    /**
     * factory-method for this class
     *
     * @return manager instance
     */
    public static final SapproductconfigrulescpsManager getInstance()
    {
        final ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
        return (SapproductconfigrulescpsManager)em.getExtension(SapproductconfigrulescpsConstants.EXTENSIONNAME);
    }
}
