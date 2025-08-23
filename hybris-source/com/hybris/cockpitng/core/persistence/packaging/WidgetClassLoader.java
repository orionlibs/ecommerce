/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.persistence.packaging;

import com.hybris.cockpitng.core.persistence.packaging.impl.DefaultCockpitJarCache;
import com.hybris.cockpitng.core.persistence.packaging.impl.DefaultCockpitResourceCache;
import com.hybris.cockpitng.core.spring.CockpitApplicationContext;
import com.hybris.cockpitng.util.zip.SafeZipEntry;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class loader for classes/resources located in a cockpit widget archive. Includes a cache for resources which can be
 * enabled by passing the corresponding argument value in the constructor.
 */
public class WidgetClassLoader extends CockpitClassLoader implements Closeable
{
    public static final String JAR_EXTENSION = ".jar";
    public static final String CLASS_EXTENSION = ".class";
    protected static final CockpitJarCache JAR_CACHE = new DefaultCockpitJarCache();
    private static final String REBEL_XML = "rebel.xml";
    private static final Logger LOG = LoggerFactory.getLogger(WidgetClassLoader.class);
    private static final String ERROR_MESSAGE_TEMPLATE = "error loading resource '%s' from jar '%s'";

    static
    {
        registerAsParallelCapable();
    }

    private final String jarRootDir;
    private final CockpitResourceCache cockpitResourceCache;
    private final ConcurrentHashMap<String, Enumeration<URL>> cockpitResourcesURLCache;
    private final Map<String, WeakReference<Class<?>>> loadedClassesCache = new ConcurrentHashMap<>();
    private final WidgetJarLibInfo widgetLibInfo;


    /**
     * Constructor, see {@link ClassLoader}.
     *
     * @param parent
     *           The parent class loader.
     * @param widgetLibInfo
     *           A {@link WidgetJarLibInfo} instance, containing information about the cockpit widget archive in which the
     *           widget resides, or null.
     * @param rootDir
     *           The path to the directory where the cockpit widget archive files are located.
     * @param resourceCacheEnabled
     *           Whether or not to enable the built-in resource cache.
     * @see CockpitApplicationContext#getClassLoader()
     * @see #getResourceAsStream(WidgetJarLibInfo, String)
     */
    public WidgetClassLoader(final ClassLoader parent, final WidgetJarLibInfo widgetLibInfo, final String rootDir,
                    final boolean resourceCacheEnabled)
    {
        super(parent);
        this.cockpitResourceCache = resourceCacheEnabled ? createResourceCache() : null;
        this.cockpitResourcesURLCache = resourceCacheEnabled ? new ConcurrentHashMap() : null;
        this.jarRootDir = rootDir;
        this.widgetLibInfo = widgetLibInfo;
    }


    /**
     * Constructor, see {@link ClassLoader}.
     *
     * @param parent
     *           The parent class loader.
     * @param rootDir
     *           The path to the directory where the cockpit widget archive files are located.
     * @param resourceCacheEnabled
     *           Whether or not to enable the built-in resource cache.
     */
    public WidgetClassLoader(final ClassLoader parent, final String rootDir, final boolean resourceCacheEnabled)
    {
        this(parent, null, rootDir, resourceCacheEnabled);
    }


    /**
     * @return cockpit cache implementation
     * @deprecated since 1811 <br>
     *     not used anymore
     */
    @Deprecated(since = "1811", forRemoval = true)
    protected CockpitResourceCache createResourceCache()
    {
        return new DefaultCockpitResourceCache();
    }


    public WidgetJarLibInfo getWidgetLibInfo()
    {
        return widgetLibInfo;
    }


    @Override
    public Enumeration<URL> getResources(final String resourceName) throws IOException
    {
        final Optional<Enumeration<URL>> resourcesURLsFromCache = getResourcesFromCache(resourceName);
        if(resourcesURLsFromCache.isPresent())
        {
            return resourcesURLsFromCache.get();
        }
        final List<URL> result = new ArrayList<>();
        if(this.widgetLibInfo != null)
        {
            final Optional<URL> resourceAsURLFromWidgetLib = getResourceAsURLFromWidgetLib(resourceName);
            resourceAsURLFromWidgetLib.ifPresent(result::add);
        }
        if(StringUtils.isNotEmpty(this.jarRootDir))
        {
            result.addAll(findResourceURLsInDir(resourceName, new File(this.jarRootDir)));
        }
        final Enumeration<URL> resourcesAsURLs = getParent().getResources(resourceName);
        if(resourcesAsURLs != null)
        {
            result.addAll(Collections.list(resourcesAsURLs));
        }
        final List<URL> unixConvertedResult = convertCRLFToCR(result);
        final Enumeration<URL> convertedResult = Collections.enumeration(unixConvertedResult);
        cacheResourcesURLs(resourceName, convertedResult);
        return convertedResult;
    }


