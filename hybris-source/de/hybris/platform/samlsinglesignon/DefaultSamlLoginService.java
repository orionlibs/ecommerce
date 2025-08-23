package de.hybris.platform.samlsinglesignon;

import com.google.common.base.Splitter;
import com.google.common.net.InternetDomainName;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.persistence.security.EJBPasswordEncoderNotFoundException;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.util.Utilities;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

public class DefaultSamlLoginService implements SamlLoginService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultSamlLoginService.class);
    private static final String SSO_RETURN_PARAM_NAME = "sso.return.url.param.name";
    private static final String SSO_REDIRECT_WHITELIST_PARAM_NAME = "sso.return.url.domain.whitelist";
    private static final String SSO_COOKIE_MAX_AGE = "sso.cookie.max.age";
    private static final String SSO_COOKIE_PATH = "sso.cookie.path";
    private static final String SSO_COOKIE_DOMAIN = "sso.cookie.domain";
    private static final String SSO_DEFAULT_COOKIE_DOMAIN = null;
    private static final String SSO_DEFAULT_COOKIE_NAME = "samlPassThroughToken";
    private static final String DEFAULT_COOKIE_PATH = "/";
    private static final int DEFAULT_COOKIE_MAX_AGE = 60;
    private static final String SSO_COOKIE_NAME = "sso.cookie.name";
    private final HttpSessionRequestCache requestCache = new HttpSessionRequestCache();


    public void storeLoginToken(HttpServletResponse response, UserModel user, String languageIsoCode)
    {
        try
        {
            String cookieMaxAgeStr = Utilities.getConfig().getString("sso.cookie.max.age", String.valueOf(60));
            int cookieMaxAge = NumberUtils.isCreatable(cookieMaxAgeStr) ? Integer.parseInt(cookieMaxAgeStr) : 60;
            UserManager.getInstance().storeLoginTokenCookie(
                            Utilities.getConfig().getString("sso.cookie.name", "samlPassThroughToken"), user
                                            .getUid(), languageIsoCode, null,
                            Utilities.getConfig().getString("sso.cookie.path", "/"),
                            Utilities.getConfig().getString("sso.cookie.domain", SSO_DEFAULT_COOKIE_DOMAIN), true, cookieMaxAge, response);
        }
        catch(EJBPasswordEncoderNotFoundException e)
        {
            throw new SystemException(e);
        }
    }


    public Optional<String> getRedirectionUrl(HttpServletRequest request)
    {
        return Optional.<SavedRequest>ofNullable(this.requestCache.getRequest(request, null))
                        .flatMap(this::parseUrl)
                        .filter(url -> canRedirectToUrl(request, url));
    }


    private Optional<String> parseUrl(SavedRequest savedRequest)
    {
        String redirectUrlParameter = Utilities.getConfig().getParameter("sso.return.url.param.name");
        if(StringUtils.isBlank(redirectUrlParameter))
        {
            return Optional.empty();
        }
        return Optional.<String>ofNullable(savedRequest.getRedirectUrl())
                        .flatMap(this::getQuery)
                        .map(query -> StringUtils.substringAfter(query, redirectUrlParameter + "="))
                        .filter(StringUtils::isNotBlank);
    }


    private Optional<String> getQuery(String url)
    {
        try
        {
            return Optional.ofNullable((new URI(url)).getQuery());
        }
        catch(URISyntaxException e)
        {
            LOGGER.error("Encountered problems with URL parsing :", e);
            return Optional.empty();
        }
    }


    protected boolean canRedirectToUrl(HttpServletRequest request, String url)
    {
        try
        {
            URL parsedURL = new URL(url);
            URL requestURL = new URL(request.getRequestURL().toString());
            return (parsedURL.getHost().equals(requestURL.getHost()) || isOnWhiteList(parsedURL.getHost()) ||
                            isOnWhiteList(InternetDomainName.from(parsedURL.getHost()).topPrivateDomain().toString()));
        }
        catch(MalformedURLException | IllegalArgumentException e)
        {
            LOGGER.error("Exception while parsing url: ", e);
            return false;
        }
    }


    protected final boolean isOnWhiteList(String domain)
    {
        return (domain != null &&
                        Splitter.on(",")
                                        .omitEmptyStrings()
                                        .splitToList(Utilities.getConfig().getString("sso.return.url.domain.whitelist", ""))
                                        .contains(domain));
    }
}
