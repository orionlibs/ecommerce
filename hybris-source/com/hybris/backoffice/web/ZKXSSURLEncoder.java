/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.zkoss.web.servlet.http.Encodes;
import org.zkoss.web.servlet.http.Https;

public class ZKXSSURLEncoder implements Encodes.URLEncoder
{
    @Override
    public String encodeURL(final ServletContext servletContext,
                    final ServletRequest request,
                    final ServletResponse response,
                    final String url,
                    final Encodes.URLEncoder defaultEncoder) throws Exception
    {
        String result = defaultEncoder.encodeURL(servletContext, request, response, url, defaultEncoder);
        String requestContextPath = getContextPath(request);
        // for those request URLs that don't start with application context path e.g. wpd, wcs
        // find the context path with path parameters e.g. &sol;ptst.io&sol;xsspoc&quest;/..;/zk9support/
        if(!requestContextPath.equals(servletContext.getContextPath())
                        //manually include relative js doesn't end with request context path
                        && result.contains(servletContext.getContextPath()))
        {
            //replace their prepending path with the application context path
            return result.substring(result.lastIndexOf(servletContext.getContextPath()));
        }
        return result;
    }


    /**
     * Get context path.
     *
     * @param request
     *           servlet request
     * @deprecated since 1811
     */
    @Deprecated(since = "1811", forRemoval = true)
    protected String getContextPath(ServletRequest request)
    {
        return Https.getThisContextPath(request);
    }
}
