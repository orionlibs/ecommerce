package de.hybris.platform.mediaweb;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.suspend.SystemIsSuspendedException;
import de.hybris.platform.core.threadregistry.OperationInfo;
import de.hybris.platform.core.threadregistry.RegistrableThread;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.media.MediaManager;
import de.hybris.platform.media.exceptions.MediaInvalidLocationException;
import de.hybris.platform.media.exceptions.MediaNotFoundException;
import de.hybris.platform.media.services.MediaHeadersRegistry;
import de.hybris.platform.media.url.PrettyUrlStrategy;
import de.hybris.platform.media.url.impl.PrettyUrlStrategyFactory;
import de.hybris.platform.media.web.DefaultMediaFilterLogic;
import de.hybris.platform.media.web.MediaFilterLogicContext;
import de.hybris.platform.servicelayer.web.CustomMediaHeaderConfigurator;
import de.hybris.platform.util.config.ConfigIntf;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.UUID;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.GenericValidator;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Deprecated(since = "6.3.0", forRemoval = true)
public class MediaFilter implements Filter
{
    protected static final String INTERNAL_PRETTY_URL_MARKER = "PRETTY" + UUID.randomUUID();
    private static final Logger LOG = LoggerFactory.getLogger(MediaFilter.class);
    private static final String CONTENT_TYPE_TEXT_PLAIN = "text/plain";
    private static final char DOT = '.';
    private static final String SLASH = "/";
    private static final String SUSPENDED = "Application is in the SUSPENDED state";
    private static final String ALLOWED_EXT_FOR_CL = "media.allowed.extensions.for.ClassLoader";
    private static final String HEADER_ETAG = "ETag";
    private static final String HEADER_IF_NONE_MATCH = "If-None-Match";
    private static final String HEADER_X_CONTENT_OPTIONS = "X-Content-Type-Options";
    private static final String NOSNIFF = "nosniff";
    private static final Splitter CTX_SPLITTER = Splitter.on("|");
    private static final String FORCE_DOWNLOAD_DIALOG_FILE_EXTENSIONS = "media.force.download.dialog.fileextensions";
    private final CustomMediaHeaderConfigurator customMediaHeaderConfigurator = new CustomMediaHeaderConfigurator();


    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
    {
        if(request instanceof HttpServletRequest && response instanceof HttpServletResponse)
        {
            HttpServletRequest httpRequest = (HttpServletRequest)request;
            SecureResponseWrapper secureResponseWrapper = new SecureResponseWrapper((HttpServletResponse)response);
            try
            {
                RegistrableThread.registerThread(
                                OperationInfo.builder().withCategory(OperationInfo.Category.WEB_REQUEST).asNotSuspendableOperation().build());
            }
            catch(SystemIsSuspendedException e)
            {
                LOG.info("System is {}. Request will not be processed.", e.getSystemStatus());
                secureResponseWrapper.sendError(503, "Application is in the SUSPENDED state");
                return;
            }
            try
            {
                doFilterMedia(httpRequest, (HttpServletResponse)secureResponseWrapper, getMediaContext(httpRequest));
            }
            catch(IllegalArgumentException e)
            {
                sendBadRequestResponseStatus((HttpServletResponse)secureResponseWrapper, e);
            }
            finally
            {
                RegistrableThread.unregisterThread();
            }
        }
        else
        {
            chain.doFilter(request, response);
        }
    }


