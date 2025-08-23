package de.hybris.platform.servicelayer.web;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.base.Suppliers;
import com.google.common.collect.Iterables;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.media.MediaManager;
import de.hybris.platform.media.exceptions.MediaInvalidLocationException;
import de.hybris.platform.media.exceptions.MediaNotFoundException;
import de.hybris.platform.media.services.MediaHeadersRegistry;
import de.hybris.platform.media.url.PrettyUrlStrategy;
import de.hybris.platform.media.url.impl.PrettyUrlStrategyFactory;
import de.hybris.platform.media.web.DefaultMediaFilterLogic;
import de.hybris.platform.media.web.MediaFilterLogicContext;
import de.hybris.platform.servicelayer.media.MediaPermissionService;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.servicelayer.web.urlrenderer.DefaultPublicMediaURLRenderer;
import de.hybris.platform.servicelayer.web.urlrenderer.DefaultSecureMediaURLRenderer;
import de.hybris.platform.util.MediaUtil;
import de.hybris.platform.util.config.ConfigIntf;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.function.Supplier;
import javax.servlet.FilterChain;
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
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.filter.GenericFilterBean;

public class WebAppMediaFilter extends GenericFilterBean
{
    public static final String MEDIAS_RESOURCE_PATH_PREFIX = "medias";
    public static final String SECURE_MEDIAS_RESOURCE_PATH_PREFIX_PATTERN = "medias/{0}";
    protected static final String CONTENT_TYPE_TEXT_PLAIN = "text/plain";
    protected static final String CONTENT_DISPOSITION = "Content-Disposition";
    protected static final String ALLOWED_EXT_FOR_CL = "media.allowed.extensions.for.ClassLoader";
    protected static final String HEADER_ETAG = "ETag";
    protected static final String HEADER_IF_NONE_MATCH = "If-None-Match";
    protected static final String HEADER_X_CONTENT_OPTIONS = "X-Content-Type-Options";
    protected static final String NOSNIFF = "nosniff";
    protected static final Splitter CTX_SPLITTER = Splitter.on("|");
    protected static final String FORCE_DOWNLOAD_DIALOG_FILE_EXTENSIONS = "media.force.download.dialog.fileextensions";
    private static final Logger LOG = LoggerFactory.getLogger(WebAppMediaFilter.class);
    private static final char DOT = '.';
    private static final String SLASH = "/";
    protected static final String INTERNAL_PRETTY_URL_MARKER = "PRETTY" + UUID.randomUUID();
    protected static String[] MEDIAS_RESOURCE_PATH_PREFIXES = new String[] {"medias?", "medias/", "/medias?", "/medias/"};
    private final CustomMediaHeaderConfigurator customMediaHeaderConfigurator = new CustomMediaHeaderConfigurator();
    protected String MEDIAS_CUSTOM_RESOURCE_PATH_PREFIX = getCustomMediaWebWebRoot();
    protected String[] MEDIAS_CUSTOM_RESOURCE_PATH_PREFIXES = new String[] {this.MEDIAS_CUSTOM_RESOURCE_PATH_PREFIX + "?", this.MEDIAS_CUSTOM_RESOURCE_PATH_PREFIX + "/", "/" + this.MEDIAS_CUSTOM_RESOURCE_PATH_PREFIX + "?", "/" + this.MEDIAS_CUSTOM_RESOURCE_PATH_PREFIX + "/"};
    protected Supplier<Integer> firstEssentialOrdinal = (Supplier<Integer>)Suppliers.memoize(this::getFirstEssentialOrdinal);
    private boolean addContextPath;
    private ModelService modelService;
    private MediaPermissionService mediaPermissionService;
    private UserService userService;
    private MediaService mediaService;
    private String secureMediasResourcePathPrefix = MessageFormat.format("medias/{0}", new Object[] {"__secure__"});


    private String getCustomMediaWebWebRoot()
    {
        String mediaWebWebroot = Registry.getCurrentTenantNoFallback().getConfig().getString("mediaweb.webroot", "");
        if(mediaWebWebroot.startsWith("/"))
        {
            return mediaWebWebroot.substring(1);
        }
        return mediaWebWebroot;
    }


