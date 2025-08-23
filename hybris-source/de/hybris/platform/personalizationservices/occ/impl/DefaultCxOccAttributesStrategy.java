package de.hybris.platform.personalizationservices.occ.impl;

import de.hybris.platform.personalizationservices.configuration.CxConfigurationService;
import de.hybris.platform.personalizationservices.model.config.CxConfigModel;
import de.hybris.platform.personalizationservices.occ.CxOccAttributesStrategy;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.time.TimeService;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

@Deprecated(since = "2005", forRemoval = true)
public class DefaultCxOccAttributesStrategy implements CxOccAttributesStrategy
{
    private TimeService timeService;
    private ConfigurationService configurationService;
    private CxConfigurationService cxConfigurationService;


    public void setPersonalizationId(String personalizationId, HttpServletRequest request, HttpServletResponse response)
    {
        response.setHeader(getPersonalizationIdHeader(), personalizationId);
        if(isPersonalizationCookieEnabled())
        {
            Cookie cookie = createCookie(getPersonalizationIdCookie(), personalizationId, request.getContextPath());
            response.addCookie(cookie);
        }
    }


    public Optional<String> readPersonalizationId(HttpServletRequest request)
    {
        return readValueFromRequest(request, this::getPersonalizationIdHeader, this::getPersonalizationIdCookie,
                        Function.identity());
    }


    public void setPersonalizationCalculationTime(HttpServletRequest request, HttpServletResponse response)
    {
        String calculationTime = Long.toString(this.timeService.getCurrentTime().getTime());
        response.setHeader(getPersonalizationTimeHeader(), calculationTime);
        if(isPersonalizationCookieEnabled())
        {
            Cookie cookie = createCookie(getPersonalizationTimeCookie(), calculationTime, request.getContextPath());
            response.addCookie(cookie);
        }
    }


    public Optional<Long> readPersonalizationCalculationTime(HttpServletRequest request)
    {
        return readValueFromRequest(request, this::getPersonalizationTimeHeader, this::getPersonalizationTimeCookie, this::safeValueOf);
    }


    private Cookie createCookie(String name, String value, String contextPath)
    {
        Cookie cookie = new Cookie(name, value);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        if(contextPath != null)
        {
            cookie.setPath(contextPath);
        }
        else
        {
            cookie.setPath("/");
        }
        return cookie;
    }


    protected <T> Optional<T> readValueFromRequest(HttpServletRequest request, Supplier<String> headerNameSupplier, Supplier<String> cookieNameSupplier, Function<String, T> valueMaper)
    {
        if(request == null)
        {
            return Optional.empty();
        }
        String value = request.getHeader(headerNameSupplier.get());
        if(StringUtils.isBlank(value) && isPersonalizationCookieEnabled() && request.getCookies() != null)
        {
            String cookieName = cookieNameSupplier.get();
            return Stream.<Cookie>of(request.getCookies())
                            .filter(c -> cookieName.equals(c.getName()))
                            .findAny()
                            .map(Cookie::getValue).map(valueMaper);
        }
        return Optional.<String>ofNullable(value).map(valueMaper);
    }


    protected Long safeValueOf(String value)
    {
        try
        {
            return Long.valueOf(value);
        }
        catch(NumberFormatException e)
        {
            return null;
        }
    }


    protected boolean isPersonalizationCookieEnabled()
    {
        return ((Boolean)this.cxConfigurationService.getConfiguration().map(CxConfigModel::getOccPersonalizationIdCookieEnabled)
                        .orElse(Boolean.FALSE)).booleanValue();
    }


    protected String getPersonalizationIdHeader()
    {
        return this.configurationService.getConfiguration().getString("personalizationservices.personalizationIdHeader", "Occ-Personalization-Id");
    }


    protected String getPersonalizationIdCookie()
    {
        return this.configurationService.getConfiguration().getString("personalizationservices.personalizationIdCookie", "personalizationId");
    }


    protected String getPersonalizationTimeHeader()
    {
        return this.configurationService.getConfiguration().getString("personalizationservices.personalizationTimeHeader", "Occ-Personalization-Time");
    }


    protected String getPersonalizationTimeCookie()
    {
        return this.configurationService.getConfiguration().getString("personalizationservices.personalizationTimeCookie", "CxCt");
    }


    protected TimeService getTimeService()
    {
        return this.timeService;
    }


    @Required
    public void setTimeService(TimeService timeService)
    {
        this.timeService = timeService;
    }


    protected ConfigurationService getConfigurationService()
    {
        return this.configurationService;
    }


    @Required
    public void setConfigurationService(ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }


    protected CxConfigurationService getCxConfigurationService()
    {
        return this.cxConfigurationService;
    }


    @Required
    public void setCxConfigurationService(CxConfigurationService cxConfigurationService)
    {
        this.cxConfigurationService = cxConfigurationService;
    }
}
