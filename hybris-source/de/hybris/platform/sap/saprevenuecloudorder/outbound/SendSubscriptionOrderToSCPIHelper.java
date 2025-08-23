/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.saprevenuecloudorder.outbound;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.hybris.scpiconnector.data.ResponseData;
import com.sap.hybris.scpiconnector.httpconnection.impl.DefaultCloudPlatformIntegrationConnection;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.sap.saprevenuecloudorder.constants.SaprevenuecloudorderConstants;
import de.hybris.platform.sap.saprevenuecloudorder.data.SubscriptionOrder;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import java.io.IOException;
import org.apache.log4j.Logger;

public class SendSubscriptionOrderToSCPIHelper
{
    private DefaultCloudPlatformIntegrationConnection defaultCloudPlatformIntegrationConnection;
    private ConfigurationService configurationService;
    private Populator<AbstractOrderModel, SubscriptionOrder> sapSubscriptionOrderPopulator;
    private static final Logger LOG = Logger.getLogger(SendSubscriptionOrderToSCPIHelper.class);


    public Populator<AbstractOrderModel, SubscriptionOrder> getSapSubscriptionOrderPopulator()
    {
        return sapSubscriptionOrderPopulator;
    }


    public void setSapSubscriptionOrderPopulator(
                    final Populator<AbstractOrderModel, SubscriptionOrder> sapSubscriptionOrderPopulator)
    {
        this.sapSubscriptionOrderPopulator = sapSubscriptionOrderPopulator;
    }


    public ResponseData sendOrder(final AbstractOrderModel order)
    {
        ResponseData response = new ResponseData();
        final SubscriptionOrder subscriptionOrder = new SubscriptionOrder();
        getSapSubscriptionOrderPopulator().populate(order, subscriptionOrder);
        convertToJSON(subscriptionOrder);
        try
        {
            response = defaultCloudPlatformIntegrationConnection.sendPost(
                            getConfigurationService().getConfiguration().
                                            getString(SaprevenuecloudorderConstants.SUBSCRIPTION_ORDER_IFLOW_KEY)
                            , convertToJSON(subscriptionOrder));
        }
        catch(final IOException e)
        {
            LOG.info("exception while sending order to sci");
            LOG.error(e);
        }
        return response;
    }


    public ConfigurationService getConfigurationService()
    {
        return configurationService;
    }


    public void setConfigurationService(final ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }


    public DefaultCloudPlatformIntegrationConnection getDefaultCloudPlatformIntegrationConnection()
    {
        return defaultCloudPlatformIntegrationConnection;
    }


    public void setDefaultCloudPlatformIntegrationConnection(
                    final DefaultCloudPlatformIntegrationConnection defaultCloudPlatformIntegrationConnection)
    {
        this.defaultCloudPlatformIntegrationConnection = defaultCloudPlatformIntegrationConnection;
    }


    protected String convertToJSON(final SubscriptionOrder subscriptionOrder)
    {
        final ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
        String subsricptionJson = "";
        try
        {
            subsricptionJson = objectMapper.writeValueAsString(subscriptionOrder);
        }
        catch(final JsonProcessingException e)
        {
            LOG.info("exception while sending order to sci");
            LOG.error(e);
        }
        return subsricptionJson;
    }
}