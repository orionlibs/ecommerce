/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config;

import com.hybris.cockpitng.core.config.impl.jaxb.Config;
import com.hybris.cockpitng.core.ui.WidgetInstance;
import com.hybris.cockpitng.core.util.jaxb.SchemaValidationStatus;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * This service provides functionality to load and store cockpit related configuration. The configuration is queried by
 * a context object which is a String-&gt;String map. Each entry represents single value in a single dimension. For example
 * context like this: <code>
 * {
 * 	"component"="editorArea",
 * 	"type"="Product",
 * 	"principal"="productmanager"
 * }
 * </code> would find configuration for type "Product", component "editorArea" and principal "productmanager".
 * Dimensions and their values are not limited, you can use any amount of any dimensions to store and load your
 * configuration.
 */
public interface CockpitConfigurationService
{
    /**
     * Loads a single piece of cockpit configuration according to context provided.
     *
     * @param context
     *           the context to be used to look for the configuration
     * @param configurationType
     *           desired configuration type
     * @return single piece of cockpit configuration according to context provided
     * @throws CockpitConfigurationException
     *            if configuration could not be found or some error occured during loading
     */
    <C> C loadConfiguration(ConfigContext context, Class<C> configurationType) throws CockpitConfigurationException;


    /**
     * Loads a single piece of cockpit configuration according to context provided for specific widgetInstance
     *
     * @param context
     *           the context to be used to look for the configuration
     * @param configType
     *           desired configuration type
     * @param widgetInstance
     *           given widget instance
     * @return single piece of cockpit configuration according to context provided
     * @throws CockpitConfigurationException
     *            if configuration could not be found or some error occured during loading
     */
    default <C> C loadConfiguration(final ConfigContext context, final Class<C> configType, final WidgetInstance widgetInstance)
                    throws CockpitConfigurationException
    {
        return loadConfiguration(context, configType);
    }


    /**
     * Stores a single piece of cockpit configuration according to context provided.
     *
     * @param context
     *           the context to be used to store the configuration
     * @param configuration
     *           the configuration to be stored
     * @throws CockpitConfigurationException
     *            if configuration could not be stored for some reason
     */
    <C> void storeConfiguration(ConfigContext context, C configuration) throws CockpitConfigurationException;


    /**
     * Clears stored configuration and sets it to defaults.
     */
    default void resetToDefaults()
    {
    }


    /**
     * Responsible for processing a validation for a given content.
     *
     * @param contentStream
     *           represents a xml content that needs to be validated
     * @return {@link SchemaValidationStatus} status of the validation
     */
    default SchemaValidationStatus validate(final InputStream contentStream)
    {
        return SchemaValidationStatus.success();
    }


    /**
     * Serialize config as xml string
     *
     * @param originalConfig
     *           subject for serialization
     * @param marshaller
     *           marshaller to be used during serialization
     * @return xml representation of config
     * @throws JAXBException
     *            If any unexpected problem occurs during the marshalling.
     */
    default String getConfigAsString(final Config originalConfig, final Marshaller marshaller) throws JAXBException
    {
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        final StringWriter sw = new StringWriter();
        marshaller.marshal(originalConfig, sw);
        return sw.toString();
    }


    /**
     * Deserialize xml string as config object
     *
     * @param changes
     *           xml string
     * @param unmarshaller
     *           unmarshaller to be used during deserialization
     * @return config object
     * @throws JAXBException If any unexpected errors occur while unmarshalling.
     * @throws IllegalArgumentException If the passed string is null.
     */
    default Config getChangesAsConfig(final String changes, final Unmarshaller unmarshaller) throws JAXBException
    {
        if(changes == null)
        {
            throw new IllegalArgumentException("Changes configuration may not be null at this point");
        }
        final StringReader modifiedConfigStringReader = new StringReader(changes);
        return (Config)unmarshaller.unmarshal(modifiedConfigStringReader);
    }


    /**
     * Allows to perform arbitrary calls to storeRootConfig(Config) without writing the actual data into the
     * backing storage. Only at the end of this call the storage is written.
     *
     * @param logic
     *           the business logic potentially invoking
     */
    default void withDelayedWrite(final Runnable logic)
    {
    }
}
