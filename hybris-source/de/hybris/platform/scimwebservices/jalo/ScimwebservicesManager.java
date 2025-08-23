/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.scimwebservices.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.scimwebservices.constants.ScimwebservicesConstants;
import org.apache.log4j.Logger;

public class ScimwebservicesManager extends GeneratedScimwebservicesManager
{
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(ScimwebservicesManager.class.getName());


    public static final ScimwebservicesManager getInstance()
    {
        final ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
        return (ScimwebservicesManager)em.getExtension(ScimwebservicesConstants.EXTENSIONNAME);
    }
}
