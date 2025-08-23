/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.runtime.ssc.impl;

import de.hybris.platform.sap.productconfig.runtime.interf.ConfigurationProvider;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.IOException;
import java.io.Serializable;

/**
 * Container for commerce session attributes used in with SSC
 */
public class SSCProductConfigSessionAttributeContainer implements Serializable
{
    private static final long serialVersionUID = 1L;
    @SuppressFBWarnings("SE_TRANSIENT_FIELD_NOT_RESTORED")
    private transient ConfigurationProvider configurationProvider;


    /**
     * @return the configurationProvider
     */
    public ConfigurationProvider getConfigurationProvider()
    {
        return configurationProvider;
    }


    /**
     * @param configurationProvider
     *           the configurationProvider to set
     */
    public void setConfigurationProvider(final ConfigurationProvider configurationProvider)
    {
        this.configurationProvider = configurationProvider;
    }


    private void readObject(final java.io.ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        in.defaultReadObject();
    }
}
