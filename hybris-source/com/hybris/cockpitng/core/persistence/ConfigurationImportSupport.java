/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.persistence;

import java.io.IOException;

/**
 * An interface for tooling classes that helps resolving {@link com.hybris.cockpitng.core.persistence.impl.jaxb.Import}
 * tags in configuration files.
 */
public interface ConfigurationImportSupport
{
    /**
     * Resolves all imports defined in specified root configuration. All nested imports are also resolved.
     *
     * @param root
     *           root configuration, which imports are to be resolved
     * @param interpreter
     *           an object capable of extracting information from configuration and modifying it
     * @param <C>
     *           type of configuration root
     * @return root configuration merged with all imported onces
     */
    <C> C resolveImports(final C root, final ConfigurationInterpreter<C> interpreter) throws IOException;
}
