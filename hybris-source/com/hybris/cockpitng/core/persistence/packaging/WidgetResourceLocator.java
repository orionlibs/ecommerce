/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.persistence.packaging;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Interface used by resource loader to help to resolve resources.
 */
public interface WidgetResourceLocator
{
    /**
     * @param req request
     * @param servletConfig
     * @return true if the locator can resolve the resource
     */
    boolean isApplicableTo(HttpServletRequest req, ServletConfig servletConfig);


    /**
     * @param req request
     * @param resp response
     * @param servletConfig
     * @return true if the resource is completely resolved and requires no further processing
     */
    boolean apply(HttpServletRequest req, HttpServletResponse resp, ServletConfig servletConfig);
}
