/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.persistence.packaging;

import com.hybris.cockpitng.core.util.CockpitProperties;
import com.hybris.cockpitng.core.util.Resettable;
import com.hybris.cockpitng.core.util.impl.WidgetRequest;
import com.hybris.cockpitng.core.util.impl.WidgetRequestUtils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Serves resources located in a widget library.
 *
 * @see <a href="https://blogs.msdn.microsoft.com/ieinternals/2011/05/14/stylesheet-limits-in-internet-explorer/">IE 8
 *      &amp; 9 limitations of numer of imported files in css stylesheet</a>
 * @see #isSplitForLegacyIeEnabled()
 */
public class SimpleHybrisWidgetResourceLoader extends HttpServlet implements Resettable
{
    public static final String COCKPITNG_RESOURCELOADER_CACHE_EXPIRATION = "cockpitng.resourceloader.cache.expiration";
    public static final String COCKPITNG_WIDGET_RESOURCE_LOADER_CACHE_CONTROL = "cockpitng.resourceloader.cache.control";
    public static final String CSS_CACHE_ENABLED = "cssCacheEnabled";
    public static final String CSS_LEGACY_IE_IMPORT = "cockpitng.css.legacy.ie9.import.support.enabled";
    public static final String APPLICATION_THEME_CSS = "applicationThemeCss";
    public static final int MAX_LEGACY_IE_IMPORT_FILES = 31;
    public static final int MAX_LEGACY_IE_IMPORT_FILES_LEVEL_1 = 20;
    public static final int MAX_LEGACY_IE_IMPORT_FILES_NESTING_LEVEL = 4;
    public static final String CNGPRELOAD_CSS = "_cngpreload.css";
    public static final String CNGPRELOAD = "_cngpreload";
    protected static final Map<String, String> CACHED_MERGED_CSS = new ConcurrentHashMap<>();
    protected static final Map<String, String> IE_CSS_SPLIT_CACHE = new ConcurrentHashMap<>();
    protected static final String JS_DEPENDENCY_VARIABLE = "imp";
    private static final long serialVersionUID = 4663780831619026804L;
    private static final Logger LOG = LoggerFactory.getLogger(SimpleHybrisWidgetResourceLoader.class);
    private static final String APPLICATION_THEME_END_CSS = "applicationThemeCssEnd";
    private static final String DEFAULT_CACHE_CONTROLLER_HEADER = "max-age:3600,must-revalidate";
    private static final DateFormat HTML_DATE_FORMAT = createHtmlDateFormat();
    private static final int DEFAULT_EXPIRATION_IN_SECONDS = -1;
    private final transient Map<Class<?>, Map<String, ?>> resolverCache = new HashMap<>();
    private CockpitResourceLoader widgetResourceReader;
    private WidgetLibUtils widgetLibUtils;
    private CockpitProperties cockpitProperties;
    private ResourceLoader fallbackResourceLoader;


    @Override
    public void init() throws ServletException
    {
        widgetLibUtils = getApplicationContext().getBean("widgetLibUtils", WidgetLibUtils.class);
        cockpitProperties = getApplicationContext().getBean("cockpitProperties", CockpitProperties.class);
        widgetResourceReader = getApplicationContext().getBean("widgetResourceReader", CockpitResourceLoader.class);
        fallbackResourceLoader = getApplicationContext().getBean("simpleHybrisFallbackResourceLoader", ResourceLoader.class);
        final boolean anyNull = Stream.of(widgetLibUtils, cockpitProperties, widgetResourceReader, fallbackResourceLoader)
                        .anyMatch(Objects::isNull);
        if(anyNull)
        {
            throw new ServletException("Problem with initialize servlet, ");
        }
    }


    public static void clearCssCache()
    {
        LOG.debug("Clearing css cache");
        CACHED_MERGED_CSS.clear();
    }


    public static DateFormat createHtmlDateFormat()
    {
        final DateFormat httpDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        httpDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return httpDateFormat;
    }


    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException
    {
        doGetInternal(req, resp);
    }