    private List<URL> convertCRLFToCR(final Collection<URL> urls)
    {
        return urls.stream()
                        .map(URL::toString)
                        .map(urlText -> urlText.replaceAll("\\\\", "/"))
                        .map(urlText -> {
                            try
                            {
                                return new URL(urlText);
                            }
                            catch(MalformedURLException e)
                            {
                                LOG.warn("URL creation failed", e);
                                return null;
                            }
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
    }


    void cacheResourcesURLs(final String resourceName, final Enumeration<URL> resourcesAsURLs)
    {
        if(resourcesAsURLs != null && this.cockpitResourcesURLCache != null)
        {
            this.cockpitResourcesURLCache.put(resourceName, resourcesAsURLs);
        }
    }


    @Override
    public InputStream getResourceAsStream(final String resourceName)
    {
        final Optional<InputStream> resourceAsStreamFromCache = getResourceAsStreamFromCache(resourceName);
        if(resourceAsStreamFromCache.isPresent())
        {
            return resourceAsStreamFromCache.get();
        }
        if(this.widgetLibInfo != null)
        {
            final Optional<InputStream> resourceAsStreamFromWidgetLib = getResourceAsStreamFromWidgetLib(resourceName);
            if(resourceAsStreamFromWidgetLib.isPresent())
            {
                return resourceAsStreamFromWidgetLib.get();
            }
        }
        else if(StringUtils.isNotEmpty(this.jarRootDir))
        {
            final InputStream resourceAsStream = findResourceInDir(resourceName, new File(this.jarRootDir));
            if(resourceAsStream != null)
            {
                return loadAndCacheStream(resourceName, resourceAsStream);
            }
        }
        final InputStream resourceAsStream = getParent().getResourceAsStream(resourceName);
        if(resourceAsStream != null)
        {
            return loadAndCacheStream(resourceName, resourceAsStream);
        }
        return null;
    }


    @Override
    public InputStream getResourceAsStream(final WidgetJarLibInfo componentInfo, final String resourceName)
    {
        final String resourcePrefix = (componentInfo.getPrefix() == null) ? "" : (componentInfo.getPrefix() + "/");
        if(componentInfo.getJarPath() != null)
        {
            final File jarRoot = StringUtils.isNotEmpty(jarRootDir) ? new File(jarRootDir)
                            : componentInfo.getJarPath().getParentFile();
            try(final JarFile jarFile = new JarFile(componentInfo.getJarPath()))
            {
                return Optional.ofNullable(findAndCache(jarRoot, jarFile, resourcePrefix + resourceName))
                                .map(entry -> getResourceAsStream(jarFile, entry)).map(stream -> loadAndCacheStream(resourceName, stream))
                                .orElse(null);
            }
            catch(final IOException ex)
            {
                LOG.error(String.format(ERROR_MESSAGE_TEMPLATE, resourceName, componentInfo.getJarPath()), ex);
            }
            return null;
        }
        else
        {
            return super.getResourceAsStream(resourcePrefix + resourceName);
        }
    }


    private Optional<Enumeration<URL>> getResourcesFromCache(final String resourceName)
    {
        if(this.cockpitResourcesURLCache != null)
        {
            if(this.cockpitResourcesURLCache.contains(resourceName))
            {
                return Optional.of(this.cockpitResourcesURLCache.get(resourceName));
            }
        }
        else if(LOG.isDebugEnabled())
        {
            LOG.debug("Cache disabled, try to load resources URLs for name: {}", resourceName);
        }
        return Optional.empty();
    }


    private Optional<InputStream> getResourceAsStreamFromCache(final String resourceName)
    {
        if(this.cockpitResourceCache != null)
        {
            final byte[] cachedResource = this.cockpitResourceCache.getResourceAsBytes(resourceName);
            if(cachedResource != null && cachedResource.length != 0)
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Found resource in cache: {}", resourceName);
                }
                return Optional.of(new ByteArrayInputStream(cachedResource));
            }
        }
        else if(LOG.isDebugEnabled())
        {
            LOG.debug("Cache disabled, try to load resource: {}", resourceName);
        }
        return Optional.empty();
    }


