/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.util.web.authorization;

import com.hybris.cockpitng.admin.CockpitMainWindowComposer;
import com.hybris.cockpitng.core.user.CockpitUserService;
import com.hybris.cockpitng.core.util.impl.TypedSettingsMap;
import com.hybris.cockpitng.i18n.CockpitLocaleService;
import com.hybris.cockpitng.util.web.authentication.BackofficeAuthenticationDetails;
import java.io.IOException;
import java.util.Base64;
import java.util.Locale;
import java.util.Objects;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.util.Assert;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.zkoss.web.Attributes;

public class BackofficeAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler
{
    public static final String BO_LOGIN_BOOKMARK = CockpitMainWindowComposer.BO_LOGIN_BOOKMARK;
    private CockpitUserService cockpitUserService;
    private CockpitLocaleService cockpitLocaleService;
    private String loginInfoSessionBeanName;
    private Boolean contextRelative;


    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response,
                    final Authentication authentication) throws ServletException, IOException
    {
        clearLoginHandlerCredentials(request.getServletContext());
        // user info
        if(cockpitUserService != null)
        {
            cockpitUserService.setCurrentUser(authentication.getName());
        }
        // locale
        final String locale = getDetails(authentication).getLocale();
        if(!StringUtils.isBlank(locale))
        {
            final Locale preferredLocale;
            try
            {
                preferredLocale = LocaleUtils.toLocale(locale);
                if(cockpitLocaleService != null)
                {
                    cockpitLocaleService.setCurrentLocale(preferredLocale);
                }
                request.getSession().setAttribute(Attributes.PREFERRED_LOCALE, preferredLocale);
            }
            catch(final IllegalArgumentException e)
            {
                LoggerFactory.getLogger(this.getClass())
                                .warn("wrong locale parameter " + locale + ", not activating locale for session", e);
            }
        }
        this.onAuthenticationSuccessHandler(request, response);
    }


    protected void onAuthenticationSuccessHandler(final HttpServletRequest request, final HttpServletResponse response)
    {
        if(response.isCommitted())
        {
            LoggerFactory.getLogger(this.getClass())
                            .warn("Skip redirect as response has been already committed.");
        }
        else
        {
            final String targetUrl = this.determineTargetUrl(request, response);
            final String redirectUrl = this.calculateRedirectUrl(request.getContextPath(), targetUrl);
            if(UrlUtils.isValidRedirectUrl(redirectUrl))
            {
                response.addHeader("Location", redirectUrl);
            }
            this.clearAuthenticationAttributes(request);
        }
    }


    protected void clearLoginHandlerCredentials(final ServletContext servletContext)
    {
        final TypedSettingsMap loginInfoModel = getSessionLoginInfoModel(servletContext);
        if(loginInfoModel != null)
        {
            loginInfoModel.put("j_username", StringUtils.EMPTY);
            loginInfoModel.put("j_password", StringUtils.EMPTY);
        }
    }


    protected final BackofficeAuthenticationDetails getDetails(final Authentication authentication)
    {
        if(!(authentication.getDetails() instanceof BackofficeAuthenticationDetails))
        {
            throw new IllegalStateException(
                            "Needs to have authentication details of type '" + BackofficeAuthenticationDetails.class + "' set");
        }
        return (BackofficeAuthenticationDetails)authentication.getDetails();
    }


    @Override
    protected String determineTargetUrl(final HttpServletRequest request, final HttpServletResponse response)
    {
        if(isAlwaysUseDefaultTargetUrl())
        {
            return super.determineTargetUrl(request, response);
        }
        final StringBuilder builder = new StringBuilder(super.determineTargetUrl(request, response));
        for(final Cookie cookie : request.getCookies())
        {
            if(BO_LOGIN_BOOKMARK.equals(cookie.getName()))
            {
                builder.append('#');
                builder.append(new String(Base64.getDecoder().decode(cookie.getValue())));
                cookie.setMaxAge(0);
                response.addCookie(cookie);
                break;
            }
        }
        return builder.toString();
    }


    protected String calculateRedirectUrl(String contextPath, String url)
    {
        if(!UrlUtils.isAbsoluteUrl(url))
        {
            return this.getContextRelative() ? url : (contextPath + url);
        }
        else if(!this.getContextRelative())
        {
            return url;
        }
        else
        {
            Assert.isTrue(url.contains(contextPath), "The fully qualified URL does not include context path.");
            final String urlSchemeSeparator = "://";
            url = url.substring(url.lastIndexOf(urlSchemeSeparator) + urlSchemeSeparator.length());
            url = url.substring(url.indexOf(contextPath) + contextPath.length());
            if(url.length() > 1 && url.charAt(0) == '/')
            {
                url = url.substring(1);
            }
            return url;
        }
    }


    public TypedSettingsMap getSessionLoginInfoModel(final ServletContext servletContext)
    {
        final WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        return Objects.nonNull(context) ? context.getBean(loginInfoSessionBeanName, TypedSettingsMap.class) : null;
    }


    public String getLoginInfoSessionBeanName()
    {
        return loginInfoSessionBeanName;
    }


    @Required
    public void setLoginInfoSessionBeanName(final String loginInfoSessionBeanName)
    {
        this.loginInfoSessionBeanName = loginInfoSessionBeanName;
    }


    public CockpitUserService getCockpitUserService()
    {
        return cockpitUserService;
    }


    public void setCockpitUserService(final CockpitUserService cockpitUserService)
    {
        this.cockpitUserService = cockpitUserService;
    }


    public CockpitLocaleService getCockpitLocaleService()
    {
        return cockpitLocaleService;
    }


    public void setCockpitLocaleService(final CockpitLocaleService cockpitLocaleService)
    {
        this.cockpitLocaleService = cockpitLocaleService;
    }


    public void setContextRelative(Boolean contextRelative)
    {
        this.contextRelative = contextRelative;
    }


    public boolean getContextRelative()
    {
        return this.contextRelative;
    }
}
