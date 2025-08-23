/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.util.web.authentication;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.authentication.AuthenticationDetailsSource;

/**
 * Provides authentication details object for a backoffice web application spring security configuration.
 * It will be an instance of {@link BackofficeAuthenticationDetails}.
 */
public class BackofficeAuthenticationDetailsSource implements
                AuthenticationDetailsSource<HttpServletRequest, BackofficeAuthenticationDetails>
{
    private String localeRequestParameter;


    @Override
    public BackofficeAuthenticationDetails buildDetails(final HttpServletRequest context)
    {
        return new BackofficeAuthenticationDetails(context, this.localeRequestParameter);
    }


    @Required
    public void setLocaleRequestParameter(final String localeRequestParameter)
    {
        this.localeRequestParameter = localeRequestParameter;
    }
}