    private void doFilterMedia(HttpServletRequest httpRequest, HttpServletResponse httpResponse, Iterable<String> mediaContext) throws IOException, ServletException
    {
        setCurretTenantByID(mediaContext);
        if(getMediaManager().isSecuredFolder(getContextPart(ContextPart.FOLDER, mediaContext)))
        {
            httpResponse.setStatus(403);
        }
        else
        {
            String resourcePath = getResourcePath(httpRequest);
            if(isResourceFromClassLoader(resourcePath))
            {
                loadFromClassLoader(httpResponse, resourcePath);
            }
            else
            {
                String responseETag = generateETag(getContextPart(ContextPart.LOCATION, mediaContext));
                httpResponse.setHeader("ETag", responseETag);
                String requestETag = httpRequest.getHeader("If-None-Match");
                if(responseETag.equals(requestETag))
                {
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug("ETag [{}] equal to If-None-Match, sending 304", responseETag);
                    }
                    readConfiguredHeaderParamsAndWriteToResponse(httpResponse);
                    modifyResponseWithConfiguredHeaders(httpResponse);
                    httpResponse.setStatus(304);
                }
                else
                {
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug("ETag [{}] is not equal to If-None-Match, sending standard response", responseETag);
                    }
                    processStandardResponse(httpRequest, httpResponse, mediaContext);
                }
            }
        }
    }


    protected void setXContentTypeOptionsHeader(HttpServletResponse httpResponse)
    {
        httpResponse.setHeader("X-Content-Type-Options", "nosniff");
    }


    protected void setCurretTenantByID(Iterable<String> mediaContext)
    {
        String tenantId = getContextPart(ContextPart.TENANT, mediaContext);
        Preconditions.checkArgument(StringUtils.isNotBlank(tenantId), "tenantId is required to process this request");
        Registry.setCurrentTenantByID(tenantId);
    }


    private Iterable<String> getMediaContext(HttpServletRequest httpRequest)
    {
        String encodedMediaCtx = getLocalMediaWebUrlContextParam(httpRequest);
        if(encodedMediaCtx == null)
        {
            return createLegacyLocalMediaWebUrlContext(httpRequest);
        }
        return createLocalMediawebUrlContext(encodedMediaCtx);
    }


    protected boolean isLegacyPrettyUrlSupport()
    {
        return getConfig().getBoolean("media.legacy.prettyURL", false);
    }


    private Iterable<String> createLegacyLocalMediaWebUrlContext(HttpServletRequest httpRequest)
    {
        String resourcePath = getLegacyResourcePath(httpRequest);
        if(resourcePath.startsWith("/"))
        {
            resourcePath = resourcePath.substring(1);
        }
        return splitLegacyPath(resourcePath);
    }


    protected Iterable<String> splitLegacyPath(String path)
    {
        PrettyUrlStrategy.ParsedPath parsedPath = PrettyUrlStrategyFactory.getDefaultPrettyUrlStrategy().parsePath(path);
        List<String> result = new ArrayList<>(7);
        result.add(parsedPath.getTenantId());
        result.add(parsedPath.getFolderQualifier());
        result.add(null);
        result.add("-");
        result.add(parsedPath.getLocation());
        result.add("-");
        result.add(INTERNAL_PRETTY_URL_MARKER);
        return result;
    }


    private String getLegacyResourcePath(HttpServletRequest httpRequest)
    {
        String resourcePath = httpRequest.getServletPath();
        if(resourcePath == null || resourcePath.trim().isEmpty())
        {
            String reqURI = httpRequest.getRequestURI();
            String ctxPath = httpRequest.getContextPath();
            resourcePath = reqURI.replace(ctxPath, "");
        }
        return resourcePath;
    }


    private Iterable<String> createLocalMediawebUrlContext(String encodedMediaCtx)
    {
        Preconditions.checkArgument(!GenericValidator.isBlankOrNull(encodedMediaCtx), "incorrect media context in request");
        Iterable<String> mediaContext = CTX_SPLITTER.split(decodeBase64(encodedMediaCtx));
        Preconditions.checkArgument((Iterables.size(mediaContext) == 6), "incorrect media context in request");
        return mediaContext;
    }


    private String getLocalMediaWebUrlContextParam(HttpServletRequest httpRequest)
    {
        return httpRequest.getParameter("context");
    }


    private String generateETag(String location)
    {
        HashFunction md5 = Hashing.md5();
        return md5.hashUnencodedChars(location).toString();
    }


    private String getResourcePath(HttpServletRequest httpRequest)
    {
        String resourcePath = httpRequest.getServletPath();
        if(GenericValidator.isBlankOrNull(resourcePath))
        {
            String reqURI = httpRequest.getRequestURI();
            String ctxPath = httpRequest.getContextPath();
            resourcePath = reqURI.replace(ctxPath, "");
        }
        return resourcePath;
    }


    private void processStandardResponse(HttpServletRequest httpRequest, HttpServletResponse httpResponse, Iterable<String> mediaContext) throws IOException, ServletException
    {
        String resourcePath = getResourcePath(httpRequest);
        MediaFilterLogicContext ctx = new MediaFilterLogicContext(mediaContext, resourcePath);
        try
        {
            verifyHash(ctx);
            readConfiguredHeaderParamsAndWriteToResponse(httpResponse);
            addContentDisposition(httpRequest, httpResponse, resourcePath);
            addContentType(httpResponse, ctx);
            loadFromMediaStorage(httpResponse, mediaContext);
        }
        catch(MediaInvalidLocationException e)
        {
            sendForbiddenResponseStatus(httpResponse, (Exception)e);
        }
        catch(MediaNotFoundException e)
        {
            sendResourceNotFoundResponseStatus(httpResponse, (Exception)e);
        }
        catch(Exception e)
        {
            sendBadRequestResponseStatus(httpResponse, e);
        }
    }


    private void readConfiguredHeaderParamsAndWriteToResponse(HttpServletResponse httpResponse) throws UnsupportedEncodingException
    {
        MediaHeadersRegistry mediaHeadersRegistry = getMediaManager().getMediaHeadersRegistry();
        if(mediaHeadersRegistry != null)
        {
            Map<String, String> headerParams = mediaHeadersRegistry.getHeaders();
            for(Map.Entry<String, String> me : headerParams.entrySet())
            {
                httpResponse.setHeader(me.getKey(), me.getValue());
            }
        }
    }


    private void addContentDisposition(HttpServletRequest httpRequest, HttpServletResponse httpResponse, String resourcePath)
    {
        if(isAddContentDisposition(httpRequest, resourcePath))
        {
            httpResponse.addHeader("Content-Disposition", " attachment; filename=" + getRealFileNameFromResource(resourcePath));
        }
    }


    private boolean isAddContentDisposition(HttpServletRequest httpRequest, String resourcePath)
    {
        boolean addContentDisposition = Boolean.parseBoolean(httpRequest.getParameter("attachment"));
        if(!addContentDisposition)
        {
            Set<String> extensions = getAllowedExtensions("media.force.download.dialog.fileextensions");
            if(extensions != null && !extensions.isEmpty())
            {
                String lowerCaseResource = resourcePath.toLowerCase();
                for(String ext : extensions)
                {
                    if(lowerCaseResource.endsWith(ext))
                    {
                        return true;
                    }
                }
            }
        }
        return addContentDisposition;
    }


    @Deprecated(since = "2011", forRemoval = true)
    protected void addContentType(HttpServletResponse httpResponse, Iterable<String> mediaContext, String resourcePath)
    {
        addContentType(httpResponse, new MediaFilterLogicContext(mediaContext, resourcePath));
    }


    protected void addContentType(HttpServletResponse httpResponse, MediaFilterLogicContext ctx)
    {
        getMediaFilterLogic().addContentType(httpResponse, ctx);
        setXContentTypeOptionsHeader(httpResponse);
    }


    protected DefaultMediaFilterLogic getMediaFilterLogic()
    {
        return (DefaultMediaFilterLogic)Registry.getApplicationContext().getBean("mediaFilterLogic", DefaultMediaFilterLogic.class);
    }


    private boolean isResourceFromClassLoader(String resourcePath)
    {
        return (resourcePath != null && resourcePath.contains("/fromjar"));
    }


    private String getRealFileNameFromResource(String resourcePath)
    {
        int index = resourcePath.lastIndexOf("/");
        return (index >= 0) ? resourcePath.substring(index + 1) : resourcePath;
    }


    private void loadFromClassLoader(HttpServletResponse httpResponse, String resourcePath) throws IOException
    {
        String resourceName = resourcePath.substring(resourcePath
                        .indexOf("/fromjar") + 1 + "fromjar".length());
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Trying to load resource '{}' from classloader.", resourceName);
        }
        PolicyFactory policy = (new HtmlPolicyBuilder()).toFactory();
        if(isDeniedByExtensionForClassloader(resourceName))
        {
            httpResponse.setContentType("text/plain");
            setXContentTypeOptionsHeader(httpResponse);
            modifyResponseWithConfiguredHeaders(httpResponse, "text/plain");
            httpResponse.getOutputStream()
                            .println("not allowed to load media '" + policy
                                            .sanitize(resourceName) + "' from classloader. Check parameter media.allowed.extensions.for.ClassLoader in advanced.properties to change the file extensions (e.g. *.gif) that are allowed to download.");
        }
        else
        {
            InputStream inputStream = null;
            try
            {
                inputStream = getResourceAsStream(resourceName);
                if(inputStream == null)
                {
                    httpResponse.setContentType("text/plain");
                    setXContentTypeOptionsHeader(httpResponse);
                    modifyResponseWithConfiguredHeaders(httpResponse, "text/plain");
                    httpResponse.getOutputStream().println("file '" + policy.sanitize(resourceName) + "' not found!");
                }
                else
                {
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug("Loading resource '{}' from classloader.", resourceName);
                    }
                    modifyResponseWithConfiguredHeaders(httpResponse);
                    IOUtils.copy(inputStream, (OutputStream)httpResponse.getOutputStream());
                }
            }
            finally
            {
                IOUtils.closeQuietly(inputStream);
            }
        }
    }


    private boolean isDeniedByExtensionForClassloader(String resourceName)
    {
        Set<String> extensions = getAllowedExtensions("media.allowed.extensions.for.ClassLoader");
        if(extensions == null || extensions.isEmpty())
        {
            return false;
        }
        String check = resourceName.toLowerCase();
        for(String ext : extensions)
        {
            if(check.endsWith(ext))
            {
                return false;
            }
        }
        return true;
    }


    protected InputStream getResourceAsStream(String resourceName)
    {
        return Media.class.getResourceAsStream(resourceName);
    }


    private void loadFromMediaStorage(HttpServletResponse httpResponse, Iterable<String> mediaContext)
    {
        InputStream inputStream = null;
        try
        {
            String folderQualifier = getContextPart(ContextPart.FOLDER, mediaContext);
            String location = getContextPart(ContextPart.LOCATION, mediaContext);
            String contentType = httpResponse.getContentType();
            verifyFolderIsNotSecured(folderQualifier);
            MediaManager.InputStreamWithSize inputStreamWithSize = getMediaAsStreamWithSize(folderQualifier, location);
            inputStream = inputStreamWithSize.getInputStream();
            httpResponse.setContentLengthLong(inputStreamWithSize.getSize());
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Loading resource [location: {}] from media storage.", location);
            }
            modifyResponseWithConfiguredHeaders(httpResponse, contentType, folderQualifier);
            IOUtils.copy(inputStream, (OutputStream)httpResponse.getOutputStream());
        }
        catch(MediaInvalidLocationException e)
        {
            sendForbiddenResponseStatus(httpResponse, (Exception)e);
        }
        catch(MediaNotFoundException e)
        {
            sendResourceNotFoundResponseStatus(httpResponse, (Exception)e);
        }
        catch(Exception e)
        {
            sendBadRequestResponseStatus(httpResponse, e);
        }
        finally
        {
            IOUtils.closeQuietly(inputStream);
            unsetCurrentTenant();
        }
    }


    private void verifyFolderIsNotSecured(String folderQualifier)
    {
        if(getMediaManager().isFolderConfiguredAsSecured(folderQualifier))
        {
            throw new MediaInvalidLocationException("Access to file denied");
        }
    }


    protected void unsetCurrentTenant()
    {
        Registry.unsetCurrentTenant();
    }


    private void verifyHash(MediaFilterLogicContext ctx)
    {
        if(isBasedOnPrettyUrl(ctx.getMediaContext()))
        {
            return;
        }
        getMediaFilterLogic().verifyHash(ctx);
    }


    private boolean isBasedOnPrettyUrl(Iterable<String> mediaContext)
    {
        return (mediaContext != null &&
                        Iterables.size(mediaContext) > 6 && INTERNAL_PRETTY_URL_MARKER
                        .equals(Iterables.get(mediaContext, 6)));
    }


    private MediaManager.InputStreamWithSize getMediaAsStreamWithSize(String folderQualifier, String location)
    {
        return getMediaManager().getMediaAsStreamWithSize(folderQualifier, location);
    }


    protected MediaManager getMediaManager()
    {
        return MediaManager.getInstance();
    }


    private String decodeBase64(String value)
    {
        String decodedValue = "";
        if(StringUtils.isNotBlank(value))
        {
            try
            {
                decodedValue = new String((new Base64(-1, null, true)).decode(value));
            }
            catch(Exception e)
            {
                throw new RuntimeException("Cannot decode base32 coded string: " + value);
            }
        }
        return decodedValue;
    }


    private String getContextPart(ContextPart contextPart, Iterable<String> mediaContext)
    {
        return (String)Iterables.get(mediaContext, contextPart.getPartNumber());
    }


    private void sendForbiddenResponseStatus(HttpServletResponse httpResponse, Exception exception)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Access forbidden for given media", exception);
        }
        httpResponse.setStatus(403);
    }


    private void sendBadRequestResponseStatus(HttpServletResponse httpResponse, Exception exception)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("The request sent by the client was syntactically incorrect", exception);
        }
        httpResponse.setStatus(400);
    }


    private void sendResourceNotFoundResponseStatus(HttpServletResponse httpResponse, Exception exception)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Requested resource not found", exception);
        }
        httpResponse.setStatus(404);
    }


    private Set<String> getAllowedExtensions(String configParameter)
    {
        Set<String> extensions;
        String str = getConfig().getParameter(configParameter);
        if(StringUtils.isBlank(str))
        {
            extensions = Collections.emptySet();
        }
        else
        {
            Set<String> set = new LinkedHashSet<>();
            for(StringTokenizer tok = new StringTokenizer(str, ",;"); tok.hasMoreTokens(); )
            {
                String strElement = tok.nextToken().toLowerCase().trim();
                if(strElement.length() > 0)
                {
                    set.add(strElement);
                }
            }
            extensions = Collections.unmodifiableSet(set);
        }
        if(LOG.isDebugEnabled())
        {
            LOG.debug("{}: Supported media extensions: {}", configParameter, extensions.toString());
        }
        return extensions;
    }


    protected void modifyResponseWithConfiguredHeaders(HttpServletResponse httpResponse)
    {
        this.customMediaHeaderConfigurator.modifyResponseWithConfiguredHeaders(httpResponse);
    }


    protected void modifyResponseWithConfiguredHeaders(HttpServletResponse httpResponse, String mime, String folder)
    {
        this.customMediaHeaderConfigurator.modifyResponseWithConfiguredHeaders(httpResponse, mime, folder);
    }


    protected void modifyResponseWithConfiguredHeaders(HttpServletResponse httpResponse, String mime)
    {
        this.customMediaHeaderConfigurator.modifyResponseWithConfiguredHeaders(httpResponse, mime);
    }


    protected ConfigIntf getConfig()
    {
        return Registry.getMasterTenant().getConfig();
    }


    public void init(FilterConfig filterConfig) throws ServletException
    {
        if(StringUtils.isNotBlank(filterConfig.getInitParameter("allowedExtensionsWhenUsingClassLoader")))
        {
            LOG.error("Found 'allowedExtensionsWhenUsingClassLoader' definition in web.xml - this setting is no more supported.Use property 'media.allowed.extensions.for.ClassLoader' in the advanced.properties file.");
        }
    }


    public void destroy()
    {
    }
}
