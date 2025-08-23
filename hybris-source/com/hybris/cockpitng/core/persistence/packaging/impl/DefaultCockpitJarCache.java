/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.persistence.packaging.impl;

import com.hybris.cockpitng.core.persistence.packaging.CockpitJarCache;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class DefaultCockpitJarCache implements CockpitJarCache
{
    /**
     * Cache of parsed jar files identified by path
     */
    private final Map<File, JarFile> jars = Collections.synchronizedMap(new HashMap<>());
    /**
     * Cache of jar entries. Each element is identified by pair &lt;root directory for jars, resource name&gt; and contains
     * value as &lt;jar file for resource, jar entry for resource&gt;
     */
    private final Map<Object, Pair<JarFile, JarEntry>> entries = Collections.synchronizedMap(new HashMap<>());


    @Override
    public void addJarFile(final File filePath, final JarFile jarFile)
    {
        jars.put(filePath, jarFile);
    }


    @Override
    public JarFile removeJarFile(final File filePath)
    {
        return jars.remove(filePath);
    }


    @Override
    public boolean containsJarFile(final File filePath)
    {
        return jars.containsKey(filePath);
    }


    @Override
    public JarFile getJarFile(final File filePath)
    {
        return jars.get(filePath);
    }


    protected Object createEntryKey(final File rootPath, final String resourceName)
    {
        return new ImmutablePair<>(rootPath, resourceName);
    }


    @Override
    public void addEntry(final File rootPath, final JarFile jarFile, final JarEntry entry)
    {
        entries.put(createEntryKey(rootPath, entry.getName()), new ImmutablePair<>(jarFile, entry));
    }


    @Override
    public JarEntry removeEntry(final File rootPath, final String resourceName)
    {
        final Pair<JarFile, JarEntry> result = entries.remove(createEntryKey(rootPath, resourceName));
        return result != null ? result.getValue() : null;
    }


    @Override
    public boolean containsEntry(final File rootPath, final String resourceName)
    {
        return entries.containsKey(createEntryKey(rootPath, resourceName));
    }


    @Override
    public JarEntry getEntry(final File rootPath, final String resourceName)
    {
        final Pair<JarFile, JarEntry> result = entries.get(createEntryKey(rootPath, resourceName));
        return result != null ? result.getValue() : null;
    }


    @Override
    public JarFile getEntryJarFile(final File rootPath, final String resourceName)
    {
        final Pair<JarFile, JarEntry> result = entries.get(createEntryKey(rootPath, resourceName));
        return result != null ? result.getKey() : null;
    }


    @Override
    public void close() throws IOException
    {
        for(final JarFile file : jars.values())
        {
            file.close();
        }
        jars.clear();
        entries.clear();
    }
}
