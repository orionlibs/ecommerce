/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */

package com.hybris.backoffice.filter.responseheaders;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

public class BackofficeLoginErrorFilter extends GenericFilterBean
{
    private static final String LOGIN_ERROR = "login_error";
    private static final String ERROR_CODE_HEADER = "X-BO-Login-Error-Code";


    @Override
    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain)
                    throws IOException, ServletException
    {
        final HttpServletRequest request = (HttpServletRequest)servletRequest;
        final HttpServletResponse response = (HttpServletResponse)servletResponse;
        final String errorCode = request.getParameter(LOGIN_ERROR);
        if(StringUtils.isNotEmpty(errorCode))
        {
            response.setHeader(ERROR_CODE_HEADER, errorCode);
        }
        filterChain.doFilter(request, response);
    }
}
