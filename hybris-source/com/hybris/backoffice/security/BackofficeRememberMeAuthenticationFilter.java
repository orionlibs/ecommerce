/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.security;

import de.hybris.platform.servicelayer.session.SessionService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;

public class BackofficeRememberMeAuthenticationFilter extends RememberMeAuthenticationFilter
{
    public static final String REMEMBER_ME_AUTH_FAILED_PARAM = "rememberMeAuthFailed";
    private SessionService sessionService;


    public BackofficeRememberMeAuthenticationFilter(final AuthenticationManager authenticationManager,
                    final RememberMeServices rememberMeServices, final SessionService sessionService)
    {
        super(authenticationManager, rememberMeServices);
        this.sessionService = sessionService;
    }


    @Override
    protected void onUnsuccessfulAuthentication(final HttpServletRequest request, final HttpServletResponse response,
                    final AuthenticationException failed)
    {
        sessionService.closeCurrentSession();
        request.getSession().setAttribute(REMEMBER_ME_AUTH_FAILED_PARAM, true);
    }
}