    protected void doGetInternal(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException
    {
        String requestURI = req.getRequestURI();
        if(StringUtils.isNotBlank(requestURI))
        {
            requestURI = WidgetRequestUtils.cleanupRequestUri(requestURI);
            processResponseHeader(requestURI, resp);
            if(applyAdditionalWidgetResourceLocators(req, resp))
            {
                return;
            }
            if(requestURI.endsWith(CNGPRELOAD_CSS))
            {
                final String baseName = FilenameUtils.getBaseName(requestURI);
                if(baseName != null)
                {
                    writeMergedWidgetCss(baseName.replace(CNGPRELOAD, ""), req, resp);
                    return;
                }
            }
            else if(requestURI.matches(".*_cngpreload_\\d+.*.css"))
            {
                final String baseName = FilenameUtils.getBaseName(requestURI);
                if(baseName != null)
                {
                    writeCachedCssChunkForLegacyIe(baseName, req, resp);
                    return;
                }
            }
            else if(requestURI.contains(WidgetLibConstants.JAR_RESOURCES_PATH_PREFIX)
                            || requestURI.contains(WidgetLibConstants.JAR_ROOT_RESOURCE_PATH_PREFIX))
            {
                final InputStream resourceAsStreamFromJar = getResourceAsStreamFromJar(requestURI);
                if(resourceAsStreamFromJar != null)
                {
                    appendResponse(resourceAsStreamFromJar, resp);
                    return;
                }
            }
            else
            {
                final String processedRequestURI = processRequestURI(requestURI, req);
                InputStream resourceAsStream = getResourceAsStream(processedRequestURI);
                if(resourceAsStream != null)
                {
                    appendResponse(resourceAsStream, resp);
                    return;
                }
                resourceAsStream = getResourceAsStreamFromJar(processedRequestURI);
                if(resourceAsStream != null)
                {
                    appendResponse(resourceAsStream, resp);
                    return;
                }
            }
        }
        super.doGet(req, resp);
    }


    private boolean applyAdditionalWidgetResourceLocators(final HttpServletRequest req, final HttpServletResponse resp)
    {
        final Map<String, WidgetResourceLocator> beans = findBeansInWebApplication(WidgetResourceLocator.class);
        if(MapUtils.isNotEmpty(beans))
        {
            for(final WidgetResourceLocator locator : beans.values())
            {
                if(locator.isApplicableTo(req, getServletConfig()) && locator.apply(req, resp, getServletConfig()))
                {
                    return true;
                }
            }
        }
        return false;
    }


    private <T> Map<String, T> findBeansInWebApplication(final Class<T> clazz)
    {
        Map<String, T> result = (Map<String, T>)resolverCache.get(clazz);
        if(result != null)
        {
            return result;
        }
        result = new HashMap<>();
        ApplicationContext context = getApplicationContext();
        while(context instanceof WebApplicationContext)
        {
            for(final Map.Entry<String, T> entry : context.getBeansOfType(clazz).entrySet())
            {
                if(!result.containsKey(entry.getKey()))
                {
                    result.put(entry.getKey(), entry.getValue());
                }
            }
            context = context.getParent();
        }
        resolverCache.put(clazz, result);
        return result;
    }


    protected void processResponseHeader(final String requestURI, final HttpServletResponse resp)
    {
        resp.setContentType(getServletContext().getMimeType(requestURI));
        String cacheControlHeader = DEFAULT_CACHE_CONTROLLER_HEADER;
        final String initParameter = getCockpitProperties().getProperty(COCKPITNG_WIDGET_RESOURCE_LOADER_CACHE_CONTROL);
        if(initParameter != null)
        {
            cacheControlHeader = initParameter;
        }
        resp.setHeader("Cache-Control", cacheControlHeader);
        int expirationInSeconds = DEFAULT_EXPIRATION_IN_SECONDS;
        final String expirationProperty = getCockpitProperties().getProperty(COCKPITNG_RESOURCELOADER_CACHE_EXPIRATION);
        if(StringUtils.isNotBlank(expirationProperty))
        {
            try
            {
                expirationInSeconds = Integer.parseInt(expirationProperty);
                if(expirationInSeconds < DEFAULT_EXPIRATION_IN_SECONDS)
                {
                    expirationInSeconds = DEFAULT_EXPIRATION_IN_SECONDS;
                }
            }
            catch(final NumberFormatException e)
            {
                final String warningMsg = String.format("Could not pars %s because of wrong format. Must be an integer, but got '%s'. Expiration disabled.", COCKPITNG_RESOURCELOADER_CACHE_EXPIRATION, expirationProperty);
                LOG.warn(warningMsg);
            }
        }
        final Calendar cal = new GregorianCalendar();
        cal.add(Calendar.SECOND, expirationInSeconds);
        synchronized(HTML_DATE_FORMAT)
        {
            resp.setHeader("Expires", HTML_DATE_FORMAT.format(cal.getTime()));
            resp.setHeader("Last-Modified", HTML_DATE_FORMAT.format(new Date()));
        }
    }


    protected WidgetLibUtils getWidgetLibUtils()
    {
        return widgetLibUtils;
    }


    public CockpitProperties getCockpitProperties()
    {
        return cockpitProperties;
    }


    public CockpitResourceLoader getWidgetResourceReader()
    {
        return widgetResourceReader;
    }


    public ResourceLoader getFallbackResourceLoader()
    {
        return fallbackResourceLoader;
    }


    protected WebApplicationContext getApplicationContext()
    {
        return WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
    }


    protected InputStream getResourceAsStreamFromJar(final WidgetJarLibInfo widgetJarLibInfo, final String resourceName)
    {
        return getWidgetResourceReader().getResourceAsStream(widgetJarLibInfo, resourceName);
    }


    protected InputStream getResourceAsStreamFromJar(final File jarFile, final String resourceName)
    {
        return getWidgetResourceReader().getResourceAsStream(jarFile, resourceName);
    }


    protected InputStream getResourceAsStreamFromJar(final String uri)
    {
        if(uri == null)
        {
            return null;
        }
        InputStream stream = null;
        final String requestURI = uri.trim();
        final WidgetRequest request = WidgetRequestUtils.parseRequestURI(getWidgetLibUtils(), requestURI);
        WidgetJarLibInfo widgetJarLibInfo = null;
        if(Objects.nonNull(request))
        {
            widgetJarLibInfo = getWidgetLibUtils().getWidgetJarLibInfo(request.getWidgetId());
        }
        if(Objects.nonNull(widgetJarLibInfo))
        {
            stream = getResourceAsStreamFromJar(widgetJarLibInfo, request.getResourcePath());
        }
        else if(requestURI.startsWith("/cockpitng/cng"))
        {
            final String resourceName = requestURI.replaceFirst("/cockpitng/", "/");
            for(final WidgetJarLibInfo jarInfo : getWidgetLibUtils().getAllJarLibInfos())
            {
                final InputStream result = getResourceAsStreamFromJar(jarInfo, resourceName);
                if(Objects.nonNull(result))
                {
                    stream = result;
                    break;
                }
            }
        }
        else if(Objects.nonNull(request))
        {
            stream = getResourceAsStreamFromJar(request.getJarFile(), request.getResourcePath());
        }
        if(Objects.nonNull(stream))
        {
            return stream;
        }
        final InputStream resource = getFallbackResourceLoader().getResourceAsStream(requestURI);
        if(resource == null)
        {
            LOG.warn("Unable to resolve JarInfo for '{}'", uri);
        }
        return resource;
    }


    protected boolean hasResourceInJar(final String uri)
    {
        if(uri == null)
        {
            return false;
        }
        final String requestURI = uri.trim();
        final WidgetRequest request = WidgetRequestUtils.parseRequestURI(getWidgetLibUtils(), requestURI);
        WidgetJarLibInfo widgetJarLibInfo = null;
        if(request != null && request.getWidgetId() != null)
        {
            widgetJarLibInfo = getWidgetLibUtils().getWidgetJarLibInfo(request.getWidgetId());
        }
        boolean storedResource = false;
        if(widgetJarLibInfo != null)
        {
            storedResource = getWidgetResourceReader().hasResource(widgetJarLibInfo, request.getResourcePath());
        }
        else if(requestURI.startsWith("cockpitng/cng"))
        {
            final String resourceName = requestURI.replaceFirst("/cockpitng/", "/");
            for(final WidgetJarLibInfo jarInfo : getWidgetLibUtils().getAllJarLibInfos())
            {
                if(getWidgetResourceReader().hasResource(jarInfo, resourceName))
                {
                    storedResource = true;
                    break;
                }
            }
        }
        else if(request != null)
        {
            storedResource = getWidgetResourceReader().hasResource(request.getJarFile(), request.getResourcePath());
        }
        if(storedResource)
        {
            return true;
        }
        return getFallbackResourceLoader().hasResource(requestURI);
    }


    protected InputStream getResourceAsStream(final String uri)
    {
        return getWidgetResourceReader().getResourceAsStream(uri);
    }


    protected String processRequestURI(final String requestURI, final HttpServletRequest req)
    {
        String ret = requestURI;
        final String[] split = requestURI.replaceFirst(req.getContextPath(), StringUtils.EMPTY).split("/");
        if(split.length > 1)
        {
            if(!isMappedToRoot(req)) // if not mapped to root, we have to remove the webroot part
            {
                ret = requestURI.replaceFirst(req.getContextPath(), StringUtils.EMPTY);
            }
            ret = WidgetLibConstants.RESOURCES_SUBFOLDER + ret;
        }
        return ret;
    }


    protected void writeMergedWidgetCss(final String mainSlotId, final HttpServletRequest req, final HttpServletResponse resp)
                    throws IOException
    {
        final String mainSlotIdFallback = StringUtils.isBlank(mainSlotId) ? "mainSlot" : mainSlotId;
        if(isCssCached() && CACHED_MERGED_CSS.containsKey(mainSlotIdFallback))
        {
            resp.getOutputStream().write(CACHED_MERGED_CSS.get(mainSlotIdFallback).getBytes(Charset.defaultCharset()));
        }
        else
        {
            final StringBuilder builder = new StringBuilder();
            final String appCss = getServletConfig().getInitParameter(APPLICATION_THEME_CSS);
            if(appCss != null)
            {
                builder.append("@import url('../../").append(appCss).append("');");
            }
            final List<String> cssFilesAsStrings = getCssFilesAsStrings(mainSlotIdFallback, req);
            if(cssFilesAsStrings.size() > MAX_LEGACY_IE_IMPORT_FILES_LEVEL_1 && isSplitForLegacyIeEnabled())
            {
                IE_CSS_SPLIT_CACHE.clear();
                splitImportsForLegacyIe(mainSlotIdFallback + CNGPRELOAD, cssFilesAsStrings, builder, 1);
            }
            else
            {
                for(final String string : cssFilesAsStrings)
                {
                    builder.append("\n").append(string);
                }
            }
            final String app2Css = getServletConfig().getInitParameter(APPLICATION_THEME_END_CSS);
            if(app2Css != null)
            {
                builder.append("\n").append("@import url('../../").append(app2Css).append("');");
            }
            final String css = builder.toString();
            if(isCssCached())
            {
                CACHED_MERGED_CSS.put(mainSlotIdFallback, css);
            }
            resp.getOutputStream().write(css.getBytes(Charset.defaultCharset()));
        }
    }


    /**
     * Method used to distribute the files according to the limit of imports allowed by legacy IE versions.
     *
     * @param idPrefix
     *           prefix for caching partial css files
     * @param urls
     *           collection of files to import
     * @param builder
     *           content of a file should be written to the builder
     */
    protected void splitImportsForLegacyIe(final String idPrefix, final List<String> urls, final StringBuilder builder,
                    final int level)
    {
        if(level > MAX_LEGACY_IE_IMPORT_FILES_NESTING_LEVEL)
        {
            LOG.warn("Exceeded maximal allowed nesting level: {}, application may not work appropriately!",
                            MAX_LEGACY_IE_IMPORT_FILES_NESTING_LEVEL);
        }
        if(CollectionUtils.isNotEmpty(urls))
        {
            final int maxLegacyIeImportFiles = level == 1 ? MAX_LEGACY_IE_IMPORT_FILES_LEVEL_1 : MAX_LEGACY_IE_IMPORT_FILES;
            if(urls.size() > maxLegacyIeImportFiles)
            {
                final int divideInto = Math.min(
                                urls.size() / maxLegacyIeImportFiles + (urls.size() % maxLegacyIeImportFiles == 0 ? 0 : 1),
                                maxLegacyIeImportFiles);
                final int divisionSize = urls.size() / divideInto;
                for(int i = 0; i < divideInto; i++)
                {
                    final StringBuilder passedBuilder = new StringBuilder();
                    final String key = idPrefix + "_" + i;
                    final int fromIndex = i * divisionSize;
                    final int toIndex = i < (divideInto - 1) ? (i + 1) * divisionSize : urls.size();
                    final List<String> subSetOfImports = urls.subList(fromIndex, toIndex);
                    splitImportsForLegacyIe(key, subSetOfImports, passedBuilder, level + 1);
                    builder.append("\n").append("@import url('").append(key).append(".css');");
                    IE_CSS_SPLIT_CACHE.put(key, passedBuilder.toString());
                }
            }
            else
            {
                for(final String file : urls)
                {
                    builder.append("\n").append(file);
                }
            }
        }
    }


    /**
     * As described
     * <a href="https://blogs.msdn.microsoft.com/ieinternals/2011/05/14/stylesheet-limits-in-internet-explorer/">here on
     * MSDN</a> legacy IE versions may struggle interpreting more than 31 import declarations in css stylesheets. This
     * method allows to enable legacy IE support workaround.
     *
     * @return true if legacy IE should be supported
     */
    protected boolean isSplitForLegacyIeEnabled()
    {
        return getCockpitProperties().getBoolean(CSS_LEGACY_IE_IMPORT);
    }


    /**
     * The method should write the content of the requested resource into the output stream.
     *
     * @param cacheKey
     *           key under which the css is stored
     * @param req
     *           http request
     * @param resp
     *           http response
     * @throws IOException
     */
    protected void writeCachedCssChunkForLegacyIe(final String cacheKey, final HttpServletRequest req,
                    final HttpServletResponse resp) throws IOException
    {
        final String css = IE_CSS_SPLIT_CACHE.get(cacheKey);
        if(StringUtils.isNotBlank(css))
        {
            final ServletOutputStream outputStream = resp.getOutputStream();
            outputStream.write(css.getBytes(Charset.defaultCharset()));
        }
    }


    /**
     * Returns all css resources that should be preloaded as strings.
     *
     * @param mainSlotId
     *           the widgets mainSlot id
     * @param request
     *           the request passed to {@link #doGet(HttpServletRequest, HttpServletResponse)}
     */
    protected List<String> getCssFilesAsStrings(final String mainSlotId, final HttpServletRequest request)
    {
        return Collections.emptyList();
    }


    protected boolean isCssCached()
    {
        return Boolean.parseBoolean(getServletConfig().getInitParameter(CSS_CACHE_ENABLED));
    }


    private static boolean isMappedToRoot(final HttpServletRequest request)
    {
        final String requestURI = request.getRequestURI();
        return requestURI.startsWith("/cng") || requestURI.startsWith("/" + WidgetLibConstants.CLASSPATH_RESOURCES_PATH_PREFIX)
                        || requestURI.startsWith("/" + WidgetLibConstants.JAR_RESOURCES_PATH_PREFIX)
                        || requestURI.startsWith("/" + WidgetLibConstants.JAR_ROOT_RESOURCE_PATH_PREFIX);
    }


    protected void appendResponse(final InputStream is, final HttpServletResponse resp) throws IOException
    {
        try
        {
            if(resp.getOutputStream() != null && is != null)
            {
                IOUtils.copy(is, resp.getOutputStream());
            }
        }
        finally
        {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(resp.getOutputStream());
        }
    }


    @Override
    public void reset()
    {
        resolverCache.clear();
    }
}
