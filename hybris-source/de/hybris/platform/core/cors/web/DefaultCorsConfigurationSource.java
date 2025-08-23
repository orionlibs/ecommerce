package de.hybris.platform.core.cors.web;

import de.hybris.platform.core.cors.converter.ConfigPropertiesToCorsConfigurationConverter;
import de.hybris.platform.core.cors.exception.MissingDefaultCorsConfigurationException;
import de.hybris.platform.core.cors.loader.CorsPropertiesLoader;
import de.hybris.platform.core.model.cors.CorsConfigurationPropertyModel;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.util.localization.Localization;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

public class DefaultCorsConfigurationSource implements CorsConfigurationSource
{
    private static final Logger LOG = Logger.getLogger(DefaultCorsConfigurationSource.class);
    private DefaultGenericDao<CorsConfigurationPropertyModel> corsConfigurationPropertyDao;
    private ConfigPropertiesToCorsConfigurationConverter configPropertiesToCorsConfigurationConverter;
    private CorsPropertiesLoader propertiesLoader;


    public CorsConfiguration getCorsConfiguration(HttpServletRequest request)
    {
        String contextName = extractContextName(request);
        CorsConfiguration result = getDefaultOrCreateCorsConfiguration(contextName);
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Using following CORS configuration for context: " + contextName);
            LOG.debug(ToStringBuilder.reflectionToString(result));
        }
        return result;
    }


    protected CorsConfiguration getDefaultOrCreateCorsConfiguration(String contextName)
    {
        Map<String, String> corsConfiguration = readCorsConfiguration(contextName);
        if(corsConfiguration.isEmpty())
        {
            LOG.info(Localization.getLocalizedString("exception.cors.missing.cors.configuration", (Object[])new String[] {contextName}));
            return getDefaultCorsConfiguration();
        }
        return this.configPropertiesToCorsConfigurationConverter.createCorsConfiguration(contextName, corsConfiguration);
    }


    protected Map<String, String> readCorsConfiguration(String contextName)
    {
        Map<String, String> params = new HashMap<>();
        params.put("context", contextName);
        return mergeCorsConfigurations(loadPropertiesFromHybrisConfig(contextName), new HashSet<>(this.corsConfigurationPropertyDao
                        .find(params)));
    }


    protected CorsConfiguration getDefaultCorsConfiguration()
    {
        Map<String, String> corsConfiguration = readCorsConfiguration("default");
        if(corsConfiguration.isEmpty())
        {
            throw new MissingDefaultCorsConfigurationException(
                            Localization.getLocalizedString("exception.cors.missing.default.configuration"));
        }
        return this.configPropertiesToCorsConfigurationConverter.createCorsConfiguration("default", corsConfiguration);
    }


    protected Map<String, String> loadPropertiesFromHybrisConfig(String contextName)
    {
        return (Map<String, String>)this.propertiesLoader.loadProperties(contextName);
    }


    protected Map<String, String> mergeCorsConfigurations(Map<String, String> props, Set<CorsConfigurationPropertyModel> current)
    {
        HashMap<String, String> result;
        if(props == null)
        {
            result = new HashMap<>();
        }
        else
        {
            result = new HashMap<>(props);
        }
        current.forEach(p -> result.put("corsfilter." + p.getContext() + "." + p.getKey(), p.getValue()));
        return result;
    }


    protected String extractContextName(HttpServletRequest request)
    {
        String result, contextName = request.getServletContext().getServletContextName();
        if("/".equals(contextName))
        {
            result = "ROOT";
        }
        else if(contextName.startsWith("/"))
        {
            result = contextName.substring(1);
        }
        else
        {
            result = contextName;
        }
        return result;
    }


    @Required
    public void setCorsConfigurationPropertyDao(DefaultGenericDao<CorsConfigurationPropertyModel> corsConfigurationDao)
    {
        this.corsConfigurationPropertyDao = corsConfigurationDao;
    }


    @Required
    public void setConfigPropertiesToCorsConfigurationConverter(ConfigPropertiesToCorsConfigurationConverter configPropertiesToCorsConfigurationConverter)
    {
        this.configPropertiesToCorsConfigurationConverter = configPropertiesToCorsConfigurationConverter;
    }


    @Required
    public void setPropertiesLoader(CorsPropertiesLoader propertiesLoader)
    {
        this.propertiesLoader = propertiesLoader;
    }


    protected CorsPropertiesLoader getPropertiesLoader()
    {
        return this.propertiesLoader;
    }
}