    private Optional<URL> getResourceAsURLFromWidgetLib(final String resourceName)
    {
        if(this.widgetLibInfo == null)
        {
            return Optional.empty();
        }
        final String resourcePrefix = (this.widgetLibInfo.getPrefix() == null) ? "" : (this.widgetLibInfo.getPrefix() + "/");
        final File jarRoot = StringUtils.isNotEmpty(jarRootDir) ? new File(jarRootDir)
                        : this.widgetLibInfo.getJarPath().getParentFile();
        try
        {
            final JarFile jarFile = parseAndCache(getWidgetLibInfo().getJarPath());
            final JarEntry entry = findAndCache(jarRoot, jarFile, resourcePrefix + resourceName);
            if(entry != null)
            {
                final URL resourceURL = getResourceURL(jarFile, entry);
                return Optional.ofNullable(resourceURL);
            }
        }
        catch(final IOException ex)
        {
            LOG.error(String.format(ERROR_MESSAGE_TEMPLATE, resourceName, this.widgetLibInfo.getJarPath()), ex);
        }
        return Optional.empty();
    }


    private Optional<InputStream> getResourceAsStreamFromWidgetLib(final String resourceName)
    {
        if(this.widgetLibInfo == null)
        {
            return Optional.empty();
        }
        final String resourcePrefix = (this.widgetLibInfo.getPrefix() == null) ? "" : (this.widgetLibInfo.getPrefix() + "/");
        final File jarRoot = StringUtils.isNotEmpty(jarRootDir) ? new File(jarRootDir)
                        : this.widgetLibInfo.getJarPath().getParentFile();
        try
        {
            final JarFile jarFile = parseAndCache(getWidgetLibInfo().getJarPath());
            final JarEntry entry = findAndCache(jarRoot, jarFile, resourcePrefix + resourceName);
            if(entry != null)
            {
                return Optional.ofNullable(getResourceAsStream(jarFile, entry))
                                .map(stream -> loadAndCacheStream(resourceName, stream));
            }
        }
        catch(final IOException ex)
        {
            LOG.error(String.format(ERROR_MESSAGE_TEMPLATE, resourceName, this.widgetLibInfo.getJarPath()), ex);
        }
        return Optional.empty();
    }


    @Override
    protected URL findResource(final String resourceName)
    {
        // 1) try jar specific jar file
        if(this.widgetLibInfo != null)
        {
            final String resourcePrefix = (this.widgetLibInfo.getPrefix() == null) ? "" : (this.widgetLibInfo.getPrefix() + "/");
            final File jarRoot = StringUtils.isNotEmpty(jarRootDir) ? new File(jarRootDir)
                            : this.widgetLibInfo.getJarPath().getParentFile();
            try
            {
                return Optional.ofNullable(parseAndCache(getWidgetLibInfo().getJarPath()))
                                .map(jarFile -> Pair.of(jarFile, findAndCache(jarRoot, jarFile, resourcePrefix + resourceName)))
                                .map(jarFileWithEntry -> getResourceURL(jarFileWithEntry.getLeft(), jarFileWithEntry.getRight())).orElse(null);
            }
            catch(final IOException ex)
            {
                LOG.error(String.format(ERROR_MESSAGE_TEMPLATE, resourceName, this.widgetLibInfo.getJarPath()), ex);
            }
        }
        // 2) try in any jar in the jar root dir
        else if(StringUtils.isNotEmpty(this.jarRootDir))
        {
            final URL url = findResourceURLInDir(resourceName, new File(this.jarRootDir));
            if(url != null)
            {
                return url;
            }
        }
        return null;
    }


    private InputStream loadAndCacheStream(final String uid, final InputStream inStream)
    {
        if(this.cockpitResourceCache == null)
        {
            return inStream;
        }
        else
        {
            try
            {
                final byte[] byteArray = IOUtils.toByteArray(inStream);
                this.cockpitResourceCache.addResourceToCache(uid, byteArray);
                return new ByteArrayInputStream(byteArray);
            }
            catch(final IOException e)
            {
                LOG.error("Could not add resource to cache, reason: ", e);
                return null;
            }
            finally
            {
                IOUtils.closeQuietly(inStream);
            }
        }
    }


    private static InputStream getResourceAsStream(final JarFile jarFile, final JarEntry entry)
    {
        try
        {
            return jarFile.getInputStream(entry);
        }
        catch(final IOException ex)
        {
            LOG.error(String.format(ERROR_MESSAGE_TEMPLATE, entry.getName(), jarFile.getName()), ex);
            return null;
        }
    }