    private Boolean isCustomWebRootAllowed()
    {
        return Boolean.valueOf(Registry.getCurrentTenantNoFallback()
                        .getConfig()
                        .getBoolean("allow.custom.mediaweb.webroot", false));
    }


    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
    {
        if(isNotHttpRequestResponse(request, response))
        {
            chain.doFilter(request, response);
            return;
        }
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        SecureResponseWrapper secureResponseWrapper = new SecureResponseWrapper((HttpServletResponse)response);
        String resourcePath = getResourcePath(httpRequest);
        try
        {
            setSecureURLRendererForThread(httpRequest);
            setPublicURLRendererForThread(httpRequest);
            if(isMedia(resourcePath))
            {
                processMedias(httpRequest, (HttpServletResponse)secureResponseWrapper, resourcePath);
            }
            else
            {
                chain.doFilter(request, response);
            }
        }
        finally
        {
            unsetSecureURLRendererForThread();
            unsetPublicURLRendererForThread();
        }
    }


    private void processMedias(HttpServletRequest httpRequest, HttpServletResponse httpResponse, String resourcePath) throws IOException, ServletException
    {
        try
        {
            if(isResourceFromClassLoader(resourcePath))
            {
                loadFromClassLoader(httpResponse, resourcePath);
            }
            else if(isSecureMedia(resourcePath))
            {
                processSecureMedia(httpRequest, httpResponse);
            }
            else
            {
                doFilterMedia(httpRequest, httpResponse, getMediaContext(httpRequest));
            }
        }
        catch(IllegalArgumentException e)
        {
            sendBadRequestResponseStatus(httpResponse, e);
        }
    }


    protected boolean isMedia(String resourcePath)
    {
        if(StringUtils.isNotBlank(this.MEDIAS_CUSTOM_RESOURCE_PATH_PREFIX) && isCustomWebRootAllowed().booleanValue())
        {
            return StringUtils.startsWithAny(resourcePath, this.MEDIAS_CUSTOM_RESOURCE_PATH_PREFIXES);
        }
        return StringUtils.startsWithAny(resourcePath, MEDIAS_RESOURCE_PATH_PREFIXES);
    }


    protected boolean isSecureMedia(String resourcePath)
    {
        return StringUtils.contains(resourcePath, this.secureMediasResourcePathPrefix);
    }


