package de.hybris.platform.personalizationservices.occ.impl;

import de.hybris.platform.personalizationservices.configuration.CxConfigurationService;
import de.hybris.platform.personalizationservices.occ.CxOccAttributesStrategy;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.time.TimeService;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Required;

public class CxOccAttributesFromHeaderStrategy implements CxOccAttributesStrategy
{
    private TimeService timeService;
    private ConfigurationService configurationService;
    private CxConfigurationService cxConfigurationService;


    public void setPersonalizationId(String personalizationId, HttpServletRequest request, HttpServletResponse response)
    {
        response.setHeader(getPersonalizationIdHeader(), personalizationId);
    }


    public Optional<String> readPersonalizationId(HttpServletRequest request)
    {
        return readValueFromRequest(request, this::getPersonalizationIdHeader, Function.identity());
    }


    public void setPersonalizationCalculationTime(HttpServletRequest request, HttpServletResponse response)
    {
        String calculationTime = Long.toString(this.timeService.getCurrentTime().getTime());
        response.setHeader(getPersonalizationTimeHeader(), calculationTime);
    }


    public Optional<Long> readPersonalizationCalculationTime(HttpServletRequest request)
    {
        return readValueFromRequest(request, this::getPersonalizationTimeHeader, this::safeValueOf);
    }


    protected <T> Optional<T> readValueFromRequest(HttpServletRequest request, Supplier<String> headerNameSupplier, Function<String, T> valueMaper)
    {
        if(request == null)
        {
            return Optional.empty();
        }
        String value = request.getHeader(headerNameSupplier.get());
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


    protected String getPersonalizationIdHeader()
    {
        return this.configurationService.getConfiguration().getString("personalizationservices.personalizationIdHeader", "Occ-Personalization-Id");
    }


    protected String getPersonalizationTimeHeader()
    {
        return this.configurationService.getConfiguration().getString("personalizationservices.personalizationTimeHeader", "Occ-Personalization-Time");
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
