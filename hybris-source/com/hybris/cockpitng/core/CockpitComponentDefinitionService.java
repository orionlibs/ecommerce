/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core;

import com.hybris.cockpitng.core.spring.CockpitApplicationContext;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.context.ApplicationContext;

/**
 * This service is responsible for loading widget definitions and providing them in form of {@link WidgetDefinition}
 * objects.
 */
public interface CockpitComponentDefinitionService
{
    /**
     * @deprecated since 1808, use {@link CockpitApplicationContext#getClassLoader()} instead
     * @see CockpitApplicationContext#getCockpitApplicationContext(ApplicationContext)
     */
    @Deprecated(since = "1808", forRemoval = true)
    ClassLoader getClassLoader(AbstractCockpitComponentDefinition definition);


    /**
     * @deprecated since 1808, use {@link org.springframework.context.ApplicationContextAware} instead
     * @see CockpitApplicationContext#getCockpitApplicationContext(ApplicationContext)
     */
    @Deprecated(since = "1808", forRemoval = true)
    ApplicationContext getApplicationContext(AbstractCockpitComponentDefinition definition);


    /**
     * Provides all component definitions available in the system.
     *
     * @return all component definitions available in the system
     */
    List<AbstractCockpitComponentDefinition> getAllComponentDefinitions();


    /**
     * Provides all component definitions of given type.
     *
     * @param componentType
     *           class of component
     * @return all component definitions available in the system
     */
    <T extends AbstractCockpitComponentDefinition> List<T> getComponentDefinitionsByClass(final Class<T> componentType);


    /**
     * Looks for a component definition with the given Editor code (example: 'com.hybris.cockpitng.editor.defaulttext')
     *
     * @param code
     *           the code of the component definition to be found
     * @return the component definition with the given code
     * @throws IllegalArgumentException
     *            if component definition with the given code does not exist
     */
    AbstractCockpitComponentDefinition getComponentDefinitionForCode(String code);


    /**
     * Looks for a component definition with the given code.
     *
     * @param code
     *           the code of the component definition to be found
     * @param componentType
     *           the expected class of the definition
     * @return the component definition with the given code
     * @throws IllegalArgumentException
     *            if component definition with the given code does not exist or is not instance of expected componentType
     */
    <T extends AbstractCockpitComponentDefinition> T getComponentDefinitionForCode(String code, Class<T> componentType);


    /**
     * Clears components definitions.
     */
    default void clearDefinitions()
    {
    }


    /**
     * Get components definitions for extensions.
     */
    default Collection<AbstractCockpitComponentDefinition> getExtensionsDefinitions()
    {
        return Collections.EMPTY_LIST;
    }


    /**
     * Reloads all component definitions.
     */
    void reloadDefinitions();


    /**
     * Returns all stub widget definitions that were registered for 'non-widget' aware components
     *
     * @return all stub widget definitions
     */
    Map<String, WidgetDefinition> getStubWidgetDefinitions();


    /**
     * Creates autowired component
     *
     * @param definition
     *           component definition
     * @param className
     *           class of the component to be created
     * @param context
     *           Spring application context from which the dependencies should be wired; if null the dependencies are not
     *           wired only the instance is created
     * @param <T>
     *           type of created component
     * @return component with wired dependencies
     * @throws ReflectiveOperationException
     *            when it is impossible to create instance of class of given name
     */
    <T> T createAutowiredComponent(AbstractCockpitComponentDefinition definition, String className, ApplicationContext context)
                    throws ReflectiveOperationException;


    /**
     * Creates autowired component
     *
     * @param definition
     *           component definition
     * @param className
     *           class of the component to be created
     * @param context
     *           Spring application context from which the dependencies should be wired; if null the dependencies are not
     *           wired only the instance is created
     * @param cached
     *           if true the created object will be cached; for stateful objects the parameter should be false
     * @param <T>
     *           type of created component
     * @return component with wired dependencies
     * @throws ReflectiveOperationException
     *            when it is impossible to create instance of class of given name
     */
    <T> T createAutowiredComponent(AbstractCockpitComponentDefinition definition, String className, ApplicationContext context,
                    boolean cached) throws ReflectiveOperationException;
}
