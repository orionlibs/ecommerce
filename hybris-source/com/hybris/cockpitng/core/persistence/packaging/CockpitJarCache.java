/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.persistence.packaging;

import java.io.Closeable;
import java.io.File;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Cache of loaded jar files and entries found in them
 */
public interface CockpitJarCache extends Closeable
{
    /**
     * Adds a loaded jar file into cache
     *
     * @param filePath
     *           full path to jar file
     * @param jarFile
     *           loaded jar file
     */
    void addJarFile(final File filePath, final JarFile jarFile);


    /**
     * Removes a loaded jar file from cache
     *
     * @param filePath
     *           full path to jar file
     * @return jar file removed ot <code>null</code> if there was nothing cached for provided path
     */
    JarFile removeJarFile(final File filePath);


    /**
     * Checks whether a jar file is cached
     *
     * @param filePath
     *           full path to jar file
     * @return <code>true</code> if jar file is cached
     */
    boolean containsJarFile(final File filePath);


    /**
     * Retrieves a jar file from cache
     *
     * @param filePath
     *           full path to jar file
     * @return jar file or <code>null</code> if jar file is not cached
     */
    JarFile getJarFile(final File filePath);


    /**
     * Adds new result of searching for entry into cache
     *
     * @param rootPath
     *           root path, in which an entry was found
     * @param jarFile
     *           jar file in which an entry was found
     * @param entry
     *           entry found
     */
    void addEntry(final File rootPath, final JarFile jarFile, final JarEntry entry);


    /**
     * Removes previously cached result of searching for entry
     *
     * @param rootPath
     *           root path, in which an entry was found
     * @param resourceName
     *           name of resource searched
     * @return jar entry removed ot <code>null</code> if there was nothing cached for provided conditions
     */
    JarEntry removeEntry(final File rootPath, final String resourceName);


    /**
     * Checks whether result of searching for entry is cached
     *
     * @param rootPath
     *           root path, in should have been found
     * @param resourceName
     *           name of resource searched
     */
    boolean containsEntry(final File rootPath, final String resourceName);


    /**
     * Retrieves previously cached result of searching for entry
     *
     * @param rootPath
     *           root path, in should have been found
     * @param resourceName
     *           name of resource searched
     * @return jar entry or <code>null</code> if jar entry is not cached
     */
    JarEntry getEntry(final File rootPath, final String resourceName);


    /**
     * Retrieves jar file of previously cached result of searching for entry
     *
     * @param rootPath
     *           root path, in should have been found
     * @param resourceName
     *           name of resource searched
     * @return jar entry or <code>null</code> if jar entry is not cached
     */
    JarFile getEntryJarFile(final File rootPath, final String resourceName);
}
