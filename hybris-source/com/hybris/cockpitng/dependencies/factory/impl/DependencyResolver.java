/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dependencies.factory.impl;

/**
 * Resolves dependencies
 *
 * @param <T>
 */
public interface DependencyResolver<T>
{
    /** Resolves and injects dependencies for given object */
    void injectDependencies(T obj);
}
