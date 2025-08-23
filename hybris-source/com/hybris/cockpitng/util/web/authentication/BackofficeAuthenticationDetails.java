/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.util.web.authentication;

import javax.servlet.http.HttpServletRequest;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

/**
 *
 */
public class BackofficeAuthenticationDetails extends WebAuthenticationDetails
{
    private static final long serialVersionUID = -8436490820891383938L;
    private final String locale;


    public BackofficeAuthenticationDetails(final HttpServletRequest request, final String localeRequestParameter)
    {
        super(request);
        this.locale = request.getParameter(localeRequestParameter);
    }


    public String getLocale()
    {
        return this.locale;
    }


    @Override
    public boolean equals(final Object obj)
    {
        if(this == obj)
        {
            return true;
        }
        if(obj == null)
        {
            return false;
        }
        if(!super.equals(obj) || obj.getClass() != this.getClass())
        {
            return false;
        }
        final BackofficeAuthenticationDetails details = (BackofficeAuthenticationDetails)obj;
        return this.localeEquals(details);
    }


    private boolean localeEquals(final BackofficeAuthenticationDetails details)
    {
        if(this.locale == null && details.locale != null)
        {
            return false;
        }
        if(this.locale != null && details.locale == null)
        {
            return false;
        }
        return !(this.locale != null && !this.locale.equals(details.locale));
    }


    @Override
    public int hashCode()
    {
        int code = super.hashCode();
        if(this.locale != null)
        {
            code = code * this.locale.hashCode();
        }
        return code;
    }
}
