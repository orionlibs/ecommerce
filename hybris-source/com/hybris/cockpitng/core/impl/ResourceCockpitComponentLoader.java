/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.impl;

import com.hybris.cockpitng.core.CockpitComponentInfo;
import com.hybris.cockpitng.core.CockpitComponentLoader;
import com.hybris.cockpitng.core.persistence.packaging.WidgetLibUtils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.jar.JarFile;
import java.util.regex.Pattern;
import org.apache.commons.collections.CollectionUtils;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.reflections.vfs.Vfs;
import org.reflections.vfs.Vfs.Dir;
import org.reflections.vfs.Vfs.UrlType;
import org.reflections.vfs.ZipDir;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

/**
 * This widget definition loader loads all definitions from a resource package.
 */
public class ResourceCockpitComponentLoader implements CockpitComponentLoader
{
    public static final String DEFINITION_XML = "definition.xml";
    public static final String JAR_SUFFIX = ".jar!";
    private static final Logger LOG = LoggerFactory.getLogger(ResourceCockpitComponentLoader.class);
    private static final UrlType VFS_URL_TYPE = new VfsUrlType();
    private static final String MESSAGE_ERROR_CAUGHT_EXCEPTION = "Caught exception: {}";
    private List<String> packages;
    private ClassLoader classLoader;
    private WidgetLibUtils widgetLibUtils;
    private String packageUrlFilter;


    @Override
    public Set<CockpitComponentInfo> load()
    {
        return load(null);
    }


    @Override
    public Set<CockpitComponentInfo> load(final ClassLoader parentClassLoader)
    {
        final Set<CockpitComponentInfo> infos = new HashSet<>();
        final Set<String> widgetInfoFiles = getWidgetInfoFiles();
        if(!CollectionUtils.isEmpty(widgetInfoFiles))
        {
            for(final String widgetFile : widgetInfoFiles)
            {
                final Properties props = new Properties();
                if(!widgetLibUtils.fillPropertiesFromXml(widgetFile, getDefintionAsStream(widgetFile), props))
                {
                    continue;
                }
                final String widgetPath = getWidgetPath(widgetFile);
                infos.add(
                                new CockpitComponentInfo(props, widgetPath, parentClassLoader == null ? getClassLoader() : parentClassLoader));
            }
        }
        return infos;
    }


    protected InputStream getDefintionAsStream(final String definitionFilename)
    {
        return getClassResourceAsStream(definitionFilename);
    }


    private static InputStream getClassResourceAsStream(final String definitionFilename)
    {
        return ResourceCockpitComponentLoader.class.getResourceAsStream(definitionFilename);
    }


    private static String getWidgetPath(final String widgetMf)
    {
        return widgetMf.substring(0, widgetMf.length() - DEFINITION_XML.length() - 1);
    }


    protected Set<String> getWidgetInfoFiles()
    {
        final Set<String> result = new HashSet<>();
        if(CollectionUtils.isEmpty(this.packages))
        {
            return result;
        }
        patchVfsDefaultUrlTypes();
        for(final String pkg : this.packages)
        {
            final Collection<URL> urls = filterUrls(ClasspathHelper.forPackage(pkg));
            final Set<String> resources = getResources(urls, pkg);
            for(String resource : resources)
            {
                if(!(resource.charAt(0) == '/'))
                {
                    resource = "/" + resource;
                }
                result.add(resource);
            }
        }
        return result;
    }


    protected Collection<URL> filterUrls(final Collection<URL> urls)
    {
        if(packageUrlFilter != null && urls != null)
        {
            final Collection<URL> ret = new ArrayList<>();
            for(final URL url : urls)
            {
                if(url.toString().matches(packageUrlFilter))
                {
                    LOG.debug("Filter out {}", url);
                }
                else
                {
                    ret.add(url);
                }
            }
            return ret;
        }
        return urls;
    }


    protected Set<String> getResources(final Collection<URL> urls, final String pkg)
    {
        final Reflections reflections = new Reflections(
                        new ConfigurationBuilder().filterInputsBy(new FilterBuilder.Include(FilterBuilder.prefix(pkg))).setUrls(urls)
                                        .setScanners(new ResourcesScanner()));
        return reflections.getResources(Pattern.compile(".*" + DEFINITION_XML));
    }


    private static void patchVfsDefaultUrlTypes()
    {
        final List<UrlType> types = Vfs.getDefaultUrlTypes();
        if(!types.contains(VFS_URL_TYPE))
        {
            Vfs.addDefaultURLTypes(VFS_URL_TYPE);
        }
    }


    public void setPackages(final List<String> packages)
    {
        this.packages = packages;
    }


    protected ClassLoader getClassLoader()
    {
        if(this.classLoader == null)
        {
            this.classLoader = Thread.currentThread().getContextClassLoader();
        }
        return this.classLoader;
    }


    protected WidgetLibUtils getWidgetLibUtils()
    {
        return widgetLibUtils;
    }


    @Required
    public void setWidgetLibUtils(final WidgetLibUtils widgetLibUtils)
    {
        this.widgetLibUtils = widgetLibUtils;
    }


    public String getPackageUrlFilter()
    {
        return packageUrlFilter;
    }


    public void setPackageUrlFilter(final String packageUrlFilter)
    {
        this.packageUrlFilter = packageUrlFilter;
    }


    private static class VfsUrlType implements UrlType
    {
        @Override
        public boolean matches(final URL url)
        {
            return "vfs".equals(url.getProtocol()) && url.toExternalForm().endsWith(".jar");
        }


        @Override
        public Dir createDir(final URL url)
        {
            try
            {
                return new ZipDir(new JarFile(getFile(url)));
            }
            catch(final IOException e)
            {
                if(LOG.isErrorEnabled())
                {
                    LOG.error(e.getMessage(), e);
                }
                throw new RuntimeException(e);
            }
        }


        // XXX this is just a patch, method copied from org.reflections.vfs.Vfs
        private static File getFile(final URL url)
        {
            java.io.File file;
            String path;
            try
            {
                path = url.toURI().getSchemeSpecificPart();
                file = new File(path);
                if(file.exists())
                {
                    return file;
                }
            }
            catch(final URISyntaxException e)
            {
                LOG.debug(MESSAGE_ERROR_CAUGHT_EXCEPTION, e);
            }
            try
            {
                path = URLDecoder.decode(url.getPath(), "UTF-8");
                if(path.contains(JAR_SUFFIX))
                {
                    path = path.substring(0, path.lastIndexOf(JAR_SUFFIX) + ".jar".length());
                }
                file = new File(path);
                if(file.exists())
                {
                    return file;
                }
            }
            catch(final UnsupportedEncodingException e)
            {
                LOG.debug(MESSAGE_ERROR_CAUGHT_EXCEPTION, e);
            }
            try
            {
                path = url.toExternalForm();
                if(path.startsWith("jar:"))
                {
                    path = path.substring("jar:".length());
                }
                if(path.startsWith("file:"))
                {
                    path = path.substring("file:".length());
                }
                if(path.contains(JAR_SUFFIX))
                {
                    path = path.substring(0, path.indexOf(JAR_SUFFIX) + ".jar".length());
                }
                file = new File(path);
                if(file.exists())
                {
                    return file;
                }
                path = path.replace("%20", " ");
                file = new File(path);
                if(file.exists())
                {
                    return file;
                }
            }
            catch(final Exception e)
            {
                LOG.debug(MESSAGE_ERROR_CAUGHT_EXCEPTION, e);
            }
            return null;
        }
    }
}
