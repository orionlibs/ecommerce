/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2webservices.facade;

import javax.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

/**
 * Receives an HttpServletRequest and delegates its handling. Returns an ResponseEntity.
 */
public interface IntegrationDataFacade
{
    /**
     * Converts and handle's http server requests
     *
     * @param request contains information about the request verb to be handled
     * @return response for integrationDataFacade
     */
    ResponseEntity<String> convertAndHandleRequest(HttpServletRequest request);


    /**
     * Converts and handle's http server requests
     *
     * @param request contains information about $metadata request to be handled
     * @return response for integrationDataFacade
     */
    ResponseEntity<String> convertAndHandleSchemaRequest(HttpServletRequest request);
}
