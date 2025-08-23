/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmswebservices.filter;

import de.hybris.platform.servicelayer.session.Session;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * Interface for adding additional header fields to the session
 *
 */
public interface RestSessionFilterArgument
{
    /**
     * Allows the definition of additional rest headers to the session
     *
     * @param request
     *           the http request object
     * @param response
     *           the http response object
     * @param session
     *           the local session object
     */
    void addSessionArgument(HttpServletRequest request, ServletResponse response, Session session);
}
