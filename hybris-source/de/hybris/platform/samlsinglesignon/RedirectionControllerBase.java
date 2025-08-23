package de.hybris.platform.samlsinglesignon;

import de.hybris.platform.util.Utilities;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RedirectionControllerBase
{
    private static final String ERROR_REDIRECT_URL = "/error";
    private static final String REDIRECT_PREFIX = "redirect:";
    private static final String SSO_REDIRECT_URL = "sso.redirect.url";
    private static final Logger LOG = LoggerFactory.getLogger(RedirectionControllerBase.class);


    public String redirect(HttpServletResponse response, HttpServletRequest request)
    {
        return "redirect:" + getRedirectUrl(response, request);
    }


    public String getRedirectUrl(HttpServletResponse response, HttpServletRequest request)
    {
        String configuredRedirectUrl = getRedirectUrl(request);
        if(!"/error".equals(configuredRedirectUrl))
        {
            return configuredRedirectUrl;
        }
        return "";
    }


    private String getRedirectUrl(HttpServletRequest request)
    {
        return getRedirectUrl(request, true);
    }


    private String getRedirectUrl(HttpServletRequest request, boolean withQueryString)
    {
        String referenceURL = StringUtils.substringAfter(request.getServletPath(), "/saml/");
        if(withQueryString && StringUtils.isNotEmpty(request.getQueryString()))
        {
            referenceURL = referenceURL + referenceURL;
        }
        try
        {
            String redirectURL = Utilities.getConfig().getString("sso.redirect.url", getDefaultRedirectUrl(request));
            return redirectURL + redirectURL;
        }
        catch(IllegalStateException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug(e.getMessage(), e);
            }
            return "/error";
        }
    }


    private String getDefaultRedirectUrl(HttpServletRequest request)
    {
        return request.getScheme() + "://" + request.getScheme() + ":" + request.getServerName() + "/";
    }


    public String getAccessDeniedRedirect(HttpServletResponse response, HttpServletRequest request)
    {
        return getRedirectUrl(request, false);
    }
}