    private static JarFile parseAndCache(final File jarFile) throws IOException
    {
        if(JAR_CACHE.containsJarFile(jarFile))
        {
            return JAR_CACHE.getJarFile(jarFile);
        }
        else
        {
            final JarFile result = new JarFile(jarFile);
            JAR_CACHE.addJarFile(jarFile, result);
            return result;
        }
    }


    private static JarEntry findAndCache(final File rootDir, final JarFile jarFile, final String resourceName)
    {
        final JarEntry entry = jarFile.getJarEntry(resourceName);
        if(entry != null)
        {
            JAR_CACHE.addEntry(rootDir, jarFile, entry);
        }
        return entry;
    }


    private static Collection<URL> findResourceURLsInDir(final String resourceName, final File rootDir)
    {
        final Collection<URL> result = new ArrayList<>();
        final List<File> jarFiles = getModuleJars(rootDir);
        for(final File file : jarFiles)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Try to find resource '{}' in '{}' [URL in dir].", resourceName, file);
            }
            try
            {
                final JarFile jarFile = parseAndCache(file);
                final JarEntry jarEntry = findAndCache(rootDir, jarFile, resourceName);
                if(jarEntry != null)
                {
                    final URL jarURL = getResourceURL(jarFile, jarEntry);
                    result.add(jarURL);
                }
            }
            catch(final IOException e)
            {
                LOG.error(String.format(ERROR_MESSAGE_TEMPLATE, resourceName, file.getName()), e);
            }
        }
        return result;
    }


    private static InputStream findResourceInDir(final String resourceName, final File rootDir)
    {
        JarEntry jarEntry = null;
        JarFile jarFile = null;
        if(JAR_CACHE.containsEntry(rootDir, resourceName))
        {
            jarEntry = JAR_CACHE.getEntry(rootDir, resourceName);
            jarFile = JAR_CACHE.getEntryJarFile(rootDir, resourceName);
        }
        else
        {
            final List<File> jarFiles = getModuleJars(rootDir);
            for(final Iterator<File> it = jarFiles.iterator(); it.hasNext() && jarEntry == null; )
            {
                final File file = it.next();
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Try to find resource '{}' in '{}' [resource in dir].", resourceName, file);
                }
                try
                {
                    jarFile = parseAndCache(file);
                    jarEntry = findAndCache(rootDir, jarFile, resourceName);
                }
                catch(final IOException e)
                {
                    LOG.error(String.format(ERROR_MESSAGE_TEMPLATE, resourceName, file.getName()), e);
                }
            }
        }
        if(jarEntry != null && jarFile != null)
        {
            return getResourceAsStream(jarFile, jarEntry);
        }
        return null;
    }


    private static URL findResourceURLInDir(final String resourceName, final File rootDir)
    {
        final Pair<JarFile, ZipEntry> resourceInDir = findZipEntryInRootDir(resourceName, rootDir);
        if(resourceInDir != null)
        {
            final JarFile jarFile = resourceInDir.getLeft();
            final ZipEntry entry = resourceInDir.getRight();
            return getResourceURL(jarFile, entry);
        }
        return null;
    }


    private static URL getResourceURL(final JarFile jarFile, final ZipEntry entry)
    {
        try
        {
            return new URL("jar:file:" + jarFile.getName() + "!/" + entry.getName());
        }
        catch(final IOException e)
        {
            LOG.error("error getting resource URL for '" + entry.getName() + "' from jar '" + jarFile.getName() + "'", e);
        }
        return null;
    }


    @Override
    protected Enumeration<URL> findResources(final String name) throws IOException
    {
        if(StringUtils.equals(name, REBEL_XML))
        {
            return Collections.enumeration(Collections.singleton(this.getResource(REBEL_XML)));
        }
        else
        {
            return super.findResources(name);
        }
    }


    @Override
    protected Class<?> loadClass(final String name, final boolean resolve) throws ClassNotFoundException
    {
        if(StringUtils.isBlank(name))
        {
            throw new ClassNotFoundException("Empty class name");
        }
        Class<?> c = getClassFromCache(name);
        if(c != null)
        {
            return c;
        }
        synchronized(getClassLoadingLock(name))
        {
            c = getClassFromCache(name);
            if(c != null)
            {
                return c;
            }
            try
            {
                c = findClass(name);
            }
            catch(final ClassNotFoundException e)
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Class not found", e);
                }
                if(getParent() != null)
                {
                    c = getParent().loadClass(name);
                }
                else
                {
                    c = super.loadClass(name, resolve);
                }
            }
            if(c == null)
            {
                throw new ClassNotFoundException(name);
            }
            if(resolve)
            {
                resolveClass(c);
            }
            putClassInCache(name, c);
            return c;
        }
    }


    protected Class<?> getClassFromCache(final String name)
    {
        Class<?> c = null;
        final WeakReference<Class<?>> classWeakReference = loadedClassesCache.get(name);
        if(classWeakReference != null)
        {
            c = classWeakReference.get();
        }
        return c;
    }


    protected void putClassInCache(final String name, final Class<?> c)
    {
        loadedClassesCache.put(name, new WeakReference<>(c));
    }


    @Override
    protected Class<?> findClass(final String name) throws ClassNotFoundException
    {
        Class<?> clazz = null;
        if(StringUtils.isNotEmpty(this.jarRootDir))
        {
            clazz = findClassInDir(name, new File(this.jarRootDir));
        }
        if(clazz != null)
        {
            return clazz;
        }
        throw new ClassNotFoundException(name);
    }


    protected Class<?> findClassInDir(final String className, final File rootDir) throws ClassNotFoundException
    {
        final String resolvedName = StringUtils.replace(className, ".", "/") + CLASS_EXTENSION;
        final Pair<JarFile, ZipEntry> resourceInDir = findZipEntryInRootDir(resolvedName, rootDir);
        if(resourceInDir == null)
        {
            return null;
        }
        final JarFile jarFile = resourceInDir.getLeft();
        final ZipEntry entry = resourceInDir.getRight();
        InputStream classInputStream = null;
        try
        {
            classInputStream = jarFile.getInputStream(entry);
            return getClassFromInputStream(classInputStream, className);
        }
        catch(final IOException e)
        {
            LOG.error(String.format(ERROR_MESSAGE_TEMPLATE, className, jarFile.getName()), e);
        }
        finally
        {
            IOUtils.closeQuietly(classInputStream);
        }
        return null;
    }


    private static Pair<JarFile, ZipEntry> findZipEntryInRootDir(final String resourceName, final File rootDir)
    {
        for(final File file : getModuleJars(rootDir))
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Try to find resource '{}' in '{}'.", resourceName, file);
            }
            try
            {
                final JarFile jarFile = parseAndCache(file);
                final ZipEntry jarEntry = jarFile.getJarEntry(resourceName);
                if(jarEntry != null)
                {
                    return new ImmutablePair<>(jarFile, new SafeZipEntry(jarEntry));
                }
            }
            catch(final IOException e)
            {
                LOG.error(String.format(ERROR_MESSAGE_TEMPLATE, resourceName, file.getName()), e);
            }
        }
        return null;
    }


    protected Class<?> getClassFromInputStream(final InputStream inputStream, final String className)
                    throws IOException, ClassNotFoundException
    {
        final int index = className.lastIndexOf('.');
        if(index != -1)
        {
            final String packageName = className.substring(0, index);
            if(getPackage(packageName) == null)
            {
                try
                {
                    definePackage(packageName, null, null, null, null, null, null, null);
                }
                catch(final IllegalArgumentException e)
                {
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug("Error defining package " + packageName + ", maybe it already exists.", e);
                    }
                }
            }
        }
        try
        {
            final byte[] fileContent = IOUtils.toByteArray(inputStream);
            final ByteBuffer byteBuffer = ByteBuffer.wrap(fileContent);
            return super.defineClass(className, byteBuffer, null);
        }
        catch(final ClassFormatError | NoClassDefFoundError e)
        {
            throw new ClassNotFoundException(e.getMessage(), e);
        }
    }


    private static List<File> getModuleJars(final File rootDir)
    {
        final List<File> jarFiles = new ArrayList<>();
        final File[] listFiles = rootDir.listFiles((dir, name) -> name.endsWith(JAR_EXTENSION));
        if(listFiles != null)
        {
            jarFiles.addAll(Arrays.asList(listFiles));
        }
        return jarFiles;
    }


    @Override
    public void close() throws IOException
    {
        if(cockpitResourceCache != null)
        {
            cockpitResourceCache.reset();
        }
        loadedClassesCache.clear();
        JAR_CACHE.close();
    }


    @Override
    public void reset()
    {
        try
        {
            if(cockpitResourceCache != null)
            {
                cockpitResourceCache.reset();
            }
            JAR_CACHE.close();
        }
        catch(final IOException e)
        {
            LOG.error(e.getLocalizedMessage(), e);
        }
    }
}
