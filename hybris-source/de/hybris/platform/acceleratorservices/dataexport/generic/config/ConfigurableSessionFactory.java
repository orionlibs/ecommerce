/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.dataexport.generic.config;

import org.springframework.integration.file.remote.session.Session;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.messaging.Message;

/**
 * Service that session configuration from messages.
 */
public interface ConfigurableSessionFactory<F> extends SessionFactory<F>
{
    /**
     * Get a session using the message.
     *
     * @param message
     *           spring message
     * @return session connection
     */
    Session<F> getSession(Message<?> message);
}
