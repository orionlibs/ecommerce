/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.orderexchange.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.sap.orderexchange.constants.SaporderexchangeConstants;
import org.apache.log4j.Logger;

@SuppressWarnings("javadoc")
public class SaporderexchangeManager extends GeneratedSaporderexchangeManager
{
    @SuppressWarnings("unused")
    private static final Logger log = Logger.getLogger(SaporderexchangeManager.class.getName());


    public static final SaporderexchangeManager getInstance()
    {
        final ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
        return (SaporderexchangeManager)em.getExtension(SaporderexchangeConstants.EXTENSIONNAME);
    }
}
