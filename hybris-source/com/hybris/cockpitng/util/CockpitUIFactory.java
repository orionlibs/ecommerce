/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.util;

import com.hybris.cockpitng.core.persistence.packaging.CockpitResourceLoader;
import com.hybris.cockpitng.core.persistence.packaging.WidgetLibConstants;
import com.hybris.cockpitng.core.util.ClassLoaderUtils;
import com.hybris.cockpitng.core.util.CockpitProperties;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.WebApp;
import org.zkoss.zk.ui.http.SerializableUiFactory;
import org.zkoss.zk.ui.metainfo.PageDefinition;
import org.zkoss.zk.ui.sys.RequestInfo;

/**
 * Extension to {@link SerializableUiFactory}, loading zul files from cockpitng widget archives. It includes a page
 * definition cache which can be enabled by 'cockpitng.uifactory.cache.enabled' in {@link CockpitProperties}.
 */
public class CockpitUIFactory extends SerializableUiFactory
{
    public static final String COCKPITNG_UIFACTORY_CACHE_ENABLED = "cockpitng.uifactory.cache.enabled";
    private static final String COCKPIT_BEAN_ACCESS_HELPER = "COCKPIT_BEAN_ACCESS_HELPER";
    private static final String COCKPIT_BEAN_ACCESS_HELPER_INITIALIZED = "COCKPIT_BEAN_ACCESS_HELPER_INITIALIZED";
    private static final Logger LOG = LoggerFactory.getLogger(CockpitUIFactory.class);


    @Override
    public Session newSession(final WebApp wapp, final Object nativeSess, final Object request)
    {
        if(nativeSess instanceof HttpSession)
        {
            return new BackofficeSession(wapp, (HttpSession)nativeSess, request);
        }
        else
        {
            return new BackofficeSession(wapp, nativeSess, request);
        }
    }


    @Override
    public PageDefinition getPageDefinition(final RequestInfo requestInfo, final String path)
    {
        PageDefinition pageDefinition = super.getPageDefinition(requestInfo, path);
        if(pageDefinition == null || "/cockpit.zul".equalsIgnoreCase(path))
        {
            pageDefinition = getPageDefinitionExternal(requestInfo, path);
        }
        return pageDefinition;
    }


