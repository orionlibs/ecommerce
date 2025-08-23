package com.hybris.datahub.client;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import javax.ws.rs.core.Feature;
import org.apache.commons.lang3.StringUtils;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

public class ClientConfiguration implements InitializingBean
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientConfiguration.class);
    private Set<Class<?>> providerClasses = new HashSet<>();
    private Set<Feature> features = new HashSet<>();
    private final Map<String, Object> properties = new HashMap<>();
    private String contentType = "application/json";
    private HttpAuthenticationFeature securityConfig;
    private SecurityCredentialsInfo securityCredentialsInfo;


    public void afterPropertiesSet()
    {
        if(!Objects.isNull(this.securityCredentialsInfo))
        {
            this.securityCredentialsInfo.applyTo(this);
        }
    }


    public void setSecurityClientFilter(HttpAuthenticationFeature securityConfig)
    {
        this.securityConfig = securityConfig;
    }


    public void setProviderClasses(Set<Class<?>> providers)
    {
        if(providers != null)
        {
            this.providerClasses = providers;
        }
    }


    public ClientConfiguration addProviderClass(Class<JacksonJsonProvider> providerClass)
    {
        if(providerClass != null)
        {
            this.providerClasses.add(providerClass);
        }
        return this;
    }


    public void setFeatures(Set<Feature> f)
    {
        if(f != null)
        {
            this.features = f;
        }
    }


    public ClientConfiguration addFeature(Feature feature)
    {
        if(!Objects.isNull(feature))
        {
            this.features.add(feature);
        }
        return this;
    }


    public ClientConfig toJerseyClientConfig()
    {
        ClientConfig config = new ClientConfig();
        if(!Objects.isNull(this.providerClasses))
        {
            Objects.requireNonNull(config);
            this.providerClasses.forEach(config::register);
        }
        if(!Objects.isNull(this.features))
        {
            Objects.requireNonNull(config);
            this.features.forEach(config::register);
        }
        Objects.requireNonNull(config);
        this.properties.forEach(config::property);
        if(!Objects.isNull(this.securityConfig))
        {
            config.register(this.securityConfig);
        }
        if(!Objects.isNull(this.securityCredentialsInfo))
        {
            config.register(this.securityCredentialsInfo);
        }
        if(!Objects.isNull(this.contentType))
        {
            config.register(this.contentType);
        }
        return config;
    }


    public String getContentType()
    {
        return this.contentType;
    }


    public ClientConfiguration setContentType(String type)
    {
        if(!StringUtils.isEmpty(type))
        {
            this.contentType = type;
        }
        return this;
    }


    public Map<String, Object> getProperties()
    {
        return this.properties;
    }


    public void addProperty(String propertyName, Object propertyValue)
    {
        if(!StringUtils.isEmpty(propertyName))
        {
            this.properties.put(propertyName, propertyValue);
        }
    }


    public Object getProperty(String propertyName)
    {
        return this.properties.get(propertyName);
    }


    public void setSecurityCredentialsInfo(SecurityCredentialsInfo securityCredentialsInfo)
    {
        this.securityCredentialsInfo = securityCredentialsInfo;
    }


    public Optional<HttpAuthenticationFeature> getAuthFilter()
    {
        if(this.securityCredentialsInfo != null && this.securityCredentialsInfo.isValid())
        {
            return Optional.of(this.securityConfig);
        }
        return Optional.empty();
    }


    public void setConnectTimeoutInSeconds(int timeout)
    {
        setConnectTimeoutInMillisec(timeout * 1000);
    }


    public void setConnectTimeoutInMillisec(int timeout)
    {
        if(timeout >= 0)
        {
            addProperty("jersey.config.client.connectTimeout", Integer.valueOf(timeout));
            LOGGER.info("Connection timeout set to {} milliseconds", Integer.valueOf(timeout));
        }
        else
        {
            LOGGER.warn("Negative timeout value {} is ignored. Use 0 or a positive integer for timeout value", Integer.valueOf(timeout));
        }
    }


    public void setReadTimeoutInSeconds(int timeout)
    {
        setReadTimeoutInMillisec(timeout * 1000);
    }


    public void setReadTimeoutInMillisec(int timeout)
    {
        if(timeout >= 0)
        {
            addProperty("jersey.config.client.readTimeout", Integer.valueOf(timeout));
            LOGGER.info("Read timeout set to {} milliseconds", Integer.valueOf(timeout));
        }
        else
        {
            LOGGER.warn("Negative timeout value {} is ignored. Use 0 or a positive integer for timeout value.", Integer.valueOf(timeout));
        }
    }
}
