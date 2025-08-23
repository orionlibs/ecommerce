/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.sapproductconfigbackoffice.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.sap.sapproductconfigbackoffice.constants.SapproductconfigbackofficeConstants;

/**
 * Jalo Manager class for <code>sapproductconfigbackoffice</code> extension.
 */
public class SapproductconfigbackofficeManager extends GeneratedSapproductconfigbackofficeManager
{
    /**
     * factory-method for this class
     *
     * @return manager instance
     */
    public static final SapproductconfigbackofficeManager getInstance()
    {
        final ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
        return (SapproductconfigbackofficeManager)em.getExtension(SapproductconfigbackofficeConstants.EXTENSIONNAME);
    }
}
