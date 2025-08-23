/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.backoffice.cps.jalo;

import de.hybris.platform.core.Registry;
import de.hybris.platform.sap.productconfig.backoffice.cps.constants.SapproductconfigbackofficecpsConstants;

/**
 * This is the extension manager of the Ybackoffice extension.
 */
public class SapproductconfigbackofficecpsManager extends GeneratedSapproductconfigbackofficecpsManager
{
    /**
     * Never call the constructor of any manager directly, call getInstance() You can place your business logic here -
     * like registering a jalo session listener. Each manager is created once for each tenant.
     */
    public SapproductconfigbackofficecpsManager()
    {
        //
    }


    /**
     * Get the valid instance of this manager.
     *
     * @return the current instance of this manager
     */
    public static SapproductconfigbackofficecpsManager getInstance()
    {
        return (SapproductconfigbackofficecpsManager)Registry.getCurrentTenant().getJaloConnection().getExtensionManager()
                        .getExtension(SapproductconfigbackofficecpsConstants.EXTENSIONNAME);
    }
}
