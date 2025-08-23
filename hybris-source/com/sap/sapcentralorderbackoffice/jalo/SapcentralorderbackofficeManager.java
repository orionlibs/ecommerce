/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.sap.sapcentralorderbackoffice.jalo;

import com.sap.sapcentralorderbackoffice.constants.SapcentralorderbackofficeConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

/**
 * SapcentralorderbackofficeManager
 */
public class SapcentralorderbackofficeManager extends GeneratedSapcentralorderbackofficeManager
{
    @SuppressWarnings("unused")
    private static final Logger log = Logger.getLogger(SapcentralorderbackofficeManager.class.getName());


    public static final SapcentralorderbackofficeManager getInstance()
    {
        final ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
        return (SapcentralorderbackofficeManager)em.getExtension(SapcentralorderbackofficeConstants.EXTENSIONNAME);
    }
}
