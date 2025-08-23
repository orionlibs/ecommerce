/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Strategy for persisting cockpit configuration.
 * Used by {@link com.hybris.cockpitng.core.config.CockpitConfigurationService}.
 */
public interface CockpitConfigurationPersistenceStrategy
{
    /**
     * Returns the timestamp of the last modification of the configuration.
     * Used for caching. Returns 0 if not available.
     *
     * @return timestamp of the last modification of the configuration. Used for caching. Returns 0 if not available.
     */
    long getLastModification();


    /**
     * Returns the cockpit configuration XML document as an input stream.
     * @return cockpit configuration XML document as an input stream
     */
    InputStream getConfigurationInputStream() throws IOException;


    /**
     * Returns the output stream to write the cockpit configuration XML document to.
     * @return output stream to write the cockpit configuration XML document to
     */
    OutputStream getConfigurationOutputStream() throws IOException;


    /**
     * Returns the DEFAULT cockpit configuration XML document as an input stream.
     * @return DEFAULT cockpit configuration XML document as an input stream
     */
    InputStream getDefaultConfigurationInputStream() throws IOException;
}
