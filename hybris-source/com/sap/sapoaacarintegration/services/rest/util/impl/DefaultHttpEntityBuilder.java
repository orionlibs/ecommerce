/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.sap.sapoaacarintegration.services.rest.util.impl;

import com.sap.retail.oaa.commerce.services.reservation.jaxb.pojos.request.ReservationAbapRequest;
import com.sap.retail.oaa.commerce.services.sourcing.jaxb.pojos.request.SourcingAbapRequest;
import com.sap.sapoaacarintegration.services.rest.util.HttpEntityBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

/**
 * Default Implementation for HttpEntityBuilder
 */
public class DefaultHttpEntityBuilder implements HttpEntityBuilder
{
    @Override
    public HttpEntity<ReservationAbapRequest> createHttpEntity(final HttpHeaders header, final ReservationAbapRequest abap)
    {
        return new HttpEntity<>(abap, header);
    }


    @Override
    public HttpEntity<SourcingAbapRequest> createHttpEntityForSourcing(final HttpHeaders header, final SourcingAbapRequest abap)
    {
        return new HttpEntity<>(abap, header);
    }


    @Override
    public HttpEntity<String> createHttpEntity(final HttpHeaders header, final String string)
    {
        return new HttpEntity<>(string, header);
    }


    @Override
    public HttpEntity createHttpEntity(final HttpHeaders header)
    {
        return new HttpEntity(header);
    }
}
