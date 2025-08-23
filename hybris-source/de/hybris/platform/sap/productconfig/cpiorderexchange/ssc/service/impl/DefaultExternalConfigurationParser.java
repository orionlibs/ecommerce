/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.cpiorderexchange.ssc.service.impl;

import com.sap.sce.kbrt.ext_configuration;
import com.sap.sce.kbrt.imp.c_ext_cfg_imp;
import de.hybris.platform.sap.productconfig.cpiorderexchange.ssc.service.ExternalConfigurationParser;

public class DefaultExternalConfigurationParser extends c_ext_cfg_imp implements ExternalConfigurationParser
{
    protected static final String RUNTIME_ENVIRONMENT = "runtimeEnvironment";
    private static final long serialVersionUID = 8609611206044445824L;
    private String language;
    private String runtimeEnvironment = "standalone";


    /**
     * Set language assuming that provided code is already SAP code
     */
    @Override
    public void set_language(final String val)
    {
        this.language = val;
    }


    @Override
    public String get_language()
    {
        return language;
    }


    public ext_configuration readExternalConfigFromString(final String str)
    {
        initializeRuntimeEnvironment();
        final DefaultExternalConfigurationParser result = new DefaultExternalConfigurationParser();
        result.cfg_ext_load_data_from_string(str);
        return result;
    }


    protected void initializeRuntimeEnvironment()
    {
        final String environment = System.getProperty(RUNTIME_ENVIRONMENT);
        if(environment == null || environment.isEmpty())
        {
            System.setProperty(RUNTIME_ENVIRONMENT, this.runtimeEnvironment);
        }
    }


    public void setRuntimeEnvironment(final String runtimeEnvironment)
    {
        if(runtimeEnvironment != null && !runtimeEnvironment.isEmpty())
        {
            this.runtimeEnvironment = runtimeEnvironment.intern();
        }
        else
        {
            throw new IllegalArgumentException("System property 'runtimeEnvironment' must not be null or empty.");
        }
    }
}



