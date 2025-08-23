package de.hybris.platform.util.logging.log4j2;

import de.hybris.platform.util.Utilities;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.Optional;
import java.util.Properties;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Order;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.properties.PropertiesConfiguration;
import org.apache.logging.log4j.core.config.properties.PropertiesConfigurationFactory;
import org.apache.logging.log4j.core.config.xml.XmlConfiguration;

@Plugin(name = "CustomConfigurationFactory", category = "ConfigurationFactory")
@Order(50)
public class HybrisLog4j2ConfigurationFactory extends ConfigurationFactory
{
    private static final String LOG4J2_CONFIG_FILENAME = "log4j2.config.xml";


    public Configuration getConfiguration(LoggerContext loggerContext, ConfigurationSource source)
    {
        return getConfiguration(loggerContext, source.toString(), null);
    }


    public Configuration getConfiguration(LoggerContext loggerContext, String name, URI configLocation)
    {
        Properties properties = Utilities.loadPlatformProperties();
        String xmlConfigFilename = properties.getProperty("log4j2.config.xml");
        Optional<Configuration> xmlConfig = loadXmlConfiguration(loggerContext, xmlConfigFilename);
        if(xmlConfig.isPresent())
        {
            return xmlConfig.get();
        }
        Optional<Configuration> propertiesConfig = loadConfigFromProperties(loggerContext, properties);
        if(propertiesConfig.isPresent())
        {
            return propertiesConfig.get();
        }
        return configureFallback();
    }


    private Optional<Configuration> loadConfigFromProperties(LoggerContext loggerContext, Properties platformProperties)
    {
        HybrisToLog4j2PropsConverter configManager = new HybrisToLog4j2PropsConverter();
        Properties log4j2Config = configManager.convertToLog4jProps(platformProperties);
        try
        {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            try
            {
                log4j2Config.store(output, (String)null);
                InputStream input = new ByteArrayInputStream(output.toByteArray());
                ConfigurationSource configurationSource = new ConfigurationSource(input);
                PropertiesConfigurationFactory factory = new PropertiesConfigurationFactory();
                Optional<PropertiesConfiguration> optional = Optional.of(factory.getConfiguration(loggerContext, configurationSource));
                output.close();
                return (Optional)optional;
            }
            catch(Throwable throwable)
            {
                try
                {
                    output.close();
                }
                catch(Throwable throwable1)
                {
                    throwable.addSuppressed(throwable1);
                }
                throw throwable;
            }
        }
        catch(IOException e)
        {
            System.out.println("Failed to create log4j2 configuration from properties");
            return Optional.empty();
        }
    }


    private Optional<Configuration> loadXmlConfiguration(LoggerContext loggerContext, String log4j2Config)
    {
        if(StringUtils.isBlank(log4j2Config))
        {
            return Optional.empty();
        }
        URL resource = getClass().getClassLoader().getResource(log4j2Config);
        try
        {
            InputStream inputStream = resource.openStream();
            try
            {
                Optional<XmlConfiguration> optional = Optional.of(new XmlConfiguration(loggerContext, new ConfigurationSource(inputStream)));
                if(inputStream != null)
                {
                    inputStream.close();
                }
                return (Optional)optional;
            }
            catch(Throwable throwable)
            {
                if(inputStream != null)
                {
                    try
                    {
                        inputStream.close();
                    }
                    catch(Throwable throwable1)
                    {
                        throwable.addSuppressed(throwable1);
                    }
                }
                throw throwable;
            }
        }
        catch(IOException e)
        {
            System.out.println("Failed to load log4j2 configuration from '" + log4j2Config + "' file");
            return Optional.empty();
        }
    }


    private Configuration configureFallback()
    {
        ConfigurationBuilder<BuiltConfiguration> builder = newConfigurationBuilder();
        return (Configuration)builder.build();
    }


    protected String[] getSupportedTypes()
    {
        return new String[] {"*"};
    }
}