    protected PageDefinition getPageDefinitionExternal(final RequestInfo requestInfo, final String path)
    {
        final CockpitProperties cockpitProperties = getCockpitProperties(requestInfo);
        final boolean cacheEnabled = cockpitProperties != null
                        && Boolean.parseBoolean(cockpitProperties.getProperty(COCKPITNG_UIFACTORY_CACHE_ENABLED));
        if(cacheEnabled)
        {
            final PageDefinition pageDefinitionFromCache = getPageDefinitionFromCache(requestInfo, path);
            if(pageDefinitionFromCache != null)
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Found page definition in cache: " + path);
                }
                return pageDefinitionFromCache;
            }
        }
        else if(LOG.isDebugEnabled())
        {
            LOG.debug("ZUL Cache disabled, try to locate page definition: " + path);
        }
        final PageDefinition pageDefinition;
        final Optional<InputStream> resourceAsStream = getViewStream(requestInfo, path);
        try
        {
            pageDefinition = resourceAsStream.map(stream -> loadViewFromStream(stream, path))
                            .map(view -> getPageDefinition(requestInfo, path, view)).orElse(null);
        }
        finally
        {
            resourceAsStream.ifPresent(IOUtils::closeQuietly);
        }
        if(pageDefinition == null)
        {
            LOG.error("Could not find resource '{}' in classpath.", path);
        }
        else if(cacheEnabled)
        {
            final CockpitZulCache zulCache = getZulCache(requestInfo);
            if(zulCache != null)
            {
                zulCache.addPageDefinitionToCache(path, pageDefinition);
            }
        }
        return pageDefinition;
    }


    protected Optional<InputStream> getViewStream(final RequestInfo requestInfo, final String path)
    {
        Optional<InputStream> resourceAsStream = getViewStreamFromAdditionalLoader(requestInfo, path);
        resourceAsStream = resourceAsStream.map(Optional::of).orElse(getComponentViewStream(path));
        resourceAsStream = resourceAsStream.map(Optional::of).orElse(getCoreViewStream(path));
        return resourceAsStream;
    }


    protected Optional<InputStream> getViewStreamFromAdditionalLoader(final RequestInfo requestInfo, final String path)
    {
        final CockpitResourceLoader additionalCockpitResourceLoader = getAdditionalCockpitResourceLoader(requestInfo);
        if(additionalCockpitResourceLoader != null)
        {
            final String cockpitResourcesPrefix = StringUtils.wrapIfMissing(WidgetLibConstants.ROOT_NAME_COCKPIT_RESOURCES,
                            IOUtils.DIR_SEPARATOR_UNIX);
            String adaptedPath = path;
            if(!path.startsWith(cockpitResourcesPrefix))
            {
                adaptedPath = StringUtils.prependIfMissing(WidgetLibConstants.ROOT_NAME_COCKPIT_RESOURCES,
                                String.valueOf(IOUtils.DIR_SEPARATOR_UNIX))
                                + path;
            }
            return Optional.ofNullable(additionalCockpitResourceLoader.getViewResourceAsStream(adaptedPath));
        }
        else
        {
            return Optional.empty();
        }
    }


    protected Optional<InputStream> getComponentViewStream(final String path)
    {
        return Optional.ofNullable(ClassLoaderUtils.getResourceAsStream(getClass(), path));
    }


    protected Optional<InputStream> getCoreViewStream(final String path)
    {
        final String adaptedPath = StringUtils.prependIfMissing(WidgetLibConstants.ROOT_NAME_COCKPIT_RESOURCES,
                        String.valueOf(IOUtils.DIR_SEPARATOR_UNIX))
                        + path;
        return Optional.ofNullable(ClassLoaderUtils.getResourceAsStream(getClass(), adaptedPath));
    }


    protected String loadViewFromStream(final InputStream stream, final String path)
    {
        try
        {
            return IOUtils.toString(stream);
        }
        catch(final IOException e)
        {
            LOG.error("Error reading file '" + path + "', reason was ", e);
            return null;
        }
    }


    protected PageDefinition getPageDefinition(final RequestInfo requestInfo, final String path, final String view)
    {
        final PageDefinition pageDefinition;
        try
        {
            pageDefinition = getPageDefinitionDirectly(requestInfo, view, "zul");
        }
        catch(final UiException uie)
        {
            if(LOG.isWarnEnabled())
            {
                LOG.warn(String.format("Error loading page definition [path: %s]", path));
            }
            throw uie;
        }
        return pageDefinition;
    }


    private PageDefinition getPageDefinitionFromCache(final RequestInfo requestInfo, final String path)
    {
        final CockpitZulCache zulCache = getZulCache(requestInfo);
        return zulCache == null ? null : zulCache.getPageDefinition(path);
    }


    private CockpitUIFactoryBeanAccessHelper getBeanAccessHelper(final RequestInfo requestInfo)
    {
        final WebApp webApp = requestInfo.getWebApp();
        if(webApp.getAttribute(COCKPIT_BEAN_ACCESS_HELPER_INITIALIZED) == null)
        {
            final ServletContext servletContext = requestInfo.getWebApp().getServletContext();
            final WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
            CockpitUIFactoryBeanAccessHelper beanAccesHelper = null;
            try
            {
                beanAccesHelper = wac.getBean("cockpitUIFactoryBeanAccesHelper", CockpitUIFactoryBeanAccessHelper.class);
            }
            catch(final NoSuchBeanDefinitionException e)
            {
                LOG.debug("No cockpitAdditionalResourceLoader found.", e);
            }
            webApp.setAttribute(COCKPIT_BEAN_ACCESS_HELPER, beanAccesHelper);
            webApp.setAttribute(COCKPIT_BEAN_ACCESS_HELPER_INITIALIZED, "true");
        }
        final Object attribute = webApp.getAttribute(COCKPIT_BEAN_ACCESS_HELPER);
        return attribute instanceof CockpitUIFactoryBeanAccessHelper ? (CockpitUIFactoryBeanAccessHelper)attribute : null;
    }


    private CockpitResourceLoader getAdditionalCockpitResourceLoader(final RequestInfo requestInfo)
    {
        final CockpitUIFactoryBeanAccessHelper beanAccessHelper = getBeanAccessHelper(requestInfo);
        return beanAccessHelper == null ? null : beanAccessHelper.getCockpitResourceLoader();
    }


    private CockpitProperties getCockpitProperties(final RequestInfo requestInfo)
    {
        final CockpitUIFactoryBeanAccessHelper beanAccessHelper = getBeanAccessHelper(requestInfo);
        return beanAccessHelper == null ? null : beanAccessHelper.getCockpitProperties();
    }


    private CockpitZulCache getZulCache(final RequestInfo requestInfo)
    {
        final CockpitUIFactoryBeanAccessHelper beanAccessHelper = getBeanAccessHelper(requestInfo);
        return beanAccessHelper == null ? null : beanAccessHelper.getCockpitZulCache();
    }
}