    protected boolean isNotHttpRequestResponse(ServletRequest request, ServletResponse response)
    {
        return (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse));
    }


    protected void setSecureURLRendererForThread(HttpServletRequest httpRequest)
    {
        MediaUtil.setCurrentSecureMediaURLRenderer((MediaUtil.SecureMediaURLRenderer)new DefaultSecureMediaURLRenderer(this.secureMediasResourcePathPrefix, this.addContextPath, httpRequest
                        .getContextPath()));
    }


    protected void unsetSecureURLRendererForThread()
    {
        MediaUtil.unsetCurrentSecureMediaURLRenderer();
    }


    protected void setPublicURLRendererForThread(HttpServletRequest httpRequest)
    {
        if(this.addContextPath)
        {
            MediaUtil.setCurrentPublicMediaURLRenderer((MediaUtil.PublicMediaURLRenderer)new DefaultPublicMediaURLRenderer(httpRequest.getContextPath()));
        }
    }


    protected void unsetPublicURLRendererForThread()
    {
        MediaUtil.unsetCurrentPublicMediaURLRenderer();
    }


    @Deprecated(since = "1811", forRemoval = true)
    protected void doFilterMedia(HttpServletRequest httpRequest, HttpServletResponse httpResponse, Iterable<String> mediaContext, String resourcePath) throws IOException, ServletException
    {
        doFilterMedia(httpRequest, httpResponse, mediaContext);
    }


    protected void doFilterMedia(HttpServletRequest httpRequest, HttpServletResponse httpResponse, Iterable<String> mediaContext) throws IOException, ServletException
    {
        String responseETag = generateETag(getContextPart(ContextPart.LOCATION, mediaContext));
        httpResponse.setHeader("ETag", responseETag);
        String requestETag = httpRequest.getHeader("If-None-Match");
        if(responseETag.equals(requestETag))
        {
            LOG.debug("ETag [{}] equal to If-None-Match, sending 304", responseETag);
            readConfiguredHeaderParamsAndWriteToResponse(httpResponse);
            modifyResponseWithConfiguredHeaders(httpResponse);
            httpResponse.setStatus(304);
        }
        else
        {
            LOG.debug("ETag [{}] is not equal to If-None-Match, sending standard response", responseETag);
            processStandardResponse(httpRequest, httpResponse, mediaContext);
        }
    }


    protected void processSecureMedia(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException
    {
        String mediaPKStr = httpRequest.getParameter("mediaPK");
        MediaModel media = findMediaModel(mediaPKStr);
        if(media == null)
        {
            sendForbiddenResponseStatus(httpResponse, null);
            return;
        }
        if(isAccessDenied(media))
        {
            sendForbiddenResponseStatus(httpResponse, null);
            return;
        }
        int mediaSize = (media.getSize() == null) ? 0 : media.getSize().intValue();
        InputStream inputStream = null;
        try
        {
            inputStream = this.mediaService.getStreamFromMedia(media);
            httpResponse.setContentLength(mediaSize);
            String mime = (media.getMime() == null) ? "application/octet-stream" : media.getMime();
            httpResponse.setContentType(mime);
            setXContentTypeOptionsHeader(httpResponse);
            if(isAddContentDisposition(httpRequest))
            {
                httpResponse.addHeader("Content-Disposition", " attachment; filename=" + media.getRealFileName());
            }
            LOG.debug("Loading secure resource from media storage [PK: {}, User: {}, Mime: {}]", new Object[] {media.getPk(), this.userService
                            .getCurrentUser(), mime});
            if(Objects.nonNull(media.getFolder()))
            {
                String folderQualifier = media.getFolder().getQualifier();
                modifyResponseWithConfiguredHeaders(httpResponse, mime, folderQualifier);
            }
            else
            {
                modifyResponseWithConfiguredHeaders(httpResponse, mime);
            }
            IOUtils.copy(inputStream, (OutputStream)httpResponse.getOutputStream());
        }
        finally
        {
            IOUtils.closeQuietly(inputStream);
        }
    }


    protected MediaModel findMediaModel(String mediaPKString)
    {
        try
        {
            return (MediaModel)this.modelService.get(PK.parse(mediaPKString));
        }
        catch(Exception e)
        {
            LOG.debug("Invalid or outdated secure media pk: {} [{}]", mediaPKString, e);
            LOG.info("Invalid or outdated secure media pk: {}", mediaPKString);
            return null;
        }
    }


    protected boolean isAccessDenied(MediaModel mediaModel)
    {
        boolean result = this.mediaPermissionService.isReadAccessGranted(mediaModel, (PrincipalModel)this.userService.getCurrentUser());
        String readAccessText = result ? " is granted to read the media" : " has NO read access for the media";
        LOG.debug("The Principal {} {} {}", new Object[] {this.userService.getCurrentUser().getName(), readAccessText, mediaModel.getCode()});
        return !result;
    }


    protected Iterable<String> getMediaContext(HttpServletRequest httpRequest)
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


    protected Iterable<String> createLegacyLocalMediaWebUrlContext(HttpServletRequest httpRequest)
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
        int firstSlashIdx = StringUtils.ordinalIndexOf(path, "/", findFirstEssentialSlashIdx());
        if(-1 == firstSlashIdx)
        {
            throw new IllegalArgumentException("The URL has wrong format");
        }
        String pathWithoutFilterMapping = path.substring(firstSlashIdx + 1);
        PrettyUrlStrategy.ParsedPath parsedPath = PrettyUrlStrategyFactory.getDefaultPrettyUrlStrategy().parsePath(pathWithoutFilterMapping);
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


    private int findFirstEssentialSlashIdx()
    {
        if(StringUtils.isBlank(this.MEDIAS_CUSTOM_RESOURCE_PATH_PREFIX) || !isCustomWebRootAllowed().booleanValue())
        {
            return 1;
        }
        return ((Integer)this.firstEssentialOrdinal.get()).intValue();
    }


    protected Integer getFirstEssentialOrdinal()
    {
        int slashes = StringUtils.countMatches(this.MEDIAS_CUSTOM_RESOURCE_PATH_PREFIX, "/");
        return Integer.valueOf((slashes > 0) ? (slashes + 1) : 1);
    }


    protected String getLegacyResourcePath(HttpServletRequest httpRequest)
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


    protected Iterable<String> createLocalMediawebUrlContext(String encodedMediaCtx)
    {
        Preconditions.checkArgument(!GenericValidator.isBlankOrNull(encodedMediaCtx), "incorrect media context in request");
        Iterable<String> mediaContext = CTX_SPLITTER.split(decodeBase64(encodedMediaCtx));
        Preconditions.checkArgument((Iterables.size(mediaContext) == 6), "incorrect media context in request");
        return mediaContext;
    }


    protected String getLocalMediaWebUrlContextParam(HttpServletRequest httpRequest)
    {
        return httpRequest.getParameter("context");
    }


    protected String generateETag(String location)
    {
        HashFunction md5 = Hashing.md5();
        return md5.hashUnencodedChars(location).toString();
    }


    protected String getResourcePath(HttpServletRequest httpRequest)
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


    protected void processStandardResponse(HttpServletRequest httpRequest, HttpServletResponse httpResponse, Iterable<String> mediaContext) throws IOException, ServletException
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


    protected void readConfiguredHeaderParamsAndWriteToResponse(HttpServletResponse httpResponse) throws UnsupportedEncodingException
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


    protected void addContentDisposition(HttpServletRequest httpRequest, HttpServletResponse httpResponse, String resourcePath)
    {
        if(isAddContentDisposition(httpRequest, resourcePath))
        {
            httpResponse.addHeader("Content-Disposition", " attachment; filename=" + getRealFileNameFromResource(resourcePath));
        }
    }


    protected boolean isAddContentDisposition(HttpServletRequest httpRequest, String resourcePath)
    {
        boolean addContentDisposition = isAddContentDisposition(httpRequest);
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


    protected boolean isAddContentDisposition(HttpServletRequest httpRequest)
    {
        return Boolean.parseBoolean(httpRequest.getParameter("attachment"));
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


    protected boolean isResourceFromClassLoader(String resourcePath)
    {
        return (resourcePath != null && resourcePath.contains("/fromjar"));
    }


    protected String getRealFileNameFromResource(String resourcePath)
    {
        int index = resourcePath.lastIndexOf("/");
        return (index >= 0) ? resourcePath.substring(index + 1) : resourcePath;
    }


    protected void loadFromClassLoader(HttpServletResponse httpResponse, String resourcePath) throws IOException
    {
        String resourceName = resourcePath.substring(resourcePath
                        .indexOf("/fromjar") + 1 + "fromjar".length());
        LOG.debug("Trying to load resource '{}' from classloader.", resourceName);
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
                    LOG.debug("Loading resource '{}' from classloader.", resourceName);
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


    protected boolean isDeniedByExtensionForClassloader(String resourceName)
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


    protected void loadFromMediaStorage(HttpServletResponse httpResponse, Iterable<String> mediaContext) throws IOException
    {
        try
        {
            String folderQualifier = getContextPart(ContextPart.FOLDER, mediaContext);
            String location = getContextPart(ContextPart.LOCATION, mediaContext);
            String contentType = httpResponse.getContentType();
            verifyFolderIsNotSecured(folderQualifier);
            MediaManager.InputStreamWithSize streamWithSize = getMediaAsStreamWithSize(folderQualifier, location);
            try
            {
                httpResponse.setContentLengthLong(streamWithSize.getSize());
                LOG.debug("Loading resource [location: '{}'] from media storage.", location);
                modifyResponseWithConfiguredHeaders(httpResponse, contentType, folderQualifier);
                IOUtils.copy(streamWithSize.getInputStream(), (OutputStream)httpResponse.getOutputStream());
            }
            finally
            {
                IOUtils.closeQuietly(streamWithSize.getInputStream());
            }
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


    private void verifyFolderIsNotSecured(String folderQualifier)
    {
        if(getMediaManager().isFolderConfiguredAsSecured(folderQualifier))
        {
            throw new MediaInvalidLocationException("Access to file denied");
        }
    }


    protected void verifyHash(MediaFilterLogicContext ctx)
    {
        if(isBasedOnPrettyUrl(ctx.getMediaContext()))
        {
            return;
        }
        getMediaFilterLogic().verifyHash(ctx);
    }


    protected boolean isBasedOnPrettyUrl(Iterable<String> mediaContext)
    {
        return (mediaContext != null &&
                        Iterables.size(mediaContext) > 6 && INTERNAL_PRETTY_URL_MARKER
                        .equals(Iterables.get(mediaContext, 6)));
    }


    @Deprecated(since = "2011", forRemoval = true)
    protected void verifyHashForLocation(String folderQualifier, String location, String storedHash)
    {
        if(isLegacyPrettyUrlSupport())
        {
            return;
        }
        getMediaManager().verifyMediaHashForLocation(folderQualifier, location, storedHash);
    }


    protected MediaManager.InputStreamWithSize getMediaAsStreamWithSize(String folderQualifier, String location)
    {
        return getMediaManager().getMediaAsStreamWithSize(folderQualifier, location);
    }


    @Deprecated(since = "1905", forRemoval = true)
    protected InputStream getMediaAsStream(String folderQualifier, String location)
    {
        LOG.warn("de.hybris.platform.servicelayer.web.WebAppMediaFilter.getMediaAsStream is deprecated and will be removed.");
        return getMediaAsStreamWithSize(folderQualifier, location).getInputStream();
    }


    protected MediaManager getMediaManager()
    {
        return MediaManager.getInstance();
    }


    protected String decodeBase64(String value)
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


    protected String getContextPart(ContextPart contextPart, Iterable<String> mediaContext)
    {
        return (String)Iterables.get(mediaContext, contextPart.getPartNumber());
    }


    protected void sendForbiddenResponseStatus(HttpServletResponse httpResponse, Exception exception)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Access forbidden for given media", exception);
        }
        httpResponse.setStatus(403);
    }


    protected void sendBadRequestResponseStatus(HttpServletResponse httpResponse, Exception exception)
    {
        LOG.debug("The request sent by the client was syntactically incorrect", exception);
        httpResponse.setStatus(400);
    }


    protected void sendResourceNotFoundResponseStatus(HttpServletResponse httpResponse, Exception exception)
    {
        LOG.debug("Requested resource not found", exception);
        httpResponse.setStatus(404);
    }


    protected Set<String> getAllowedExtensions(String configParameter)
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
        LOG.debug("{}: Supported media extensions: {}", configParameter, extensions);
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


    protected void setXContentTypeOptionsHeader(HttpServletResponse httpResponse)
    {
        httpResponse.setHeader("X-Content-Type-Options", "nosniff");
    }


    protected ConfigIntf getConfig()
    {
        return Registry.getMasterTenant().getConfig();
    }


    public void destroy()
    {
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    @Required
    public void setMediaPermissionService(MediaPermissionService mediaPermissionService)
    {
        this.mediaPermissionService = mediaPermissionService;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    @Required
    public void setMediaService(MediaService mediaService)
    {
        this.mediaService = mediaService;
    }


    public void setAddContextPath(boolean addContextPath)
    {
        this.addContextPath = addContextPath;
    }


    public void setSecureMediasResourcePathPrefix(String token)
    {
        this.secureMediasResourcePathPrefix = MessageFormat.format("medias/{0}", new Object[] {token});
    }
}
