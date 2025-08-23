/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.sap.sapcentralorderservices.services.config.impl;

import com.sap.sapcentralorderservices.constants.SapcentralorderservicesConstants;
import com.sap.sapcentralorderservices.services.config.CoConfigurationService;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.sap.core.configuration.SAPConfigurationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementation for COS configuration service
 */
public class DefaultCoConfigurationService implements CoConfigurationService
{
    private SAPConfigurationService sapConfigurationService;
    private static final Logger LOG = LoggerFactory.getLogger(DefaultCoConfigurationService.class);


    /**
     *
     * @return {@link String}
     */
    @Override
    public String getCoSourceSystemId()
    {
        return sapConfigurationService.getProperty(SapcentralorderservicesConstants.CO_SOURCE_SYSTEM_ID);
    }


    /**
     *
     * @return {@link Boolean}
     */
    @Override
    public Boolean isCoActive()
    {
        try
        {
            return sapConfigurationService.getProperty(SapcentralorderservicesConstants.CO_ACTIVE);
        }
        catch(final Exception e)
        {
            LOG.warn(e.toString());
            return false;
        }
    }


    /**
     *
     * @return {@link Boolean}
     */
    @Override
    public Boolean isCoActiveFromBaseStore(final OrderModel orderModel)
    {
        return null != orderModel.getStore() && null != orderModel.getStore().getSAPConfiguration()
                        && null != orderModel.getStore().getSAPConfiguration().getSapco_active()
                        && orderModel.getStore().getSAPConfiguration().getSapco_active();
    }


    /**
     * @return the sapConfigurationService
     */
    public SAPConfigurationService getSapConfigurationService()
    {
        return sapConfigurationService;
    }


    /**
     * @param sapConfigurationService
     *           the sapConfigurationService to set
     */
    public void setSapConfigurationService(final SAPConfigurationService sapConfigurationService)
    {
        this.sapConfigurationService = sapConfigurationService;
    }
}
