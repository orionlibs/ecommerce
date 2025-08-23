/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.web.js.impl;

import java.io.File;
import java.util.Set;

/**
 * Chained dependencies that are resolved on basis of regular expression. This dependencies check whether a name of
 * dependency matches configured regular expression. If so, then configured dependencies urls are returned.
 */
public class NameChainedDependencies extends AbstractRegexpChainedDependencies
{
    @Override
    protected String getDiscriminator(final File moduleJar, final ResolvedDependency dependency, final Set<String> dependingWidgets)
    {
        return dependency.getDefinition().getName();
    }
}
