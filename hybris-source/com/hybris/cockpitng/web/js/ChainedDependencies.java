/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.web.js;

import com.hybris.cockpitng.web.js.impl.ResolvedDependency;
import java.io.File;
import java.util.Set;

/**
 * Defines some chained dependencies.
 * <P>
 * Sometimes one dependency causes others. This interface shows relations between dependencies.
 *
 */
public interface ChainedDependencies
{
    /**
     * Gets all dependencies that should be automatically caused by provided dependency.
     *
     * @param moduleJar jar file with module that defines the dependency
     * @param dependency dependency defined by module
     * @param dependingWidgets identities of dependent widgets
     * @return chained dependencies
     */
    Set<ResolvedDependency> getChainedDependencies(final File moduleJar, ResolvedDependency dependency, Set<String> dependingWidgets);
}
