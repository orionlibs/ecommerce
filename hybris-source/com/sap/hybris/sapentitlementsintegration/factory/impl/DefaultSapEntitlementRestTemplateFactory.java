/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.sap.hybris.sapentitlementsintegration.factory.impl;

import com.sap.hybris.sapentitlementsintegration.factory.SapEntitlementRestTemplateFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Default implementation of SapEntitlementRestTemplateFactory
 */
public class DefaultSapEntitlementRestTemplateFactory implements SapEntitlementRestTemplateFactory
{
    private ClientHttpRequestFactory clientHttpRequestFactory;


    @Override
    public RestTemplate create()
    {
        return new RestTemplate(getClientHttpRequestFactory());
    }


    /**
     * @return the clientHttpRequestFactory
     */
    public ClientHttpRequestFactory getClientHttpRequestFactory()
    {
        return clientHttpRequestFactory;
    }


    /**
     * @param clientHttpRequestFactory
     *           the clientHttpRequestFactory to set
     */
    public void setClientHttpRequestFactory(final ClientHttpRequestFactory clientHttpRequestFactory)
    {
        this.clientHttpRequestFactory = clientHttpRequestFactory;
    }
}
