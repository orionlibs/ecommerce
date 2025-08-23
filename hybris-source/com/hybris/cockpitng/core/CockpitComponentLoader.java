/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core;

import java.util.Set;

/**
 * Implementations of this interface are responsible for loading cockpit components from all kind of stores - could be a
 * directory in a file system, a jar file, a database etc.
 *
 */
public interface CockpitComponentLoader
{
    /**
     * Loads all cockpit components this loader is managing.
     *
     * @return list of cockpit components this loader is managing
     */
    Set<CockpitComponentInfo> load();


    /**
     * @param classLoader
     * @return
     */
    Set<CockpitComponentInfo> load(ClassLoader classLoader);
}
