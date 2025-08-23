/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.sap.sapcentralorderservices.clients;

import com.sap.sapcentralorderservices.exception.SapCentralOrderException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponents;

/**
 *
 */
public interface CentralOrderApiClient
{
    /**
     *
     * @param uriComponents
     * @param entity
     * @throws SapCentralOrderException
     */
    public void postEntity(final UriComponents uriComponents, final Object entity) throws SapCentralOrderException;


    /**
     *
     * @param uriComponents
     * @param clazz
     * @param <T>
     * @return {@link ResponseEntity<T>}
     * @throws SapCentralOrderException
     */
    public <T> ResponseEntity<T> getEntity(final UriComponents uriComponents, final Class<T> clazz)
                    throws SapCentralOrderException;
}
