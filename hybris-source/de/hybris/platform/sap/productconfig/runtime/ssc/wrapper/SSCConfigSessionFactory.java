/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.runtime.ssc.wrapper;

import com.sap.custdev.projects.fbs.slc.cfg.IConfigSession;
import com.sap.custdev.projects.fbs.slc.cfg.imp.ConfigSessionImpl;

/**
 * Wraps the creation of an IConfigSession in order to decouple code
 */
public class SSCConfigSessionFactory
{
    public IConfigSession provideInstance()
    {
        return new ConfigSessionImpl();
    }
}
