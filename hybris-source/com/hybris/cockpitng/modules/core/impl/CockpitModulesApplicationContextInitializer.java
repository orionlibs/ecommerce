/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.modules.core.impl;

import javax.servlet.ServletContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * Initializes cockpit modules context.
 */
public interface CockpitModulesApplicationContextInitializer extends ApplicationListener<ContextRefreshedEvent>
{
    /**
     * @deprecated since 1808, use {@link ApplicationListener#onApplicationEvent(ApplicationEvent)} instead
     */
    @Deprecated(since = "1808", forRemoval = true)
    void initializeCockpitModulesApplicationContext(final ServletContext servletContext);


    /**
     * @return <code>true</code> if application has bean successfully initialized
     */
    boolean isInitialized();
}
