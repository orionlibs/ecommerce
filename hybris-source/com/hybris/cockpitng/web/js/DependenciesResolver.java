/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.web.js;

import com.hybris.cockpitng.web.js.impl.ResolvedDependency;

/**
 * An interface for classes that are able to resolve a single dependency. Resolver may be able to assign version of
 * library on basis of it's url, resolve libraries url on basis of its name and version, etc.
 */
public interface DependenciesResolver
{
    ResolvedDependency resolveDependency(ResolvedDependency dependency);
}
