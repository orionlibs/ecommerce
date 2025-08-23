/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.util.web.authorization;

import static com.hybris.cockpitng.util.web.authorization.BackofficeAuthenticationSuccessHandler.BO_LOGIN_BOOKMARK;

import java.io.IOException;
import java.util.Base64;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

public class BackofficeAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler
{
    public static final String DEFAULT_FAILURE_URL = "/login.zul?login_error=1";
    public static final String LOCATION_HEADER = "Location";


    public BackofficeAuthenticationFailureHandler()
    {
        super.setDefaultFailureUrl(DEFAULT_FAILURE_URL);
    }


    @Override
    public void onAuthenticationFailure(final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse,
                    final AuthenticationException e) throws IOException, ServletException
    {
        super.saveException(httpServletRequest, e);
        for(final Cookie cookie : httpServletRequest.getCookies())
        {
            if(BO_LOGIN_BOOKMARK.equals(cookie.getName()))
            {
                final String targetUrl = DEFAULT_FAILURE_URL + '#' + new String(Base64.getDecoder().decode(cookie.getValue()));
                httpServletResponse.addHeader(LOCATION_HEADER, httpServletRequest.getContextPath() + targetUrl);
                return;
            }
        }
        httpServletResponse.addHeader(LOCATION_HEADER, httpServletRequest.getContextPath() + DEFAULT_FAILURE_URL);
    }
}
